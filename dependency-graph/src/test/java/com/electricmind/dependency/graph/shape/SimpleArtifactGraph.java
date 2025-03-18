package com.electricmind.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class SimpleArtifactGraph {
	
	public static void main(String[] args) throws Exception {
		new SimpleArtifactGraph().process();
	}

	private void process() throws FileNotFoundException, IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("artifact1", "artifact2");
		manager.add("artifact1", "artifact3");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "SimpleArtifactGraph.svg");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.setShape(new ArtifactShape<String>());
			grapher.createSvg(output);
		} finally {
			output.close();
		}
	}
}
