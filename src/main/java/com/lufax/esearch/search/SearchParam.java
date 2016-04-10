package com.lufax.esearch.search;

public class SearchParam {
	private String paramName;
	private String matchValue;
	private String[] inValues;
	private long ltValue;
	private long lteValue;
	private long gtValue;
	private long gteValue;
	private float boost;
	
	public SearchParam(String paramName) {
		this.paramName = paramName;
	}
	
	public SearchParam in(String... inValues) {
		this.inValues = inValues;
		return this;
	}
	
	public SearchParam match(String matchValue) {
		this.matchValue = matchValue;
		return this;
	}
	
	public SearchParam lt(long ltValue) {
		this.ltValue = ltValue;
		return this;
	}
	
	public SearchParam lte(long lteValue) {
		this.lteValue = lteValue;
		return this;
	}
	
	public SearchParam gt(long gtValue) {
		this.gtValue = gtValue;
		return this;
	}
	
	public SearchParam gte(long gteValue) {
		this.gteValue = gteValue;
		return this;
	}
	
	public SearchParam boost(float boost) {
		this.boost = boost;
		return this;
	}
}
