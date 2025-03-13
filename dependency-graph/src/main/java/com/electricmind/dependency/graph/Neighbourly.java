package com.electricmind.dependency.graph;

import java.util.List;

public interface Neighbourly<T extends Ordered> {
	public List<T> getNeighboursInLayer(int layer);
}
