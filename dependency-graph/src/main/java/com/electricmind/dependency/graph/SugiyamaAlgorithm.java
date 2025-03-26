package com.electricmind.dependency.graph;

import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.LayeredGraph;

/**
 * <p>An implementation of Sugiyama's algorithm for graph-drawing.
 *
 * <p>Sugiyama's algorithm for drawing layered, directional diagrams is a very
 * commonly-used method in graph-drawing software.  It was devised by Kozo Sugiyama,
 * and two of his colleagues and is cited extensively.  The algorithm has four
 * main stages:
 *
 * <ol>
 * <li>Cycle removal: First, we consider cycle removal, the attempt to take a set of
 * relationships, and reverse some dependencies until the relationships are acyclic.
 *
 * <li>Layer assignment and normalization: Next, we assign layers to the nodes of the
 * graph.  Also, at this time, we attempt to normalize the graph by introducing
 * "dummy vertices" that help us handle relationships that span multiple layers.
 *
 * <li>Crossing reduction: Thirdly, we iterate over each layer multiple times,
 * seeking to reorder the nodes with an eye to avoiding lines that cross.
 *
 * <li>Horizontal coordinate assignment: Lastly, we assign an x value to each item
 * in the layer in an attempt to produce the most visually-pleasing graph.
 * </ol>
 *
 * @author BC Holmes
 */
class SugiyamaAlgorithm {
	
	private Log log = LogFactory.getLog(getClass());

	public Graph apply(DependencyManager<?> dependencyManager) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		removeCycles(dependencyManager);
		Graph graph = assignLayers(dependencyManager);
		this.log.info("Layer assignment took " + stopWatch.getDuration() + " ms");
		reduceCrossings(graph);
		assignHorizontalCoordinates(graph);

		stopWatch.stop();
		LayeredGraph<?> layeredGraph = dependencyManager.getLayeredGraph();
		this.log.info("Graph with " + layeredGraph.getNodes().size() + " nodes and " 
				+ layeredGraph.getLayers().size() + " layers processed in " 
				+ stopWatch.getDuration() + " ms");
		return graph;
	}

	private void assignHorizontalCoordinates(Graph graph) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		stopWatch.stop();
		this.log.info("Horizontal coordinate assignment took " + stopWatch.getDuration() + " ms");
	}

	private void cycleBottomToTop(Graph graph) {
		List<GraphLayer> layers = graph.getLayers();
		for (int i = 1, length = layers.size(); i < length; i++) {
			arrangeVerticesInLayer(layers, i, i-1);
		}
	}

	private void cycleTopToBottom(Graph graph) {
		List<GraphLayer> layers = graph.getLayers();
		for (int i = layers.size() - 2; i >= 0; i--) {
			arrangeVerticesInLayer(layers, i, i+1);
		}
	}

	private void arrangeVerticesInLayer(List<GraphLayer> layers, int currentLayer, int previousLayer) {
		GraphLayer current = layers.get(currentLayer);
		double max = 0;
		for (Vertex vertex : current.getVertices()) {
			List<Vertex> neighbours = vertex.getNeighboursInLayer(previousLayer);
			double position = calculateBarycenter(neighbours, max);
			max = Math.max(position, max);
			vertex.setBarycenter(position);
		}
		current.orderVertices();
	}

	private double calculateBarycenter(List<Vertex> neighbours, double max) {
		if (neighbours == null || neighbours.size() == 0) {
			return max+1;
		} else {
			double total = 0;
			for (Vertex vertex : neighbours) {
				total += vertex.getOrdinal();
			}
			return total / neighbours.size();
		}
	}

	private void reduceCrossings(Graph graph) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		GraphLayer layer = graph.getTop();
		layer.assignArbitraryOrder();

		do {
			graph.resetChanges();
			cycleTopToBottom(graph);
			cycleBottomToTop(graph);
			orderLayers(graph);
		} while (graph.hasChanged());
		this.log.info("Crossing reduction took " + stopWatch.getDuration() + " ms");
	}

	private void orderLayers(Graph graph) {
		for (GraphLayer layer : graph.getLayers()) {
			layer.orderVertices();
		}
	}

	private Graph assignLayers(DependencyManager<?> dependencyManager) {
		return Graph.createGraph(dependencyManager);
	}

	private void removeCycles(DependencyManager<?> dependencyManager) {
		// the dependency manager class is able to take care of the cycles
	}
}
