package com.electricmind.dependency.graph.shape;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.NodeShape;

public class ComponentShape<T> extends NodeShape<T> {

	private ImageIcon image;

	private void drawImage(Graphics2D graphics, Node<T> node) {
		if (this.image != null) {
			graphics.drawImage(this.image.getImage(), (int) (getWidth() - 20), 4, null);
		}
	}

	@Override
	protected void draw(Graphics2D graphics, Node<T> node) {
		super.draw(graphics, node);
		drawImage(graphics, node);
	}

	@Override
	protected void drawNodeName(Graphics2D graphics, Node<T> node) {
		double x = getWidth() / 2.0;
		double y = getHeight() / 2.0;
		if (this.image != null) {
			y += this.image.getIconHeight() / 2.0;
		}
		drawNodeName(graphics, node, x, y);
	}

	public void setImage(ImageProvider imageProvider) {
		this.image = imageProvider != null ? imageProvider.getImage() : null;
	}
}
