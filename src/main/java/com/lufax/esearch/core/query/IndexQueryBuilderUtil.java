package com.lufax.esearch.core.query;

import java.util.ArrayList;
import java.util.List;

import com.lufax.esearch.config.SearchEntity;
import com.lufax.esearch.domain.BaseSearchDomain;

public class IndexQueryBuilderUtil {
	public static <T extends BaseSearchDomain> List<IndexQuery> buildIndexQuery(List<T> list,SearchEntity<T> searchEntity) {
		List<IndexQuery> queryList = new ArrayList<IndexQuery>();
		if(null == list) {
			return queryList;
		}
		
		for(T data:list) {
			queryList.add(new IndexQueryBuilder().withId(extractIdFromObject(data, searchEntity)).withObject(data).withIndexName(searchEntity.getIndex()).withType(searchEntity.getType()).build());
		}
		
		return queryList;
	}
	
	public static <T extends BaseSearchDomain> IndexQuery buildIndexQuery(T data,SearchEntity<T> searchEntity) {
		if(null == data) {
			return null;
		}
		return new IndexQueryBuilder().withId(extractIdFromObject(data, searchEntity)).withObject(data).withIndexName(searchEntity.getIndex()).withType(searchEntity.getType()).build();
	}
	
	private static <T extends BaseSearchDomain> String extractIdFromObject(T obj,SearchEntity<T> searchEntity) {
		Object id;
		try {
			id = searchEntity.getIdProperty().getReadMethod().invoke(obj, null);
		} catch (Exception e) {
			return null;
		}
		if(null == id) {
			return null;
		}
		return (String)id;
	}
}
