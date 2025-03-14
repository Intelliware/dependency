package com.electricmind.dependency.graph;

import static com.electricmind.dependency.graph.CoordinateSystem.HEIGHT_SCALE_FACTOR;
import static com.electricmind.dependency.graph.CoordinateSystem.WIDTH_SCALE_FACTOR;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.electricmind.dependency.Coupling;
import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.Layer;
import com.electricmind.dependency.Node;
import com.electricmind.dependency.graph.Graph.BasicVertex;

@SuppressWarnings("unchecked")
public class Grapher<T> {

	public interface Drawer {
		public void drawGraph(Graphics2D g2);
	}

	private final DependencyManager<T> dependencyManager;
	private Plot plot = new Plot();
	private NodeShape<T> shape = new NodeShape<T>();
	private ArrowShape arrowShape = new ArrowShape();
	private Map<Object, Rectangle2D> locations = Collections.synchronizedMap(new HashMap<Object, Rectangle2D>());
	private Graph graph;
	private CoordinateSystem coordinateSystem;
	private int maxWeight = 0;

	public Grapher(DependencyManager<T> dependencyManager) {
		this.dependencyManager = dependencyManager;
	}

	protected synchronized void draw(Graphics2D graphics, Rectangle2D rectangle) {
		double scale = calculateScale(rectangle);
		draw(graphics, rectangle, scale);
	}

	private synchronized void initialize() {
		this.shape.setPlot(this.plot);
		this.arrowShape.setPlot(this.plot);
		this.graph = new SugiyamaAlgorithm().apply(this.dependencyManager);
		this.maxWeight = 0;
		for (Layer<Node<T>> layer : this.dependencyManager.getLayeredGraph().getLayers()) {
			for (Node<T> node : layer.getContents()) {
				for (Coupling<T> efferent : node.getEfferentCouplings()) {
					this.maxWeight = Math.max(this.maxWeight, efferent.getWeight());
				}
			}
		}
	}
	
	public void setStripeColour(Color color) {
		this.plot.setLayerAlternatingColor(color);
	}
	
	public Color getStripeColour() {
		return this.plot.getLayerAlternatingColor();
	}
	
	private synchronized void draw(Graphics2D graphics, Rectangle2D rectangle, double scale) {
		rectangle = scale(scale, rectangle);
		graphics.scale(1.0/scale, 1.0/scale);
		
		this.shape.initialize(graphics, this.dependencyManager.getLayeredGraph().getNodes());
		
		drawLayerBars(graphics, rectangle);

		this.coordinateSystem = new CoordinateSystem(this.shape, this.graph.getHeight());
		double excessWidth = rectangle.getWidth() - this.coordinateSystem.getWidth(this.graph.getWidth());
		double excessHeight = rectangle.getHeight() - this.coordinateSystem.getTotalHeight();
		graphics.translate(excessWidth / 2.0, excessHeight);
		rectangle = new Rectangle2D.Double(rectangle.getX() + getLeftBorder(),
				rectangle.getY(),
				rectangle.getWidth() - (2 * getLeftBorder()), rectangle.getHeight());

		
		drawBoxes(graphics);
		drawArrows(graphics);
	}
	
