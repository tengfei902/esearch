package com.lufax.esearch.search;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria {
	private List<SearchParam> params = new ArrayList<SearchParam>();
	private SearchType searchType;
	
	public enum SearchType{
		MUST,SHOULD,MUST_NOT
	}
	
	public SearchCriteria(SearchParam param,SearchType searchType) {
		this.params.add(param);
		this.searchType = searchType;
	}
	
	public SearchCriteria(List<SearchParam> params,SearchType searchType) {
		this.params.addAll(params);
		this.searchType = searchType;
	}
}
