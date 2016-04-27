package com.lufax.esearch.core;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.elasticsearch.client.Requests.indicesExistsRequest;
import static org.elasticsearch.index.VersionType.EXTERNAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.lufax.esearch.client.ElasticsearchClientFactory;
import com.lufax.esearch.config.SearchEntity;
import com.lufax.esearch.config.SearchEntityRegister;
import com.lufax.esearch.core.page.PageRequest;
import com.lufax.esearch.core.query.DeleteQuery;
import com.lufax.esearch.core.query.IndexQuery;
import com.lufax.esearch.core.query.SearchQuery;
import com.lufax.esearch.core.query.builder.NativeSearchQueryBuilder;
import com.lufax.esearch.exception.ESearchException;

public class ElasticsearchTemplate {
	
	private ElasticsearchClientFactory clientFactory;
	
	public ElasticsearchTemplate(ElasticsearchClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public Client getClient() {
		return clientFactory.getClient();
	}
	
	public <T> boolean createIndex(String indexName) {
		CreateIndexRequestBuilder createIndexRequestBuilder = getClient().admin().indices().prepareCreate(indexName);
		return createIndexRequestBuilder.execute().actionGet().isAcknowledged();
	}

	public <T> boolean putMapping(Class<T> clazz) {
		SearchEntity<T> searchEntity = SearchEntityRegister.findSearchEntity(clazz);
		if(null == searchEntity) {
			throw new ESearchException(String.format("Class:%s not registered", clazz.getName()));
		}
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = MappingBuilder.buildMapping(clazz, searchEntity.getType(), searchEntity.getIdProperty().getName(), searchEntity.getParentType());
		} catch (Exception e) {
			throw new ESearchException("Failed to build mapping for " + clazz.getSimpleName());
		}
		return putMapping(searchEntity.getIndex(),searchEntity.getType(), xContentBuilder);
	}

	public <T> boolean putMapping(String index,String type, XContentBuilder xContentBuilder) {
		PutMappingRequestBuilder requestBuilder = getClient().admin().indices().preparePutMapping(index).setType(type);
		requestBuilder.setSource(xContentBuilder);
		return requestBuilder.execute().actionGet().isAcknowledged();
	}
	
	public <T> boolean indexExists(Class<T> clazz) {
		return indexExists(SearchEntityRegister.findSearchEntity(clazz).getIndex());
	}
	
	public boolean indexExists(String index) {
		return getClient().admin().indices().exists(indicesExistsRequest(index)).actionGet().isExists();
	}
	
	public boolean typeExists(String index, String type) {
		return getClient().admin().cluster().prepareState().execute().actionGet().getState().metaData().index(index).getMappings().containsKey(type);
	}
	
	public <T> boolean deleteIndex(Class<T> clazz) {
		return deleteIndex(SearchEntityRegister.findSearchEntity(clazz).getIndex());
	}
	
	public boolean deleteIndex(String index) {
		if (indexExists(index)) {
			return getClient().admin().indices().delete(new DeleteIndexRequest(index)).actionGet().isAcknowledged();
		}
		return false;
	}
	
	public void bulkIndex(List<IndexQuery> queries) {
		BulkRequestBuilder bulkRequest = getClient().prepareBulk();
		for (IndexQuery query : queries) {
			bulkRequest.add(prepareIndex(query));
		}
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			Map<String, String> failedDocuments = new HashMap<String, String>();
			for (BulkItemResponse item : bulkResponse.getItems()) {
				if (item.isFailed())
					failedDocuments.put(item.getId(), item.getFailureMessage());
			}
			throw new ESearchException(String.format("Bulk indexing has failures. Use ElasticsearchException.getFailedDocuments() for detailed messages [%s]",failedDocuments));
		}
	}
	
	public String index(IndexQuery query) {
		return prepareIndex(query).execute().actionGet().getId();
	}
	
	private IndexRequestBuilder prepareIndex(IndexQuery query) {
		String indexName = query.getIndexName();
		String type = query.getType();

		IndexRequestBuilder indexRequestBuilder = null;

		if (query.getObject() != null) {
			// If we have a query id and a document id, do not ask ES to generate one.
			if (query.getId() != null) {
				indexRequestBuilder = getClient().prepareIndex(indexName, type, query.getId());
			} else {
				indexRequestBuilder = getClient().prepareIndex(indexName, type);
			}
//				indexRequestBuilder.setSource(resultsMapper.getEntityMapper().mapToString(query.getObject()));
		} else if (query.getSource() != null) {
			indexRequestBuilder = getClient().prepareIndex(indexName, type, query.getId()).setSource(query.getSource());
		} else {
			throw new ESearchException("object or source is null, failed to index the document [id: " + query.getId() + "]");
		}
		if (query.getVersion() != null) {
			indexRequestBuilder.setVersion(query.getVersion());
			indexRequestBuilder.setVersionType(EXTERNAL);
		}

		if (query.getParentId() != null) {
			indexRequestBuilder.setParent(query.getParentId());
		}

		return indexRequestBuilder;
	}
	
	public <T> String delete(String index,String type,String id) {
		return getClient().prepareDelete(index, type, id).execute().actionGet().getId();
	}
	
	public <T> void delete(List<String> ids,String index,String type) {
		BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
		for(String id : ids) {
			bulkRequestBuilder.add(getClient().prepareDelete(index, type, id));
		}
		if(bulkRequestBuilder.numberOfActions() > 0) {
			bulkRequestBuilder.execute().actionGet();
		}
	}
	
	public <T> void delete(DeleteQuery deleteQuery, String index,String type) {

		Integer pageSize = deleteQuery.getPageSize() != null ? deleteQuery.getPageSize() : 1000;
		Long scrollTimeInMillis = deleteQuery.getScrollTimeInMillis() != null ? deleteQuery.getScrollTimeInMillis() : 10000l;
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(deleteQuery.getQuery())
				.withIndices(index)
				.withTypes(type)
				.withPageable(new PageRequest(0, pageSize))
				.build();

		String scrollId = scan(searchQuery, scrollTimeInMillis, true);

		BulkRequestBuilder bulkRequestBuilder = getClient().prepareBulk();
		List<String> ids = new ArrayList<String>();
		boolean hasRecords = true;
		while (hasRecords) {
			Page<String> page = scroll(scrollId, scrollTimeInMillis, new SearchResultMapper() {
				@Override
				public <T> Page<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
					List<String> result = new ArrayList<String>();
					for (SearchHit searchHit : response.getHits()) {
						String id = searchHit.getId();
						result.add(id);
					}
					if (result.size() > 0) {
						return new PageImpl<T>((List<T>) result);
					}
					return null;
				}
			});
			if (page != null && page.getContent().size() > 0) {
				ids.addAll(page.getContent());
			} else {
				hasRecords = false;
			}
		}

		for(String id : ids) {
			bulkRequestBuilder.add(getClient().prepareDelete(index, type, id));
		}

		if(bulkRequestBuilder.numberOfActions() > 0) {
			bulkRequestBuilder.execute().actionGet();
		}

		clearScroll(scrollId);
	}
	
	public void clearScroll(String scrollId) {
		getClient().prepareClearScroll().addScrollId(scrollId).execute().actionGet();
	}
}
