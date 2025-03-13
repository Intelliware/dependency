package ca.intelliware.commons.dependency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LayeredGraph<T> extends DirectedGraph<T> {

	class NodeImpl implements Node<T> {

		private final T item;

		public NodeImpl(T item) {
			this.item = item;
		}
		public Set<Coupling<T>> getAfferentCouplings() {
			return LayeredGraph.this.getAfferentCouplings(this.item);
		}
		public Set<Coupling<T>> getEfferentCouplings() {
			return LayeredGraph.this.getEfferentCouplings(this.item);
		}
		public T getItem() {
			return this.item;
		}
		public String getName() {
			return new Describer().getDescription(this.item);
		}
		public String toString() {
			return getName();
		}
		public int getLayer() {
			return LayeredGraph.this.getLayer(this.item);
		}
	}

	private List<Layer<Node<T>>> layers;
	private Map<T,Integer> layerMap;


	LayeredGraph(Set<T> nodes, Map<T,Set<Coupling<T>>> efferentCouplings, Map<T, Set<Coupling<T>>> afferentCouplings) {
		super(nodes, efferentCouplings, afferentCouplings);
	}

	public int getLayer(T t) {
		if (this.layerMap == null) {
			this.layerMap = initializeLayerMap();
		}
		return this.layerMap.containsKey(t) ? this.layerMap.get(t) : -1;
	}

	public Node<T> getNode(T t) {
		int index = getLayer(t);
		if (index < 0) {
			return null;
		} else {
			Layer<Node<T>> layer = this.layers.get(index);
			Node<T> result = null;
			for (Node<T> node : layer.getContents()) {
				if (t.equals(node.getItem())) {
					result = node;
					break;
				}
			}
			return result;
		}
	}
	
	private Map<T, Integer> initializeLayerMap() {
		Map<T, Integer> map = Collections.synchronizedMap(new HashMap<T, Integer>());
		for (Layer<Node<T>> layer : getLayers()) {
			for (Node<T> node : layer.getContents()) {
				map.put(node.getItem(), layer.getLevel());
			}
		}
		return map;
	}
	
	public List<Node<T>> getNodes() {
		List<Node<T>> nodes = new ArrayList<Node<T>>();
		for (Layer<Node<T>> layer : getLayers()) {
			for (Node<T> node : layer.getContents()) {
				nodes.add(node);
			}
		}
		return nodes;
	}

    public List<Layer<Node<T>>> getLayers() {
    	if (this.layers == null) {
    		this.layers = initializeLayers();
    	}
    	return this.layers;
    }

	private synchronized List<Layer<Node<T>>> initializeLayers() {
		DirectedGraph<T> graph = new GreedyCycleRemovalAlgorithm<T>(this).execute();

        List<Layer<Node<T>>> list = new ArrayList<Layer<Node<T>>>();
        Set<T> all = graph.getAll();
        Set<T> sorted = new HashSet<T>();

        int level = 0;
        while (!all.isEmpty()) {
            Set<Node<T>> layer = new HashSet<Node<T>>();
            for (T t : all) {
                Collection<T> dependencies = graph.getEfferentCouplings(t).stream().map(c -> c.getItem()).collect(Collectors.toSet());
                dependencies.removeAll(sorted);
                if (dependencies.isEmpty()) {
                    layer.add(new NodeImpl(t));
                }
            }

            if (!layer.isEmpty()) {
                list.add(new Layer<Node<T>>(level, layer));
                level++;
                for (Node<T> node : layer) {
                	all.remove(node.getItem());
                	sorted.add(node.getItem());
				}
            } else {
                throw new IllegalStateException(
                		"Somehow a cycle managed to be missed by the cycle removal algorithm");
            }
        }
        return list;
	}
}
