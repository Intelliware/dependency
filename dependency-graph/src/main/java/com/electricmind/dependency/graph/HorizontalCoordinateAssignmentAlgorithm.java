package com.electricmind.dependency.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.electricmind.dependency.Coupling;
import com.electricmind.dependency.DependencyManager;
import com.electricmind.dependency.LayeredGraph;
import com.electricmind.dependency.Node;

/**
 * <p>An algorithm that implements horizontal coordinate assignment for a typical 
 * Sugiyama graphing method.  The fourth (and final) stage of the Sugiyama method is to 
 * assign horizontal coordinates to all vertices in the graph.  Before this algorithm
 * begins, we assume that each vertex has been assigned a layer in the graph (which 
 * becomes the vertical coordinate), and that all vertices in a layer are ordered
 * from 0 to <i>n</i> (where <i>n</i> is the number of vertices in the layer).
 * 
 * <p>This implementation of the horizontal coordinate assignment algorithm is based on
 * the algorithm published in the paper "Fast and Simple Horizontal Coordinate Assignment"
 * by Ulrik Brandes and Boris K&ouml;pf, in the <cite>Proceedings of the 9th 
 * International Symposium on Graph Drawing</cite>, 2001.
 * 
 * <p>In essence, this algorithm has three parts.  The first two parts are performed 
 * four times each, and the third part uses the output from each of the four iterations
 * to find a "balanced" answer.
 * 
 * <ul>
 * <li>Step one: Vertical alignment
 * <ol>
 * <li type="a">First determine which edges (lines) between vertices cross.  We categorize 
 * these crosses (conflicts) as Type 0, Type 1, or Type 2 conflicts.  We choose to 
 * selectively ignore enough of these crossing lines so that we have a set of edges
 * that don't cross.  (For the purposes of this exercise, we consider two edges that 
 * share a vertex to "cross" -- although they're always considered to be Type 0 
 * conflicts).
 * <li type="a">Now that we have a set of edges that don't cross, we can consider the remaining
 * data as forming "chains" or "blocks" of vertices.  Some of these blocks will contain
 * a single isolated vertex, and some will contain a single chain of connected vertices.
 * Consider each of these blocks to be aligned vertically.
 * </ol>
 * <li>Step two: Horizontal assignment
 * <ol>
 * <li type="a">Now that we have bunch of vertically aligned blocks, we want to consider
 * a directed graph between blocks.  In essence this is identical to the layering
 * problem.  Further, we put these blocks into a group of clusters.  Within each cluster,
 * we want the blocks as close to each other as possible.  
 * <li type="a">Afterward, we can compact certain segments of the final graph even more
 * by putting the clusters as close together as possible.
 * </ol>
 * </ul>
 * 
 * <p>Each of the four iterations of steps one and two deal with a "bias" -- the 
 * operations are biased by vertical and horizontal alignment.  What that means is that 
 * at the end of the algorithm, each vertex is given 4 coordinates, and the final step
 * computes a "balanced" set of vertices.
 * 
 * @author BC Holmes
 */
class HorizontalCoordinateAssignmentAlgorithm {
	
	enum ConflictType {
		NO_CONFLICT, TYPE_0, TYPE_1, TYPE_2;
	}
	
	public void process(SugiyamaGraph sugiyamaGraph) {
		initialize(sugiyamaGraph);
		for (Bias bias : Bias.values()) {
			align(sugiyamaGraph.getLayers(), bias);
		}
	}

	void initialize(SugiyamaGraph sugiyamaGraph) {
		for (Vertex v : sugiyamaGraph.getAllVertices()) {
			v.setHorizontalCoordinate(new HorizontalCoordinateImpl());
		}
	}

	void align(List<? extends OrderedLayer<Vertex>> layers, Bias bias) {
		BlockManager blocks = alignVertically(layers, bias);
		alignHorizontally(blocks, bias);
	}

	private void alignHorizontally(BlockManager blocks, Bias bias) {
		LayeredGraph<Block> layeredGraph = layerBlocks(blocks, bias);
		SortedSet<Cluster> clusters = assignClusters(layeredGraph);
		assignRelativeHorizontalCoordinates(clusters, layeredGraph);
		performHorizontalCompaction(clusters, layeredGraph, bias);
		assignFinalHorizontalCoordinate(bias, layeredGraph);
	}

	private void performHorizontalCompaction(SortedSet<Cluster> clusters,
			LayeredGraph<Block> layeredGraph, Bias bias) {
		
		boolean firstTime = true;
		for (Cluster cluster : clusters) {
			if (firstTime) {
				cluster.setShift(0.0);
			} else {
				cluster.setShift(calculateShift(layeredGraph, cluster));
			}
			firstTime = false;
		}
		
		double excess = Double.MAX_VALUE;
		for (Cluster cluster : clusters) {
			excess = Math.min(excess, cluster.getShift());
		}
		for (Cluster cluster : clusters) {
			cluster.offset(-excess);
		}
	}

