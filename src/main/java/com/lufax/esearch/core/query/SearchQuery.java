package com.lufax.esearch.core.query;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import com.lufax.esearch.core.script.ScriptField;

public interface SearchQuery {
	
	QueryBuilder getQuery();

	QueryBuilder getFilter();

	List<SortBuilder> getElasticsearchSorts();

	/*List<FacetRequest> getFacets();*/

	List<AbstractAggregationBuilder> getAggregations();

	HighlightBuilder.Field[] getHighlightFields();

	List<IndexBoost> getIndicesBoost();

    List<ScriptField> getScriptFields();
}
