package com.electricmind.dependency.maven.pom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VersionNumberTest {

	@Test
	public void shouldUnderstandBasicCases() {
		VersionNumber version2 = new VersionNumber("2.0");
		VersionNumber version31 = new VersionNumber("3.1");
		VersionNumber version32 = new VersionNumber("3.2");
		
		assertTrue(version31.isGreaterThan(version2));
		assertFalse(version31.isGreaterThan(version32));
	}
	
	@Test
	public void shouldUnderstandNumericParts() {
		VersionNumber version310 = new VersionNumber("3.10");
		VersionNumber version32 = new VersionNumber("3.2");
		
		assertTrue(version310.isGreaterThan(version32));
	}

	@Test
	public void shouldUnderstandMoreParts() {
		VersionNumber version310 = new VersionNumber("3.1.0");
		VersionNumber version31 = new VersionNumber("3.1");
		
		assertTrue(version310.isGreaterThan(version31));
	}
}
