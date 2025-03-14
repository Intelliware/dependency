package com.electricmind.dependency.graph.shape;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;

import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.NodeShape;

public class ArtifactShape<T> extends NodeShape<T> {

	private static String ARTIFACT_SHAPE = "<path  "
			+ "style=\"fill:none;stroke:#000000;stroke-width:0.999972px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" "
			+ "d=\"M 10.575434,0.49998599 V 5.7056524 h 5.373582 M 0.49998599,0.49998599 V 19.979214 H 15.949016 V 5.7056524 L 10.575434,0.49998599 Z\"/>";
	
	public ArtifactShape() {
		super(new Dimension(160, 75));
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
		
	}
}
