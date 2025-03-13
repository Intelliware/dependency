package ca.intelliware.commons.dependency.graph;

import java.util.SortedSet;

import com.electricmind.dependency.Layer;

class OrderedLayer<T> extends Layer<T> {

	private final SortedSet<T> orderedContents;

	OrderedLayer(int level, SortedSet<T> contents) {
		super(level, contents);
		this.orderedContents = contents;
	}

	SortedSet<T> getOrderedContents() {
		return this.orderedContents;
	}
}
