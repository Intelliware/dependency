package ca.intelliware.commons.dependency.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class BlockManager {
	private List<Block> blocks = new ArrayList<Block>();
	private Map<Integer,Map<Integer,Block>> blockMap = new TreeMap<Integer,Map<Integer,Block>>();
	private int layerCount;
	
	public Block getBlock(Vertex vertex) {
		Map<Integer,Block> map = getMap(vertex.getLayer());
		if (!map.containsKey(vertex.getOrdinal())) {
			Block block = new Block(this, this.blocks.size());
			block.add(vertex);
			this.blocks.add(block);
			map.put(vertex.getOrdinal(), block);
			return block;
		} else {
			return map.get(vertex.getOrdinal());
		}
	}
	Map<Integer, Block> getMap(int layer) {
		if (!this.blockMap.containsKey(layer)) {
			this.blockMap.put(layer, new HashMap<Integer, Block>());
		}
		this.layerCount = Math.max(layer+1, this.layerCount);
		return this.blockMap.get(layer);
	}

	@Override
	public String toString() {
		return this.blocks.toString();
	}
	int getLayerCount() {
		return this.layerCount;
	}
	
	Iterable<Block> getBlocks() {
		return this.blocks;
	}
}