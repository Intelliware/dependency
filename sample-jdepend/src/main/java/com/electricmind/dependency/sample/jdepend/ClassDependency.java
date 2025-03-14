package com.electricmind.dependency.sample.jdepend;

import java.util.Collection;

import jdepend.framework.JavaClass;
import jdepend.framework.JavaPackage;

public class ClassDependency {

	public static int dependencyWeight(JavaPackage javaPackage, JavaPackage dependency) {
		Collection<JavaClass> classes = javaPackage.getClasses();
		int result = 0;
		for (JavaClass javaClass : classes) {
			if (javaClass.getImportedPackages().contains(dependency)) {
				result++;
			}
		}
		return result;
	}
}
