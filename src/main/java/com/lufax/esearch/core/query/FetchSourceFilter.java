package com.lufax.esearch.core.query;

public class FetchSourceFilter implements SourceFilter {

	private final String[] includes;
	private final String[] excludes;

	public FetchSourceFilter(final String[] includes, final String[] excludes) {
		this.includes = includes;
		this.excludes = excludes;
	}

	public String[] getIncludes() {
		return includes;
	}

	public String[] getExcludes() {
		return excludes;
	}

	
}
