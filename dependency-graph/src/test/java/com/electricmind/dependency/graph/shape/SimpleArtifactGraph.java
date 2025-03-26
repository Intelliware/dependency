package com.electricmind.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class SimpleArtifactGraph {
	
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

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<ArtifactName> manager = new DependencyManager<ArtifactName>();
		ArtifactName artifact1 = new ArtifactName("com.example.thing:artifact1:1.1");
		ArtifactName artifact2 = new ArtifactName("com.example.thing:artifact2:1.1");
		manager.add(artifact1, artifact2);
		manager.add(artifact1, new ArtifactName("com.example.thing:artifact3:1.1"));
		manager.add(artifact2, new ArtifactName("com.example.thing:artifact4:1.1"));

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "SimpleArtifactGraph.svg");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<ArtifactName> grapher = new Grapher<>(manager);
			ArtifactShape<ArtifactName> shape = new ArtifactShape<>();
			shape.setLabelStrategy(new MavenArtifactLabelStrategy());
			grapher.setShape(shape);
			grapher.createSvg(output);
		} finally {
			output.close();
		}
	}
}
