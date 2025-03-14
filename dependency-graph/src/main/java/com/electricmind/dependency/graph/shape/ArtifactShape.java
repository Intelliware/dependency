package com.electricmind.dependency.graph.shape;

import java.awt.Dimension;

import com.electricmind.dependency.graph.NodeShape;

public class ArtifactShape<T> extends NodeShape<T> {

	public ArtifactShape() {
		super(new Dimension(160, 75));
	}
}