	private double calculateShift(LayeredGraph<Block> layeredGraph, Cluster cluster) {
		double shift = Double.MAX_VALUE;
		for (Node<Block> node : layeredGraph.getNodes()) {
			Block block = node.getItem();
			if (block.getCluster() == cluster) {
				for (Coupling<Block> afferent : node.getAfferentCouplings()) {
					if (afferent.getItem().getCluster() == cluster) {
						// ignore it
					} else if (afferent.getItem().getCluster().isAssigned()) {
						shift = Math.min(shift, 
								afferent.getItem().getHorizontalCoordinate() 
									- block.getRelativeHorizontalCoordinate() - Block.minimumDistance(afferent.getItem(), block));
					} else {
						throw new RuntimeException("Warning!  Expected cluster to be assigned a shift value");
					}
				}
			}
		}
		return shift;
	}

	private void assignRelativeHorizontalCoordinates(SortedSet<Cluster> clusters, LayeredGraph<Block> layeredGraph) {
		for (Cluster cluster : clusters) {
			assignRelativeHorizontalCoordinates(layeredGraph, cluster);
		}
	}

	private void assignRelativeHorizontalCoordinates(LayeredGraph<Block> layeredGraph, Cluster cluster) {
		DependencyManager<Block> dependencyManager = new DependencyManager<Block>();
		
		for (Node<Block> node : layeredGraph.getNodes()) {
			Block block = node.getItem();
			if (block.getCluster() == cluster) {
				dependencyManager.add(block);
				for (Coupling<Block> afferent : node.getAfferentCouplings()) {
					if (afferent.getItem().getCluster() == cluster) {
						dependencyManager.add(afferent.getItem(), block);
					}						
				}
			}
		}
		
		Block previous = null;
		for (Node<Block> node : dependencyManager.getLayeredGraph().getNodes()) {
			Block block = node.getItem();
			block.setRelativeHorizontalCoordinate(block.after(previous));
			previous = node.getItem();
		}
	}

	private SortedSet<Cluster> assignClusters(LayeredGraph<Block> layeredGraph) {
		SortedSet<Cluster> clusters = new TreeSet<Cluster>();
		for (Node<Block> node : layeredGraph.getNodes()) {
			Block block = node.getItem();
			if (isSink(node)) {
				Cluster cluster = new Cluster(block);
				block.assignCluster(cluster);
				clusters.add(cluster);
			} else {
				for (Coupling<Block> efferent : node.getEfferentCouplings()) {
					block.assignCluster(efferent.getItem().getCluster());
				}
			}
		}
		return clusters;
	}

	private boolean isSink(Node<Block> node) {
		return node.getEfferentCouplings().size() == 0;
	}

	private void assignFinalHorizontalCoordinate(Bias bias,
			LayeredGraph<Block> layeredGraph) {
		for (Node<Block> node : layeredGraph.getNodes()) {
			node.getItem().setHorizontalCoordinate(bias);
		}
	}

	private LayeredGraph<Block> layerBlocks(BlockManager blockManager, Bias bias) {
		DependencyManager<Block> stack = new DependencyManager<Block>();
		for (Block block : blockManager.getBlocks()) {
			stack.add(block);
			for (Vertex vertex : block.getVertices()) {
				Map<Integer, Block> blocks = blockManager.getMap(vertex.getLayer());
				Block neighbour = blocks.get(vertex.getOrdinal() + 1);
				if (neighbour != null) {
					stack.add(neighbour, block);
				}
			}
		}
		LayeredGraph<Block> layeredGraph = stack.getLayeredGraph();
		return layeredGraph;
	}

	private BlockManager alignVertically(List<? extends OrderedLayer<Vertex>> layers, Bias bias) {
		Map<Edge,ConflictType> edges = determineConflicts(layers, bias);
		
		BlockManager blocks = new BlockManager();
		for (OrderedLayer<Vertex> layer : bias.iterate(layers)) {
			alignLayerVertically(layer, bias.getNextLayer(layer.getLevel(), layers.size()), blocks, bias, edges);
		}
		return blocks;
	}

	private Map<Edge, ConflictType> determineConflicts(
			List<? extends OrderedLayer<Vertex>> layers, Bias bias) {
		
		Map<Edge, ConflictType> result = new HashMap<Edge, ConflictType>();
		for (OrderedLayer<Vertex> layer : bias.iterate(layers)) {
			result.putAll(determineConflictsInLayer(layer, bias.getNextLayer(layer.getLevel(), layers.size()), bias));
		}		
		return result;
	}

	Map<Edge, ConflictType> determineConflictsInLayer(
			OrderedLayer<Vertex> fromLayer, Integer toLayer, Bias bias) {
		Map<Edge, ConflictType> result = new HashMap<Edge, ConflictType>();

		if (toLayer != null) {
			markAllType1AndType2Conflicts(fromLayer, toLayer, bias, result);
			markAllType0Conflicts(fromLayer, toLayer, bias, result);
		}
		return result;
	}

