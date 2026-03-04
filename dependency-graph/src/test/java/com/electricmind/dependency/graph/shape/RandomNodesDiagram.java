package com.electricmind.dependency.graph.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class RandomNodesDiagram extends DependencyGraphTester<String> {

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


	@Override
	public Grapher<String> createGrapher(DependencyManager<String> manager) {
		Grapher<String> grapher = new Grapher<String>(manager);
		grapher.getPlot().setShapeFillColor(new Color(216, 223, 238));
		grapher.getShape().setDimension(new Dimension(40, 40));
		return grapher;
	}

	@Override
	public void graphAll(Grapher<String> grapher) throws IOException {
		super.graphToPng(grapher);
	}

	@Override
	public String getFileNameBase() {
		return "Random" + this.nodes + "x" + this.edges;
	}

	@Override
	public DependencyManager<String> createManager() {
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
		return manager;
	}
}
