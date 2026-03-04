package com.electricmind.dependency.graph.shape;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class PackageGraphWithCrossings extends DependencyGraphTester<String> {

	public static void main(String[] args) throws Exception {
		new PackageGraphWithCrossings().process();
	}

	@Override
	public Grapher<String> createGrapher(DependencyManager<String> manager)
	{
		Grapher<String> grapher = new Grapher<String>(manager);
		grapher.setShape(new BigPackageShape<String>());
		return grapher;
	}

	@Override
	public DependencyManager<String> createManager() {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example.sub2");
		manager.add("ca.intelliware.example.sub1", "ca.intelliware.example");
		manager.add("ca.intelliware.example.sub3", "ca.intelliware.example.sub2");
		manager.add("ca.intelliware.example.sub3", "ca.intelliware.example");
		return manager;
	}
}
