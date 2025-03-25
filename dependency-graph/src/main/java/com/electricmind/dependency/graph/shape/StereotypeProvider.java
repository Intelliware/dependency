package com.electricmind.dependency.graph.shape;

import com.electricmind.dependency.Node;

public interface StereotypeProvider {

	String getStereotype(Node<?> node);
}
