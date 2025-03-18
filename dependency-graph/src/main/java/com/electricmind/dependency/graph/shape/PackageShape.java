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
import java.util.List;
import java.util.stream.Collectors;

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
	
	private List<PackageName> prefixes;

	public PackageShape() {
		super(new Dimension(150, 75));
	}

	@Override
	protected TextLabel createLabel() {
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
		PackageName prefix = getPrefixOf(packageName);
		if (StringUtils.isBlank(prefix.toString()) || prefix.equals(packageName)) {
			this.label.drawStringSvg(node.getName(), 0, 
					new Point2D.Double(upperLeft.getX() + (getWidth() - getTextAreaWidth()) / 2.0,
							upperLeft.getY() + TAB_HEIGHT + (getHeight() - TAB_HEIGHT - this.label.getRectangle().getHeight()) / 2.0), 
					outputStream);
		} else {
			this.label.drawStringSvg(prefix.toString() + ".", 0, 
					new Point2D.Double(upperLeft.getX() + (getWidth() - getTextAreaWidth()) / 2.0,
							upperLeft.getY() + TAB_HEIGHT + (getHeight() - TAB_HEIGHT - this.label.getRectangle().getHeight()) / 2.0), 
					outputStream);
			this.label.drawStringSvg(packageName.removePrefix(prefix).toString(), 1,
					new Point2D.Double(upperLeft.getX() + (getWidth() - getTextAreaWidth()) / 2.0,
							upperLeft.getY() + TAB_HEIGHT + (getHeight() - TAB_HEIGHT - this.label.getRectangle().getHeight()) / 2.0), 
					outputStream);
		}
	}

	private PackageName getPrefixOf(PackageName packageName) {
		List<PackageName> list = this.prefixes.stream().filter(p -> packageName.startsWith(p)).collect(Collectors.toList());
		return list.isEmpty() ? new PackageName("") : list.get(0);
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
		PackageName prefix = getPrefixOf(packageName);
		if (StringUtils.isBlank(prefix.toString()) || prefix.equals(packageName)) {
			super.drawNodeName(graphics, node, centredAt);
		} else {
			graphics = (Graphics2D) graphics.create();
			try {
				graphics.setFont(getFont());
				FontMetrics metrics = graphics.getFontMetrics();
				
				graphics.translate(centredAt.getX(), centredAt.getY() - metrics.getHeight() / 2.0);
				drawString(graphics, prefix.toString() + ".");
	
				graphics.translate(0.0, metrics.getHeight());
				drawString(graphics, packageName.removePrefix(prefix).toString());
				
			} finally {
				graphics.dispose();
			}
		}
	}
	
	public void initialize(Graphics2D graphics, List<Node<T>> nodes) {
		this.prefixes = getPackageNamePrefix(nodes);
		List<String> remainders = new ArrayList<>();
		
		for (Node<T> node : nodes) {
			PackageName packageName = new PackageName(node.getName());
			for (PackageName prefix : this.prefixes) {
				if (packageName.startsWith(prefix)) {
					remainders.add(packageName.removePrefix(prefix).toString());
				}
			}
		}
		
		this.label.initialize(graphics, 
				new TextLabelOption(Font.PLAIN, this.prefixes.stream().map(p -> p.toString()).collect(Collectors.toList())), 
				new TextLabelOption(Font.BOLD, remainders));
		setFont(this.label.getFonts().get(0));
	}

	private List<PackageName> getPackageNamePrefix(List<Node<T>> nodes) {
		List<PackageName> prefixes = new ArrayList<>();
		for (Node<T> node : nodes) {
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
	
	protected double getTextAreaWidth() {
		return this.label.getRectangle().getWidth();
	}
}
