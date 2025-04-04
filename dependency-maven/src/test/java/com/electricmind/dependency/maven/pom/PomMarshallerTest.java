package com.electricmind.dependency.maven.pom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

public class PomMarshallerTest {

	@Test
	public void shouldParsePom() throws Exception {
		try (InputStream input = new FileInputStream("./pom.xml")) {
			PomModel pom = new PomMarshaller().parsePom(input);
			
			assertEquals("artifact id", "dependency-maven", pom.getArtifactId());
			assertFalse("has dependencies", pom.getDependencies().isEmpty());
		}
	}

	@Test
	public void shouldParsePomWithProperties() throws Exception {
		try (InputStream input = new FileInputStream("../pom.xml")) {
			PomModel pom = new PomMarshaller().parsePom(input);
			
			assertEquals("artifact id", "dependency-root", pom.getArtifactId());
			assertNotNull("properties", pom.getProperties());
			assertEquals("property", "1.8", pom.getProperties().get("maven.compiler.source"));
		}
	}
}
