package ca.intelliware.commons.dependency;

import java.util.Set;

public interface Node<T> {

	public T getItem();
	public String getName();
	/**
	 * <p>Get a collection of the current item's dependencies.
	 *
	 * <p>For example, we say that one class depends upon another in cases of
	 * inheritance, interface implementation, parameter types, variable types, or
	 * thrown and caught exceptions. In short, all types referred to anywhere
	 * within the source of the measured class.
	 */
	public Set<Coupling<T>> getEfferentCouplings();

	/**
	 * <p>The number of other items that depend upon the current item.
	 */
	public Set<Coupling<T>> getAfferentCouplings();

	public int getLayer();
}
