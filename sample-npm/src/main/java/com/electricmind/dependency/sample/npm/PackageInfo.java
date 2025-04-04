package com.electricmind.dependency.sample.npm;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageInfo {
	
	String name;
	String version;
	Map<String, String> dependencies;

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getDependencies() {
		return this.dependencies == null ? Collections.emptyMap() : this.dependencies;
	}

	public void setDependencies(Map<String, String> dependencies) {
		this.dependencies = dependencies;
	}
}
