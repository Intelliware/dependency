package com.electricmind.dependency.graph.shape;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.TextLabel;
import com.electricmind.dependency.graph.TextLabelOption;
import com.electricmind.dependency.graph.TextLabelStrategy;

public class PackageNameLabelStrategy extends TextLabelStrategy {

	List<PackageName> prefixes;

	@Override
	public void populate(Node<?> node, TextLabel textLabel, Point2D upperLeft, OutputStream outputStream) throws IOException {
		PackageName packageName = new PackageName(node.getName());
		PackageName prefix = getPrefixOf(packageName);
		if (StringUtils.isBlank(prefix.toString()) || prefix.equals(packageName)) {
			textLabel.drawStringSvg(node.getName(), 0, upperLeft, outputStream);
		} else {
			textLabel.drawStringSvg(prefix.toString() + ".", 0, upperLeft, outputStream);
			textLabel.drawStringSvg(packageName.removePrefix(prefix).toString(), 1, upperLeft, outputStream);
		}
	}

	PackageName getPrefixOf(PackageName packageName) {
		List<PackageName> list = this.prefixes.stream().filter(p -> packageName.startsWith(p)).collect(Collectors.toList());
		return list.isEmpty() ? new PackageName("") : list.get(0);
	}
	
	private List<PackageName> getPackageNamePrefix(List<Node<?>> nodes) {
		List<PackageName> prefixes = new ArrayList<>();
		for (Node<?> node : nodes) {
			if (prefixes.isEmpty()) {
				prefixes.add(new PackageName(node.getName()));
			} else {
				for (int i = 0; i < prefixes.size(); i++) {					
					PackageName prefix = prefixes.get(i);
					PackageName temp = prefix.getCommonPrefix(new PackageName(node.getName()));
					if (temp.toString().length() == 0 && i == (prefixes.size() - 1)) {
						prefixes.add(new PackageName(node.getName()));
						break;
					} else if (temp.toString().length() == 0) {
						// doesn't match this one... try another
					} else if (!prefix.equals(temp)) {
						prefixes.set(i, temp);
						break;
					}
				}
			}
		}
		
		return prefixes;
	}
	
	@Override
	public void initialize(Graphics2D graphics, TextLabel textLabel, List<Node<?>> nodes) {
		this.prefixes = getPackageNamePrefix(nodes);
		List<String> remainders = new ArrayList<>();
		
		for (Node<?> node : nodes) {
			PackageName packageName = new PackageName(node.getName());
			for (PackageName prefix : this.prefixes) {
				if (packageName.startsWith(prefix)) {
					remainders.add(packageName.removePrefix(prefix).toString());
				}
			}
		}
		
		textLabel.initialize(graphics, 
				new TextLabelOption(Font.PLAIN, this.prefixes.stream().map(p -> p.toString()).collect(Collectors.toList())), 
				new TextLabelOption(Font.BOLD, remainders));
		
	}
}
