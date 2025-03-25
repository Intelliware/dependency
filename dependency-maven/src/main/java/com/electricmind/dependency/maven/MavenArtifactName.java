package com.electricmind.dependency.maven;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MavenArtifactName {

	private String groupId;
	private String artifactId;
	private String packaging;
	private String version;
	
	public MavenArtifactName(String groupId, String artifactId, String version, String packaging) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.packaging = packaging;
	}
	
	public String getGroupId() {
		return this.groupId;
	}
	
	public String getArtifactId() {
		return this.artifactId;
	}
	
	public String getPackaging() {
		return this.packaging;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public String getName() {
		return this.groupId + ":" + this.artifactId + ":" + this.version;
	}
	
	public boolean matchesWithoutVersion(MavenArtifactName name) {
		return this.groupId.equals(name.groupId) && this.artifactId.equals(name.artifactId);
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
			MavenArtifactName that = (MavenArtifactName) obj;
			EqualsBuilder builder = new EqualsBuilder();
			return builder
					.append(this.groupId, that.groupId)
					.append(this.artifactId, that.artifactId)
					.append(this.version, that.version)
					.append(this.packaging, that.packaging)
					.isEquals();
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.groupId)
				.append(this.artifactId)
				.append(this.version)
				.append(this.packaging)
				.toHashCode();
	}
}
