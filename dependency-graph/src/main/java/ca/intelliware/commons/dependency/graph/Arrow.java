package ca.intelliware.commons.dependency.graph;

import static java.lang.Math.PI;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Arrow {
	
	private static final double NINETY_DEGREES_IN_RADIANS = PI / 2.0;
	private static final double ONE_EIGHTY_DEGREES_IN_RADIANS = PI;
	private List<Point2D> points;
	private float width;
	
	Arrow(List<Point2D> points) {
		this(points, 1.0f);
	}

	Arrow(List<Point2D> points, float width) {
		this.points = points;
		this.width = width;
	}

	List<Point2D> getPoints() {
		return this.points;
	}

	void setPoints(List<Point2D> points) {
		this.points = points;
	}
	
	Shape toShape() {
		if (this.points.size() == 2) {
			return new Line2D.Double(getFirstPoint(), getLastPoint());
		} else {
			GeneralPath path = new GeneralPath();
			boolean firstTime = true;
			for (Point2D point : this.points) {
				if (firstTime) {
					path.moveTo((float) point.getX(), (float) point.getY());
				} else {
					path.lineTo((float) point.getX(), (float) point.getY());
				}
				firstTime = false;
			}
			return path;
		}
	}

	Point2D getLastPoint() {
		return this.points.get(this.points.size()-1);
	}

	Point2D getFirstPoint() {
		return this.points.get(0);
	}
	
	@Override
	public String toString() {
		return this.points.toString();
	}
	
	boolean isPointingUpward() {
		return getFirstPoint().getY() > getLastPoint().getY();
	}
	
	static Arrow join(Arrow... arrows) {
		List<Point2D> points = new ArrayList<Point2D>();
		for (Arrow arrow : arrows) {
			points.addAll(arrow.getPoints());
		}
		return new Arrow(points);
	}

	static Arrow reverse(Arrow arrow) {
		List<Point2D> points = new ArrayList<Point2D>(arrow.points);
		Collections.reverse(points);
		return new Arrow(points);
	}

	public double getAngleOfLastSegment() {
		Point2D secondLast = this.points.get(this.points.size()-2);
		Point2D last = getLastPoint();
		
		if (secondLast.getY() == last.getY()) {
			return secondLast.getX() <= last.getX() ? 0.0 : ONE_EIGHTY_DEGREES_IN_RADIANS;
		} else if (secondLast.getX() == last.getX()) {
			return secondLast.getY() > last.getY() 
				? NINETY_DEGREES_IN_RADIANS : -NINETY_DEGREES_IN_RADIANS;
		} else {
			double theta = LinearFunction.create(secondLast, last).getTheta();
			if (!isPointingUpward() && theta > 0) {
				return theta - ONE_EIGHTY_DEGREES_IN_RADIANS;
			} else if (isPointingUpward() && theta < 0) {
				return theta + ONE_EIGHTY_DEGREES_IN_RADIANS;
			} else {
				return theta;
			}
		}
	}
	
	Arrow clipEnd(Rectangle2D rectangle) {
		Point2D secondLast = this.points.get(this.points.size()-2);
		Point2D last = getLastPoint();
		LinearFunction function = LinearFunction.create(secondLast, last);
		
		Point2D to = BoundsUtil.get(function, secondLast, rectangle);
		
		List<Point2D> points = new ArrayList<Point2D>(this.points.subList(0, this.points.size()-1));
		points.add(to);
		return new Arrow(points);
	}
	
	Arrow clipStart(Rectangle2D rectangle) {
		Point2D second = this.points.get(1);
		Point2D first = getFirstPoint();
		LinearFunction function = LinearFunction.create(first, second);
		
		Point2D to = BoundsUtil.get(function, second, rectangle);
		
		List<Point2D> points = new ArrayList<Point2D>(this.points.subList(1, this.points.size()));
		points.add(0, to);
		return new Arrow(points);
	}

	public float getWidth() {
		return this.width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}
