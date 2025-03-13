package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CoordinateSystemTest {
	
	@Mock
	Plottable plottable1;
	
	@Mock
	Plottable plottable2;

	@Test
	public void testGetWidth() throws Exception {
		CoordinateSystem coordinateSystem = new CoordinateSystem(new NodeShape<Object>(), 4);
		assertEquals("width", 500.0, coordinateSystem.getWidth(4), 0.0001);
	}	
	
	@Test
	public void testGetTopY() throws Exception {
		CoordinateSystem coordinateSystem = new CoordinateSystem(new NodeShape<Object>(), 4);
		assertEquals("top x", 325.0, coordinateSystem.getTopY(0), 0.0001);
		assertEquals("bottom x", 375.0, coordinateSystem.getBottomY(0), 0.0001);
		assertEquals("top x", 225.0, coordinateSystem.getTopY(1), 0.0001);
		assertEquals("bottom x", 275.0, coordinateSystem.getBottomY(1), 0.0001);
		assertEquals("total", 400.0, coordinateSystem.getTotalHeight(), 0.0001);
	}
	
	@Test
	public void testGetCentreX() throws Exception {
		Mockito.when(this.plottable1.getWidth()).thenReturn(1.0);
		Mockito.when(this.plottable1.getX()).thenReturn(0.5);
		
		Mockito.when(this.plottable2.getWidth()).thenReturn(1.0);
		Mockito.when(this.plottable2.getX()).thenReturn(2.5);
		
		CoordinateSystem coordinateSystem = new CoordinateSystem(new NodeShape<Object>(), 4);
		assertEquals("centre x", 62.5, coordinateSystem.getCenterX(this.plottable1), 0.0001);
		assertEquals("left x", 12.5, coordinateSystem.getLeftX(this.plottable1), 0.0001);

		assertEquals("centre x2", 312.5, coordinateSystem.getCenterX(this.plottable2), 0.0001);
		assertEquals("left x2", 262.5, coordinateSystem.getLeftX(this.plottable2), 0.0001);
	}
}
