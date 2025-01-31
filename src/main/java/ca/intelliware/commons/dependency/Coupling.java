package ca.intelliware.commons.dependency;

public class Coupling<T> {
	private T t;
	private int weight;

	Coupling(T t) {
		this(t, 1);
	}

	Coupling(T t, int weight) {
		this.t = t;
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	void setWeight(int weight) {
		this.weight = weight;
	}

	public T getT() {
		return t;
	}
}