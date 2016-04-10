package com.lufax.esearch.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanInfoUtil {
	private static List<String> objPropertyList;
	
	static {
		objPropertyList = new ArrayList<String>();
		try {
			BeanInfo objectInfo = Introspector.getBeanInfo(Object.class);
			for(PropertyDescriptor descriptor:objectInfo.getPropertyDescriptors()) {
				objPropertyList.add(descriptor.getName());
			} 
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String,PropertyDescriptor> parseBeanInfo(Class beanType) throws IntrospectionException {
		BeanInfo beanInfo = Introspector.getBeanInfo(beanType);
		
		Map<String,PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();
		for(PropertyDescriptor propertyDescriptor:beanInfo.getPropertyDescriptors()) {
			if(objPropertyList.contains(propertyDescriptor.getName())) {
				continue;
			}
			propertyMap.put(propertyDescriptor.getName(), propertyDescriptor);
		}
		
		return propertyMap;
	}
}
