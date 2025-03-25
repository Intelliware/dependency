package com.electricmind.dependency.maven.pom;

import java.util.ArrayList;
import java.util.List;

import com.electricmind.dependency.maven.MavenArtifactName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PomModel {
	
	String groupId;
	String artifactId;
	String version;
	String packaging;
	
	ParentModel parent;
	
	@JacksonXmlElementWrapper(useWrapping = true, localName = "dependencies")
	List<DependencyModel> dependencies;

	public String getGroupId() {
		if (this.groupId == null && this.parent != null) {
			return this.parent.getGroupId();
		} else {
			return this.groupId;
		}
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getArtifactId() {
		if (this.artifactId == null && this.parent != null) {
			return this.parent.getArtifactId();
		} else {
			return this.artifactId;
		}
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getVersion() {
		if (this.version == null && this.parent != null) {
			return this.parent.getVersion();
		} else {
			return this.version;
		}
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackaging() {
		return this.packaging == null ? "jar" : this.packaging;
	}

	public void setPackaging(String packaging) {
		this.packaging = packaging;
	}

	public List<DependencyModel> getDependencies() {
		return this.dependencies == null ? new ArrayList<>() : this.dependencies;
	}

	public void setDependencies(List<DependencyModel> dependencies) {
		this.dependencies = dependencies;
	}

	public ParentModel getParent() {
		return this.parent;
	}

	public void setParent(ParentModel parent) {
		this.parent = parent;
	}

	public MavenArtifactName getArtifactName() {
		return new MavenArtifactName(getGroupId(), getArtifactId(), getVersion(), getPackaging());
	}
}
