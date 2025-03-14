package com.electricmind.dependency.graph.shape;

import static com.electricmind.dependency.graph.shape.CommonImage.PACKAGE_ICON;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.HtmlColor;
import com.electricmind.dependency.graph.NodeShape;
import com.electricmind.dependency.graph.TextLabel;
import com.electricmind.dependency.graph.TextLabelOption;

public class PackageShape<T> extends NodeShape<T> {
	
	private static final double TAB_HEIGHT = 20.0;
	private static final double PADDING = 8.0;
	
	private PackageName prefix;

	public PackageShape() {
		super(new Dimension(150, 75));
	}

	protected TextLabel initializeLabel() {
		Rectangle2D rectangle = new Rectangle2D.Double(PADDING, PADDING + TAB_HEIGHT, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING);
		return new TextLabel(rectangle);
	}

	@Override
	protected void draw(Graphics2D graphics, Node<T> node) {
		graphics.setPaint(getPlot().getShadowColor());
		graphics.fill(new Rectangle2D.Double(3, 3, getWidth() / 3.0, TAB_HEIGHT));
		graphics.fill(new Rectangle2D.Double(3, 3 + TAB_HEIGHT, getWidth(), getHeight() - TAB_HEIGHT));
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setPaint(getPlot().getShapeFillColor());
		graphics.fill(new Rectangle2D.Double(0, 0, getWidth() / 3.0, TAB_HEIGHT));
		graphics.fill(new Rectangle2D.Double(0, TAB_HEIGHT, getWidth(), getHeight() - TAB_HEIGHT));
		graphics.setColor(getPlot().getShapeLineColor());
		graphics.draw(new Rectangle2D.Double(0, 0, getWidth() / 3.0, TAB_HEIGHT));
		graphics.draw(new Rectangle2D.Double(0, TAB_HEIGHT, getWidth(), getHeight() - TAB_HEIGHT));
		
		graphics.drawImage(getPackageImage().getImage(), (int) 2, 2, null);
		drawNodeName(graphics, node);
	}
	
	protected void drawSvg(Node<T> node, Point2D upperLeft, OutputStream outputStream) throws IOException {
		
		String shadowFill = HtmlColor.asHtml(getPlot().getShadowColor());
		String shapeFill = HtmlColor.asHtml(getPlot().getShapeFillColor());
		String shapeStroke = HtmlColor.asHtml(getPlot().getShapeLineColor());

		outputStream.write(("<rect x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 3) + "\" height=\"" 
				+ TAB_HEIGHT + "\" width=\"" + (getDimension().getWidth() / 3.0) + "\" fill=\"" + shadowFill + "\" />").getBytes("UTF-8"));
		outputStream.write(("<rect x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 3  + TAB_HEIGHT) + "\" height=\"" 
				+ (getDimension().getHeight() - TAB_HEIGHT) + "\" width=\"" + getDimension().getWidth() + "\" fill=\"" 
				+ shadowFill + "\" />").getBytes("UTF-8"));
		outputStream.write(("<rect x=\"" + (upperLeft.getX()) + "\" y=\"" + upperLeft.getY() + "\" height=\"" 
				+ TAB_HEIGHT + "\" width=\"" + (getDimension().getWidth() / 3.0) + "\" fill=\"" 
				+ shapeFill + "\" stroke=\"" + shapeStroke + "\" stroke-width=\"1\"  />").getBytes("UTF-8"));
		outputStream.write(("<rect x=\"" + (upperLeft.getX()) + "\" y=\"" + (upperLeft.getY() + TAB_HEIGHT) + "\" height=\"" 
				+ (getDimension().height - TAB_HEIGHT) + "\" width=\"" + getDimension().getWidth() + "\" fill=\"" 
				+ shapeFill + "\" stroke=\"" + shapeStroke + "\" stroke-width=\"1\"  />").getBytes("UTF-8"));
		
		outputStream.write(("<image x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 2) + "\" href=\"data:image/png;base64," 
				+ PACKAGE_ICON.getBase64EncodedImage() + "\" /> ").getBytes("UTF-8"));

		
		PackageName packageName = new PackageName(node.getName());
		if (StringUtils.isBlank(this.prefix.toString()) || this.prefix.equals(packageName)) {
			this.label.drawStringSvg(node.getName(), 0, 
					new Point2D.Double(upperLeft.getX() + (getWidth() - getTextAreaWidth()) / 2.0,
							upperLeft.getY() + TAB_HEIGHT + (getHeight() - TAB_HEIGHT - this.label.getRectangle().getHeight()) / 2.0), 
					outputStream);
		} else {
			this.label.drawStringSvg(this.prefix.toString() + ".", 0, 
					new Point2D.Double(upperLeft.getX() + (getWidth() - getTextAreaWidth()) / 2.0,
							upperLeft.getY() + TAB_HEIGHT + (getHeight() - TAB_HEIGHT - this.label.getRectangle().getHeight()) / 2.0), 
					outputStream);
			this.label.drawStringSvg(packageName.removePrefix(this.prefix).toString(), 1,
					new Point2D.Double(upperLeft.getX() + (getWidth() - getTextAreaWidth()) / 2.0,
							upperLeft.getY() + TAB_HEIGHT + (getHeight() - TAB_HEIGHT - this.label.getRectangle().getHeight()) / 2.0), 
					outputStream);
		}
	}

	protected ImageIcon getPackageImage() {
		return PACKAGE_ICON.getImage();
	}
	
	@Override
	protected void drawNodeName(Graphics2D graphics, Node<T> node) {
		double x = getWidth() / 2.0;
		double y = getHeight() / 2.0 + getPackageImage().getIconHeight() / 2.0;
		drawNodeName(graphics, node, x, y);
	}
	
	protected void drawNodeName(Graphics2D graphics, Node<T> node, Point2D.Double centredAt) {
		PackageName packageName = new PackageName(node.getName());
		if (StringUtils.isBlank(this.prefix.toString()) || this.prefix.equals(packageName)) {
			super.drawNodeName(graphics, node, centredAt);
		} else {
			graphics = (Graphics2D) graphics.create();
			try {
				graphics.setFont(getFont());
				FontMetrics metrics = graphics.getFontMetrics();
				
				graphics.translate(centredAt.getX(), centredAt.getY() - metrics.getHeight() / 2.0);
				drawString(graphics, this.prefix.toString() + ".");
	
				graphics.translate(0.0, metrics.getHeight());
				drawString(graphics, packageName.removePrefix(this.prefix).toString());
				
			} finally {
				graphics.dispose();
			}
		}
	}
	
	public void initialize(Graphics2D graphics, List<Node<T>> nodes) {
		this.prefix = getPackageNamePrefix(nodes);
		List<String> remainders = new ArrayList<>();
		for (Node<T> node : nodes) {
			remainders.add(new PackageName(node.getName()).removePrefix(this.prefix).toString());
		}
		
		this.label.initialize(graphics, 
				new TextLabelOption(Font.PLAIN, Arrays.asList(this.prefix.toString())), 
				new TextLabelOption(Font.BOLD, remainders));
		setFont(this.label.getFonts().get(0));
	}

	private PackageName getPackageNamePrefix(List<Node<T>> nodes) {
		PackageName prefix = null;
		for (Node<T> node : nodes) {
			if (prefix == null) {
				prefix = new PackageName(node.getName());
			} else {
				prefix = prefix.getCommonPrefix(new PackageName(node.getName()));
			}
		}
		return prefix;
	}
	
	protected double getTextAreaWidth() {
		return this.label.getRectangle().getWidth();
	}
}
