package ca.intelliware.commons.dependency.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

class GraphLayer extends OrderedLayer<Vertex> {

	class PositionComparator implements Comparator<Vertex> {
		public int compare(Vertex o1, Vertex o2) {
			return new Double(o1.getBarycenter()).compareTo(o2.getBarycenter());
		}
	}
	private final int levelNumber;
	private final List<Vertex> vertices;

	GraphLayer(int levelNumber, List<Vertex> vertices) {
		super(levelNumber, toTreeSet(vertices));
		this.levelNumber = levelNumber;
		this.vertices = vertices;
	}

	private static SortedSet<Vertex> toTreeSet(List<Vertex> vertices) {
		SortedSet<Vertex> result = new TreeSet<Vertex>(new OrdinalComparator());
		result.addAll(vertices);
		return result;
	}

	public int getLevelNumber() {
		return this.levelNumber;
	}

	void assignArbitraryOrder() {
		for (int i = 0, length = this.vertices == null ? 0 : this.vertices.size(); i < length; i++) {
			getVertex(i).setOrdinal(i);
		}
	}

	private Vertex getVertex(int i) {
		return this.vertices.get(i);
	}

	void orderVertices() {
		Collections.sort(this.vertices, new PositionComparator());
		for (int i = 0, length = this.vertices == null ? 0 : this.vertices.size(); i < length; i++) {
			getVertex(i).setOrdinal(i);
		}
	}

	public List<Vertex> getVertices() {
		return this.vertices;
	}

	@Override
	SortedSet<Vertex> getOrderedContents() {
		return toTreeSet(this.vertices);
	}
}
