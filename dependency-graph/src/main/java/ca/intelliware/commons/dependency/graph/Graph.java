package ca.intelliware.commons.dependency.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.electricmind.dependency.Coupling;
import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.Node;

class Graph implements SugiyamaGraph {

	class BasicVertex extends Vertex {

		private final Node<?> node;

		BasicVertex(int layer, Node<?> node) {
			super(layer);
			this.node = node;
		}

		@Override
		public List<Vertex> getNeighboursInLayer(int layer) {
			List<Vertex> result = new ArrayList<Vertex>();
			List<Vertex> list = Graph.this.vertices.get(layer);
			for (Vertex vertex : list == null ? new ArrayList<Vertex>() : list) {
				if (vertex.getLayer() == layer && isNeighbour(vertex)) {
					result.add(vertex);
				}
			}
			return result;
		}

		public String toString() {
			return this.node.getName();
		}

		@Override
		boolean isNeighbour(Vertex vertex) {
			if (vertex == null) {
				return false;
			} else if (vertex instanceof BasicVertex) {
				Object object = ((BasicVertex) vertex).node.getItem();
				return this.node.getAfferentCouplings().stream().map(c -> c.getItem()).collect(Collectors.toList()).contains(object) 
						|| this.node.getEfferentCouplings().stream().map(c -> c.getItem()).collect(Collectors.toList()).contains(object);
			} else {
				return vertex.isNeighbour(this);
			}
		}
		
		boolean isBidirectionalWith(BasicVertex vertex) {
			return this.node.getAfferentCouplings().contains(vertex.node.getItem()) 
					&& this.node.getEfferentCouplings().contains(vertex.node.getItem());
		}

		Node<?> getNode() {
			return this.node;
		}
	}

	private Map<Integer,List<Vertex>> vertices = Collections.synchronizedMap(new TreeMap<Integer,List<Vertex>>());

	public List<GraphLayer> getLayers() {
		return convert(this.vertices);
	}

	private List<GraphLayer> convert(Map<Integer, List<Vertex>> layers) {
		List<GraphLayer> result = new ArrayList<GraphLayer>();
		for (Integer layer : new TreeSet<Integer>(layers.keySet())) {
			result.add(new GraphLayer(layer, layers.get(layer)));
		}
		return result;
	}

	public GraphLayer getTop() {
		List<GraphLayer> layers = getLayers();
		return layers.isEmpty() ? null : layers.get(layers.size()-1);
	}


	static <T> Graph createGraph(DependencyManager<T> dependencyManager) {
		Map<Object,Vertex> map = new HashMap<Object, Vertex>();
		Graph graph = new Graph();
		List<Layer<Node<T>>> nodeLayers = dependencyManager.getNodeLayers();
		for (int i = 0, length = nodeLayers == null ? 0 : nodeLayers.size(); i < length; i++) {
			Layer<Node<T>> layer = nodeLayers.get(i);
			for (Node<T> node : layer.getContents()) {
				BasicVertex vertex = graph.new BasicVertex(i, node);
				graph.add(vertex);

				map.put(node.getItem(), vertex);
			}
		}
		
		for (Layer<Node<T>> layer : nodeLayers) {
			for (Node<T> node : layer.getContents()) {
				Vertex vertex = map.get(node.getItem());
				createDummyVertices(map, node, vertex, graph);
			}
		}
		return graph;
	}

	private void add(Vertex vertex) {
		if (!this.vertices.containsKey(vertex.getLayer())) {
			this.vertices.put(vertex.getLayer(), new ArrayList<Vertex>());
		}
		this.vertices.get(vertex.getLayer()).add(vertex);
	}

	private static <T> void createDummyVertices(Map<Object, Vertex> map, Node<T> node, Vertex top, Graph graph) {
		for (Coupling<?> dependency : node.getEfferentCouplings()) {
			Vertex bottom = map.get(dependency.getItem());
			if (node.getLayer() - bottom.getLayer() > 1) {
				createDummyVertices(node.getLayer(), top, bottom, graph);
			} else if (bottom.getLayer() - node.getLayer() > 1) {
				createDummyVertices(node.getLayer(), top, bottom, graph);
			}
		}
	}

	private static void createDummyVertices(int currentLayer, Vertex top,
			Vertex bottom, Graph graph) {
		int sign = getSign(top.getLayer() - bottom.getLayer());
		DummyVertex dependent = null;
		for (int l = bottom.getLayer() + sign; l * sign < currentLayer * sign; l += sign) {
			DummyVertex upper = new DummyVertex(l);
			graph.add(upper);
			if (dependent != null) {
				dependent.setUpper(upper);
				upper.setLower(dependent);
			} else {
				upper.setLower(bottom);
			}
			dependent = upper;
		}
		dependent.setUpper(top);
	}

	private static int getSign(int value) {
		if (value < 0) {
			return -1;
		} else if (value > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	boolean hasChanged() {
		boolean changed = false;
		for (Vertex vertex : getAllVertices()) {
			changed |= vertex.hasChanged();
			if (changed) {
				break;
			}
		}
		return changed;
	}

	public List<Vertex> getAllVertices() {
		List<Vertex> result = new ArrayList<Vertex>();
		for (List<Vertex> list : this.vertices.values()) {
			result.addAll(list);
		}
		return result;
	}

	public void resetChanges() {
		for (Vertex vertex : getAllVertices()) {
			vertex.resetChanges();
		}
	}

	public double getWidth() {
		double width = 0;
		for (Vertex vertex : getAllVertices()) {
			width = Math.max(width, vertex.getHorizontalCoordinate().getPosition() + vertex.getWidth() / 2.0);
		}
		return width;
	}

	public double getHeight() {
		return getLayers().size() + 1.0;
	}
}
