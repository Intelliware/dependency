package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClusterTest {
	
	@Mock
	Block block;
	
	@Test
	public void testExcessShift() throws Exception {
		Mockito.when(this.block.getWidth()).thenReturn(1.0);
		Cluster cluster = new Cluster(this.block);
		cluster.setShift(3.0);
		
		assertEquals("excess shift", 2.5, cluster.getExcessShift(), 0.0001);
	}
	
	@Test
	public void testExcessShiftNegative() throws Exception {
		
		Mockito.when(this.block.getWidth()).thenReturn(1.0);
		Cluster cluster = new Cluster(this.block);
		cluster.setShift(-1.0);
		
		assertEquals("excess shift", -1.5, cluster.getExcessShift(), 0.0001);
	}
}
