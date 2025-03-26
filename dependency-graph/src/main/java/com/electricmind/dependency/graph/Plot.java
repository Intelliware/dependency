package com.electricmind.dependency.graph;

import static java.awt.Color.GRAY;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import com.electricmind.dependency.Node;

public class Plot {
	
	class SimpleColorProvider implements ColorProvider {
		Color color;
		
		SimpleColorProvider(Color color) {
			this.color = color;
		}
		
		@Override
		public Color getColor(Node<?> node) {
			return this.color;
		}
	}

	private static final int FIFTY_PERCENT_TRANSPARENT = 128;
	private static final Color FIFTY_PERCENT_TRANSPARENT_GRAY = 
		new Color(GRAY.getRed(), GRAY.getGreen(), GRAY.getBlue(), FIFTY_PERCENT_TRANSPARENT);
	private static final Color DARK_RED = new Color(127, 0, 0);
	private static final Stroke DASHED_STROKE;
	
	static {
		BasicStroke stroke = new BasicStroke();
		DASHED_STROKE = new BasicStroke(stroke.getLineWidth(), stroke.getEndCap(),
				stroke.getLineJoin(), stroke.getMiterLimit(), new float[] { 8f, 5f}, 0f);
	}
	
	private Color shadowColor = FIFTY_PERCENT_TRANSPARENT_GRAY;
	private ColorProvider shapeFillColorProvider = new SimpleColorProvider(Color.WHITE);
	private Color shapeLineColor = Color.BLACK;
	private Color layerBackgroundColor = Color.WHITE;
	private Color layerAlternatingColor = new Color(214, 239, 199);
	private Color arrowColor = Color.DARK_GRAY;
	private Color reverseArrowColor = DARK_RED;
	private Stroke arrowStroke = DASHED_STROKE;
	private boolean useWeights = true;

	public Color getShadowColor() {
		return this.shadowColor;
	}

	public void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
	}

	public Color getShapeLineColor() {
		return this.shapeLineColor;
	}

	public void setShapeLineColor(Color shapeLineColor) {
		this.shapeLineColor = shapeLineColor;
	}

	public ColorProvider getShapeFillColorProvider() {
		return this.shapeFillColorProvider;
	}

	public void setShapeFillColor(Color shapeFillColor) {
		this.shapeFillColorProvider = new SimpleColorProvider(shapeFillColor);
	}

	public Color getLayerBackgroundColor() {
		return this.layerBackgroundColor;
	}

	public void setLayerBackgroundColor(Color layerBackgroundColor) {
		this.layerBackgroundColor = layerBackgroundColor;
	}

	public Color getLayerAlternatingColor() {
		return this.layerAlternatingColor;
	}

	public void setLayerAlternatingColor(Color layerAlternatingColor) {
		this.layerAlternatingColor = layerAlternatingColor;
	}

	public Color getArrowColor() {
		return this.arrowColor;
	}

	public void setArrowColor(Color arrowColor) {
		this.arrowColor = arrowColor;
	}

	public Stroke getArrowStroke() {
		return this.arrowStroke;
	}

	public void setArrowStroke(Stroke arrowStroke) {
		this.arrowStroke = arrowStroke;
	}

	public Color getReverseArrowColor() {
		return this.reverseArrowColor;
	}

	public void setReverseArrowColor(Color reverseArrowColor) {
		this.reverseArrowColor = reverseArrowColor;
	}

	public boolean isUseWeights() {
		return this.useWeights;
	}

	public void setUseWeights(boolean useWeights) {
		this.useWeights = useWeights;
	}

	public void setShapeFillColorProvider(ColorProvider shapeFillColorProvider) {
		this.shapeFillColorProvider = shapeFillColorProvider;
	}
}
