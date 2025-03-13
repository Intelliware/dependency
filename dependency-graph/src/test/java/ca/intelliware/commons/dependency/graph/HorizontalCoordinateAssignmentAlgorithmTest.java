package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import ca.intelliware.commons.dependency.graph.HorizontalCoordinateAssignmentAlgorithm.ConflictType;

public class HorizontalCoordinateAssignmentAlgorithmTest {
	
	@Test
	public void testEdgeMarking() throws Exception {
		
		MockVertex v1 = new MockVertex("v1", 0, 0);
		MockVertex v2 = new MockVertex("v2", 0, 1);
		MockVertex v3 = new MockVertex("v3", 0, 2);
		MockVertex v4 = new MockVertex("v4", 1, 0);
		MockVertex v5 = new MockVertex("v5", 1, 1);
		MockVertex v6 = new MockVertex("v6", 1, 2, true);
		MockVertex v7 = new MockVertex("v7", 1, 3, true);
		MockVertex v8 = new MockVertex("v8", 1, 4, true);
		MockVertex v9 = new MockVertex("v9", 1, 5);
		MockVertex v10 = new MockVertex("v10", 1, 6, true);
		
		Edge edge1 = makeEdge(v1, v4);
		Edge edge2 = makeEdge(v1, v6);
		makeEdge(v2, v4);
		makeEdge(v2, v5);
		makeEdge(v3, v7);
		Edge edge3 = makeEdge(v3, v8);
		makeEdge(v3, v9);
		makeEdge(v3, v10);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
		HorizontalCoordinateAssignmentAlgorithm algorithm = new HorizontalCoordinateAssignmentAlgorithm();
		
		Map<Edge, ConflictType> conflicts = algorithm.determineConflictsInLayer(graph.getLayers().get(0), 1, Bias.LEFTWARD_UPPER);
		
		System.out.println(conflicts);
		System.out.println(edge2);
		
		assertEquals("edge 1", ConflictType.NO_CONFLICT, conflicts.get(edge1));
		assertEquals("edge 2", ConflictType.TYPE_0, conflicts.get(edge2));
		assertEquals("edge 3", ConflictType.NO_CONFLICT, conflicts.get(edge3));
	}
	
	@Test
	public void testEdgeMarkingRightward() throws Exception {
		
		MockVertex v1 = new MockVertex("v1", 0, 0);
		MockVertex v2 = new MockVertex("v2", 0, 1);
		MockVertex v3 = new MockVertex("v3", 0, 2);
		MockVertex v4 = new MockVertex("v4", 1, 0);
		MockVertex v5 = new MockVertex("v5", 1, 1);
		MockVertex v6 = new MockVertex("v6", 1, 2, true);
		MockVertex v7 = new MockVertex("v7", 1, 3, true);
		MockVertex v8 = new MockVertex("v8", 1, 4, true);
		MockVertex v9 = new MockVertex("v9", 1, 5);
		MockVertex v10 = new MockVertex("v10", 1, 6, true);
		
		Edge edge1 = makeEdge(v1, v4);
		Edge edge2 = makeEdge(v1, v6);
		makeEdge(v2, v4);
		Edge edge3 = makeEdge(v2, v5);
		makeEdge(v3, v7);
		makeEdge(v3, v8);
		Edge edge4 = makeEdge(v3, v9);
		makeEdge(v3, v10);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);
		HorizontalCoordinateAssignmentAlgorithm algorithm = new HorizontalCoordinateAssignmentAlgorithm();
		
		Map<Edge, ConflictType> conflicts = algorithm.determineConflictsInLayer(graph.getLayers().get(0), 1, Bias.RIGHTWARD_UPPER);
		
