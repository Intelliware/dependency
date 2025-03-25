package com.electricmind.dependency.maven.pom;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

public class MavenM2PomResolver implements PomResolver {

	@Override
	public File resolvePom(String groupId, String artifactId, String version) {
		File userHome = SystemUtils.getUserHome();
		File m2Repo = new File(userHome, ".m2/repository");
		File directory = new File(m2Repo, groupId.replace(".", "/") + "/" + artifactId.replace(".", "/") + "/" + version);
		
		File pom = new File(directory, artifactId + "-" + version + ".pom");
		System.out.println(pom.getAbsolutePath());
		return pom.exists() ? pom : null;
	}
}
