package ca.intelliware.commons.dependency.graph;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.junit.Test;

public class CountCrossingAlgorithmTest {
	
	final class OrderedImpl implements Ordered {
		private final int value;
		
		private OrderedImpl(int value) {
			this.value = value;
		}
		public int getOrdinal() {
			return this.value;
		}
	}
	class Cluster implements Comparable<Cluster>, Neighbourly<Ordered> {

		private final int from;
		private final int[] to;

		Cluster(int from, int... to) {
			this.from = from;
			this.to = to;
		}

		public int compareTo(Cluster o) {
			return this.from - o.from;
		}

		public List<Ordered> getNeighboursInLayer(int layer) {
			List<Ordered> result = new ArrayList<Ordered>();
			for (int to : this.to) {
				result.add(new OrderedImpl(to));
			}
			return result;
		}
	}
	
	/**
	 * <p>This example is taken directly from the paper cited in the Javadoc
	 * of the CrossCountingAlgorithm.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCountCrossings() throws Exception {
		
		OrderedLayer<Cluster> layer1 = new OrderedLayer<Cluster>(0, new TreeSet<Cluster>(Arrays.asList(
				new Cluster(0, 0),
				new Cluster(1, 1, 2), 
				new Cluster(2, 0, 3, 4), 
				new Cluster(3, 0, 2), 
				new Cluster(4, 3), 
				new Cluster(5, 2, 4))));
		OrderedLayer<Cluster> layer2 = new OrderedLayer<Cluster>(1, new TreeSet<Cluster>());
		
		assertEquals("count", 12, new CountCrossingAlgorithm().countCrossings(layer1, layer2));
	}

	@Test
	public void testCountCrossingsTrivialCase() throws Exception {
		
		OrderedLayer<Cluster> layer1 = new OrderedLayer<Cluster>(0, new TreeSet<Cluster>(Arrays.asList(
				new Cluster(0, 0),
				new Cluster(1, 1, 2))));
		OrderedLayer<Cluster> layer2 = new OrderedLayer<Cluster>(1, new TreeSet<Cluster>());
		
		assertEquals("count", 0, new CountCrossingAlgorithm().countCrossings(layer1, layer2));
	}
	
	@Test
	public void testCountCrossingsWithGaps() throws Exception {
		
		OrderedLayer<Cluster> layer1 = new OrderedLayer<Cluster>(0, new TreeSet<Cluster>(Arrays.asList(
				new Cluster(0, 0),
				new Cluster(1, 2))));
		OrderedLayer<Cluster> layer2 = new OrderedLayer<Cluster>(1, new TreeSet<Cluster>());
		
		assertEquals("count", 0, new CountCrossingAlgorithm().countCrossings(layer1, layer2));
	}
	
}
