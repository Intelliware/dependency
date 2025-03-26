package com.electricmind.dependency.graph;

import java.awt.Color;

import com.electricmind.dependency.Node;

public interface ColorProvider {

	Color getColor(Node<?> node);
}
