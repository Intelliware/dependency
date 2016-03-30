package ca.intelliware.commons.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>An algorithm that takes a directed graph and reverses some of the edges
 * (connections) to produce an acyclic directed graph.
 *
 * <p>This algorithm is intended to find a "good" set of edges to reverse -- a relatively
 * small set -- but I don't think that it's guaranteed to find the "best" set of edges
 * to reverse.  This implementation is based on the description of the greedy
 * cycle removal algorithm in <cite>Graph Drawing: Algorithms for the Visualization
 * of Graphs</cite>, and is attractive because it's relatively fast.
 *
 * @author BC Holmes
 */
class GreedyCycleRemovalAlgorithm<T> {

	class ComparatorImpl implements Comparator<T> {
		public int compare(T o1, T o2) {
			DirectedGraph<T> graph = GreedyCycleRemovalAlgorithm.this.graph;
			if (graph.isSink(o1) && graph.isSink(o2)) {
				return compareExtraInformation(o1, o2);
			} else if (graph.isSink(o1)) {
				return 1;
			} else if (graph.isSink(o2)) {
				return -1;
			} else if (graph.isSource(o1) && graph.isSource(o2)) {
				return compareExtraInformation(o1, o2);
			} else if (graph.isSource(o1)) {
				return -1;
			} else if (graph.isSource(o2)) {
				return 1;
			} else if (getDegreeDelta(o1) != getDegreeDelta(o2)) {
				return getDegreeDelta(o1) > getDegreeDelta(o2) ? -1 : 1;
			} else {
				return compareExtraInformation(o1, o2);
			}
		}

		/**
		 * <p>The greedy cycle removal algorithm doesn't specify these comparisons, but
		 * I want to have a certain amount of determinism to the edges that get reversed.
		 */
		private int compareExtraInformation(T o1, T o2) {
			DirectedGraph<T> graph = GreedyCycleRemovalAlgorithm.this.graph;
			if (graph.getEfferentCouplingCount(o1) != graph.getEfferentCouplingCount(o2)) {
				return graph.getEfferentCouplingCount(o2) - graph.getEfferentCouplingCount(o1);
			} else if (graph.getAfferentCouplingCount(o1) != graph.getAfferentCouplingCount(o2)) {
				return graph.getAfferentCouplingCount(o2) - graph.getAfferentCouplingCount(o1);
			} else {
				Describer describer = new Describer();
				return describer.getDescription(o1).compareTo(describer.getDescription(o2));
			}
		}

		private int getDegreeDelta(T t) {
			return getOutDegree(t) - getInDegree(t);
		}

		private int getInDegree(T t) {
			return GreedyCycleRemovalAlgorithm.this.graph.getAfferentCouplingCount(t);
		}

		private int getOutDegree(T t) {
			return GreedyCycleRemovalAlgorithm.this.graph.getEfferentCouplingCount(t);
		}
	}

	class VertexSequence {

		private List<T> sequence;

		public VertexSequence(Set<T> set) {
			this.sequence = new ArrayList<T>(set);
			Collections.sort(this.sequence, new ComparatorImpl());
		}

		/**
		 * 
		 * @param from
		 * @param to
		 * @return true if the 
		 */
		boolean isLeftward(T from, T to) {
			int fromIndex = this.sequence.indexOf(from);
			int toIndex = this.sequence.indexOf(to);
			return fromIndex > toIndex;
		}

		public List<T> getElements() {
			return this.sequence;
		}

		@Override
		public String toString() {
			return this.sequence.toString();
		}
	}

	private final DirectedGraph<T> graph;

	GreedyCycleRemovalAlgorithm(DirectedGraph<T> graph) {
		this.graph = graph;
	}

	public DirectedGraph<T> execute() {
		if (this.graph.isAcyclic()) {
			return this.graph;
		} else {
			Map<T, Set<T>> acyclicEfferents = new HashMap<T, Set<T>>();
			VertexSequence sequence = getVertexSequence();
			for (T key : sequence.getElements()) {
				for (T efferent : this.graph.getEfferentCouplings(key)) {
					if (key.equals(efferent)) {
						// skip it
					} else if (sequence.isLeftward(key, efferent)) {
						put(acyclicEfferents, efferent, key);
					} else {
						put(acyclicEfferents, key, efferent);
					}
				}
			}
			return new DirectedGraph<T>(graph.getAll(), acyclicEfferents);
		}
	}

	private void put(Map<T, Set<T>> map, T key, T value) {
		if (!map.containsKey(key)) {
			map.put(key, new HashSet<T>());
		}
		map.get(key).add(value);
	}

	VertexSequence getVertexSequence() {
		return new VertexSequence(this.graph.getAll());
	}
}
