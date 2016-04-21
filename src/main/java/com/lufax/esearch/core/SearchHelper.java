package com.lufax.esearch.core;

import java.util.List;

import com.lufax.esearch.client.ElasticsearchClientFactory;
import com.lufax.esearch.config.SearchEntity;
import com.lufax.esearch.config.SearchEntityRegister;
import com.lufax.esearch.core.query.IndexQueryBuilderUtil;
import com.lufax.esearch.domain.BaseSearchDomain;
import com.lufax.esearch.exception.ESearchException;
import com.lufax.esearch.search.SearchCondition;
import com.lufax.esearch.util.SearchContants;

public class SearchHelper {
	private ElasticsearchTemplate elasticsearchTemplate;
	
	public SearchHelper(ElasticsearchClientFactory clientFactory) {
		elasticsearchTemplate = new ElasticsearchTemplate(clientFactory);
	}
	
	/**
	 * Create Index
	 * 1.Check index exist & index version
	 * 2.Create new index & update version & put mapping
	 * 3.Update All
	 * 4.Set alias to new Index
	 * 5.Update Recent change
	 * @param dataType
	 */
	public <T extends BaseSearchDomain> void rebuildIndex(Class<T> dataType) {
		SearchEntity<T> searchEntity = getSearchEntityByDataType(dataType);
		Integer currentIndexVersion = getCurrentIndexVersion(searchEntity);
		Integer nextIndexVersion = SearchContants.INDEX_VERSION[(currentIndexVersion+1)%SearchContants.INDEX_VERSION.length];
		
		elasticsearchTemplate.createIndex(searchEntity.getIndex()+nextIndexVersion);
		elasticsearchTemplate.putMapping(dataType);
		
		updateAll(dataType);
	}
	
	public <T extends BaseSearchDomain> void updateAll(Class<T> dataType) {
		update(getSearchEntityByDataType(dataType).getReader().readAll(),getSearchEntityByDataType(dataType));
	}
	
	public <T extends BaseSearchDomain> void updateById(String id,Class<T> dataType) {
		update(getSearchEntityByDataType(dataType).getReader().readById(id),getSearchEntityByDataType(dataType));
	}
	
	public <T extends BaseSearchDomain> void update(List<T> list,Class<T> dataType) {
		update(list,getSearchEntityByDataType(dataType));
	}
	
	public <T extends BaseSearchDomain> void update(List<T> list,SearchEntity<T> searchEntity) {
		elasticsearchTemplate.bulkIndex(IndexQueryBuilderUtil.buildIndexQuery(list, searchEntity));
	}
	
	public <T extends BaseSearchDomain> void update(T data,Class<T> dataType) {
		update(data,getSearchEntityByDataType(dataType));
	}
	
	public <T extends BaseSearchDomain> void update(T data,SearchEntity<T> searchEntity) {
		elasticsearchTemplate.index(IndexQueryBuilderUtil.buildIndexQuery(data, searchEntity));
	}
	
	public <T extends BaseSearchDomain> void deleteIndex(Class<T> dataType) {
		deleteIndex(getSearchEntityByDataType(dataType).getIndex());
	}
	
	public void deleteIndex(String index) {
		elasticsearchTemplate.deleteIndex(index);
	}
	
	public <T extends BaseSearchDomain> void deleteById(String id,Class<T> dataType) {
		elasticsearchTemplate.delete(getSearchEntityByDataType(dataType).getIndex(),getSearchEntityByDataType(dataType).getType(), id);
	}
	
	public <T extends BaseSearchDomain> void deleteObj(T data,Class<T> dataType) {
		deleteObj(data, getSearchEntityByDataType(dataType));
	}
	
	public <T extends BaseSearchDomain> void deleteObj(T data,SearchEntity<T> searchEntity) {
		elasticsearchTemplate.delete(searchEntity.getIndex(), searchEntity.getType(), extractIdFromObj(data,searchEntity));
	}
	
	public <T extends BaseSearchDomain> void deleteByCondition(SearchCondition condition,Class<T> dataType) {
		
	}
	
	public <T extends BaseSearchDomain> void deleteByCondition(SearchCondition condition,SearchEntity<T> searchEntity) {
		
	}
	
	public <T extends BaseSearchDomain> void deleteAll(List<T> list,Class<T> dataType) {
		
	}
	
	public <T extends BaseSearchDomain> void deleteAll(List<T> list,SearchEntity<T> searchEntity) {
		
	}
	
	public <T extends BaseSearchDomain> void delete(List<T> list,Class<T> dataType) {
		
	} 
	
	public <T extends BaseSearchDomain> void delete(List<T> list,SearchEntity<T> searchEntity) {
		
	}
	
	public <T extends BaseSearchDomain> T searchById(String id,Class<T> dataType) {
		return null;
	}
	
	public <T extends BaseSearchDomain> T searchById(String id,SearchEntity<T> searchEntity) {
		return null;
	}
	
	public <T extends BaseSearchDomain> List<T> searchByCondition(SearchCondition condition,Class<T> dataType) {
		return null;
	}
	
	public <T extends BaseSearchDomain> List<T> searchByCondition(SearchCondition condition,SearchEntity<T> searchEntity) {
		return null;
	}
	
	private Integer getCurrentIndexVersion(SearchEntity searchEntity) {
		Integer version = searchEntity.getVersion();
		String currentIndex = searchEntity.getIndex()+version;
		if(elasticsearchTemplate.indexExists(currentIndex)) {
			return version;
		}
		
		for(int index:SearchContants.INDEX_VERSION) {
			currentIndex = searchEntity.getIndex()+index;
			if(elasticsearchTemplate.indexExists(currentIndex)) {
				return index;
			}
		}
		return -1;
	}
	
	private <T extends BaseSearchDomain> SearchEntity<T> getSearchEntityByDataType(Class<T> dataType) {
		SearchEntity<T> searchEntity = SearchEntityRegister.findSearchEntity(dataType);
		if(null == searchEntity) {
			throw new ESearchException(String.format("Class %s not registered", dataType.getName()));
		}
		return searchEntity;
	}
	
	private <T extends BaseSearchDomain> String extractIdFromObj(T obj,SearchEntity<T> searchEntity) {
		try {
			return String.valueOf(searchEntity.getIdProperty().getReadMethod().invoke(obj, null));
		} catch (Exception e) {
			throw new ESearchException("Error extract id:"+e.getMessage());
		}
	}
}
