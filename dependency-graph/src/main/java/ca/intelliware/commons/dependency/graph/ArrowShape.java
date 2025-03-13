package ca.intelliware.commons.dependency.graph;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;

class ArrowShape {
	
	private Plot plot;
	private double arrowHeadSize = 5.0;

	public void drawArrow(Graphics2D graphics, Arrow arrow) {
		graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		if (arrow.isPointingUpward()) {
			graphics.setColor(this.plot.getReverseArrowColor());
		} else {
			graphics.setColor(this.plot.getArrowColor());
		}
		Stroke stroke = this.plot.getArrowStroke();
		if (stroke instanceof BasicStroke) {
			BasicStroke basicStroke = (BasicStroke) stroke;
			stroke = new BasicStroke(basicStroke.getLineWidth() * arrow.getWidth(), basicStroke.getEndCap(), 
					basicStroke.getLineJoin(), basicStroke.getMiterLimit(), adjustDashes(basicStroke.getDashArray(), arrow.getWidth()), basicStroke.getDashPhase());
		}
		graphics.setStroke(stroke);
		graphics.draw(arrow.toShape());
		
		drawArrowHead(graphics, arrow);
	}

	private float[] adjustDashes(float[] dashArray, float width) {
		float[] result = new float[dashArray.length];
		for (int i = 0; i < dashArray.length; i++) {
			result[i] = dashArray[i] * width;
		}
		return result;
	}

	protected void drawArrowHead(Graphics2D graphics, Arrow arrow) {
		graphics = (Graphics2D) graphics.create();
		try {
			BasicStroke stroke = new BasicStroke();
			graphics.setStroke(new BasicStroke(stroke.getLineWidth() * arrow.getWidth()));
			graphics.translate(arrow.getLastPoint().getX(), arrow.getLastPoint().getY());
			
			graphics.rotate(arrow.getAngleOfLastSegment());
			
			GeneralPath path = new GeneralPath();
			path.moveTo((float) this.arrowHeadSize, (float) -this.arrowHeadSize);
			path.lineTo(0, 0);
			path.lineTo((float) this.arrowHeadSize, (float) this.arrowHeadSize);
			graphics.draw(path);
		} finally {
			graphics.dispose();
		}
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public void drawArrowSvg(OutputStream output, Arrow arrow) throws IOException {
		/*
		 * <marker xmlns="http://www.w3.org/2000/svg" style="overflow:visible" id="ArrowWide" refX="0" refY="0" orient="auto-start-reverse" inkscape:stockid="Wide arrow" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" markerWidth="1" markerHeight="1" viewBox="0 0 1 1" inkscape:isstock="true" inkscape:collect="always" preserveAspectRatio="xMidYMid"><path style="fill:none;stroke:context-stroke;stroke-width:1;stroke-linecap:butt" d="M 3,-3 0,0 3,3" transform="rotate(180,0.125,0)" sodipodi:nodetypes="ccc" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" id="path3"/></marker>
		 */
		
		
		String strokeColour = HtmlColor.asHtml(this.plot.getArrowColor());
		if (arrow.isPointingUpward()) {
			strokeColour = HtmlColor.asHtml(this.plot.getReverseArrowColor());
		}

		String instruction = "M ";
		String path = "";
		for (Point2D p : arrow.getPoints()) {
			path += instruction + p.getX() + " " + p.getY() + " ";
			instruction = "L ";
		}
		output.write(("<marker style=\"overflow:visible\" id=\"ArrowWide-" + path.hashCode() 
			+ "\" refX=\"0\" refY=\"0\" orient=\"auto-start-reverse\" markerWidth=\"1\" markerHeight=\"1\" viewBox=\"0 0 1 1\" "
			+ "preserveAspectRatio=\"xMidYMid\">").getBytes("UTF-8"));
		output.write(("<path style=\"fill:none;stroke:context-stroke;stroke-width:" + arrow.getWidth() 
			+ ";stroke-linecap:butt\" d=\"M 5,-5 0,0 5,5\" transform=\"rotate(180,0.125,0)\" />").getBytes("UTF-8"));
		output.write(("</marker>").getBytes("UTF-8"));
		output.write(("<path d=\"" + path +  "\" stroke-width=\"" + arrow.getWidth() + "\" stroke=\"" 
				+ strokeColour + "\" fill=\"none\" stroke-dasharray=\"8 5\" marker-end=\"url(#ArrowWide-" + path.hashCode() + "\" />").getBytes());
	}
}
