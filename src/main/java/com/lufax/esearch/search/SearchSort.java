package com.lufax.esearch.search;

import org.elasticsearch.search.sort.SortOrder;

public class SearchSort {
	private String field;
	private SortOrder sortOrder;
	
	public SearchSort(String field,SortOrder sortOrder){
		this.field = field;
		this.sortOrder = sortOrder;
	}
}
