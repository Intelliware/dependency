package ca.intelliware.commons.dependency.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.CompareToBuilder;

public class MockVertex extends Vertex implements Comparable<MockVertex> {
	
	private Map<Integer, List<Vertex>> neighbours = new HashMap<Integer, List<Vertex>>();
	private final String name;
	private final boolean isDummy;
	
	public MockVertex(String name, int layer, int ordinal) {
		this(name, layer, ordinal, false);
	}
	public MockVertex(String name, int layer, int ordinal, boolean isDummy) {
		super(layer);
		this.name = name;
		this.isDummy = isDummy;
		setOrdinal(ordinal);
	}

	@Override
	public List<Vertex> getNeighboursInLayer(int layer) {
		List<Vertex> list = this.neighbours.get(layer);
		return list == null ? new ArrayList<Vertex>() : new ArrayList<Vertex>(list);
	}
	
	void addNeighbour(Vertex vertex) {
		int layer = vertex.getLayer();
		if (!this.neighbours.containsKey(layer)) {
			this.neighbours.put(layer, new ArrayList<Vertex>());
		}
		this.neighbours.get(layer).add(vertex);
	}

	@Override
	boolean isNeighbour(Vertex vertex) {
		throw new NotImplementedException();
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return getName();
	}

	public int compareTo(MockVertex o) {
		return new CompareToBuilder().append(this.getOrdinal(), o.getOrdinal())
			.append(this.getLayer(), o.getLayer()).toComparison();
	}
	@Override
	public boolean isDummy() {
		return this.isDummy;
	}
}
