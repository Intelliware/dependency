package com.electricmind.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class PackageGraphWithSimpleCycles {
	
	public static void main(String[] args) throws Exception {
		new PackageGraphWithSimpleCycles().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example");
		manager.add("ca.intelliware.example.sub4", "ca.intelliware.example.sub5");
		manager.add("ca.intelliware.example.sub5", "ca.intelliware.example.sub4");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "PackageGraphWithSimpleCycles.png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new PackageShape<String>());
			grapher.createPng(output);
		} finally {
			output.close();
		}
	}
}
