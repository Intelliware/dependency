package com.electricmind.dependency.sample.npm;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;
import com.electricmind.dependency.graph.shape.ArtifactNpmStereotypeShapeProvider;
import com.electricmind.dependency.graph.shape.ArtifactShape;
import com.electricmind.dependency.graph.shape.MavenArtifactLabelStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleNpmDependencies {

	public static void main(String[] args) throws Exception {

		DependencyManager<NpmPackageName> dependencies = new DependencyManager<>();

		try (InputStream input = SimpleNpmDependencies.class.getResourceAsStream("/package-lock.json")) {
			Map<String, NpmPackageName> map = new HashMap<>();
			PackageLock packageLock = new ObjectMapper().readValue(input, PackageLock.class);
			for (Map.Entry<String, PackageInfo> entry : packageLock.getPackages().entrySet()) {
				String name = entry.getKey();				
				if (!"".equals(name)) {
					PackageInfo info = entry.getValue();
					NpmPackageName packageName = new NpmPackageName(StringUtils.substringAfter(name, "node_modules/"), info.getVersion());
					map.put(packageName.getName(), packageName);
				}
			}

			for (Map.Entry<String, PackageInfo> entry : packageLock.getPackages().entrySet()) {
				String name = entry.getKey();				
				PackageInfo info = entry.getValue();
				name = "".equals(name) ? info.getName() : StringUtils.substringAfter(name, "node_modules/");
				NpmPackageName packageName = new NpmPackageName(name, info.getVersion());

				for (String dependency : info.getDependencies().keySet()) {
					NpmPackageName dependencyName = map.get(dependency);
					
					dependencies.add(packageName, dependencyName);
				}
			}
		}
		createGraphDiagram(dependencies);
	}

	private static void createGraphDiagram(DependencyManager<NpmPackageName> dependencies) throws IOException {
		Grapher<NpmPackageName> grapher = new Grapher<>(dependencies);
		grapher.getPlot().setShadowColor(new Color(0f, 0f, 0f, 0.4f));
		grapher.getPlot().setLayerAlternatingColor(new Color(203, 219, 252));
		grapher.getPlot().setUseWeights(false);
		ArtifactShape<NpmPackageName> shape = new ArtifactShape<>();
		shape.setLabelStrategy(new NpmPackageLabelStrategy());
		shape.setStereotypeShapeProvider(new ArtifactNpmStereotypeShapeProvider());
		grapher.setShape(shape);
		try (OutputStream output = new FileOutputStream("./target/npmArtifactGraph.svg")) {
			grapher.createSvg(output);
		}
	}
}
