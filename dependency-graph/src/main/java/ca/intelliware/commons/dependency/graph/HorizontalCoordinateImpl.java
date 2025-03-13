package ca.intelliware.commons.dependency.graph;

import java.util.Arrays;

class HorizontalCoordinateImpl implements HorizontalCoordinate {
	private Float[] positions = new Float[Bias.values().length];
	
	void setPosition(double value, Bias bias) {
		this.positions[bias.ordinal()] = (float) value;
	}

	public double getPosition() {
		for (Float f : this.positions) {
			if (f == null) {
				throw new IllegalStateException(
						"Horizontal coordinate assignment has not been completed.");
			}
		}
		Float[] p = new Float[this.positions.length];
		System.arraycopy(this.positions, 0, p, 0, p.length);
		Arrays.sort(p);
		float max = p.length - 1;
		int middleLow = (int) Math.floor(max / 2.0f);
		int middleHigh = (int) Math.ceil(max / 2.0f);
		return (p[middleLow] + p[middleHigh]) / 2.0;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.positions[Bias.LEFTWARD_UPPER.ordinal()])
				.append("     ")
				.append(this.positions[Bias.RIGHTWARD_UPPER.ordinal()])
				.append("\n");
		builder.append("\n");
		builder.append(this.positions[Bias.LEFTWARD_LOWER.ordinal()])
				.append("     ")
				.append(this.positions[Bias.RIGHTWARD_LOWER.ordinal()])
				.append("\n");
		return builder.toString();
	}
}