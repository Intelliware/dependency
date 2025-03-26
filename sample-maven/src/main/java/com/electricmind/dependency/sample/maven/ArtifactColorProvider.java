package com.electricmind.dependency.sample.maven;

import java.awt.Color;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.ColorProvider;

class ArtifactColorProvider implements ColorProvider {

	@Override
	public Color getColor(Node<?> node) {
		if (node.getName().startsWith("com.electricmind.dependency:")) {
			return new Color(246f / 256f, 241f / 256f, 254f / 256f);
		} else {
			return Color.WHITE;
		}
	}
}
