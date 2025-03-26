package com.electricmind.dependency.graph;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class TextLabel {

	Rectangle2D rectangle;
	List<Font> fonts = new ArrayList<>();
	List<Float> fontHeights = new ArrayList<>();
	
	public TextLabel(Rectangle2D rectangle) {
		this.rectangle = rectangle;
	}
	
	public void drawStringSvg(String text, int ordinal, Point2D upperLeft, OutputStream output) throws IOException {
		Font font = this.fonts.get(ordinal);
		float totalHeight = 0f;
		for (Float height : this.fontHeights) {
			totalHeight += height;
		}
		
		double y = upperLeft.getY() + this.getRectangle().getY() + (this.rectangle.getHeight() - totalHeight) / 2.0;
		for (int i = 0; i <= ordinal; i++) {
			if (i == ordinal) {
				y += (this.fontHeights.get(ordinal) / 2.0);
			} else {
				y += this.fontHeights.get(ordinal);
			}
		}
		drawStringSvg(text, upperLeft.getX() + this.rectangle.getCenterX(), y, font, output);
	}
	
	protected void drawStringSvg(String text, double x, double y, Font font, OutputStream output) throws IOException {
		String fontName = font.getFamily();
		float size = font.getSize2D();
		
		String fontWeight = "";
		if (font.getStyle() == Font.BOLD) {
			fontWeight = " font-weight=\"bold\" ";
		} else if (font.getStyle() == Font.ITALIC) {
			fontWeight = " font-style=\"italic\" ";
		}
		
		output.write(("<text x=\"" + x + "\" y=\"" + y + "\" text-anchor=\"middle\" font-size=\"" 
				+ size +"\" font-family=\"" + fontName + "\" " 
				+ fontWeight + " dominant-baseline=\"middle\">" 
				+ StringEscapeUtils.escapeXml(text) 
				+ "</text>").getBytes("UTF-8"));
	}
	
	public void initialize(Graphics2D graphics, TextLabelOption... options) {
		for (TextLabelOption option : options) {
			initializeFont(graphics, option.getFontStyle(), option.getText());
		}
	}

	private void initializeFont(Graphics2D graphics, int fontStyle, List<String> text) {
		float size = 10;
		Font base = new Font("Helvetica", fontStyle, (int) size);
		Font font = base;
		float fontHeight = 0;
		while (size >= 5.0f) {
			font = base.deriveFont(size);
			FontMetrics metrics = graphics.getFontMetrics(font);
			Rectangle2D bounds;
			double width = 0;
			for (String t : text) {
				bounds = metrics.getStringBounds(t.toString(), graphics);
				width = Math.max(bounds.getWidth(), width);
			}
			
			fontHeight = metrics.getHeight();
			if (width < this.rectangle.getWidth()) {
				break;
			} else {
				size -= 0.25;
			}
		}
		this.fonts.add(font);
		this.fontHeights.add(fontHeight);
	}
	
	
	public Rectangle2D getRectangle() {
		return this.rectangle;
	}

	public List<Font> getFonts() {
		return this.fonts;
	}
}
