package com.electricmind.dependency;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class DependencyManager<T> {

	private Set<T> all = Collections.synchronizedSet(new HashSet<T>());
	/**
	 * Efferent couplings represent all the types that a particular type (the key of the Map) knows about.
	 */
    private Map<T,Set<Coupling<T>>> efferents = Collections.synchronizedMap(new HashMap<>());
	/**
	 * Afferent couplings represent all the types that know about a particular type (the key of the Map).
	 */
    private Map<T,Set<Coupling<T>>> afferents = Collections.synchronizedMap(new HashMap<>());

    private LayeredGraph<T> layeredGraph;

    public void add(T t) {
    	this.layeredGraph = null;
    	if (t != null) {
    		this.all.add(t);
    	}
    }
    
    public void add(T dependant, T dependsOn) {
    	add(dependant, dependsOn, 1);
    }

    public void add(T dependant, T dependsOn, int weight) {
    	add(dependant);
    	add(dependsOn);
    	add(this.afferents, dependsOn, dependant, weight);
    	add(this.efferents, dependant, dependsOn, weight);
    }

    private void add(Map<T, Set<Coupling<T>>> map, T key, T value, int weight) {
    	this.layeredGraph = null;
    	if (key != null) {
			if (!map.containsKey(key)) {
				map.put(key, new HashSet<>());
			}

			if (value != null) {
				List<Coupling<T>> couplings = map.get(key)
						.stream()
						.filter(c -> c.getItem().equals(value))
						.collect(Collectors.toList());
				if (couplings.isEmpty()) {
					map.get(key).add(new Coupling<>(value, weight));
				} else {
					couplings.get(0).setWeight(couplings.get(0).getWeight() + weight);
				}
			}
    	}
	}
    
	public int getNodeCount() {
    	return getAll().size();
    }

    public Set<Coupling<T>> getDirectDependencies(T dependent) {
    	Set<Coupling<T>> result = new HashSet<>();
    	Set<Coupling<T>> set = this.efferents.get(dependent);
    	if (set != null) {
    		set.forEach(c -> result.add(c));
    	}
		return result;
    }

	private synchronized LayeredGraph<T> initializeLayers() {
    	return new LayeredGraph<T>(this.all, this.efferents, this.afferents);
	}

    public List<Layer<Node<T>>> getNodeLayers() {
        return getLayeredGraph().getLayers();
    }

    private Set<T> getAll() {
        return new HashSet<T>(this.all);
    }
	public LayeredGraph<T> getLayeredGraph() {
		if (this.layeredGraph == null) {
			this.layeredGraph = initializeLayers();
		}
		return this.layeredGraph;
	}
}
