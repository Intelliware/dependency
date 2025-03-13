package ca.intelliware.commons.dependency.graph.shape;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;


public enum CommonImage implements ImageProvider {

	XML_DOCUMENT("xml.png"), CLASS_ICON("classIcon.png"), PACKAGE_ICON("package.png"),
	PACKAGE_BIG_ICON("packageBig.png");

	private final String resourceName;
	private ImageIcon image;
	private BufferedImage bufferedImage;

	private CommonImage(String resourceName) {
		this.resourceName = resourceName;
	}

	public ImageIcon getImage() {
		if (this.image == null) {
			this.image = new ImageIcon(getClass().getResource(this.resourceName));
		}
		return this.image;
	}

	BufferedImage getBufferedImage() throws IOException {
		if (this.bufferedImage == null) {
			this.bufferedImage = ImageIO.read(getClass().getResource(this.resourceName));
		}
		return this.bufferedImage;
	}
	
	String getBase64EncodedImage() throws IOException {
		try (InputStream input = getClass().getResourceAsStream(this.resourceName)) {
			return Base64.getEncoder().encodeToString(IOUtils.toByteArray(input));
		}
	}
}
