package ca.intelliware.commons.dependency.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

class MockSugiyamaGraph implements SugiyamaGraph {

	private List<MockVertex> vertices;

	MockSugiyamaGraph(MockVertex... vertices) {
		this.vertices = Arrays.asList(vertices);
	}
	
	public List<Vertex> getAllVertices() {
		return new ArrayList<Vertex>(this.vertices);
	}

	public List<? extends OrderedLayer<Vertex>> getLayers() {
		List<OrderedLayer<Vertex>> result = new ArrayList<OrderedLayer<Vertex>>();
		for (int i = 0, length = getMaxLayer(); i <= length; i++) {
			result.add(getLayer(i));
		}
		return result;
	}

	private OrderedLayer<Vertex> getLayer(int layer) {
		TreeSet<Vertex> set = new TreeSet<Vertex>();
		for (MockVertex mockVertex : this.vertices) {
			if (mockVertex.getLayer() == layer) {
				set.add(mockVertex);
			}
		}
		return new OrderedLayer<Vertex>(layer, set);
	}

	private int getMaxLayer() {
		int result = 0;
		for (MockVertex vertex : this.vertices) {
			result = Math.max(result, vertex.getLayer());
		}
		return result;
	}
}
