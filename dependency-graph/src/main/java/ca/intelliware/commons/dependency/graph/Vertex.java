package ca.intelliware.commons.dependency.graph;

import java.util.List;

abstract class Vertex implements Ordered, Neighbourly<Vertex>, Plottable {

	public static final int SCALE_FACTOR = 10;

	private final int layer;
	private int ordinal;
	private int previousOrdinal;
	private HorizontalCoordinate horizontalCoordinate;
	private double barycenter;

	public Vertex(int layer) {
		this.layer = layer;
	}

	int getLayer() {
		return this.layer;
	}

	public abstract List<Vertex> getNeighboursInLayer(int layer);

	void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public int getOrdinal() {
		return this.ordinal;
	}

	public boolean hasChanged() {
		return this.ordinal != this.previousOrdinal;
	}

	public void resetChanges() {
		this.previousOrdinal = this.ordinal;
	}

	abstract boolean isNeighbour(Vertex vertex);

	public boolean isDummy() {
		return false;
	}

	HorizontalCoordinate getHorizontalCoordinate() {
		return this.horizontalCoordinate;
	}

	void setHorizontalCoordinate(HorizontalCoordinate horizontalCoordinate) {
		this.horizontalCoordinate = horizontalCoordinate;
	}
	
	public double getX() {
		return this.horizontalCoordinate.getPosition();
	}
	
	public double getY() {
		return getLayer();
	}

	double getBarycenter() {
		return this.barycenter;
	}

	void setBarycenter(double barycenter) {
		this.barycenter = barycenter;
	}

	public double getWidth() {
		return isDummy() ? 0.1 : 1.0;
	}
}
