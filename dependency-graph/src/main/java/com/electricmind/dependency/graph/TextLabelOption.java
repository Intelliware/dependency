package com.electricmind.dependency.graph;

import java.util.List;

public class TextLabelOption {

	private int fontStyle;
	private List<String> text;

	public TextLabelOption(int fontStyle, List<String> text) {
		this.fontStyle = fontStyle;
		this.text = text;
	}

	public int getFontStyle() {
		return this.fontStyle;
	}

	public List<String> getText() {
		return this.text;
	}
}
