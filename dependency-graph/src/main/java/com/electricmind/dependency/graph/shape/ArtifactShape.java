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
	private static String XML_ARTIFACT_SHAPE = ARTIFACT_SHAPE + "<path "
			+ "style=\"fill:none;stroke:#000000;stroke-width:0.7;stroke-linecap:round;stroke-linejoin:miter;stroke-dasharray:none;stroke-opacity:1\" "
			+ "d=\"m 9.2191888,7.2701619 -2.066052,5.8780631 m 3.2009262,-4.8014504 2.095151,2.0925764 -2.095151,2.097723 M 5.9891638,8.3467746 3.9887395,10.412875 5.9891638,12.537074 v 0\"/>";
	private static String JAR_ARTIFACT_SHAPE = "<g>"
			+ "<path style=\"fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:0;stroke-dasharray:none\" d=\"m 11.945981,2.6898174 -0.01687,0.8509402 c 1.444873,0 2.608076,1.1632017 2.608076,2.6080755 V 17.374897 c 0,1.444874 -1.163203,2.608075 -2.608076,2.608075 H 3.8402971 c -1.4448739,0 -2.6080756,-1.163201 -2.6080756,-2.608075 V 6.1488331 c 0,-1.4448738 1.1632017,-2.6080755 2.6080756,-2.6080755 L 3.8398948,2.6711828\"/>"
			+ "<rect style=\"fill:#000000;fill-opacity:1;fill-rule:evenodd;stroke:#000000;stroke-width:0.708036;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:0;stroke-dasharray:none\" width=\"10.439284\" height=\"1.5017917\" x=\"2.6650615\" y=\"0.35401788\" ry=\"0.18314534\"/>"
			+ "</g>";
	
	private TextLabel stereotypeLabel;
	
	public ArtifactShape() {
		super(new Dimension(160, 75));
		Rectangle2D rectangle = new Rectangle2D.Double(20, PADDING, 120, 10);
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
		
		String packaging = "artifact";
		if (this.labelStrategy instanceof StereotypeProvider) {
			packaging = ((StereotypeProvider) this.labelStrategy).getStereotype(node);
		}
		outputStream.write(("<g transform=\"translate("
				+ (upperLeft.getX() + (this.getWidth() - 25))
				+ ","
				+ (upperLeft.getY() + 8)
				+ ")\">").getBytes("UTF-8"));
		if ("pom".equals(packaging)) {
			outputStream.write(XML_ARTIFACT_SHAPE.getBytes("UTF-8"));
		} else if ("jar".equals(packaging)) {
			outputStream.write(JAR_ARTIFACT_SHAPE.getBytes("UTF-8"));
		} else {
			outputStream.write(ARTIFACT_SHAPE.getBytes("UTF-8"));
		}
		outputStream.write(("</g>").getBytes("UTF-8"));

		this.stereotypeLabel.drawStringSvg("<<" + packaging + ">>", 0, upperLeft, outputStream);
	}
}
