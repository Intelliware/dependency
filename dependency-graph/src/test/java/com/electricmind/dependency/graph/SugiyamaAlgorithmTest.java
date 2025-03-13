package com.electricmind.dependency.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Graph;
import com.electricmind.dependency.graph.GraphLayer;
import com.electricmind.dependency.graph.SugiyamaAlgorithm;
import com.electricmind.dependency.graph.Vertex;

public class SugiyamaAlgorithmTest {

	@Test
	public void testExampleWithSimpleLayout() throws Exception {
		DependencyManager<String> manager = createDependencies();
		Graph graph = new SugiyamaAlgorithm().apply(manager);
		assertNotNull("graph", graph);
		assertEquals("number of layers", 3, graph.getLayers().size());
	}

	@Test
	public void testLayers() throws Exception {
		DependencyManager<String> manager = createDependencies();
		Graph graph = new SugiyamaAlgorithm().apply(manager);

		for (GraphLayer layer : graph.getLayers()) {
			layer.orderVertices();
			for (Vertex vertex : layer.getVertices()) {
				System.out.println(layer.getLevelNumber() + " " + vertex.getOrdinal() + " " + vertex);
			}
		}
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
}
