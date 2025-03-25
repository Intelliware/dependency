package com.electricmind.dependency.maven.pom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.electricmind.dependency.Coupling;
import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.Node;
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
			
			return augmentWithVersions(processDependencies(pom, new DependencyManager<SimpleName>()));
		}
	}

	private DependencyManager<MavenArtifactName> augmentWithVersions(
			DependencyManager<SimpleName> dependencies) {
		
		DependencyManager<MavenArtifactName> result = new DependencyManager<>();
		for (Layer<Node<SimpleName>> layer : dependencies.getNodeLayers()) {
			for (Node<SimpleName> node : layer.getContents()) {
				MavenArtifactName fullName = this.versionMap.get(node.getItem());
				result.add(fullName);
				
				for (Coupling<SimpleName> coupling : node.getEfferentCouplings()) {
					MavenArtifactName dependencyName = this.versionMap.get(coupling.getItem());
					result.add(fullName, dependencyName, coupling.getWeight());
				} 
			}
		} 
		return result;
	}

	private DependencyManager<SimpleName> processDependencies(PomModel pom, DependencyManager<SimpleName> dependencies) throws IOException {
		
		SimpleName pomName = new SimpleName(pom.getGroupId(), pom.getArtifactId());
		addDependencyVersion(pomName, pom.getArtifactName());
		
		if (pom.getParent() != null) {
			processDependency(pomName, 
					parsePom(this.resolver.resolvePom(pom.getParent().getGroupId(), pom.getParent().getArtifactId(), pom.getParent().getVersion())), 
					dependencies);				
		}
		
		for (DependencyModel dependency : pom.getDependencies()) {
			if ((dependency.getScope() == null || "compile".equals(dependency.getScope()))
					&& !Boolean.TRUE.equals(dependency.getOptional())) {
				processDependency(pomName, 
						parsePom(this.resolver.resolvePom(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion())), 
						dependencies);				
			}
		}
		
		return dependencies;
	}

	private void processDependency(SimpleName pomName, PomModel dependentPom,
			DependencyManager<SimpleName> dependencies) throws IOException {
		
		if (dependentPom != null) {
			SimpleName parentName = new SimpleName(dependentPom.getGroupId(), dependentPom.getArtifactId());
			addDependencyVersion(parentName, dependentPom.getArtifactName());
			dependencies.add(pomName, parentName);

			processDependencies(dependentPom, dependencies);
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
