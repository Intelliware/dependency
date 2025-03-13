package ca.intelliware.commons.dependency.graph;

import java.util.Comparator;

class OrdinalComparator implements Comparator<Vertex> {
	public int compare(Vertex o1, Vertex o2) {
		return o1.getOrdinal() - o2.getOrdinal();
	}
}