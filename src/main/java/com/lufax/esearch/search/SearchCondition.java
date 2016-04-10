package com.lufax.esearch.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lufax.esearch.search.SearchCriteria.SearchType;

public class SearchCondition {
	private boolean countOnly = false;
	private List<String> fields;
	private List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
	private List<SearchSort> sortList = new ArrayList<SearchSort>();
	private List<String> highLightFields;
	private long startNum;
	private long endNum;
	
	public SearchCondition() {
		
	}
	
	public SearchCondition(Map conditions) {
		
	}
	
	public SearchCondition countOnly(boolean countOnly) {
		this.countOnly = countOnly;
		return this;
	}
	
	public SearchCondition withFields(List<String> fields) {
		this.fields = fields;
		return this;
	}
	
	public SearchCondition withSort(List<SearchSort> sortList) {
		this.sortList = sortList;
		return this;
	}
	
	public SearchCondition withHighlight(List<String> highLightFields) {
		this.highLightFields = highLightFields;
		return this;
	}
	
	public SearchCondition withStart(long startNum) {
		this.startNum = startNum;
		return this;
	}
	
	public SearchCondition withEnd(long endNum) {
		this.endNum = endNum;
		return this;
	}
	
	public SearchCondition and(SearchParam param) {
		SearchCriteria criteria = new SearchCriteria(param, SearchType.AND);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition and(List<SearchParam> params) {
		SearchCriteria criteria = new SearchCriteria(params,SearchType.AND);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition or(SearchParam param) {
		SearchCriteria criteria = new SearchCriteria(param,SearchType.OR);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition or(List<SearchParam> params) {
		SearchCriteria criteria = new SearchCriteria(params,SearchType.OR);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition not(SearchParam param) {
		SearchCriteria criteria = new SearchCriteria(param,SearchType.NOT);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition not(List<SearchParam> params) {
		SearchCriteria criteria = new SearchCriteria(params,SearchType.NOT);
		this.criterias.add(criteria);
		return this;
	}
}