	private synchronized void drawSvg(Dimension dimension, OutputStream output) throws IOException {
		BufferedImage image = new BufferedImage(
				(int) Math.ceil(this.shape.getDimension().getWidth()), 
				(int) Math.ceil(this.shape.getDimension().getHeight()), 
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		this.shape.initialize(g2, this.dependencyManager.getLayeredGraph().getNodes());
		g2.dispose();
		
		drawLayerBarsSvg(dimension, output);
		
		this.coordinateSystem = new CoordinateSystem(this.shape, this.graph.getHeight());
		double excessWidth = dimension.getWidth() - this.coordinateSystem.getWidth(this.graph.getWidth());
		double excessHeight = dimension.getHeight() - this.coordinateSystem.getTotalHeight();
		
		output.write(("<g transform=\"translate(" + (excessWidth / 2.0) + "," + excessHeight + ")\" >").getBytes("UTF-8"));
		
		drawBoxesSvg(output);
		drawArrowsSvg(output);
		
		output.write("</g>".getBytes("UTF-8"));
	}

	private Rectangle2D scale(double scale, Rectangle2D rectangle) {
		return new Rectangle2D.Double(rectangle.getX(), rectangle.getY(),
				rectangle.getWidth() * scale, rectangle.getHeight() * scale);
	}

	private double calculateScale(Rectangle2D rectangle) {
		Dimension dimension = getPreferredDimension();
		if (dimension.getHeight() / rectangle.getHeight() > dimension.getWidth() / rectangle.getWidth()) {
			return dimension.getHeight() / rectangle.getHeight();
		} else {
			return dimension.getWidth() / rectangle.getWidth();
		}
	}

	private Dimension getPreferredDimension() {
		List<Layer<Node<T>>> layers = this.dependencyManager.getNodeLayers();
		double height = layers.size() * getLayerBandHeight();
		double shapeWidth = this.shape.getWidth();
		double width = this.graph.getWidth() * shapeWidth * WIDTH_SCALE_FACTOR + getLeftBorder();
		
		Dimension dimension = new Dimension();
		dimension.setSize(width, height);
		return dimension;
	}

	private double getLeftBorder() {
		return 5.0;
	}

	private void drawArrows(Graphics2D graphics) {
		for (GraphLayer layer : this.graph.getLayers()) {
			drawArrows(graphics, layer);
		}
	}

	private void drawArrowsSvg(OutputStream output) throws IOException {
		for (GraphLayer layer : this.graph.getLayers()) {

			if (layer.getLevelNumber() > 0) {
				for (Vertex vertex : layer.getOrderedContents()) {
					List<Vertex> dependencies = vertex.getNeighboursInLayer(layer.getLevelNumber()-1);
					for (Vertex dependency : dependencies) {
						drawArrowSvg(output, vertex, dependency);
					}
				}
			}
		}
	}


	private void drawArrows(Graphics2D graphics, GraphLayer layer) {
		if (layer.getLevelNumber() > 0) {
			for (Vertex vertex : layer.getOrderedContents()) {
				List<Vertex> dependencies = vertex.getNeighboursInLayer(layer.getLevelNumber()-1);
				for (Vertex dependency : dependencies) {
					drawArrow(graphics, vertex, dependency);
				}
			}
		}
	}

	private void drawArrowSvg(OutputStream output, Vertex vertex, Vertex dependency) throws IOException {
		if (!dependency.isDummy() && !vertex.isDummy()) {
			Node<T> node = (Node<T>) ((Graph.BasicVertex) vertex).getNode();
			Object object = ((Graph.BasicVertex) dependency).getNode().getItem();
			float width = determineStrokeWidth(node, object);
			
			Arrow line = BoundsUtil.getEndPoints(getBounds(node.getItem()), getBounds(object));
			line.setWidth(width);
			this.arrowShape.drawArrowSvg(output, line);
			if (((BasicVertex) vertex).isBidirectionalWith((BasicVertex) dependency)) {
				Point2D to = getBoundsCenter(node.getItem(), 0.05 * this.shape.getWidth());
				Point2D from = getBoundsCenter(object, 0.05 * this.shape.getWidth());
				
				Arrow arrow = new Arrow(Arrays.asList(from, to));
				arrow = arrow.clipEnd(getBounds(node.getItem()));
				arrow = arrow.clipStart(getBounds(object));
				arrow.setWidth(width);
				this.arrowShape.drawArrowSvg(output, arrow);
			}
		} else if (!vertex.isDummy() && dependency.isDummy()) {
			this.arrowShape.drawArrowSvg(output, getArrow(dependency));
		}
	}

	private float determineStrokeWidth(Node<T> node, Object object) {
		float width = 1.0f;
		if (this.plot.isUseWeights()) {
			int weight = determineWeight(node, object);
			
			if (this.maxWeight > 1) {
				width = (float) (1 + 4 * (Math.sqrt(weight) / Math.sqrt(this.maxWeight)));
			}
		}
		return width;
	}
	
	private void drawArrow(Graphics2D graphics, Vertex vertex, Vertex dependency) {
		if (!dependency.isDummy() && !vertex.isDummy()) {
			Node<T> node = (Node<T>) ((Graph.BasicVertex) vertex).getNode();
			Object object = ((Graph.BasicVertex) dependency).getNode().getItem();
			float width = determineStrokeWidth(node, object);
			
			Arrow line = BoundsUtil.getEndPoints(getBounds(node.getItem()), getBounds(object));
			line.setWidth(width);
			this.arrowShape.drawArrow(graphics, line);
			if (((BasicVertex) vertex).isBidirectionalWith((BasicVertex) dependency)) {
				Point2D to = getBoundsCenter(node.getItem(), 0.05 * this.shape.getWidth());
				Point2D from = getBoundsCenter(object, 0.05 * this.shape.getWidth());
				
				Arrow arrow = new Arrow(Arrays.asList(from, to));
				arrow = arrow.clipEnd(getBounds(node.getItem()));
				arrow = arrow.clipStart(getBounds(object));
				System.out.println(arrow);
				this.arrowShape.drawArrow(graphics, arrow);
			}
		} else if (!vertex.isDummy() && dependency.isDummy()) {
			this.arrowShape.drawArrow(graphics, getArrow(dependency));
		}
	}

	private int determineWeight(Node<T> node, Object object) {
		Set<Coupling<T>> dependencies = this.dependencyManager.getDirectDependencies(node.getItem());
		List<Coupling<T>> list = dependencies.stream().filter(c -> c.getItem().equals(object)).collect(Collectors.toList());
		return list.isEmpty() ? 1 : list.get(0).getWeight();
	}

	private Point2D getBoundsCenter(Object item, double offset) {
		Rectangle2D bounds = getBounds(item);
		return new Point2D.Double(bounds.getCenterX() + offset, bounds.getCenterY());
	}

	private Arrow getArrow(Vertex dependency) {
		DummyVertex lastDummy = getLastDummy((DummyVertex) dependency);
		Vertex end = lastDummy.getLower();
		
		DummyVertex firstDummy = getFirstDummy((DummyVertex) dependency);
		Vertex start = firstDummy.getUpper();
		
		Node<T> node = (Node<T>) ((Graph.BasicVertex) start).getNode();
		Object object = ((Graph.BasicVertex) end).getNode().getItem();
		float width = determineStrokeWidth(node, object);
		
		boolean reversed = end.getLayer() > start.getLayer();
		
		Vertex previous = reversed ? end : start;
		Point2D.Double point1 = new Point2D.Double(this.coordinateSystem.getCenterX(dependency),
				this.coordinateSystem.getTopY(reversed ? lastDummy : firstDummy ));
		Arrow arrow = BoundsUtil.getEndPoints(getBounds(previous), point1);
		
		for (Vertex v = reversed ? lastDummy : firstDummy; v.isDummy(); 
				v = reversed ? ((DummyVertex) v).getLower() : ((DummyVertex) v).getUpper()) {
			
			if (previous.isDummy()) {
				Point2D top = new Point2D.Double(this.coordinateSystem.getCenterX(previous),
						this.coordinateSystem.getBottomY(previous));
				Point2D bottom = new Point2D.Double(this.coordinateSystem.getCenterX(v),
						this.coordinateSystem.getTopY(v));
				arrow = Arrow.join(arrow, new Arrow(Arrays.asList(top, bottom)));
			}
			previous = v;
		}

		DummyVertex penultimate = reversed ? firstDummy : lastDummy;
		Point2D.Double point2 = new Point2D.Double(this.coordinateSystem.getCenterX(penultimate),
				this.coordinateSystem.getBottomY(penultimate.getLayer()));
		arrow = Arrow.join(arrow, BoundsUtil.getEndPoints(point2, getBounds(reversed ? start : end)));
		arrow.setWidth(width);

		return reversed ? Arrow.reverse(arrow) : arrow;
	}

	private DummyVertex getFirstDummy(DummyVertex vertex) {
		if (vertex.getUpper().isDummy()) {
			return getFirstDummy((DummyVertex) vertex.getUpper());
		} else {
			return vertex;
		}
	}

	private Rectangle2D getBounds(Vertex vertex) {
		return getBounds(((Graph.BasicVertex) vertex).getNode().getItem());
	}

	private DummyVertex getLastDummy(DummyVertex vertex) {
		if (vertex.getLower().isDummy()) {
			return getLastDummy((DummyVertex) vertex.getLower());
		} else {
			return vertex;
		}
	}

	private Rectangle2D getBounds(Object item) {
		return this.locations.get(item);
	}

	private void drawBoxes(Graphics2D graphics) {
		List<GraphLayer> layers = this.graph.getLayers();
		for (int i = 0, length = layers.size() ; i < length; i++) {
			drawLayer(graphics, i, layers.get(i));
		}
	}

	private void drawBoxesSvg(OutputStream output) throws IOException {
		List<GraphLayer> layers = this.graph.getLayers();
		for (int i = 0, length = layers.size() ; i < length; i++) {
			drawLayerSvg(i, layers.get(i), output);
		}
	}
	
	private void drawLayer(Graphics2D graphics, int layerNumber, GraphLayer layer) {
		for (Vertex vertex : layer.getOrderedContents()) {
			this.coordinateSystem.getCenterX(vertex);
			if (!vertex.isDummy()) {
				Node<?> node = ((Graph.BasicVertex) vertex).getNode();
				Graphics2D g = (Graphics2D) graphics.create();
				double x = this.coordinateSystem.getLeftX(vertex);
				double y = this.coordinateSystem.getTopY(vertex);
				g.translate(x, y);
				this.locations.put(node.getItem(),
						new Rectangle2D.Double(x, y,
								this.shape.getWidth(),
								this.shape.getHeight()));
				drawNode(g, node);
			}
		}
	}

	private void drawLayerSvg(int layerNumber, GraphLayer layer, OutputStream output) throws IOException {
		for (Vertex vertex : layer.getOrderedContents()) {
			this.coordinateSystem.getCenterX(vertex);
			if (!vertex.isDummy()) {
				Node<?> node = ((Graph.BasicVertex) vertex).getNode();
				double x = this.coordinateSystem.getLeftX(vertex);
				double y = this.coordinateSystem.getTopY(vertex);
				this.locations.put(node.getItem(),
						new Rectangle2D.Double(x, y,
								this.shape.getWidth(),
								this.shape.getHeight()));
				Point2D.Double upperLeft = new Point2D.Double(x, y);
				drawNodeSvg(node, upperLeft, output);
			}
		}
	}

	private void drawNodeSvg(Node<?> node, Point2D.Double upperLeft, OutputStream output) throws IOException {
		this.shape.drawSvg((Node<T>) node, upperLeft, output);
	}

	private void drawNode(Graphics2D graphics, Node<?> node) {
		this.shape.draw(graphics, (Node<T>) node);
	}

	private void drawLayerBars(Graphics2D graphics, Rectangle2D r) {
		double height = getLayerBandHeight();
		boolean alternate = false;
		for (double i = r.getHeight()-height; i > -height; i -= (height)) {
			graphics.setPaint(alternate ? this.plot.getLayerBackgroundColor() : this.plot.getLayerAlternatingColor());
			graphics.fill(new Rectangle2D.Double(r.getX(), i, r.getWidth(), height));
			alternate = !alternate;
		}
	}
	
	private void drawLayerBarsSvg(Dimension dimension, OutputStream output) throws IOException {
		double height = getLayerBandHeight();
		String fill = HtmlColor.asHtml(getStripeColour());
		for (int i = 1; (i * height) <= dimension.getHeight(); i += 2) {
			output.write(("<rect x=\"0\" y=\"" + i * height + "\" width=\"" + dimension.getWidth() + "\" height=\"" + height + "\" fill=\"" + fill + "\" />").getBytes("UTF-8"));
		}
	}

	private double getLayerBandHeight() {
		return this.shape.getHeight() * HEIGHT_SCALE_FACTOR;
	}

	public synchronized Dimension createPng(OutputStream output, final int width, final int height) throws IOException {
		initialize();
		Drawer drawer = new Drawer() {
			public void drawGraph(Graphics2D g2) {
				draw(g2, new Rectangle2D.Float(0, 0, width, height));
			}
		};
		Dimension d = new Dimension(width, height);
		ImageIO.write(createBufferedImage(d, drawer), "png", output);
		return d;
	}
	public synchronized Dimension createPng(OutputStream output) throws IOException {
		initialize();
		final Dimension dimension = getPreferredDimension();
		Drawer drawer = new Drawer() {
			public void drawGraph(Graphics2D g2) {
				draw(g2, new Rectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight()), 1.0);
			}
		};
		ImageIO.write(createBufferedImage(dimension, drawer), "png", output);
		return dimension;
	}

	public synchronized Dimension createSvg(OutputStream output) throws IOException {
		initialize();
		final Dimension dimension = getPreferredDimension();
		
		output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>".getBytes("UTF-8"));
		output.write(("<svg width=\"" + dimension.getWidth() + "\" height=\"" + dimension.getHeight() +  "\" viewBox=\"0 0 " 
				+ dimension.getWidth() + " " + dimension.getHeight() 
				+ "\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">").getBytes("UTF-8"));
		output.write("<g>".getBytes("UTF-8"));
		drawSvg(dimension, output);
		output.write("</g>".getBytes("UTF-8"));
		output.write("</svg>".getBytes("UTF-8"));
		
		return dimension;
	}

	private BufferedImage createBufferedImage(final Dimension dimension, Drawer drawer) {
		BufferedImage image = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		drawer.drawGraph(g2);
		g2.dispose();
		return image;
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public NodeShape<T> getShape() {
		return this.shape;
	}

	public void setShape(NodeShape<T> shape) {
		this.shape = shape;
	}
}
