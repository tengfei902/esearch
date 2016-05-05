package com.lufax.esearch;

import com.lufax.esearch.search.SearchCondition;
import com.lufax.esearch.search.SearchParam;

public class ESearchConditionTest {
	
	public void testBuildCondition() {
		SearchCondition condition = new SearchCondition().countOnly(false).must(new SearchParam("test").in("")).must(new SearchParam("").in(""));
		
	}
	
	public void parseSearchCondition() {
		
	}
}
