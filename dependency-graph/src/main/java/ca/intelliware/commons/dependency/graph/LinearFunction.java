package ca.intelliware.commons.dependency.graph;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A linear function.  A linear function is a function in the standard form:
 *
 * <pre>
 * y = slope * x + c
 * </pre>
 *
 * @author BC Holmes
 */
class LinearFunction {

	private final double c;
	private final double slope;

	public LinearFunction(double slope, double c) {
		this.slope = slope;
		this.c = c;
	}

	static LinearFunction create(Rectangle2D from, Rectangle2D to) {
		return create(new Point2D.Double(from.getCenterX(), from.getCenterY()), new Point2D.Double(to.getCenterX(), to.getCenterY()));
	}

	static LinearFunction create(Point2D from, Rectangle2D to) {
		return create(from, new Point2D.Double(to.getCenterX(), to.getCenterY()));
	}

	static LinearFunction create(Rectangle2D from, Point2D to) {
		return create(new Point2D.Double(from.getCenterX(), from.getCenterY()), to);
	}

	static LinearFunction create(Point2D from, Point2D to) {
		if (from.getX() - to.getX() == 0.0) {
			return new LinearFunction(from.getY() - to.getY() >= 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, from.getX());
		} else {
			double slope = (from.getY() - to.getY()) / (from.getX() - to.getX());
			double c = from.getY() - (from.getX() * slope);
			return new LinearFunction(slope, c);
		}
	}

	double getC() {
		return this.c;
	}

	double getSlope() {
		return this.slope;
	}

	double getX(double y) {
		return 	(y - this.c) / this.slope;
	}
	double getY(double x) {
		return 	x * this.slope + this.c;
	}
	double getTheta() {
		if (this.slope == Double.POSITIVE_INFINITY) {
			return Math.PI / 2.0;
		} else if (this.slope == Double.NEGATIVE_INFINITY) {
			return -Math.PI / 2.0;
		} else {
			return Math.atan(this.slope);
		}
	}

	public boolean isVertical() {
		return this.slope == Double.POSITIVE_INFINITY || this.slope == Double.NEGATIVE_INFINITY;
	}

	public boolean isHorizontal() {
		return this.slope == 0.0;
	}
}
