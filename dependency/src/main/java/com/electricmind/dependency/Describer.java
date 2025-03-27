package com.electricmind.dependency;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Describer {

	public String getDescription(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof String) {
			return (String) o;
		} else if (hasNameProperty(o)) {
			return getPropertyValue(o, "name");
		} else {
			return o.toString();
		}
	}

	public String getPropertyValue(Object o, String propertyName) {
		try {
			BeanInfo bean = Introspector.getBeanInfo(o.getClass());
			PropertyDescriptor property = Arrays.asList(bean.getPropertyDescriptors())
					.stream()
					.filter(p -> propertyName.equals(p.getName()))
					.findFirst().get();
			return (String) property.getReadMethod().invoke(o);
		} catch (IllegalAccessException|InvocationTargetException|IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean hasNameProperty(Object o) {
		return hasProperty(o, "name");
	}
	
	private boolean hasProperty(Object o, String propertyName) {
		try {
			BeanInfo bean = Introspector.getBeanInfo(o.getClass());
			return Arrays.asList(bean.getPropertyDescriptors())
					.stream()
					.filter(p -> propertyName.equals(p.getName()))
					.count() > 0;
		} catch (IntrospectionException e) {
			return false;
		}
	}
}
