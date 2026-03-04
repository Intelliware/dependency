package com.electricmind.dependency.graph.shape;

import java.io.IOException;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class SimplePackageGraph extends DependencyGraphTester<String> {

	public static void main(String[] args) throws Exception {
		new SimplePackageGraph().process();
	}

	@Override
	public void graphToPng(Grapher<String> grapher) throws IOException {
		graphToPng(grapher, 500, 500);
	}

	@Override
	public Grapher<String> createGrapher(DependencyManager<String> manager) {
		Grapher<String> grapher = new Grapher<String>(manager);
		grapher.setShape(new PackageShape<String>());
		return grapher;
	}

	@Override
	public DependencyManager<String> createManager() {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.hl7.generator.xsd", "ca.intelliware.hl7.generator");
		manager.add("ca.intelliware.hl7.referral");
		return manager;
	}

	@Override
	public String getFileNameBase() {
		return "SimplePackageGraph";
	}
}
