package com.electricmind.dependency.graph.shape;

import java.io.IOException;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class PackageGraphWithMultiplePrefixes extends DependencyGraphTester<String> {
	public static void main(String[] args) throws Exception {
		new PackageGraphWithMultiplePrefixes().process();
	}

	@Override
	public Grapher<String> createGrapher(DependencyManager<String> manager) {
		Grapher<String> grapher = new Grapher<String>(manager);
		grapher.setShape(new PackageShape<String>());
		return grapher;
	}

	@Override
	public void graphAll(Grapher<String> grapher) throws IOException {
		super.graphToSvg(grapher);
	}

	@Override
	public DependencyManager<String> createManager() {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example");
		manager.add("ca.intelliware.example.sub4", "ca.intelliware.example.sub5");
		manager.add("com.electricmind.example.sub5", "ca.intelliware.example.sub4");
		manager.add("com.electricmind.example.sub6", "ca.intelliware.example.sub4");
		return manager;
	}
}
