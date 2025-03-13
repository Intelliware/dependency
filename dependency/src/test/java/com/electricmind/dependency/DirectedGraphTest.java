package com.electricmind.dependency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DirectedGraphTest extends DependencyTestCase {

	@Test
	public void testIsNotAcyclic() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithCycles();
		assertFalse("acyclic", graph.isAcyclic());
	}
	@Test
	public void testIsNotAcyclicTrivialCase() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithSelfCycle();
		assertFalse("acyclic", graph.isAcyclic());
	}
	@Test
	public void testIsAcyclic() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithNoCycles();
		assertTrue("acyclic", graph.isAcyclic());
	}
	@Test
	public void testIsSink() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithCycles();
		assertTrue("sink", graph.isSink("play-doh"));
		assertFalse("not sink", graph.isSink("bc"));
	}
	@Test
	public void testIsSouce() throws Exception {
		DirectedGraph<String> graph = createSimpleGraphWithCycles();
		assertFalse("not source", graph.isSource("play-doh"));
		assertFalse("source", graph.isSource("bc"));
	}
}
