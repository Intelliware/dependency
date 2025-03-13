package ca.intelliware.commons.dependency.graph;

import static ca.intelliware.commons.dependency.graph.HorizontalAlignment.LEFT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


enum Bias {
	
	LEFTWARD_UPPER(HorizontalAlignment.LEFT, VerticalAlignment.UPPER), 
	RIGHTWARD_UPPER(HorizontalAlignment.RIGHT, VerticalAlignment.UPPER), 
	LEFTWARD_LOWER(HorizontalAlignment.LEFT, VerticalAlignment.LOWER), 
	RIGHTWARD_LOWER(HorizontalAlignment.RIGHT, VerticalAlignment.LOWER); 
	
	public class NaturalComparator implements Comparator<Edge> {
		public int compare(Edge o1, Edge o2) {
			return o1.compareTo(o2);
		}
	}
	public class ReverseNaturalComparator extends NaturalComparator {
		public int compare(Edge o1, Edge o2) {
			return -super.compare(o1, o2);
		}
	}
	private final HorizontalAlignment horizontal;
	private final VerticalAlignment vertical;
	private Bias(HorizontalAlignment horizontal, VerticalAlignment vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;}
	public HorizontalAlignment getHorizontal() {
		return this.horizontal;
	}
	public VerticalAlignment getVertical() {
		return this.vertical;
	}

	public <T> Iterable<T> iterate(List<T> layers) {
		if (this.vertical == VerticalAlignment.UPPER) {
			return layers;
		} else {
			List<T> result = new ArrayList<T>(layers);
			Collections.reverse(result);
			return result;
		}
	}
	public Integer getNextLayer(int level, int layerCount) {
		switch (this.vertical) {
		case UPPER:
			return (level+1) < layerCount ? level + 1 : null;
		case LOWER:
			return level > 0 ? level-1 : null;
		default:
			return null;
		}
	}
	
	Comparator<Edge> getEdgeComparator() {
		return this.horizontal == LEFT ? new NaturalComparator() : new ReverseNaturalComparator();
	}
	SortedSet<Edge> order(Set<Edge> edges) {
		SortedSet<Edge> result = new TreeSet<Edge>(getEdgeComparator());
		result.addAll(edges);
		return result;
	}
	Edge chooseMedian(List<Edge> candidates) {
		if (candidates.isEmpty()) {
			return null;
		} else {
			Collections.sort(candidates, getEdgeComparator());
			int size = candidates.size() - 1;
			int median = size / 2;
			return candidates.get(median);
		}
	}
	Iterable<Vertex> orderLayer(OrderedLayer<Vertex> layer) {
		if (this.horizontal == LEFT) {
			return layer.getOrderedContents();
		} else {
			List<Vertex> list = new ArrayList<Vertex>(layer.getOrderedContents());
			Collections.reverse(list);
			return list;
		}
	}
	Edge chooseAlternateMedian(List<Edge> candidates) {
		if (candidates.isEmpty()) {
			return null;
		} else {
			Collections.sort(candidates, getEdgeComparator());
			int size = candidates.size();
			int median = size / 2;
			return candidates.get(median);
		}
	}
}