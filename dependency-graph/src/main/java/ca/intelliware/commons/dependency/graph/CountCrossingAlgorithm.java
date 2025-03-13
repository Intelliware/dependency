package ca.intelliware.commons.dependency.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.builder.CompareToBuilder;


/**
 * <p>An algorithm to count the number of line crossings between two layers of a layered
 * directed graph.  Part of the problem of drawing a good directed graph, is that you 
 * want to minimize the number of times lines between the various nodes cross.  And to
 * minimize the number of crossings, it helps if you can actually count the number of 
 * crossings.
 * 
 * <p>This algorithm is based on an algorithm described in the paper, 
 * <a href="http://www.emis.de/journals/JGAA/accepted/2004/BarthMutzelJuenger2004.8.2.pdf">"Simple 
 * and Efficient Bilayer Cross Counting"</a> by Wilhelm Barth, Petra Mutzel,
 * and Michael J&uuml;nger, published in the <cite>Journal of Graph Algorithms 
 * and Applications</cite>, vol. 8, no. 2, pp. 179-194.  
 * 
 * <p>The paper optimizes the performance of the algorithm using a concept called 
 * accumulator trees, (which are basically binary trees) which are really fast, 
 * but if you're trying to read the code to understand what it really does, you'll 
 * probably have a hard time.  I suggest reading the paper to understand it better.
 * 
 * @author Wilhelm Barth
 * @author Petra Mutzel
 * @author Michael J&uuml;nger
 * @author BC Holmes
 */
class CountCrossingAlgorithm {
	
	private class GapRemover {
		private Map<Integer,Integer> map = new TreeMap<Integer,Integer>();
		private boolean isMapped = false;
		
		void add(int ordinal) {
			this.map.put(ordinal,null);
		}
		Integer translate(int ordinal) {
			if (!this.isMapped) {
				map();
			}
			return this.map.get(ordinal);
		}
		private void map() {
			int i = 0;
			for (Integer key : new TreeSet<Integer>(this.map.keySet())) {
				map.put(key, i++);
			}
			this.isMapped = true;
		}
	}
	
	private class Edge implements Comparable<Edge> {
		private final int ordinal;
		private final int weight;
		private final GapRemover remover;

		Edge(int ordinal, int weight, GapRemover remover) {
			this.ordinal = ordinal;
			this.weight = weight;
			this.remover = remover;
		}
		public int compareTo(Edge that) {
			return new CompareToBuilder()
					.append(this.ordinal, that.ordinal)
					.append(this.weight, that.weight)
					.toComparison();
		}
		public int getOrdinal() {
			return this.remover.translate(this.ordinal);
		}
		public int getWeight() {
			return this.weight;
		}
		@Override
		public String toString() {
			return "" + this.ordinal;
		}
	}

	<T extends Neighbourly<?>> int countCrossings(OrderedLayer<T> layer1, OrderedLayer<T> layer2) {
		List<Edge> southsequence = createSouthSequence(layer1, layer2.getLevel());

		int firstIndex = getFirstIndex(southsequence.size());
		int[] tree = createAccumulatorTree(firstIndex);
		firstIndex -= 1;
		
		int crosscount = 0;
		for (Edge e : southsequence) {
			int index = e.getOrdinal() + firstIndex;
			tree[index]++;
			while (index > 0) {
				if (index % 2 != 0) {
					crosscount += tree[index+1];
				}
				index = (index-1) / 2;
				tree[index]++;
			}
		}
		return crosscount;
	}
	
	<T extends Neighbourly<?>> int countCrossings(List<? extends OrderedLayer<T>> layers) {
		OrderedLayer<T> previous = null;
		int count = 0;
		for (OrderedLayer<T> orderedLayer : layers) {
			if (previous != null) {
				count += countCrossings(orderedLayer, previous);
			}
			previous = orderedLayer;
		}
		return count;
	}

	<T extends Neighbourly<?>> int countWeightedCrossings(OrderedLayer<T> layer1, OrderedLayer<T> layer2) {
		List<Edge> southsequence = createSouthSequence(layer1, layer2.getLevel());
		
		int firstIndex = getFirstIndex(southsequence.size());
		int[] tree = createAccumulatorTree(firstIndex);
		firstIndex -= 1;
		
		int crossweight = 0;
		for (Edge e : southsequence) {
			int index = e.getOrdinal() + firstIndex;
			tree[index] += e.getWeight();
			int weightsum = 0;
			while (index > 0) {
				if (index % 2 != 0) {
					weightsum += tree[index+1];
				}
				index = (index-1) / 2;
				tree[index] += e.getWeight();
			}
			crossweight += (e.getWeight() * weightsum);
		}
		return crossweight;
	}
	
	private List<Edge> createSouthSequence(OrderedLayer<? extends Neighbourly<?>> layer1, int level) {
		SortedSet<? extends Neighbourly<?>> contents = layer1.getOrderedContents();
		List<Edge> edges = new ArrayList<Edge>();
		GapRemover gapRemover = new GapRemover();
		for (Neighbourly<?> object : contents) {
			SortedSet<Edge> set = getNeighbourPositions(level, object, gapRemover);
			edges.addAll(set);
		}
		return edges;
	}

	private SortedSet<Edge> getNeighbourPositions(int level,
			Neighbourly<?> neighbourly, GapRemover gapRemover) {
		Collection<? extends Ordered> ordered = neighbourly.getNeighboursInLayer(level);
		SortedSet<Edge> set = new TreeSet<Edge>();
		for (Ordered o : ordered) {
			int weight =  (o instanceof Weighted) ? ((Weighted) o).getWeight() : 1;
			set.add(new Edge(o.getOrdinal(), weight, gapRemover));
			gapRemover.add(o.getOrdinal());
		}
		return set;
	}

	private int[] createAccumulatorTree(int firstIndex) {
		return new int[firstIndex * 2 - 1];
	}

	private int getFirstIndex(int q) {
		int index = 1;
		while (index < q) {
			index *= 2;
		}
		return index;
	}
}
