package com.electricmind.dependency.graph;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.electricmind.dependency.Node;

public class TextLabelStrategy {

	public void initialize(Graphics2D graphics, TextLabel textLabel, List<Node<?>> nodes) {
		List<String> text = nodes.stream().map(n -> n.getName()).collect(Collectors.toList());
		textLabel.initialize(graphics, new TextLabelOption(Font.PLAIN, text));
	}
	
	public void populate(Node<?> node, TextLabel textLabel, Point2D upperLeft, OutputStream output) throws IOException {
		textLabel.drawStringSvg(node.getName(), 0, upperLeft, output);
	}
}
