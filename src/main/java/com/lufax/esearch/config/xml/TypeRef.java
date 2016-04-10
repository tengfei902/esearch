package com.lufax.esearch.config.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class TypeRef {
	@XmlAttribute(name="class",required=true)
	private String clazz;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	
}
