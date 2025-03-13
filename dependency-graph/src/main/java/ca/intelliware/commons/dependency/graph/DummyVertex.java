package ca.intelliware.commons.dependency.graph;

import java.util.Arrays;
import java.util.List;

class DummyVertex extends Vertex {

	private Vertex upper;
	private Vertex lower;

	DummyVertex(int layer) {
		super(layer);
	}

	@Override
	public List<Vertex> getNeighboursInLayer(int layer) {
		if (this.upper != null && this.upper.getLayer() == layer) {
			return Arrays.asList(this.upper);
		} else if (this.lower != null && this.lower.getLayer() == layer) {
			return Arrays.asList(this.lower);
		} else {
			return Arrays.asList();
		}
	}

	@Override
	boolean isNeighbour(Vertex vertex) {
		return vertex != null && (vertex == this.upper || vertex == this.lower);
	}

	public Vertex getLower() {
		return this.lower;
	}

	public void setLower(Vertex next) {
		this.lower = next;
	}

	public Vertex getUpper() {
		return this.upper;
	}

	public void setUpper(Vertex previous) {
		this.upper = previous;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (this.upper != null && !(this.upper instanceof DummyVertex)) {
			builder.append(this.upper.toString());
			builder.append(" -> ");
		}
		builder.append(".");
		if (builder.length() > 0) {
			builder.append(" -> ");
		}
		builder.append(this.lower);
		return builder.toString();
	}

	@Override
	public boolean isDummy() {
		return true;
	}
}
