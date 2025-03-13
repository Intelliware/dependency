package com.electricmind.dependency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class GreedyCycleRemovalAlgorithmTest extends DependencyTestCase {

	@Test
	public void testProducesAcyclicGraph() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithCycles();
		GreedyCycleRemovalAlgorithm<String> algorithm = new GreedyCycleRemovalAlgorithm<String>(graph);
		DirectedGraph<String> result = algorithm.execute();
		assertTrue("acyclic", result.isAcyclic());
	}

	@Test
	public void testVertexSequence() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithCycles();
		GreedyCycleRemovalAlgorithm<String> algorithm = new GreedyCycleRemovalAlgorithm<String>(graph);
		GreedyCycleRemovalAlgorithm<String>.VertexSequence sequence = algorithm.getVertexSequence();

		System.out.println(sequence);
		assertEquals("elements", "play-doh", sequence.getElements().get(5));
		assertFalse("not leftward", sequence.isLeftward("bc", "play-doh"));
		assertTrue("leftward", sequence.isLeftward("dion", "ken"));
	}
}
