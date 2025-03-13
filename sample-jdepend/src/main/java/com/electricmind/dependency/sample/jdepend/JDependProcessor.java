package com.electricmind.dependency.sample.jdepend;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.Grapher;
import com.electricmind.dependency.graph.shape.PackageShape;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import jdepend.framework.PackageFilter;

public class JDependProcessor {

	private final String type;
	private final File directory;

	public JDependProcessor(String type, File directory) {
		this.type = type;
		this.directory = directory;
	}

	public static void main(String[] args) throws Exception {

		String directory = "../dependency-graph";
		JDependProcessor processor = new JDependProcessor(
				"dependencyGraph", new File(directory));
		processor.process();
		

	}

	public DependencyManager<String> process() throws IOException {
		Set<String> packageNames = getPackageNames();

		PackageFilter filter = PackageFilter.all().including(packageNames).excludingRest();
		JDepend jdepend = new JDepend(filter);
		jdepend.addDirectory(new File(this.directory, "target/classes").getAbsolutePath());

		Collection<JavaPackage> jdepentResults = jdepend.analyze();
		DependencyManager<String> dependencyManager = createDependencyGraph(packageNames, jdepentResults);
		printLayers(dependencyManager);
		
		createGraph(dependencyManager);
		new PlantUmlExporter(new File("./target/" + this.type + "PackageGraphPlantuml.puml")).export(dependencyManager);
		new HtmlDependencyChartExporter(new File("./target/" + this.type + "PackageGraphTable.html")).export(dependencyManager, jdepentResults);

		return dependencyManager;
	}

	private void createGraph(DependencyManager<String> dependencyManager)
			throws IOException {
		
		File file = new File("./target/" + this.type + "PackageGraph.svg");
		try (OutputStream output = new FileOutputStream(file)) {
			Grapher<String> grapher = new Grapher<>(dependencyManager);
			grapher.setStripeColour(new Color(0xE9, 0xD9, 0xFE));
			grapher.setShape(new PackageShape<String>());
			Dimension d = grapher.createSvg(output);
			System.out.println(d);
		}
	}

	private Set<String> getPackageNames() throws IOException {
		Set<String> result = new HashSet<String>();
		result.addAll(getPackageNames(new File(this.directory, "target/classes")));
		return result;
	}

	private static DependencyManager<String> createDependencyGraph(Set<String> packageNames, Collection<JavaPackage> javaPackages) {
		DependencyManager<String> dependencyManager = new DependencyManager<String>();
		for (JavaPackage javaPackage : javaPackages) {
			dependencyManager.add(javaPackage.getName());
			Collection<JavaPackage> efferents = javaPackage.getEfferents();
			for (JavaPackage efferent : efferents) {
				dependencyManager.add(javaPackage.getName(), efferent.getName(), 
						ClassDependency.dependencyWeight(javaPackage, efferent));
			}
		}
		return dependencyManager;
	}


	private static void printLayers(DependencyManager<String> dependencyManager) {
		List<Layer<Node<String>>> nodeLayers = dependencyManager.getNodeLayers();
		for (Layer<Node<String>> layer : nodeLayers) {
			System.out.println("Layer " + layer.getLevel() + " contains " + layer.getContents());
		}
	}

	private Set<String> getPackageNames(File directory) throws IOException {
		return new PackageFinder(directory).findAllPackages();
	}

	public String getType() {
		return this.type;
	}
}
