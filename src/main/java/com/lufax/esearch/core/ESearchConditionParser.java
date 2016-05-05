package com.lufax.esearch.core;

import com.lufax.esearch.core.query.builder.NativeSearchQueryBuilder;
import com.lufax.esearch.search.SearchCondition;

public class ESearchConditionParser {
	
	public void parseCondition(SearchCondition searchCondition) {
		NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
		if(null != searchCondition.getFields() && searchCondition.getFields().length>0) {
			searchQueryBuilder = searchQueryBuilder.withFields(searchCondition.getFields());
		}
		if(null != searchCondition.getHighLightFields() && searchCondition.getHighLightFields().length>0) {
			
		}
		
	}
}
