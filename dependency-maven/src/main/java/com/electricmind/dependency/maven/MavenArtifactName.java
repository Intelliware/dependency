package com.electricmind.dependency.maven;

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
}
