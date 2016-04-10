package com.lufax.esearch.core.query;

public class IndexQueryBuilder {
	private String id;
	private Object object;
	private Long version;
	private String indexName;
	private String type;
	private String source;
	private String parentId;
	
	public IndexQueryBuilder withId(String id) {
		this.id = id;
		return this;
	}

	public IndexQueryBuilder withObject(Object object) {
		this.object = object;
		return this;
	}

	public IndexQueryBuilder withVersion(Long version) {
		this.version = version;
		return this;
	}

	public IndexQueryBuilder withIndexName(String indexName) {
		this.indexName = indexName;
		return this;
	}

	public IndexQueryBuilder withType(String type) {
		this.type = type;
		return this;
	}

	public IndexQueryBuilder withSource(String source) {
		this.source = source;
		return this;
	}

	public IndexQueryBuilder withParentId(String parentId) {
		this.parentId = parentId;
		return this;
	}

	public IndexQuery build() {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setId(id);
		indexQuery.setIndexName(indexName);
		indexQuery.setType(type);
		indexQuery.setObject(object);
		indexQuery.setParentId(parentId);
		indexQuery.setSource(source);
		indexQuery.setVersion(version);
		return indexQuery;
	}
}
