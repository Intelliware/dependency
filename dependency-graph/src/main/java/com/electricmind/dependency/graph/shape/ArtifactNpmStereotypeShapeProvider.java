package com.electricmind.dependency.graph.shape;

import java.io.IOException;
import java.io.OutputStream;

public class ArtifactNpmStereotypeShapeProvider extends ArtifactStereotypeShapeProvider {

	private static String NPM_ARTIFACT_SHAPE = "<path "
			+ "style=\"fill:none;stroke:#000000;stroke-width:0.75;stroke-linecap:butt;stroke-linejoin:miter;stroke-dasharray:none;stroke-opacity:1\" "
			+ "d=\"M 4.0940127,7.4103913 H 12.373525 V 15.683 h -2.084446 l 0,-6.1912947 H 8.224501 V 15.683 H 4.0940127 Z\"/>";

	public void writeShape(String stereotype, OutputStream outputStream) throws IOException {
		outputStream.write(ARTIFACT_SHAPE.getBytes("UTF-8"));
		outputStream.write(NPM_ARTIFACT_SHAPE.getBytes("UTF-8"));
	}
}