		assertEquals("edge 1", ConflictType.NO_CONFLICT, conflicts.get(edge1));
		assertEquals("edge 2", ConflictType.TYPE_0, conflicts.get(edge2));
		assertEquals("edge 3", ConflictType.NO_CONFLICT, conflicts.get(edge3));
		assertEquals("edge 4", ConflictType.NO_CONFLICT, conflicts.get(edge4));
	}
	
	@Test
	public void testCoordinateImpl() throws Exception {
		
		HorizontalCoordinateImpl coordinate = 
			new HorizontalCoordinateImpl();
		
		coordinate.setPosition(7, Bias.LEFTWARD_UPPER);
		coordinate.setPosition(10, Bias.RIGHTWARD_UPPER);
		coordinate.setPosition(5, Bias.LEFTWARD_LOWER);
		coordinate.setPosition(3, Bias.RIGHTWARD_LOWER);
		
		assertEquals("position", 6.0, coordinate.getPosition(), 0.0001);
	}
	
	@Test
	public void testSimpleCase() throws Exception {
		MockVertex vertex1 = new MockVertex("one", 0, 0);
		MockVertex vertex2 = new MockVertex("two", 0, 1);
		MockVertex vertex3 = new MockVertex("three", 1, 0);
		makeEdge(vertex2, vertex3);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(vertex1, vertex2, vertex3);
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		
		double[] expected = new double[] { 0.5, 1.5, 1.5 };
		List<Vertex> vertices = graph.getAllVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);
			assertEquals(vertex.toString(), expected[i], vertex.getHorizontalCoordinate().getPosition(), 0.0001);
		}
	}

	private Edge makeEdge(MockVertex v1, MockVertex v2) {
		v2.addNeighbour(v1);
		v1.addNeighbour(v2);
		
		return new Edge(v1, v2);
	}

	@Test
	public void testTrivial() throws Exception {
		MockVertex vertex1 = new MockVertex("one", 0, 0);
		MockVertex vertex2 = new MockVertex("two", 1, 0);
		makeEdge(vertex2, vertex1);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(vertex1, vertex2);
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		
		double[] expected = new double[] { 0.5, 0.5 };
		List<Vertex> vertices = graph.getAllVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);
			assertEquals(vertex.toString(), expected[i], vertex.getHorizontalCoordinate().getPosition(), 0.0001);
		}
	}
	
	/*
	 * This test is meant to handle a situation that looks like this:
	 * 
	 * Layer 1 :       (3)       (6)
	 *                  |         |
	 *                  |         |
	 *                  v         v
	 * Layer 0 :  (1)  (2)  (4)  (5)
	 * 
	 */
	@Test
	public void testSlightlyLargerSimpleCase() throws Exception {
		MockVertex v1 = new MockVertex("one", 0, 0);
		MockVertex v2 = new MockVertex("two", 0, 1);
		MockVertex v3 = new MockVertex("three", 1, 0);
		MockVertex v4 = new MockVertex("four", 0, 2);
		MockVertex v5 = new MockVertex("five", 0, 3);
		MockVertex v6 = new MockVertex("six", 1, 1);
		makeEdge(v2, v3);
		makeEdge(v6, v5);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(v1, v2, v3, v4, v5, v6);
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		
		double[] expected = new double[] { 0, 1, 1, 2, 3, 3 };
		List<Vertex> vertices = graph.getAllVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);
			assertEquals(vertex.toString(), expected[i] + 0.5, vertex.getHorizontalCoordinate().getPosition(),0.0001);
		}
	}

	/*
	 * This example is taken from the paper cited in the Javadoc for 
	 * HorizontalCoordinateAssignmentAlgorithm, but conflicts have been
	 * removed for simplicity
	 */
	@Test
	public void testCaseRequiringHorizontalCompaction() throws Exception {
		MockVertex v1 = new MockVertex("1", 0, 0);
		MockVertex v2 = new MockVertex("2", 0, 1);
		MockVertex v3 = new MockVertex("3", 0, 2);
		MockVertex v4 = new MockVertex("4", 1, 0);
		MockVertex v5 = new MockVertex("5", 1, 1);
		MockVertex v6 = new MockVertex("6", 1, 2);
		MockVertex v7 = new MockVertex("7", 1, 3);
		MockVertex v8 = new MockVertex("8", 1, 4);
		MockVertex v9 = new MockVertex("9", 1, 5);
		MockVertex v10 = new MockVertex("10", 1, 10);
		MockVertex v11 = new MockVertex("11", 2, 0);
		MockVertex v12 = new MockVertex("12", 2, 1);
		MockVertex v13 = new MockVertex("13", 2, 2);
		MockVertex v14 = new MockVertex("14", 2, 3);
		MockVertex v15 = new MockVertex("15", 2, 4);
		MockVertex v16 = new MockVertex("16", 2, 5);
		MockVertex v17 = new MockVertex("17", 3, 0);
		MockVertex v18 = new MockVertex("18", 3, 1);
		MockVertex v19 = new MockVertex("19", 3, 2);
		MockVertex v20 = new MockVertex("20", 3, 3);
		MockVertex v21 = new MockVertex("21", 3, 4);
		MockVertex v22 = new MockVertex("22", 3, 5);
		MockVertex v23 = new MockVertex("23", 3, 6);
		MockVertex v24 = new MockVertex("24", 3, 7);
		MockVertex v25 = new MockVertex("25", 4, 0);
		MockVertex v26 = new MockVertex("26", 4, 1);

		makeEdge(v4, v1);
		makeEdge(v11, v4);
		
		makeEdge(v25, v17);
		
		makeEdge(v5, v2);
		
		makeEdge(v26, v19);

		makeEdge(v20, v12);
		
		makeEdge(v13, v7);
		makeEdge(v21, v13);
		
		makeEdge(v8, v3);
		makeEdge(v14, v8);
		makeEdge(v22, v14);
		
		makeEdge(v15, v9);
		makeEdge(v24, v15);
		
		makeEdge(v16, v10);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(v1, v2, v3, v4, v5, v6, 
				v7, v8, v9, v10, v11, v12, v13, v14, v15, v16, v17, v18, v19, v20,
				v21, v22, v23, v24, v25, v26);
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		
		double[] expected = new double[] { 1, 2, 5, 1, 2, 3, 4, 5, 7, 8, 1, 3, 4, 5, 7, 8, 0, 1, 2, 3, 4, 5, 6, 7, 0, 2 };
		List<Vertex> vertices = graph.getAllVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);
			assertEquals(vertex.toString(), expected[i] + 0.5, vertex.getHorizontalCoordinate().getPosition(), 0.0001);
		}
	}

	/*
	 * This test is meant to handle a situation that looks like this:
	 * 
	 * Layer 1 :       (3)     (6)
	 *                  |       |
	 *                  |    +--+-+
	 *                  v    v    v
	 * Layer 0 :  (1)  (2)  (4)  (5)
	 * 
	 */
	@Test
	public void testCaseWithType0Conflicts() throws Exception {
		MockVertex v1 = new MockVertex("one", 0, 0);
		MockVertex v2 = new MockVertex("two", 0, 1);
		MockVertex v3 = new MockVertex("three", 1, 0);
		MockVertex v4 = new MockVertex("four", 0, 2);
		MockVertex v5 = new MockVertex("five", 0, 3);
		MockVertex v6 = new MockVertex("six", 1, 1);
		makeEdge(v2, v3);
		makeEdge(v6, v4);
		makeEdge(v6, v5);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(v1, v2, v3, v4, v5, v6);
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		
		double[] expected = new double[] { 0, 1, 1, 2, 3, 2.5 };
		List<Vertex> vertices = graph.getAllVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);
			assertEquals(vertex.toString(), expected[i] +0.5, vertex.getHorizontalCoordinate().getPosition(), 0.0001);
		}
	}
	
	@Test
	public void testCaseWithType1Conflicts() throws Exception {
		MockVertex v1 = new MockVertex("one", 0, 0);
		MockVertex v2 = new MockVertex("two", 0, 1);
		MockVertex v3 = new MockVertex("three", 1, 0);
		MockVertex v3a = new MockVertex("three-a", 1, 1, true);
		MockVertex v4 = new MockVertex("four", 2, 0);
		MockVertex v5 = new MockVertex("five", 2, 1, true);
		MockVertex v6 = new MockVertex("six", 2, 2);
		MockVertex v7 = new MockVertex("seven", 3, 0);
		MockVertex v8 = new MockVertex("eight", 3, 1);
		
		makeEdge(v3, v1);
		makeEdge(v3, v4);
		makeEdge(v7, v4);
		makeEdge(v2, v3a);
		makeEdge(v5, v3a);
		makeEdge(v5, v8);
		makeEdge(v6, v8);
		makeEdge(v6, v3);
		
		MockSugiyamaGraph graph = new MockSugiyamaGraph(v1, v2, v3, v3a, v4, v5, v6, v7, v8);
		new HorizontalCoordinateAssignmentAlgorithm().process(graph);
		
		List<Vertex> vertices = graph.getAllVertices();
		assertEquals("inner segment", v3a.getX(), v5.getX(), 0.0001);
		for (int i = 0; i < vertices.size(); i++) {
			Vertex vertex = vertices.get(i);
			System.out.println(vertex.toString() + " " + vertex.getHorizontalCoordinate().getPosition());
		}
	}
}