	/**
	 * <p>Type 0 conflicts are resolved greedily in a leftmost (or rightmost) fashion.
	 * For each vertex in the "from" layer, we find all of its neighbours in the 
	 * "to" layer, from left to right (or right to left, according to the bias).
	 * 
	 * <p>From the list of such candidates, we look at the medians of the list.  The 
	 * leftmost median and the rightmost median.  If our bias is leftward, we first
	 * consider the leftmost median.  If it crosses one of the previously chosen items, 
	 * then we consider the other median (if there is one).
	 */
	private void markAllType0Conflicts(OrderedLayer<Vertex> fromLayer, Integer toLayer,
			Bias bias, Map<Edge, ConflictType> result) {
		for (Vertex from : bias.orderLayer(fromLayer)) {
			List<Edge> candidates = new ArrayList<Edge>();
			for (Vertex to : from.getNeighboursInLayer(toLayer)) {
				Edge edge = new Edge(from, to);
				if (result.get(edge) == null) {
					candidates.add(edge);
				}
			}
			Edge median = chooseMedian(bias, result, candidates);
			for (Edge edge : candidates) {
				if (!edge.equals(median)) {
					result.put(edge, ConflictType.TYPE_0);
				} else {
					result.put(edge, ConflictType.NO_CONFLICT);
				}
			}
		}
	}

	/**
	 * There can be up to two median neighbours.  
	 */
	private Edge chooseMedian(Bias bias, Map<Edge, ConflictType> result,
			List<Edge> candidates) {
		Edge median = bias.chooseMedian(candidates);
		if (median != null && crossesExistingChoice(median, result)) {
			median = bias.chooseAlternateMedian(candidates);
			if (crossesExistingChoice(median, result)) {
				median = null;
			}
		}
		return median;
	}

	private void markAllType1AndType2Conflicts(OrderedLayer<Vertex> fromLayer,
			Integer toLayer, Bias bias, Map<Edge, ConflictType> result) {
		Set<Edge> edges = Edge.getEdges(fromLayer, toLayer);
		for (Edge edge : bias.order(edges)) {
			for (Edge otherEdge : bias.order(edges)) {
				if (edge.crosses(otherEdge)) {
					if (edge.crosses(otherEdge) && edge.isInnerSegment() && otherEdge.isInnerSegment()) {
						result.put(edge, ConflictType.TYPE_2);
						result.put(otherEdge, ConflictType.TYPE_2);
					} else if (edge.isInnerSegment()) {
						result.put(otherEdge, ConflictType.TYPE_1);
					} else if (otherEdge.isInnerSegment()) {
						result.put(edge, ConflictType.TYPE_1);
					}
				}
			}
		}
	}

	private boolean crossesExistingChoice(Edge median, Map<Edge, ConflictType> result) {
		boolean crosses = false;
		for (Edge edge : result.keySet()) {
			ConflictType conflict = result.get(edge);
			if (conflict == null || conflict == ConflictType.NO_CONFLICT) {
				crosses |= median.crosses(edge);
			}
			if (crosses) {
				break;
			}
		}
		return crosses;
	}

	private void alignLayerVertically(OrderedLayer<Vertex> layer, Integer nextLayer, BlockManager blocks, Bias bias, Map<Edge,ConflictType> edges) {
		for (Vertex vertex : layer.getOrderedContents()) {
			Block block = blocks.getBlock(vertex);
			if (nextLayer != null) {
				List<Vertex> neighbours = getOrderedNeighbours(vertex, nextLayer, edges);
				if (!neighbours.isEmpty()) {
					block.add(getMedian(neighbours, bias));
				}
			}
		}
	}

	private Vertex getMedian(List<Vertex> neighbours, Bias bias) {
		double centre = (neighbours.size() - 1) / 2.0;
		int median = bias.getHorizontal() == HorizontalAlignment.LEFT 
				? (int) Math.floor(centre) : (int) Math.ceil(centre);
		return neighbours.get(median);
	}

	private List<Vertex> getOrderedNeighbours(Vertex vertex, Integer previousLayer, Map<Edge,ConflictType> edges) {
		List<Vertex> neighbours = vertex.getNeighboursInLayer(previousLayer);
		List<Vertex> result = new ArrayList<Vertex>();
		for (Vertex neighbour : neighbours) {
			if (hasNoConflicts(vertex, neighbour, edges)) {
				result.add(neighbour);
			}
		}
		Collections.sort(result, new OrdinalComparator());
		return result;
	}

	private boolean hasNoConflicts(Vertex vertex, Vertex neighbour,
			Map<Edge, ConflictType> edges) {
		return edges.get(new Edge(vertex, neighbour)) == ConflictType.NO_CONFLICT;
	}
}
