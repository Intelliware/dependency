package com.electricmind.dependency.sample.npm;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.TextLabel;
import com.electricmind.dependency.graph.TextLabelOption;
import com.electricmind.dependency.graph.TextLabelStrategy;
import com.electricmind.dependency.graph.shape.StereotypeProvider;

public class NpmPackageLabelStrategy extends TextLabelStrategy implements StereotypeProvider {
	
	boolean simple = false;

	@Override
	public void populate(Node<?> node, TextLabel textLabel, Point2D upperLeft, OutputStream outputStream) throws IOException {
		if (this.simple) {
			super.populate(node, textLabel, upperLeft, outputStream);
		} else {
			String name = node.getName();
			String version = ((NpmPackageName) node.getItem()).getVersion();
			textLabel.drawStringSvg(name, 0, upperLeft, outputStream);
			textLabel.drawStringSvg(version, 1, upperLeft, outputStream);
		}
	}

	
	@Override
	public void initialize(Graphics2D graphics, TextLabel textLabel, List<Node<?>> nodes) {
		List<String> names = new ArrayList<>();
		List<String> versions = new ArrayList<>();
		for (Node<?> node : nodes) {
			String name = node.getName();
			names.add(name);
			versions.add(((NpmPackageName) node.getItem()).getVersion());
		}
		
		textLabel.initialize(graphics, 
				new TextLabelOption(Font.BOLD, names), 
				new TextLabelOption(Font.PLAIN, versions));
	}


	@Override
	public String getStereotype(Node<?> node) {
		return "NPM Package";
	}
}
