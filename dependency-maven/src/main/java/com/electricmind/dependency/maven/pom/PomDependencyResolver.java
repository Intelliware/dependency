package com.electricmind.dependency.maven.pom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.maven.MavenArtifactName;

public class PomDependencyResolver {
	
	public static class SimpleName {
		String groupId;
		String artifactId;
		
		public SimpleName(String groupId, String artifactId) {
			this.groupId = groupId;
			this.artifactId = artifactId;
		}
		
		public String getGroupId() {
			return this.groupId;
		}
		public String getArtifactId() {
			return this.artifactId;
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
				SimpleName that = (SimpleName) obj;
				EqualsBuilder builder = new EqualsBuilder();
				return builder
						.append(this.groupId, that.groupId)
						.append(this.artifactId, that.artifactId)
						.isEquals();
			}
		}
		
		@Override
		public int hashCode() {
			return new HashCodeBuilder()
					.append(this.groupId)
					.append(this.artifactId)
					.toHashCode();
		}
		
		@Override
		public String toString() {
			return this.groupId + ":" + this.artifactId;
		}
	}

	PomMarshaller marshaller = new PomMarshaller();
	PomResolver resolver = new MavenM2PomResolver();
	Map<SimpleName, MavenArtifactName> versionMap = new HashMap<>();
	
	public DependencyManager<MavenArtifactName> findDependencies(File pomFile) throws IOException {
		try (InputStream input = new FileInputStream(pomFile)) {
			PomModel pom = this.marshaller.parsePom(input);
			
			findAllDependenciesAndResolveVersion(pom);
			return createDependencyManager(pom);
		}
	}

	private DependencyManager<MavenArtifactName> createDependencyManager(PomModel pom) throws IOException {
		DependencyManager<MavenArtifactName> dependencies = new DependencyManager<>();
		createDependencyManager(pom, dependencies);
		return dependencies;
	}

	private void createDependencyManager(PomModel pom, DependencyManager<MavenArtifactName> dependencies) throws IOException {
		if (pom.getParent() != null) {
			MavenArtifactName parent = this.versionMap.get(new SimpleName(pom.getParent().getGroupId(), pom.getParent().getArtifactId()));
			dependencies.add(pom.getArtifactName(), parent);
			
			PomModel parentPom = parsePom(this.resolver.resolvePom(parent.getGroupId(), parent.getArtifactId(), parent.getVersion()));
			createDependencyManager(parentPom, dependencies);
		}

		for (DependencyModel dependency : pom.getDependencies()) {
			if (isRequiredDependency(dependency)) {
				MavenArtifactName dependencyName = this.versionMap.get(new SimpleName(dependency.getGroupId(), dependency.getArtifactId()));
				if (dependencyName != null) {
					dependencies.add(pom.getArtifactName(), dependencyName);
					PomModel dependencyPom = parsePom(this.resolver.resolvePom(dependencyName.getGroupId(), dependencyName.getArtifactId(), dependencyName.getVersion()));
					if (dependencyPom == null) {
						System.out.println(dependencyName);
					}
					createDependencyManager(dependencyPom, dependencies);
				}
			}
		}
	}

	private void findAllDependenciesAndResolveVersion(PomModel pom) throws IOException {
		
		SimpleName pomName = new SimpleName(pom.getGroupId(), pom.getArtifactId());
		addDependencyVersion(pomName, pom.getArtifactName());
		PomModel parentPom = null;
		
		if (pom.getParent() != null) {
			parentPom = parsePom(this.resolver.resolvePom(pom.getParent().getGroupId(), pom.getParent().getArtifactId(), pom.getParent().getVersion()));
			findAllDependenciesAndResolveVersion(pomName, parentPom);
		}
		
		for (DependencyModel dependency : pom.getDependencies()) {
			if (isRequiredDependency(dependency)) {
				if (dependency.getVersion() == null && parentPom != null) {
					dependency = resolveDependencyVersion(dependency, parentPom);
				}
				
				if (dependency.getVersion() != null) {
					if (isVariableReference(dependency.getVersion())) {
						String resolved = resolveVariable(dependency.getVersion(), pom.getProperties(), parentPom);
						DependencyModel model = new DependencyModel();
						model.setGroupId(dependency.getGroupId());
						model.setArtifactId(dependency.getArtifactId());
						model.setVersion(resolved);
						System.out.println("Need to resolve variable " + dependency.getVersion() + ": " + resolved);
						dependency = model;
					}
					
					findAllDependenciesAndResolveVersion(pomName, 
							parsePom(this.resolver.resolvePom(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion())));
				} else {
					System.out.println("Cannot resolve version number for " + dependency.getGroupId() + ":" + dependency.getArtifactId());
				}
			}
		}
	}

	private String resolveVariable(String variable, Map<String, String> properties, PomModel parentPom) throws IOException {
		String v = new Variable(variable).getName();
		
		String result = null;
		Map<String, String> map = new HashMap<>(parentPom.getProperties());
		map.putAll(properties);
		PomModel ancestor = null;
		
		if (map.containsKey(v)) {
			result = map.get(v);
		} else if (parentPom.getParent() != null) {
			ancestor = parsePom(this.resolver.resolvePom(parentPom.getParent().getGroupId(),parentPom.getParent().getArtifactId(), parentPom.getParent().getVersion()));
			result = resolveVariable(variable, map, ancestor);
		}
		
		while (isVariableReference(result)) {
			String temp = new Variable(result).getName();
			if (map.containsKey(temp)) {
				result = map.get(temp);
			} else {
				if (ancestor != null) {
					properties = findMoreProperties(ancestor, properties);
					// only do this once
					ancestor = null;
				}
				if (map.containsKey(temp)) {
					result = map.get(temp);
				} else {
					break;
				}
			}
		}
		
		return result;
	}

	private Map<String, String> findMoreProperties(PomModel pom, Map<String, String> properties) throws IOException {
		Map<String, String> map = new HashMap<>(pom.getProperties());
		if (pom.getParent() != null) {
			PomModel parent = parsePom(this.resolver.resolvePom(pom.getParent().getGroupId(),pom.getParent().getArtifactId(), pom.getParent().getVersion()));
			map.putAll(findMoreProperties(parent, map));
		}
		map.putAll(properties);
		return map;
	}

	private boolean isVariableReference(String value) {
		return value.startsWith("${") && value.endsWith("}");
	}

	private DependencyModel resolveDependencyVersion(DependencyModel dependency, PomModel pom) throws IOException {

		DependencyModel parentDependency = null;
		if (pom.getDependencyManagement() != null && pom.getDependencyManagement().getDependencies() != null) {
			for (DependencyModel d : pom.getDependencyManagement().getDependencies()) {
				if (d.getGroupId().equals(dependency.getGroupId()) && d.getArtifactId().equals(dependency.getArtifactId())) {
					parentDependency = d;
					break;
				}
			}
		}
		
		if (parentDependency != null) {
			return parentDependency;
		} else if (pom.getParent() != null) {
			PomModel parent = parsePom(this.resolver.resolvePom(pom.getParent().getGroupId(), pom.getParent().getArtifactId(), pom.getParent().getVersion()));
			return resolveDependencyVersion(dependency, parent);
		} else {
			return dependency;
		}
	}

	private boolean isRequiredDependency(DependencyModel dependency) {
		return (dependency.getScope() == null || "compile".equals(dependency.getScope()))
				&& !Boolean.TRUE.equals(dependency.getOptional());
	}

	private void findAllDependenciesAndResolveVersion(SimpleName pomName, PomModel dependentPom) throws IOException {
		if (dependentPom != null) {
			SimpleName parentName = new SimpleName(dependentPom.getGroupId(), dependentPom.getArtifactId());
			addDependencyVersion(parentName, dependentPom.getArtifactName());

			findAllDependenciesAndResolveVersion(dependentPom);
		}
	}

	private PomModel parsePom(File pomFile) throws IOException {
		if (pomFile == null) {
			return null;
		} else {
			try (InputStream input = new FileInputStream(pomFile)) {
				PomModel pom = this.marshaller.parsePom(input);
				return pom;
			}
		}
	}

	private void addDependencyVersion(SimpleName pomName, MavenArtifactName artifactName) {
		MavenArtifactName original = this.versionMap.get(pomName);
		if (original == null) {
			this.versionMap.put(pomName, artifactName);
		} else if (new VersionNumber(artifactName.getVersion()).isGreaterThan(new VersionNumber(original.getVersion()))) {
			this.versionMap.put(pomName, artifactName);
		}
	}
}
