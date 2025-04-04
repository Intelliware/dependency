package com.electricmind.dependency.graph.shape;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.electricmind.dependency.Describer;
import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.TextLabel;
import com.electricmind.dependency.graph.TextLabelOption;
import com.electricmind.dependency.graph.TextLabelStrategy;

public class MavenArtifactLabelStrategy extends TextLabelStrategy implements StereotypeProvider {
	
	boolean simple = false;

	@Override
	public void populate(Node<?> node, TextLabel textLabel, Point2D upperLeft, OutputStream outputStream) throws IOException {
		if (this.simple) {
			super.populate(node, textLabel, upperLeft, outputStream);
		} else {
			String name = node.getName();
			String[] parts = StringUtils.split(name, ":");
			if (parts.length >= 2) {
				textLabel.drawStringSvg(parts[0], 0, upperLeft, outputStream);
				textLabel.drawStringSvg(parts[1], 1, upperLeft, outputStream);
				if (parts.length >= 3) {
					textLabel.drawStringSvg(parts[2], 2, upperLeft, outputStream);
				}
			}
		}
	}

	
	@Override
	public void initialize(Graphics2D graphics, TextLabel textLabel, List<Node<?>> nodes) {
		List<String> groupIds = new ArrayList<>();
		List<String> artifactIds = new ArrayList<>();
		List<String> versions = new ArrayList<>();
		for (Node<?> node : nodes) {
			String name = node.getName();
			String[] parts = StringUtils.split(name, ":");
			if (parts.length >= 2) {
				groupIds.add(parts[0]);
				artifactIds.add(parts[1]);
				if (parts.length >= 3) {
					versions.add(parts[2]);
				}
			} else {
				groupIds.clear();
				artifactIds.clear();
				break;
			}
		}
		
		if (groupIds.isEmpty()) {
			this.simple = true;
			super.initialize(graphics, textLabel, nodes);
		} else if (versions.isEmpty()) {
			textLabel.initialize(graphics, 
					new TextLabelOption(Font.PLAIN, groupIds), 
					new TextLabelOption(Font.PLAIN, artifactIds));
		} else {
			textLabel.initialize(graphics, 
					new TextLabelOption(Font.PLAIN, groupIds), 
					new TextLabelOption(Font.PLAIN, artifactIds),
					new TextLabelOption(Font.PLAIN, versions));
		}
	}


	@Override
	public String getStereotype(Node<?> node) {
		try {
			String packaging = new Describer().getPropertyValue(node.getItem(), "packaging");
			return StringUtils.isEmpty(packaging) ? "artifact" : packaging;
		} catch (RuntimeException e) {
			return "artifact";
		}
	}
}
