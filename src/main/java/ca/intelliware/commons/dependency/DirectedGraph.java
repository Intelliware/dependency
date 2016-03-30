package ca.intelliware.commons.dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>A directed graph.
 *
 * <p>In graph theory, a graph is considered to be a collection of vertices (nodes) and
 * edges (or links, arcs, or connections) between two nodes.
 *
 * <p>A directed graph (also called a digraph by those seeking to be opaque) adds one
 * definition over and above the definition of the graph.  This requirement is that the
 * end points of the edges are ordered (that is to say, the connection is considered to
 * be "pointing" in one direction or the other).
 *
 * @author BC Holmes
 */
public class DirectedGraph<T> {

	class Path {
		private final List<T> list;
		private boolean isAcyclic = true;

		Path() {
			this.list = new ArrayList<T>();
		}
		Path(T t) {
			this.list = new ArrayList<T>();
			this.list.add(t);
		}
		void add(T t) {
			if (this.list.contains(t)) {
				this.isAcyclic = false;
			} else {
				this.list.add(t);
			}
		}
		boolean isAcyclic() {
			return this.isAcyclic;
		}
		@Override
		public String toString() {
			return this.list.toString();
		}
		Path clonePath() {
			Path path = new Path();
			path.list.addAll(this.list);
			return path;
		}
	}

	protected final Map<T, Set<T>> efferentCouplings;
	protected final Map<T, Set<T>> afferentCouplings;
	private final HashSet<T> nodes;
	private Boolean isAcyclic;

	DirectedGraph(Set<T> nodes, Map<T,Set<T>> efferentCouplings, Map<T, Set<T>> afferentCouplings) {
		this.nodes = new HashSet<T>(nodes);
		this.efferentCouplings = createHashMap(efferentCouplings);
		this.afferentCouplings = createHashMap(afferentCouplings);
	}

	private Map<T, Set<T>> createHashMap(Map<T, Set<T>> map) {
		Map<T,Set<T>> result = new HashMap<T, Set<T>>();
		for (T key : map.keySet()) {
			result.put(key, new HashSet<T>(map.get(key)));
		}
		return result;
	}

	DirectedGraph(Set<T> nodes, Map<T,Set<T>> efferentCouplings) {
		this(nodes, efferentCouplings, determineAfferents(efferentCouplings));
	}

	private static <T> Map<T, Set<T>> determineAfferents(Map<T, Set<T>> efferentCouplings) {
		Map<T,Set<T>> afferents = new HashMap<T, Set<T>>();
		for (T key : efferentCouplings.keySet()) {
			for (T value : efferentCouplings.get(key)) {
				if (!afferents.containsKey(value)) {
					afferents.put(value, new HashSet<T>());
				}
				afferents.get(value).add(key);
			}
		}
		return afferents;
	}

	public boolean isAcyclic() {
		if (this.isAcyclic == null) {
			lookForCycles();
		}
		return this.isAcyclic;
	}

	private void lookForCycles() {
		boolean acyclic = true;
        Set<T> all = getAll();
        Set<T> sorted = new HashSet<T>();

        int level = 0;
        while (!all.isEmpty()) {
            Set<T> layer = new HashSet<T>();
            for (T t : all) {
                Collection<T> dependencies = getEfferentCouplings(t);
                dependencies.removeAll(sorted);
                if (dependencies.isEmpty()) {
                    layer.add(t);
                }
            }

            if (!layer.isEmpty()) {
                level++;
                all.removeAll(layer);
                sorted.addAll(layer);
            } else {
            	acyclic = false;
            	break;
            }
        }
        this.isAcyclic = acyclic;
	}

	/**
	 * <p>Determine if the vertex is a sink.  We say that a vertex is a sink if it has
	 * no outgoing edges.  In other words, a node is a sink if it doesn't depend on
	 * anything (and thus has no efferent couplings).
	 *
	 * @param t
	 * @return true if the vertex is a sink; false otherwise
	 */
	boolean isSink(T t) {
		return !this.efferentCouplings.containsKey(t) ||
			this.efferentCouplings.get(t) == null ||
			this.efferentCouplings.get(t).isEmpty();
	}
	/**
	 * <p>Determine if the vertex is a source.  We say that a vertex is a source if it has
	 * no incoming edges.  In other words, a node is a souce if nothing depends on
	 * it (and thus has no afferent couplings).
	 *
	 * @param t
	 * @return true if the vertex is a source; false otherwise
	 */
	boolean isSource(T t) {
		return !this.afferentCouplings.containsKey(t) ||
			this.afferentCouplings.get(t) == null ||
			this.afferentCouplings.get(t).isEmpty();
	}

	int getEfferentCouplingCount(T t) {
		return isSink(t) ? 0 : this.efferentCouplings.get(t).size();
	}
	int getAfferentCouplingCount(T t) {
		return isSource(t) ? 0 : this.afferentCouplings.get(t).size();
	}

	Set<T> getAll() {
		return new HashSet<T>(this.nodes);
	}

	Set<T> getEfferentCouplings(T t) {
		return this.efferentCouplings.containsKey(t) ? new HashSet<T>(this.efferentCouplings.get(t)) : new HashSet<T>();
	}
	Set<T> getAfferentCouplings(T t) {
		return this.afferentCouplings.containsKey(t) ? new HashSet<T>(this.afferentCouplings.get(t)) : new HashSet<T>();
	}

	Map<T, Set<T>> getAfferentCouplings() {
		return createHashMap(this.afferentCouplings);
	}

	Map<T, Set<T>> getEfferentCouplings() {
		return createHashMap(this.efferentCouplings);
	}
}
