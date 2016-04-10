package com.lufax.esearch.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TypeConfig {
	@XmlAttribute(name="id",required=true)
	private String type;
	@XmlElement(name="domain",required=true)
	private TypeRef domain;
	@XmlElement(name="reader",required=true)
	private TypeRef reader;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public TypeRef getDomain() {
		return domain;
	}
	public void setDomain(TypeRef domain) {
		this.domain = domain;
	}
	public TypeRef getReader() {
		return reader;
	}
	public void setReader(TypeRef reader) {
		this.reader = reader;
	}
}
