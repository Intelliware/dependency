package com.electricmind.dependency.graph.shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang3.SystemUtils;

import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.graph.Grapher;

public abstract class DependencyGraphTester<E> {
	public abstract DependencyManager<E> createManager();

	public abstract Grapher<E> createGrapher(DependencyManager<E> manager);

	public void process() throws FileNotFoundException, IOException {
		DependencyManager<E> manager = createManager();
		Grapher<E> grapher = createGrapher(manager);
		graphAll(grapher);
	}

	/**
	 * Graphs to PNG and SVG.
	 * Override if you only want to graph only to PNG or SVG, or you want a PNG with explicit width and height.
	 * @param grapher
	 * @throws IOException
	 */
	public void graphAll(Grapher<E> grapher) throws IOException {
		graphToPng(grapher);
		graphToSvg(grapher);
	}

	public String getFileNameBase() { return getClass().getSimpleName(); }

	public void graphToPng(Grapher<E> grapher) throws IOException {
		File file = new File(SystemUtils.JAVA_IO_TMPDIR, getFileNameBase() + ".png");
		try (OutputStream output = new FileOutputStream(file)) {
			grapher.createPng(output);
		}
	}

	public void graphToPng(Grapher<E> grapher, int width, int height) throws IOException {
		File file = new File(SystemUtils.JAVA_IO_TMPDIR, getFileNameBase() + ".png");
		try (OutputStream output = new FileOutputStream(file)) {
			grapher.createPng(output, width, height);
		}
	}

	public void graphToSvg(Grapher<E> grapher) throws IOException {
		File file = new File(SystemUtils.JAVA_IO_TMPDIR, getFileNameBase() + ".svg");
		try (OutputStream output = new FileOutputStream(file)) {
			grapher.createSvg(output);
		}
	}

	@Override
	public String toString() {
		return getFileNameBase();
	}
}