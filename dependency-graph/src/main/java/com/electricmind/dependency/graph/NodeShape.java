package com.electricmind.dependency.graph;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.electricmind.dependency.Node;

/**
 * <p>A class that takes responsibility for drawing a particular shape.  This class can 
 * be subclassed to provide different shapes for various types of directed graph images.
 * 
 * <p>The simplest representation of a node is to draw a rectangle.
 * 
 * @author BC Holmes
 */
public class NodeShape<T> {

	private Dimension dimension;
	private Plot plot;
	private Font font;
	protected TextLabel label;

	public NodeShape() {
		this(new Dimension(100, 50));
	}
	
	protected NodeShape(Dimension dimension) {
		this.dimension = dimension;
		this.label = createLabel();
	}

	public Dimension getDimension() {
		return this.dimension;
	}

	protected TextLabel createLabel() {
		Rectangle2D rectangle = new Rectangle2D.Double(getWidth() * 0.1, getHeight() * 0.1, getTextAreaWidth(), getHeight() * 0.8);
		return new TextLabel(rectangle);
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	protected void drawSvg(Node<T> node, Point2D upperLeft, OutputStream outputStream) throws IOException {
		
		String shadowFill = HtmlColor.asHtml(getPlot().getShadowColor());
		String shapeFill = HtmlColor.asHtml(getPlot().getShapeFillColor());
		String shapeStroke = HtmlColor.asHtml(getPlot().getShapeLineColor());
		
		outputStream.write(("<rect x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 3) + "\" height=\"" 
				+ this.dimension.getHeight() + "\" width=\"" + this.dimension.getWidth() + "\" fill=\"" + shadowFill + "\" />").getBytes("UTF-8"));
		
		outputStream.write(("<rect x=\"" + (upperLeft.getX()) + "\" y=\"" + (upperLeft.getY()) + "\" height=\"" 
				+ this.dimension.getHeight() + "\" width=\"" + this.dimension.getWidth() + "\" fill=\"" 
				+ shapeFill + "\" stroke=\"" + shapeStroke + "\" stroke-width=\"1\"  />").getBytes("UTF-8"));
		
		this.label.drawStringSvg(node.getName(), 0, upperLeft, outputStream);

	}
	
	protected void draw(Graphics2D graphics, Node<T> node) {
		graphics.setPaint(getPlot().getShadowColor());
		graphics.fill(new Rectangle2D.Float(3, 3, this.dimension.width, this.dimension.height));

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setPaint(this.plot.getShapeFillColor());
		graphics.fill(new Rectangle2D.Float(0, 0, this.dimension.width, this.dimension.height));
		graphics.setColor(this.plot.getShapeLineColor());
		graphics.draw(new Rectangle2D.Float(0, 0, this.dimension.width, this.dimension.height));

		drawNodeName(graphics, node);
	}

	protected void drawNodeName(Graphics2D graphics, Node<T> node) {
		drawNodeName(graphics, node, this.dimension.getWidth() / 2.0, this.dimension.getHeight() / 2.0);
	}

	protected void drawNodeName(Graphics2D graphics, Node<T> node, double x, double y) {
		drawNodeName(graphics, node, new Point2D.Double(x, y));
	}
	
	protected void drawNodeName(Graphics2D graphics, Node<T> node, Point2D.Double centredAt) {
		graphics = (Graphics2D) graphics.create();
		try {
			graphics.translate(centredAt.getX(), centredAt.getY());
			graphics.setFont(this.font);
			drawString(graphics, node.getName());
		} finally {
			graphics.dispose();
		}
	}

	/**
	 * @param graphics - a temporary graphics reference, where 0,0 is the centre of the
	 *                   text to be drawn.  We assume that it'll be disposed immediately
	 *                   after this method call.
	 * @param text - the text to draw
	 */
	protected void drawString(Graphics2D graphics, String text) {
		FontMetrics metrics = graphics.getFontMetrics();
		Rectangle2D bounds = metrics.getStringBounds(text, graphics);
		double y = metrics.getAscent() + metrics.getLeading() - (bounds.getHeight() / 2.0);
		double x = -bounds.getWidth() / 2.0;
		graphics.drawString(text, (float) x, (float) y);
	}

	public double getHeight() {
		return this.dimension.getHeight();
	}

	public double getWidth() {
		return this.dimension.getWidth();
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	/**
	 * <p>Scan the list of nodes to determine information about how to draw each one.  
	 * For example, this method might try to determine the best font size to render the
	 * names in the space available.
	 * 
	 * @param graphics
	 * @param nodes
	 */
	public void initialize(Graphics2D graphics, List<Node<T>> nodes) {
		Font font = new Font("Helvetica", Font.PLAIN, 10);
		FontMetrics metrics = graphics.getFontMetrics(font);
		double width = 1.0;
		for (Node<T> node : nodes) {
			Rectangle2D bounds = metrics.getStringBounds(node.getName(), graphics);
			width = Math.max(bounds.getWidth(), width);
		}
		
		double ratio = Math.max(1.0, width / getTextAreaWidth());
		
		AffineTransform transform = new AffineTransform();
		transform.scale(1.00 / ratio, 1.0 / ratio);
		this.font = font.deriveFont(transform);
		
		List<String> text = nodes.stream().map(n -> n.getName()).collect(Collectors.toList());
		this.label.initialize(graphics, new TextLabelOption(Font.PLAIN, text));
	}

	protected double getTextAreaWidth() {
		return getWidth() * 0.8;
	}

	protected Font getFont() {
		return this.font;
	}

	protected void setFont(Font font) {
		this.font = font;
	}
}
