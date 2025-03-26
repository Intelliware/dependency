package com.electricmind.dependency.graph.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public class SimpleNodeDiagram {
	public static void main(String[] args) throws Exception {
		new SimpleNodeDiagram().process();
	}

	private void process() throws IOException {
		DependencyManager<String> manager = new DependencyManager<String>();
		manager.add("1", "13");
		manager.add("1", "4");
		manager.add("1", "3");
		manager.add("2", "3");
		manager.add("2", "20");
		manager.add("3", "4");
		manager.add("3", "5");
		manager.add("4", "6");
		manager.add("5", "7");
		manager.add("6", "8");
		manager.add("6", "16");
		manager.add("6", "23");
		manager.add("7", "9");
		manager.add("8", "10");
		manager.add("8", "10");
		manager.add("8", "11");
		manager.add("9", "12");
		manager.add("10", "13");
		manager.add("10", "14");
		manager.add("10", "15");
		manager.add("11", "15");
		manager.add("11", "16");
		manager.add("12", "20");
		manager.add("13", "17");
		manager.add("14", "17");
		manager.add("14", "18");
		manager.add("16", "19");
		manager.add("16", "20");
		manager.add("18", "21");
		manager.add("19", "22");
		manager.add("21", "23");
		manager.add("22", "23");

		File file = new File(SystemUtils.JAVA_IO_TMPDIR, "SimpleNodeDiagram.png");
		System.out.println(file.getAbsolutePath());
		OutputStream output = new FileOutputStream(file);
		try {
			Grapher<String> grapher = new Grapher<String>(manager);
			grapher.getPlot().setShapeFillColor(new Color(216, 223, 238));
			grapher.getShape().setDimension(new Dimension(40, 40));
			grapher.createPng(output, 500, 500);
		} finally {
			output.close();
		}
	}
}
