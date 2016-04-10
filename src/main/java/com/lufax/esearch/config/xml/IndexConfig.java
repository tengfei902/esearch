package com.lufax.esearch.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

public class IndexConfig {
	@XmlAttribute(name="id",required=true)
	private String index;
	@XmlAttribute(name="shards")
	private String shards;
	@XmlAttribute(name="replicas")
	private String replicas;
	@XmlAttribute(name="refreshInterval")
	private String refreshInterval;
	@XmlAttribute(name="indexStoreType")
	private String indexStoreType;
	@XmlElements(value={@XmlElement(name="type",type=TypeConfig.class)})
	private List<TypeConfig> typeConfigs;
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
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
	public List<TypeConfig> getTypeConfigs() {
		return typeConfigs;
	}
	public void setTypeConfigs(List<TypeConfig> typeConfigs) {
		this.typeConfigs = typeConfigs;
	}
}
