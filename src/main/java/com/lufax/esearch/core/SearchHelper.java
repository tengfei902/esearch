package com.lufax.esearch.core;

import java.util.Collections;
import java.util.List;

import com.lufax.esearch.client.ElasticsearchClientFactory;
import com.lufax.esearch.config.SearchEntity;
import com.lufax.esearch.config.SearchEntityRegister;
import com.lufax.esearch.domain.BaseSearchDomain;
import com.lufax.esearch.exception.ESearchException;
import com.lufax.esearch.reader.ISearchReader;
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
		if(null == dataType) {
			throw new ESearchException("Data Type cannot be NULL");
		}
		SearchEntity<T> searchEntity = SearchEntityRegister.findSearchEntity(dataType);
		Integer currentIndexVersion = getCurrentIndexVersion(searchEntity);
		Integer nextIndexVersion = SearchContants.INDEX_VERSION[(currentIndexVersion+1)%SearchContants.INDEX_VERSION.length];
		
		elasticsearchTemplate.createIndex(searchEntity.getIndex()+nextIndexVersion);
		elasticsearchTemplate.putMapping(dataType);
		
		updateAll(dataType);
		
	}
	
	public <T extends BaseSearchDomain> Long updateAll(Class<T> dataType) {
		if(null == dataType) {
			throw new ESearchException("Data type cannot be Null");
		}
		SearchEntity<T> searchEntity = SearchEntityRegister.findSearchEntity(dataType);
		if(null == searchEntity) {
			throw new ESearchException(String.format("Class %s not registered", dataType.getName()));
		}
		ISearchReader<T> reader = searchEntity.getReader();
		List<T> list = reader.readAll();
		update(list);
	}
	
	public <T extends BaseSearchDomain> void update(List<T> list) {
		elasticsearchTemplate.bulkIndex(queries);
		
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
}
