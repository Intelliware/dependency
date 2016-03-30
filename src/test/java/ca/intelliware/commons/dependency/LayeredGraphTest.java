package ca.intelliware.commons.dependency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;


public class LayeredGraphTest extends DependencyTestCase {

	@Test
	public void testGetLayers() throws Exception {
		LayeredGraph<String> graph = createSimpleLayeredGraphWithCycles();
		List<Layer<Node<String>>> layers = graph.getLayers();
		printLayers(layers);
		assertEquals("play-doh's layer", 0, graph.getLayer("play-doh"));
		assertEquals("ken's layer", 3, graph.getLayer("ken"));
		assertEquals("superman's layer", -1, graph.getLayer("superman"));
	}

	@Test
	public void testGetNode() throws Exception {
		LayeredGraph<String> graph = createSimpleLayeredGraphWithCycles();
		List<Layer<Node<String>>> layers = graph.getLayers();
		printLayers(layers);
		Node<String> node = graph.getNode("ken");
		assertEquals("ken's layer", 3, node.getLayer());
		assertFalse("ken has efferants", node.getEfferentCouplings().isEmpty());
	}
	
	@Test
	public void testGetLayersWithSelfCycle() throws Exception {
		LayeredGraph<String> graph = toLayeredGraph(createSimpleGraphWithSelfCycle());
		List<Layer<Node<String>>> layers = graph.getLayers();
		printLayers(layers);
		assertEquals("play-doh's layer", 0, graph.getLayer("play-doh"));
		assertEquals("ken's layer", 3, graph.getLayer("ken"));
		assertEquals("superman's layer", -1, graph.getLayer("superman"));
	}

	private void printLayers(List<Layer<Node<String>>> layers) {
		for (Layer<Node<String>> layer : layers) {
			System.out.println(layer.getLevel() + ": " + layer.getContents());
		}
		System.out.println();
	}

	protected LayeredGraph<String> createSimpleLayeredGraphWithCycles() {
		return toLayeredGraph(createSimpleGraphWithCycles());
	}

	private LayeredGraph<String> toLayeredGraph(DirectedGraph<String> graph) {
		return new LayeredGraph<String>(graph.getAll(), graph.getEfferentCouplings(), graph.getAfferentCouplings());
	}

}
