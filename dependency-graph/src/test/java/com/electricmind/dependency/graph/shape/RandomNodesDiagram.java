package com.electricmind.dependency.graph.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class RandomNodesDiagram {
	
	private final int nodes;
	private final int edges;
	private Random random;

	public RandomNodesDiagram(int nodes, int edges) {
		this.nodes = nodes;
		this.edges = edges;
		this.random = new Random();
	}
	public static void main(String[] args) throws Exception {
		new RandomNodesDiagram(20, 100).process();
		new RandomNodesDiagram(30, 150).process();
		new RandomNodesDiagram(50, 400).process();
	}

	private void process() throws IOException {
		DependencyManager<String> manager = new DependencyManager<String>();

		int edgesRemaining = this.edges;
		Set<Integer> nodes = new HashSet<Integer>();
		for (int i = 0; i < this.nodes; i++) {
			manager.add(String.valueOf(i));

			if (i > 0) {
				int numberOfEdges = (i == this.nodes-1) ? edgesRemaining : Math.min(this.random.nextInt(nodes.size()), edgesRemaining);
				
				edgesRemaining -= numberOfEdges;
				List<Integer> temp = new ArrayList<Integer>(nodes);
				for (int j = 0; j < numberOfEdges; j++) {
					if (temp.size() > 0) {
						manager.add(String.valueOf(i), String.valueOf(temp.remove((int) this.random.nextInt(temp.size()))));
					}
				}
			}
			nodes.add(i);
		}
		System.out.println("Starting to graph");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "Random" + this.nodes + "x" + this.edges + ".png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.getPlot().setShapeFillColor(new Color(216, 223, 238));
			grapher.getShape().setDimension(new Dimension(40, 40));
			grapher.createPng(output);
		} finally {
			output.close();
		}
	}

}
