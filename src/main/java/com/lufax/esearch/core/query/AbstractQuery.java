package com.lufax.esearch.core.query;

import static java.util.Collections.addAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.action.search.SearchType;
import org.springframework.util.Assert;

import com.lufax.esearch.core.page.Pageable;
import com.lufax.esearch.core.query.sort.Sort;

public abstract class AbstractQuery implements Query {
	protected Pageable pageable = DEFAULT_PAGE;
	protected Sort sort;
	protected List<String> indices = new ArrayList<String>();
	protected List<String> types = new ArrayList<String>();
	protected List<String> fields = new ArrayList<String>();
	protected SourceFilter sourceFilter;
	protected float minScore;
	protected Collection<String> ids;
	protected String route;
	protected SearchType searchType = SearchType.DFS_QUERY_THEN_FETCH;

	public Sort getSort() {
		return this.sort;
	}

	public Pageable getPageable() {
		return this.pageable;
	}

	public final <T extends Query> T setPageable(Pageable pageable) {
		Assert.notNull(pageable);
		this.pageable = pageable;
		return (T) this.addSort(pageable.getSort());
	}

	public void addFields(String... fields) {
		addAll(this.fields, fields);
	}

	public List<String> getFields() {
		return fields;
	}

	public List<String> getIndices() {
		return indices;
	}

	public void addIndices(String... indices) {
		addAll(this.indices, indices);
	}

	public void addTypes(String... types) {
		addAll(this.types, types);
	}

	public List<String> getTypes() {
		return types;
	}

	public void addSourceFilter(SourceFilter sourceFilter) {
		this.sourceFilter = sourceFilter;
	}

	public SourceFilter getSourceFilter() {
		return sourceFilter;
	}

	@SuppressWarnings("unchecked")
	public final <T extends Query> T addSort(Sort sort) {
		if (sort == null) {
			return (T) this;
		}

		if (this.sort == null) {
			this.sort = sort;
		} else {
			this.sort = this.sort.and(sort);
		}

		return (T) this;
	}

	public float getMinScore() {
		return minScore;
	}

	public void setMinScore(float minScore) {
		this.minScore = minScore;
	}

	public Collection<String> getIds() {
		return ids;
	}

	public void setIds(Collection<String> ids) {
		this.ids = ids;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

	public SearchType getSearchType() {
		return searchType;
	}
}
