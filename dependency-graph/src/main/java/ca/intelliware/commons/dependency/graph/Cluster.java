package ca.intelliware.commons.dependency.graph;

class Cluster implements Comparable<Cluster> {
	
	private final Block sink;
	private Double shift = null;

	Cluster(Block sink) {
		this.sink = sink;
	}

	Double getShift() {
		return this.shift;
	}

	void setShift(double shift) {
		this.shift = shift;
	}
	
	int getWeight() {
		return this.sink.getRoot();
	}

	public int compareTo(Cluster o) {
		return o.getWeight() - getWeight();
	}
	boolean isAssigned() {
		return this.shift != null;
	}

	public double getExcessShift() {
		return this.shift - this.sink.getWidth() / 2.0;
	}

	public void offset(double offset) {
		this.shift += offset;
	}
}