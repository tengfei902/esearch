package com.lufax.esearch.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lufax.esearch.search.SearchCriteria.SearchType;

public class SearchCondition {
	private boolean countOnly = false;
	private String[] fields;
	private List<SearchCriteria> criterias = new ArrayList<SearchCriteria>();
	private List<SearchSort> sortList = new ArrayList<SearchSort>();
	private String[] highLightFields;
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
	
	public SearchCondition withFields(String... fields) {
		this.fields = fields;
		return this;
	}
	
	public SearchCondition withSort(List<SearchSort> sortList) {
		this.sortList = sortList;
		return this;
	}
	
	public SearchCondition withHighlight(String... highLightFields) {
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
	
	public SearchCondition must(SearchParam param) {
		SearchCriteria criteria = new SearchCriteria(param, SearchType.MUST);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition must(List<SearchParam> params) {
		SearchCriteria criteria = new SearchCriteria(params,SearchType.MUST);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition should(SearchParam param) {
		SearchCriteria criteria = new SearchCriteria(param,SearchType.SHOULD);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition should(List<SearchParam> params) {
		SearchCriteria criteria = new SearchCriteria(params,SearchType.SHOULD);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition mustNot(SearchParam param) {
		SearchCriteria criteria = new SearchCriteria(param,SearchType.MUST_NOT);
		this.criterias.add(criteria);
		return this;
	}
	
	public SearchCondition mustNot(List<SearchParam> params) {
		SearchCriteria criteria = new SearchCriteria(params,SearchType.MUST_NOT);
		this.criterias.add(criteria);
		return this;
	}

	public boolean isCountOnly() {
		return countOnly;
	}

	public String[] getFields() {
		return fields;
	}

	public List<SearchCriteria> getCriterias() {
		return criterias;
	}

	public List<SearchSort> getSortList() {
		return sortList;
	}

	public String[] getHighLightFields() {
		return highLightFields;
	}

	public long getStartNum() {
		return startNum;
	}

	public long getEndNum() {
		return endNum;
	}
}
