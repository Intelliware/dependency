package ca.intelliware.commons.dependency.graph;

import java.awt.Dimension;

class CoordinateSystem {
	
	static final double BORDER_SIZE = 0.25;
	static final double WIDTH_SCALE_FACTOR = 1 + BORDER_SIZE;
	static final double HEIGHT_SCALE_FACTOR = 2.0;
	
	private final Dimension dimension;
	private final double height;

	CoordinateSystem(NodeShape<?> shape, double height) {
		this(shape.getDimension(), height);
	}

	CoordinateSystem(Dimension dimension, double height) {
		this.dimension = dimension;
		this.height = height;
	}
	
	double getCenterX(Plottable vertex) {
		return getX(vertex.getX());
	}
	double getTopY(double y) {
		return getTotalHeight() - this.dimension.getHeight() * (y + 0.75) * HEIGHT_SCALE_FACTOR;
	}
	double getBottomY(double y) {
		return getTopY(y) + this.dimension.getHeight();
	}
	double getTotalHeight() {
		return this.height * this.dimension.getHeight() * HEIGHT_SCALE_FACTOR;
	}

	double getX(double x) {
		return x * this.dimension.getWidth() * WIDTH_SCALE_FACTOR;
	}
	double getLeftX(Plottable vertex) {
		return getCenterX(vertex) - (this.dimension.getWidth() / 2.0) * vertex.getWidth();
	}

	public double getWidth(double width) {
		return width * this.dimension.getWidth() * WIDTH_SCALE_FACTOR;
	}

	public double getTopY(Plottable p) {
		return getTopY(p.getY());
	}

	public double getBottomY(Plottable p) {
		return getBottomY(p.getY());
	}
}
