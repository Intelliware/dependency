package com.electricmind.dependency.sample.npm;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class NpmPackageName {

	String name;
	String version;

	
	public NpmPackageName(String name, String version) {
		this.name = name;
		this.version = version;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.name).append(this.version).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (obj.getClass() != this.getClass()) {
			return false;
		} else {
			NpmPackageName that = (NpmPackageName) obj;
			EqualsBuilder builder = new EqualsBuilder();
			return builder
					.append(this.name, that.name)
					.append(this.version, that.version)
					.isEquals();
		}
	}
}
