package com.electricmind.dependency.maven.pom;

import java.io.File;

public interface PomResolver {

	public File resolvePom(String groupId, String artifactId, String version);
}
