package com.electricmind.dependency.maven.pom;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class DependencyManagementModel {

	@JacksonXmlElementWrapper(useWrapping = true, localName = "dependencies")
	List<DependencyModel> dependencies;

	public List<DependencyModel> getDependencies() {
		return this.dependencies;
	}

	public void setDependencies(List<DependencyModel> dependencies) {
		this.dependencies = dependencies;
	}
}
