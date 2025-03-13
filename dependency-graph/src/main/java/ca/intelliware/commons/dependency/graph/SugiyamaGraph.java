package ca.intelliware.commons.dependency.graph;

import java.util.List;

interface SugiyamaGraph {
	public List<Vertex> getAllVertices();
	public List<? extends OrderedLayer<Vertex>> getLayers();
}
