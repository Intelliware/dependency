package ca.intelliware.commons.dependency.graph;

import static java.util.Arrays.asList;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class BoundsUtil {

	static Arrow getEndPoints(Rectangle2D from, Rectangle2D to) {
		if (from.getY() > to.getY()) {
			return Arrow.reverse(getEndPoints(to, from));
		} else  if (to.getCenterX() == from.getCenterX()) {
			return new Arrow(asList(toPoint(from.getCenterX(), from.getMaxY()), toPoint(to.getCenterX(), to.getY())));
		} else if (to.getCenterY() == from.getCenterY()) { // this case shouldn't happen
			return new Arrow(asList(toPoint(from.getMaxX(), from.getCenterY()), toPoint(to.getX(), to.getCenterY())));
		} else {
			LinearFunction function = LinearFunction.create(from, to);

			Point2D bottom = getBottom(function, from);
			Point2D top = getTop(function, to);

			return new Arrow(asList(bottom, top));
		}
	}

	private static Point2D toPoint(double x, double y) {
		return new Point2D.Double(x, y);
	}

	static Arrow getEndPoints(Point2D from, Rectangle2D to) {
		if (from.getY() > to.getY()) {
			return Arrow.reverse(getEndPoints(to, from));
		} else  if (to.getCenterX() == from.getX()) {
			return new Arrow(asList(from, toPoint(to.getCenterX(), to.getY())));
		} else if (to.getCenterY() == from.getY()) { // this case shouldn't happen
			return new Arrow(asList(from, toPoint(to.getX(), to.getCenterY())));
		} else {
			LinearFunction function = LinearFunction.create(from, to);
			Point2D top = getTop(function, to);
			return new Arrow(asList(from, top));
		}
	}

	static Arrow getEndPoints(Rectangle2D from, Point2D to) {
		if (from.getY() > to.getY()) {
			return Arrow.reverse(getEndPoints(to, from));
		} else if (to.getX() == from.getCenterX()) {
			return new Arrow(asList(toPoint(from.getCenterX(), from.getMaxY()), to));
		} else if (to.getY() == from.getCenterY()) { // this case shouldn't happen
			return new Arrow(asList(toPoint(from.getMaxX(), from.getCenterY()), to));
		} else {
			LinearFunction function = LinearFunction.create(from, to);
			Point2D bottom = getBottom(function, from);

			return new Arrow(asList(bottom, to));
		}
	}
	
	static Point2D get(LinearFunction function, Point2D from, Rectangle2D to) {
		if (function.isVertical()) {
			if (from.getY() <= to.getCenterY()) {
				return new Point2D.Double(from.getX(), to.getMinY());
			} else {
				return new Point2D.Double(from.getX(), to.getMaxY());
			}
		} else if (function.isHorizontal()) {
			if (from.getX() < to.getCenterX()) {
				return new Point2D.Double(to.getMinX(), from.getY());
			} else {
				return new Point2D.Double(to.getMaxX(), from.getY());
			}
		} else if (from.getY() < to.getCenterY()) {
			return getTop(function, to);
		} else {
			return getBottom(function, to);
		}
	}
	
	static Point2D getTop(LinearFunction function, Rectangle2D to) {
		double y = to.getY();
		double x = function.getX(y);

		if (x >= to.getX() && x <= to.getMaxX()) {
			return new Point2D.Double(x, y);
		} else if (x < to.getX()) {
			y = function.getY(to.getX());
			return new Point2D.Double(to.getX(), y);
		} else {
			y = function.getY(to.getMaxX());
			return new Point2D.Double(to.getMaxX(), y);
		}
	}

	static Point2D getBottom(LinearFunction function, Rectangle2D from) {
		double y = from.getMaxY();
		double x = function.getX(y);

		if (x >= from.getX() && x <= from.getMaxX()) {
			return new Point2D.Double(x, y);
		} else if (x < from.getX()) {
			y = function.getY(from.getX());
			return new Point2D.Double(from.getX(), y);
		} else {
			y = function.getY(from.getMaxX());
			return new Point2D.Double(from.getMaxX(), y);
		}
	}
}
