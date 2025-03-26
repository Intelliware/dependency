package com.electricmind.dependency.maven.pom;

import org.apache.commons.lang3.StringUtils;

class Variable {

	private String variableReference;

	Variable(String variableReference) {
		this.variableReference = variableReference;
	}

	String getName() {
		return StringUtils.substringBeforeLast(StringUtils.substringAfter(this.variableReference, "${"), "}");
	}
}
