package com.lufax.esearch.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "search")
public class SearchConfig {
	@XmlElements(value = {@XmlElement(name="index",type=IndexConfig.class,required=true)})
	private List<IndexConfig> indexConfigs;

	public List<IndexConfig> getIndexConfigs() {
		return indexConfigs;
	}
	public void setIndexConfigs(List<IndexConfig> indexConfigs) {
		this.indexConfigs = indexConfigs;
	}
}
