package com.electricmind.dependency.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.electricmind.dependency.DependencyManager;

public class GraphTest {

	@Test
	public void testOrderLayer() throws Exception {
		Graph graph = Graph.createGraph(createDependencies());
		GraphLayer layer = graph.getTop();
		layer.assignArbitraryOrder();

		assertEquals("size", 2, layer.getVertices().size());
		assertOrdinal("ordinals", 2, layer.getVertices());
	}

	@Test
	public void testGetNeighboursInLowerLayer() throws Exception {
		Graph graph = Graph.createGraph(createDependencies());
		Vertex vertex = getVertex(graph, "fred");
		assertNotNull("vertex", vertex);

		List<Vertex> neighbours = vertex.getNeighboursInLayer(0);
		assertNotNull("neighbours", neighbours);
		assertEquals("number of neighbours", 1, neighbours.size());
		assertEquals("neighbour: " + neighbours, "mr_slate", ((Graph.BasicVertex) neighbours.get(0)).getNode().getItem());
	}

	@Test
	public void testGetDummyNeighboursInUpperLayer() throws Exception {
		Graph graph = Graph.createGraph(createDependencies());
		Vertex vertex = getVertex(graph, "wilma");
		assertNotNull("vertex", vertex);

		List<Vertex> neighbours = vertex.getNeighboursInLayer(1);
		assertNotNull("neighbours", neighbours);
		assertEquals("number of neighbours", 1, neighbours.size());
		assertTrue("vertex type", neighbours.get(0) instanceof DummyVertex);
	}

	@Test
	public void testNoNeighboursInUpperLayer() throws Exception {
		Graph graph = Graph.createGraph(createDependencies());
		Vertex vertex = getVertex(graph, "bam-bam");
		assertNotNull("vertex", vertex);

		List<Vertex> neighbours = vertex.getNeighboursInLayer(2);
		assertNotNull("neighbours", neighbours);
		assertEquals("number of neighbours", 0, neighbours.size());
	}

	@Test
	public void testGetNeighboursInUpperLayer() throws Exception {
		Graph graph = Graph.createGraph(createDependencies());
		Vertex vertex = getVertex(graph, "fred");
		assertNotNull("vertex", vertex);

		List<Vertex> neighbours = vertex.getNeighboursInLayer(2);
		assertNotNull("neighbours", neighbours);
		assertEquals("number of neighbours", 2, neighbours.size());

		List<Object> items = new ArrayList<Object>();
		for (Vertex neighbour : neighbours) {
			items.add(((Graph.BasicVertex) neighbour).getNode().getItem());
		}
		assertTrue("pebbles neighbour: " + items, items.contains("pebbles"));
		assertTrue("dino neighbour: " + items, items.contains("dino"));
	}

	private Vertex getVertex(Graph graph, String string) {
		for (GraphLayer layer : graph.getLayers()) {
			for (Vertex vertex : layer.getVertices()) {
				if (vertex instanceof Graph.BasicVertex && string.equals(((Graph.BasicVertex) vertex).getNode().getItem())) {
					return vertex;
				}
			}
		}
		return null;
	}

	private DependencyManager<String> createDependencies() {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("pebbles", "fred");
		manager.add("fred", "mr_slate");
		manager.add("pebbles", "wilma");
		manager.add("bam-bam", "barney");
		manager.add("bam-bam", "betty");
		manager.add("dino", "fred");
		return manager;
	}

	private void assertOrdinal(String string, int size, List<Vertex> vertices) {
		List<Integer> ordinals = new ArrayList<Integer>();
		for (Vertex vertex : vertices) {
			ordinals.add(vertex.getOrdinal());
		}
		for (int i = 0; i < size; i++) {
			assertEquals(string + "[" + i + "]", new Integer(i), ordinals.get(i));
		}
	}
}
