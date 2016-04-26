package com.lufax.esearch.core.page;

public interface Pageable {
	int getPageNumber();
	int getPageSize();
	int totalCount();
	Pageable next();
	Pageable previous();
	Pageable first();
	boolean hasNext();
}
