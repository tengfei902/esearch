package com.lufax.esearch.core.page;

public class PageRequest implements Pageable {
	
	public PageRequest(int currentPage,int pageSize) {
		
	}

	public int getPageNumber() {
		return 0;
	}

	public int getPageSize() {
		return 0;
	}

	public int totalCount() {
		return 0;
	}

	public Pageable next() {
		return null;
	}

	public Pageable previous() {
		return null;
	}

	public Pageable first() {
		return null;
	}

	public boolean hasNext() {
		return false;
	}
}
