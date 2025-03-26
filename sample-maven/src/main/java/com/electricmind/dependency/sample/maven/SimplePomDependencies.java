package com.electricmind.dependency.sample.maven;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;
import com.electricmind.dependency.graph.shape.ArtifactShape;
import com.electricmind.dependency.graph.shape.MavenArtifactLabelStrategy;
import com.electricmind.dependency.maven.MavenArtifactName;
import com.electricmind.dependency.maven.pom.PomDependencyResolver;

public class SimplePomDependencies {

	public static void main(String[] args) throws Exception {
		
		PomDependencyResolver resolver = new PomDependencyResolver();
		DependencyManager<MavenArtifactName> dependencies = resolver.findDependencies(new File("./pom.xml"));

		Grapher<MavenArtifactName> grapher = new Grapher<>(dependencies);
		grapher.getPlot().setShapeFillColorProvider(new ArtifactColorProvider());
		grapher.getPlot().setShadowColor(new Color(0f, 0f, 0f, 0.4f));
		grapher.getPlot().setLayerAlternatingColor(new Color(203, 219, 252));
		grapher.getPlot().setUseWeights(false);
		ArtifactShape<MavenArtifactName> shape = new ArtifactShape<MavenArtifactName>();
		shape.setLabelStrategy(new MavenArtifactLabelStrategy());
		grapher.setShape(shape);
		try (OutputStream output = new FileOutputStream("./target/artifactGraph.svg")) {
			grapher.createSvg(output);
		}
	}
}
