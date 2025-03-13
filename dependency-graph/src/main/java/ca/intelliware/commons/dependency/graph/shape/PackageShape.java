package ca.intelliware.commons.dependency.graph.shape;

import static ca.intelliware.commons.dependency.graph.shape.CommonImage.PACKAGE_ICON;

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

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;

import com.electricmind.dependency.Node;

import ca.intelliware.commons.dependency.graph.HtmlColor;
import ca.intelliware.commons.dependency.graph.NodeShape;

public class PackageShape<T> extends NodeShape<T> {
	
	private PackageName prefix;
	private float fontHeight;

	public PackageShape() {
		setDimension(new Dimension(150, 75));
	}

	@Override
	protected void draw(Graphics2D graphics, Node<T> node) {
		graphics.setPaint(getPlot().getShadowColor());
		double height = 20.0;
		graphics.fill(new Rectangle2D.Double(3, 3, getWidth() / 3.0, height));
		graphics.fill(new Rectangle2D.Double(3, 3 + height, getWidth(), getHeight() - height));
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setPaint(getPlot().getShapeFillColor());
		graphics.fill(new Rectangle2D.Double(0, 0, getWidth() / 3.0, height));
		graphics.fill(new Rectangle2D.Double(0, height, getWidth(), getHeight() - height));
		graphics.setColor(getPlot().getShapeLineColor());
		graphics.draw(new Rectangle2D.Double(0, 0, getWidth() / 3.0, height));
		graphics.draw(new Rectangle2D.Double(0, height, getWidth(), getHeight() - height));
		
		graphics.drawImage(getPackageImage().getImage(), (int) 2, 2, null);
		drawNodeName(graphics, node);
	}
	
	protected void drawSvg(Node<T> node, Point2D upperLeft, OutputStream outputStream) throws IOException {
		
		String shadowFill = HtmlColor.asHtml(getPlot().getShadowColor());
		String shapeFill = HtmlColor.asHtml(getPlot().getShapeFillColor());
		String shapeStroke = HtmlColor.asHtml(getPlot().getShapeLineColor());

		double height = 20.0;
		outputStream.write(("<rect x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 3) + "\" height=\"" 
				+ height + "\" width=\"" + (getDimension().getWidth() / 3.0) + "\" fill=\"" + shadowFill + "\" />").getBytes("UTF-8"));
		outputStream.write(("<rect x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 3  + height) + "\" height=\"" 
				+ (getDimension().getHeight() - height) + "\" width=\"" + getDimension().getWidth() + "\" fill=\"" 
				+ shadowFill + "\" />").getBytes("UTF-8"));
		outputStream.write(("<rect x=\"" + (upperLeft.getX()) + "\" y=\"" + upperLeft.getY() + "\" height=\"" 
				+ height + "\" width=\"" + (getDimension().getWidth() / 3.0) + "\" fill=\"" 
				+ shapeFill + "\" stroke=\"" + shapeStroke + "\" stroke-width=\"1\"  />").getBytes("UTF-8"));
		outputStream.write(("<rect x=\"" + (upperLeft.getX()) + "\" y=\"" + (upperLeft.getY() + height) + "\" height=\"" 
				+ (getDimension().height - height) + "\" width=\"" + getDimension().getWidth() + "\" fill=\"" 
				+ shapeFill + "\" stroke=\"" + shapeStroke + "\" stroke-width=\"1\"  />").getBytes("UTF-8"));
		
		outputStream.write(("<image x=\"" + (upperLeft.getX() + 3) + "\" y=\"" + (upperLeft.getY() + 2) + "\" href=\"data:image/png;base64," + PACKAGE_ICON.getBase64EncodedImage() + "\" /> ").getBytes("UTF-8"));

		
		PackageName packageName = new PackageName(node.getName());
		if (StringUtils.isBlank(this.prefix.toString()) || this.prefix.equals(packageName)) {
			this.drawStringSvg(node.getName(), getWidth() / 2.0 + upperLeft.getX(), getHeight() / 2.0 + upperLeft.getY(), outputStream);
		} else {
			this.drawStringSvg(this.prefix.toString() + ".", 
					getWidth() / 2.0 + upperLeft.getX(), 
					getHeight() / 2.0 - this.fontHeight / 2.0 + upperLeft.getY() + getPackageImage().getIconHeight() / 2.0, 
					outputStream);
			this.drawStringSvg(packageName.removePrefix(this.prefix).toString(), 
					getWidth() / 2.0 + upperLeft.getX(), 
					getHeight() / 2.0 + this.fontHeight / 2.0 + upperLeft.getY() + getPackageImage().getIconHeight() / 2.0, 
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
		
		Font font = new Font("Helvetica", Font.PLAIN, 10);
		FontMetrics metrics = graphics.getFontMetrics(font);
		Rectangle2D bounds = metrics.getStringBounds(this.prefix.toString() + ".", graphics);
		double width = bounds.getWidth();
		for (Node<T> node : nodes) {
			PackageName name = new PackageName(node.getName());
			bounds = metrics.getStringBounds(name.removePrefix(this.prefix).toString(), graphics);
			width = Math.max(bounds.getWidth(), width);
		}
		
		double ratio = Math.max(1.0, width / getTextAreaWidth());
		
		AffineTransform transform = new AffineTransform();
		transform.scale(1.00 / ratio, 1.0 / ratio);
		setFont(font.deriveFont(transform));
		this.fontHeight = metrics.getHeight();
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
}
