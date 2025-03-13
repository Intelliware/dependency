package ca.intelliware.commons.dependency.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


class Block {
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private final BlockManager blockManager;
	private final int id;
	private double coordinate;
	private Cluster cluster;
	private double width = 0.0;
	
	public Block(BlockManager blockManager, int id) {
		this.blockManager = blockManager;
		this.id = id;
	}
	void add(Vertex vertex) {
		Map<Integer, Block> map = this.blockManager.getMap(vertex.getLayer());
		map.put(vertex.getOrdinal(), this);
		this.vertices.add(vertex);
		this.width = Math.max(this.width, vertex.getWidth());;
	}
	@Override
	public String toString() {
		return this.vertices.toString();
	}
	
	void setRelativeHorizontalCoordinate(double coordinate) {
		this.coordinate = coordinate;
	}
	double getHorizontalCoordinate() {
		return this.coordinate + this.cluster.getShift();
	}
	
	void setHorizontalCoordinate(Bias bias) {
		for (Vertex vertex : this.vertices) {
			((HorizontalCoordinateImpl) vertex.getHorizontalCoordinate()).setPosition(
					getHorizontalCoordinate(), bias);
		}
	}
	List<Vertex> getVertices() {
		return this.vertices;
	}
	@Override
	public int hashCode() {
		return this.id;
	}
	public int getId() {
		return this.id;
	}
	int getRoot() {
		return this.vertices.get(0).getLayer();
	}
	void assignCluster(Cluster cluster) {
		if (this.cluster == null) {
			this.cluster = cluster;
		} else if (cluster.getWeight() > this.cluster.getWeight()) {
			this.cluster = cluster;
		}
	}
	Cluster getCluster() {
		return this.cluster;
	}
	double getRelativeHorizontalCoordinate() {
		return this.coordinate;
	}
	public double getWidth() {
		return this.width;
	}
	double after(Block previous) {
		if (previous == null) {
			return this.width / 2.0;
		} else {
			return previous.getRelativeHorizontalCoordinate() + 
				previous.getWidth() / 2.0 + this.width / 2.0;
		}
	}
	
	static double minimumDistance(Block first, Block second) {
		return first.getWidth() / 2.0 + second.getWidth() / 2.0;
	}
}