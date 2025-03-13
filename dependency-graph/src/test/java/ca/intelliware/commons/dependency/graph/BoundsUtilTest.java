package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;

import org.junit.Test;

public class BoundsUtilTest {

	@Test
	public void testCalculationOfEndPointsWithOneBoxOnTopOfOther() throws Exception {
		Arrow arrow = BoundsUtil.getEndPoints(new Rectangle2D.Double(0, 0, 200, 100), new Rectangle2D.Double(0, 400, 200, 100));
		assertEquals("P1.x", 100.0, arrow.getFirstPoint().getX(), 0.0001);
		assertEquals("P2.x", 100.0, arrow.getLastPoint().getX(), 0.0001);
	}

	@Test
	public void testCalculationOfEndPointsSimpleCase() throws Exception {
		Arrow arrow = BoundsUtil.getEndPoints(new Rectangle2D.Double(0, 0, 200, 100), new Rectangle2D.Double(400, 400, 200, 100));
		assertEquals("P1.x", 150.0, arrow.getFirstPoint().getX(), 0.0001);
		assertEquals("P2.x", 450.0, arrow.getLastPoint().getX(), 0.0001);
		
		assertFalse("upward", arrow.isPointingUpward());
	}
	
	@Test
	public void testCalculationOfEndPointsFromLowerToUpper() throws Exception {
		Arrow arrow = BoundsUtil.getEndPoints(new Rectangle2D.Double(400, 400, 200, 100), new Rectangle2D.Double(0, 0, 200, 100));
		assertEquals("P1.x", 450.0, arrow.getFirstPoint().getX(), 0.0001);
		assertEquals("P2.x", 150.0, arrow.getLastPoint().getX(), 0.0001);
		
		assertTrue("upward", arrow.isPointingUpward());
	}
}
