package com.lufax.esearch.config;

import java.util.HashMap;
import java.util.Map;

import com.lufax.esearch.exception.ESearchException;

public class SearchEntityRegister {
	private static Map<Class, SearchEntity> searchEntityMap = new HashMap<Class, SearchEntity>();
	
	public static void register(Class clazz,SearchEntity searchEntity) {
		if(null != searchEntityMap.get(clazz)) {
			throw new RuntimeException();
		}
		searchEntityMap.put(clazz, searchEntity);
	}
	
	public static SearchEntity findSearchEntity(Class clazz) {
		if(null == clazz) {
			throw new ESearchException("【findSearchEntity】 class cannot be null");
		}
		return searchEntityMap.get(clazz);
	}
}
