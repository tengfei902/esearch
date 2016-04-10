package com.lufax.esearch.config;

import java.beans.PropertyDescriptor;
import java.util.Map;

import com.lufax.esearch.domain.BaseSearchDomain;
import com.lufax.esearch.reader.ISearchReader;

public class SearchEntity<T> {
	private String index;
	private String type;
	private String shards;
	private String replicas;
	private String refreshInterval;
	private String indexStoreType;
	private Map<String,PropertyDescriptor> propertyMap;
	private PropertyDescriptor idProperty;
	private Class domainClass;
	private ISearchReader<T> reader;
	private String parentType;
	private Integer version;
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getShards() {
		return shards;
	}
	public void setShards(String shards) {
		this.shards = shards;
	}
	public String getReplicas() {
		return replicas;
	}
	public void setReplicas(String replicas) {
		this.replicas = replicas;
	}
	public String getRefreshInterval() {
		return refreshInterval;
	}
	public void setRefreshInterval(String refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	public String getIndexStoreType() {
		return indexStoreType;
	}
	public void setIndexStoreType(String indexStoreType) {
		this.indexStoreType = indexStoreType;
	}
	
	public Map<String, PropertyDescriptor> getPropertyMap() {
		return propertyMap;
	}
	public void setPropertyMap(Map<String, PropertyDescriptor> propertyMap) {
		this.propertyMap = propertyMap;
	}
	public ISearchReader<T> getReader() {
		return reader;
	}
	public void setReader(ISearchReader<T> reader) {
		this.reader = reader;
	}
	public Class getDomainClass() {
		return domainClass;
	}
	public void setDomainClass(Class domainClass) {
		this.domainClass = domainClass;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public PropertyDescriptor getIdProperty() {
		return idProperty;
	}
	public void setIdProperty(PropertyDescriptor idProperty) {
		this.idProperty = idProperty;
	}
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
}
