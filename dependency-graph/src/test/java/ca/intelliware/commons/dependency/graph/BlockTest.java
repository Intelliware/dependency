package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockTest {

	private static final double TRIVIAL_DELTA = 0.0001;
	
	@Mock
	Vertex vertex1;
	@Mock
	Vertex vertex2;
	@Mock
	Vertex vertex3;
	
	@Test
	public void testWidth() throws Exception {
		Mockito.when(this.vertex1.getWidth()).thenReturn(0.1);
		Mockito.when(this.vertex2.getWidth()).thenReturn(0.1);
		Mockito.when(this.vertex3.getWidth()).thenReturn(1.0);
		
		Block block = new Block(new BlockManager(), 0);
		block.add(this.vertex1);
		assertEquals("width 1", 0.1, block.getWidth(), TRIVIAL_DELTA);

		block.add(this.vertex2);
		assertEquals("width 2", 0.1, block.getWidth(), TRIVIAL_DELTA);

		block.add(this.vertex3);
		assertEquals("width 3", 1.0, block.getWidth(), TRIVIAL_DELTA);
	}
	
	@Test
	public void testFirstPosition() throws Exception {
		Mockito.when(this.vertex1.getWidth()).thenReturn(0.1);
		
		BlockManager blockManager = new BlockManager();
		Block block = new Block(blockManager, 0);
		block.add(this.vertex1);

		block.setRelativeHorizontalCoordinate(block.after(null));
		assertEquals("coordinate", 0.05, block.getRelativeHorizontalCoordinate(), TRIVIAL_DELTA);
	}
	@Test
	public void testNextPosition() throws Exception {
		Mockito.when(this.vertex1.getWidth()).thenReturn(1.0);
		Mockito.when(this.vertex2.getWidth()).thenReturn(0.1);

		BlockManager blockManager = new BlockManager();
		Block first = new Block(blockManager, 0);
		first.add(this.vertex1);
		first.setRelativeHorizontalCoordinate(first.after(null));
		
		Block block = new Block(blockManager, 0);
		block.add(this.vertex2);
		block.setRelativeHorizontalCoordinate(block.after(first));
		
		assertEquals("coordinate", 1.05, block.getRelativeHorizontalCoordinate(), TRIVIAL_DELTA);
	}
}
