package com.lufax.esearch.core.script;

import org.elasticsearch.script.Script;

public class ScriptField {

	private final String fieldName;
	private final Script script;

	public ScriptField(String fieldName, Script script) {
		this.fieldName = fieldName;
		this.script = script;
	}

	public String fieldName() {
		return fieldName;
	}

	public Script script() {
		return script;
	}
}