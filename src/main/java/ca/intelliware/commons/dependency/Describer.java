package ca.intelliware.commons.dependency;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public class Describer {

	public String getDescription(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof String) {
			return (String) o;
		} else if (hasNameProperty(o)) {
			return getName(o);
		} else {
			return o.toString();
		}
	}

	private String getName(Object o) {
		try {
			return (String) PropertyUtils.getSimpleProperty(o, "name");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean hasNameProperty(Object o) {
		try {
			PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(o, "name");
			return descriptor != null && String.class.equals(descriptor.getPropertyType());
		} catch (IllegalAccessException e) {
			return false;
		} catch (InvocationTargetException e) {
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

}
