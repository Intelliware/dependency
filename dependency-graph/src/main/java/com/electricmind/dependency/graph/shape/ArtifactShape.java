package com.electricmind.dependency.graph.shape;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.NodeShape;
import com.electricmind.dependency.graph.TextLabel;
import com.electricmind.dependency.graph.TextLabelOption;

public class ArtifactShape<T> extends NodeShape<T> {

	private static final int PADDING = 8;
	private static String ARTIFACT_SHAPE = "<path  "
			+ "style=\"fill:none;stroke:#000000;stroke-width:0.999972px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" "
			+ "d=\"M 10.575434,0.49998599 V 5.7056524 h 5.373582 M 0.49998599,0.49998599 V 19.979214 H 15.949016 V 5.7056524 L 10.575434,0.49998599 Z\"/>";
	
	private TextLabel stereotypeLabel;
	
	public ArtifactShape() {
		super(new Dimension(160, 75));
		Rectangle2D rectangle = new Rectangle2D.Double(20, 2*PADDING, 120, 10);
		this.stereotypeLabel = new TextLabel(rectangle);
	}
	
	@Override
	protected TextLabel createLabel() {
		Rectangle2D rectangle = new Rectangle2D.Double(PADDING, 25, getWidth() - 2 * PADDING, getHeight() - 30);
		return new TextLabel(rectangle);
	}
	
	@Override
	public void initialize(Graphics2D graphics, List<Node<T>> nodes) {
		super.initialize(graphics, nodes);
		this.stereotypeLabel.initialize(graphics, new TextLabelOption(Font.ITALIC, Arrays.asList("<<artifact>>")));
	}
	
	@Override
	protected void drawSvg(Node<T> node, Point2D upperLeft, OutputStream outputStream) throws IOException {
		super.drawSvg(node, upperLeft, outputStream);
		
		outputStream.write(("<g transform=\"translate("
				+ (upperLeft.getX() + (this.getWidth() - 25))
				+ ","
				+ (upperLeft.getY() + 8)
				+ ")\">").getBytes("UTF-8"));
		outputStream.write(ARTIFACT_SHAPE.getBytes("UTF-8"));
		outputStream.write(("</g>").getBytes("UTF-8"));

		this.stereotypeLabel.drawStringSvg("<<artifact>>", 0, new Point2D.Double(upperLeft.getX() + 20, upperLeft.getY() + 2 * PADDING), outputStream);
	}
}
