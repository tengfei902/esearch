package com.lufax.esearch.core;

import static org.elasticsearch.client.Requests.indicesExistsRequest;
import static org.elasticsearch.index.VersionType.EXTERNAL;

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
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import com.lufax.esearch.client.ElasticsearchClientFactory;
import com.lufax.esearch.config.SearchEntity;
import com.lufax.esearch.config.SearchEntityRegister;
import com.lufax.esearch.core.query.IndexQuery;
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
	
	public <T> String delete(Class<T> clazz,String id) {
		if(null == SearchEntityRegister.findSearchEntity(clazz)) {
			throw new ESearchException(String.format("Class %s not registered", clazz.getName()));
		}
		return delete(SearchEntityRegister.findSearchEntity(clazz).getIndex(),SearchEntityRegister.findSearchEntity(clazz).getType(), id);
	}
	
	public <T> String delete(String index,String type,String id) {
		return getClient().prepareDelete(index, type, id).execute().actionGet().getId();
	}
}
