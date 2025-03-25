package com.electricmind.dependency.maven.pom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DependencyModel {
	
	String groupId;
	String artifactId;
	@JsonProperty(required = false)
	String version;
	@JsonProperty(required = false)
	String scope;
	@JsonProperty(required = false)
	Boolean optional;
	
	public String getGroupId() {
		return this.groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return this.artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getScope() {
		return this.scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Boolean getOptional() {
		return this.optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
}
