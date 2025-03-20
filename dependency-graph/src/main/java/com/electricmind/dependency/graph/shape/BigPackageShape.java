package com.electricmind.dependency.graph.shape;

import static com.electricmind.dependency.graph.shape.CommonImage.PACKAGE_BIG_ICON;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.ImageIcon;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.TextLabel;

public class BigPackageShape<T> extends PackageShape<T> {
	
	public BigPackageShape() {
		setDimension(new Dimension(120, 70));
	}
	
	@Override
	protected TextLabel createLabel() {
		int iconHeight = getPackageImage().getIconHeight();
		return new TextLabel(new Rectangle2D.Double(PADDING, iconHeight + PADDING, getWidth() - 2*PADDING, getHeight() - 2*PADDING - iconHeight));
	}

	protected void draw(Graphics2D graphics, Node<T> node) {
		double x = (getWidth() - getPackageImage().getIconWidth()) / 2.0;
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.drawImage(getPackageImage().getImage(), (int) x, 0, null);
		graphics.setColor(getPlot().getShapeLineColor());
		drawNodeName(graphics, node);
	}

	@Override
	protected void drawSvg(Node<T> node, Point2D upperLeft, OutputStream outputStream) throws IOException {
		double x = (getWidth() - getPackageImage().getIconWidth()) / 2.0;
		
		outputStream.write(("<image x=\"" + (upperLeft.getX() + x) + "\" y=\"" + upperLeft.getY() 
			+ "\" href=\"data:image/png;base64," + PACKAGE_BIG_ICON.getBase64EncodedImage() + "\" /> ").getBytes("UTF-8"));

		this.labelStrategy.populate(node, this.label, upperLeft, outputStream);
	}
	
	@Override
	protected ImageIcon getPackageImage() {
		return PACKAGE_BIG_ICON.getImage();
	}

	@Override
	protected double getTextAreaWidth() {
		return getWidth();
	}
}
