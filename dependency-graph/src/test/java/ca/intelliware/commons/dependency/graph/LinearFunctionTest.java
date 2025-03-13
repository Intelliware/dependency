package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;

import org.junit.Test;

public class LinearFunctionTest {

	private static final double THIRTY_DEGREES_IN_RADIANS = Math.PI / 6.0;
	private static final double FORTY_FIVE_DEGREES_IN_RADIANS = Math.PI / 4.0;

	@Test
	public void testCreateTrivialLinearFunction() throws Exception {
		LinearFunction function = LinearFunction.create(new Point2D.Double(0, 1), new Point2D.Double(1, 3));
		assertEquals("slope", 2.0, function.getSlope(), 0.0001);
		assertEquals("c", 1.0, function.getC(), 0.0001);
		assertEquals("y", 5.0, function.getY(2), 0.0001);
		assertEquals("x", 0.0, function.getX(1), 0.0001);
	}

	@Test
	public void testGetThetaOnStandard45DegreeAngle() throws Exception {
		LinearFunction function = LinearFunction.create(new Point2D.Double(0, 0), new Point2D.Double(1, 1));
		assertEquals("theta", FORTY_FIVE_DEGREES_IN_RADIANS, function.getTheta(), 0.0000001);
	}
	
	@Test
	public void testGetThetaOnStandard30DegreeAngle() throws Exception {
		LinearFunction function = LinearFunction.create(new Point2D.Double(0, 0), new Point2D.Double(Math.sqrt(3.0), 1.0));
		assertEquals("theta", THIRTY_DEGREES_IN_RADIANS, function.getTheta(), 0.0000001);
	}
}
