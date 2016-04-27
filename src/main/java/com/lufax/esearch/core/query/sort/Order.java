package com.lufax.esearch.core.query.sort;

public class Order {
	
	private String field;
	private SortType sortType;
	private NullHandling nullHandling;
	
	public Order(String field,SortType sortType) {
		this(field,sortType,NullHandling.NATIVE);
	}
	
	public Order(String field,SortType sortType,NullHandling nullHandling) {
		this.field = field;
		this.sortType = sortType;
		this.nullHandling = nullHandling;
	}
	
	public static enum SortType {
		ASC,
		DESC
	}
	
	public static enum NullHandling {
		NATIVE,
		NULL_FIRST,
		NULL_LAST;
	}
}
