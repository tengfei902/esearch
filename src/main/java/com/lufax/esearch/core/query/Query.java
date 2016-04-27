package com.lufax.esearch.core.query;


import java.util.Collection;
import java.util.List;

import org.elasticsearch.action.search.SearchType;

import com.lufax.esearch.core.page.PageRequest;
import com.lufax.esearch.core.page.Pageable;
import com.lufax.esearch.core.query.sort.Sort;

public interface Query {
	
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final Pageable DEFAULT_PAGE = new PageRequest(0, DEFAULT_PAGE_SIZE);
	
	<T extends Query> T setPageable(Pageable pageable);
	Pageable getPageable();
	<T extends Query> T addSort(Sort sort);
	Sort getSort();
	List<String> getIndices();
	void addIndices(String... indices);
	void addTypes(String... types);
	List<String> getTypes();
	void addFields(String... fields);
	List<String> getFields();
	void addSourceFilter(SourceFilter sourceFilter);
	SourceFilter getSourceFilter();
	float getMinScore();
	Collection<String> getIds();
	String getRoute();
	SearchType getSearchType();
}
