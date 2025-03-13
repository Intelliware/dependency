package com.electricmind.dependency.sample.jdepend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.electricmind.dependency.Coupling;
import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.LayeredGraph;
import com.electricmind.dependency.Node;

class PlantUmlExporter {
	
	private File file;

	public PlantUmlExporter(File file) {
		this.file = file;
	}

	public void export(DependencyManager<String> dependencyManager) throws IOException {
		try (Writer writer = new FileWriter(this.file)) {
			writer.write("@startuml\n\n");
			
			LayeredGraph<String> graph = dependencyManager.getLayeredGraph();
			for (Layer<Node<String>> layer : graph.getLayers()) {
				
				for (Node<String> node : layer.getContents()) {
					writer.write("package " + node.getName() + "\n");
				}
			}
			writer.write("\n");
			for (Layer<Node<String>> layer : graph.getLayers()) {
				for (Node<String> node : layer.getContents()) {
					for (Coupling<String> dependant : node.getAfferentCouplings()) {
						writer.write("" + dependant.getItem() + " -down-> " + node.getName() + "\n");
					}
				}
			}
			
			writer.write("@enduml\n");
		}		
	}
}
