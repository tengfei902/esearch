package com.lufax.esearch.reader;

import java.util.List;

public interface ISearchReader<T> {
	List<T> readAll();
	T readById(String id);
	List<T> readRecentChange();
}
