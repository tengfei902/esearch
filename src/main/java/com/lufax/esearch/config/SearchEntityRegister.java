package com.lufax.esearch.config;

import java.util.HashMap;
import java.util.Map;

public class SearchEntityRegister {
	private static Map<Class, SearchEntity> searchEntityMap = new HashMap<Class, SearchEntity>();
	
	public static void register(Class clazz,SearchEntity searchEntity) {
		if(null != searchEntityMap.get(clazz)) {
			throw new RuntimeException();
		}
		searchEntityMap.put(clazz, searchEntity);
	}
	
	public static SearchEntity findSearchEntity(Class clazz) {
		return searchEntityMap.get(clazz);
	}
}
