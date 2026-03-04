package com.electricmind.dependency.graph.shape;

import java.io.IOException;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class SimpleArtifactGraph extends DependencyGraphTester<SimpleArtifactGraph.ArtifactName> {

	public static class ArtifactName {
		private String name;

		ArtifactName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public String getPackaging() {
			return "jar";
		}
	}

	public static void main(String[] args) throws Exception {
		new SimpleArtifactGraph().process();
	}

	@Override
	public Grapher<ArtifactName> createGrapher(DependencyManager<ArtifactName> manager)
	{
		Grapher<ArtifactName> grapher = new Grapher<>(manager);
		ArtifactShape<ArtifactName> shape = new ArtifactShape<>();
		shape.setLabelStrategy(new MavenArtifactLabelStrategy());
		grapher.setShape(shape);
		return grapher;
	}

	@Override
	public void graphAll(Grapher<ArtifactName> grapher) throws IOException {
		super.graphToSvg(grapher);
	}

	@Override
	public DependencyManager<ArtifactName> createManager() {
		DependencyManager<ArtifactName> manager = new DependencyManager<ArtifactName>();
		ArtifactName artifact1 = new ArtifactName("com.example.thing:artifact1:1.1");
		ArtifactName artifact2 = new ArtifactName("com.example.thing:artifact2:1.1");
		manager.add(artifact1, artifact2);
		manager.add(artifact1, new ArtifactName("com.example.thing:artifact3:1.1"));
		manager.add(artifact2, new ArtifactName("com.example.thing:artifact4:1.1"));
		return manager;
	}
}
