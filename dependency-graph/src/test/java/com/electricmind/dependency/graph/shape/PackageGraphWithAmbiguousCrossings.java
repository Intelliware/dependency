package com.electricmind.dependency.graph.shape;

import java.io.IOException;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class PackageGraphWithAmbiguousCrossings extends DependencyGraphTester<String> {

	public static void main(String[] args) throws Exception {
		new PackageGraphWithAmbiguousCrossings().process();
	}

	@Override
	public void graphAll(Grapher<String> grapher) throws IOException {
		super.graphToPng(grapher);
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
		manager.add("a1", "b1");
		manager.add("a2", "b2");
		manager.add("a2", "b3");
		manager.add("a2", "b4");
		manager.add("a2", "b1");
		manager.add("a3", "b5");

		manager.add("b1", "c1");
		manager.add("b1", "c2");
		manager.add("b2", "c1");
		manager.add("b2", "c2");
		manager.add("b3", "c2");
		manager.add("b4", "c2");
		manager.add("b4", "c3");
		manager.add("b5", "c2");
//		manager.add("b5", "c3");

		manager.add("c1", "d1");
		manager.add("c2", "d1");
		manager.add("c3", "d1");
		return manager;
	}
}
