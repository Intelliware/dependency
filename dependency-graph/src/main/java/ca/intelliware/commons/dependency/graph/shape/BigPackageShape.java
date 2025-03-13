package ca.intelliware.commons.dependency.graph.shape;

import static ca.intelliware.commons.dependency.graph.shape.CommonImage.PACKAGE_BIG_ICON;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.ImageIcon;

import com.electricmind.dependency.Node;

public class BigPackageShape<T> extends PackageShape<T> {
	
	public BigPackageShape() {
		setDimension(new Dimension(120, 70));
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
		
		outputStream.write(("<image x=\"" + (upperLeft.getX() + x) + "\" y=\"" + upperLeft.getY() + "\" href=\"data:image/png;base64," + PACKAGE_BIG_ICON.getBase64EncodedImage() + "\" /> ").getBytes("UTF-8"));
		
		double y = getHeight() / 2.0 + getPackageImage().getIconHeight() / 2.0;
		this.drawStringSvg(node.getName(), getWidth() / 2.0 + upperLeft.getX(), y + upperLeft.getY(), outputStream);
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
