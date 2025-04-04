package com.electricmind.dependency.graph.shape;

import java.io.IOException;
import java.io.OutputStream;

public class ArtifactStereotypeShapeProvider {
	
	static String ARTIFACT_SHAPE = "<path  "
			+ "style=\"fill:none;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1\" "
			+ "d=\"M 10.575434,0.49998599 V 5.7056524 h 5.373582 M 0.49998599,0.49998599 V 19.979214 H 15.949016 V 5.7056524 L 10.575434,0.49998599 Z\"/>";
	static String XML_ARTIFACT_SHAPE = ARTIFACT_SHAPE + "<path "
			+ "style=\"fill:none;stroke:#000000;stroke-width:0.7;stroke-linecap:round;stroke-linejoin:miter;stroke-dasharray:none;stroke-opacity:1\" "
			+ "d=\"m 9.2191888,7.2701619 -2.066052,5.8780631 m 3.2009262,-4.8014504 2.095151,2.0925764 -2.095151,2.097723 M 5.9891638,8.3467746 3.9887395,10.412875 5.9891638,12.537074 v 0\"/>";
	static String JAR_ARTIFACT_SHAPE = "<g>"
			+ "<path style=\"fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:0;stroke-dasharray:none\" d=\"m 11.945981,2.6898174 -0.01687,0.8509402 c 1.444873,0 2.608076,1.1632017 2.608076,2.6080755 V 17.374897 c 0,1.444874 -1.163203,2.608075 -2.608076,2.608075 H 3.8402971 c -1.4448739,0 -2.6080756,-1.163201 -2.6080756,-2.608075 V 6.1488331 c 0,-1.4448738 1.1632017,-2.6080755 2.6080756,-2.6080755 L 3.8398948,2.6711828\"/>"
			+ "<rect style=\"fill:#000000;fill-opacity:1;fill-rule:evenodd;stroke:#000000;stroke-width:0.708036;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:0;stroke-dasharray:none\" width=\"10.439284\" height=\"1.5017917\" x=\"2.6650615\" y=\"0.35401788\" ry=\"0.18314534\"/>"
			+ "</g>";
	

	public void writeShape(String stereotype, OutputStream outputStream) throws IOException {
		if ("pom".equals(stereotype)) {
			outputStream.write(XML_ARTIFACT_SHAPE.getBytes("UTF-8"));
			outputStream.write(ARTIFACT_SHAPE.getBytes("UTF-8"));
		} else if ("jar".equals(stereotype)) {
			outputStream.write(JAR_ARTIFACT_SHAPE.getBytes("UTF-8"));
		} else {
			outputStream.write(ARTIFACT_SHAPE.getBytes("UTF-8"));
		}
	}
}
