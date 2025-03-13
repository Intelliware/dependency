package ca.intelliware.commons.dependency.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class Edge implements Comparable<Edge> {
	private final int fromLayer;
	private final int fromOrdinal;
	private final int toLayer;
	private final int toOrdinal;
	private boolean fromDummy;
	private boolean toDummy;

	Edge(Vertex from, Vertex to) {
		this.fromLayer = from.getLayer();
		this.fromOrdinal = from.getOrdinal();
		this.toLayer = to.getLayer();
		this.toOrdinal = to.getOrdinal();
		this.fromDummy = from.isDummy();
		this.toDummy = to.isDummy();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj.getClass() != this.getClass()) {
			return false;
		} else {
			Edge that = (Edge) obj;
			return new EqualsBuilder()
					.append(this.fromLayer, that.fromLayer)
					.append(this.fromOrdinal, that.fromOrdinal)
					.append(this.fromDummy, that.fromDummy)
					.append(this.toLayer, that.toLayer)
					.append(this.toOrdinal, that.toOrdinal)
					.append(this.toDummy, that.toDummy)
					.isEquals();
		}
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.fromLayer)
			.append(this.fromOrdinal)
			.toHashCode();
	}

	public boolean crosses(Edge edge) {
		if (edge.equals(this)) {
			return false;
		} else if (this.fromLayer != edge.fromLayer || this.toLayer != edge.toLayer) {
			return false;
		} else if (this.fromOrdinal == edge.fromOrdinal) {
			return true;
		} else if (this.fromOrdinal > edge.fromOrdinal) {
			return this.toOrdinal <= edge.toOrdinal;
		} else if (this.toOrdinal >= edge.toOrdinal) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		return "[" + this.fromLayer + "," + this.fromOrdinal + "]->[" + 
			this.toLayer + "," + this.toOrdinal + "]";
	}
	
	public boolean isInnerSegment() {
		return this.fromDummy && this.toDummy;
	}

	public int getFromOrdinal() {
		return this.fromOrdinal;
	}

	public int getToOrdinal() {
		return this.toOrdinal;
	}
	
	static Set<Edge> getEdges(OrderedLayer<Vertex> from, int to) {
		Set<Edge> edges = new HashSet<Edge>();
		for (Vertex fromVertex : from.getContents()) {
			List<Vertex> neighbours = fromVertex.getNeighboursInLayer(to);
			for (Vertex neighbour : neighbours) {
				edges.add(new Edge(fromVertex, neighbour));
			}
		}
		return edges;
	}

	static Edge chooseConflictEdge(Bias bias, Edge edge1, Edge edge2) {
		if (bias.getHorizontal() == HorizontalAlignment.LEFT) {
			if (edge1.getFromOrdinal() < edge2.getFromOrdinal()) {
				return edge2;
			} else if (edge1.getFromOrdinal() == edge2.getFromOrdinal()
					&& edge1.getToOrdinal() < edge2.getToOrdinal()) {
				return edge2;
			} else {
				return edge1;
			}
		} else {
			if (edge1.getFromOrdinal() < edge2.getFromOrdinal()) {
				return edge1;
			} else if (edge1.getFromOrdinal() == edge2.getFromOrdinal()
					&& edge1.getToOrdinal() < edge2.getToOrdinal()) {
				return edge1;
			} else {
				return edge2;
			}
		}
	}

	public int compareTo(Edge that) {
		return new CompareToBuilder().append(this.fromOrdinal, that.fromOrdinal)
			.append(this.toOrdinal, that.toOrdinal)
			.append(this.fromLayer, that.fromLayer)
			.append(this.toLayer, that.toLayer)
			.append(this.fromDummy, that.fromDummy)
			.append(this.toDummy, that.toDummy)
			.toComparison();
	}

	public static boolean sameFrom(Edge edge, Edge otherEdge) {
		return edge.fromLayer == otherEdge.fromLayer
			&& edge.toLayer == otherEdge.toLayer
			&& edge.fromOrdinal == otherEdge.fromOrdinal;
	}
}