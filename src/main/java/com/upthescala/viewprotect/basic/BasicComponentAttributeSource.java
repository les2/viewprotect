package com.upthescala.viewprotect.basic;

import java.util.HashMap;
import java.util.Map;

import com.upthescala.viewprotect.ComponentAttribute;
import com.upthescala.viewprotect.ComponentAttributeSource;

/**
 * A simple ComponentAttributeSource backed by a {@link Map}.
 * 
 * @author Lloyd Smith II
 */
public class BasicComponentAttributeSource implements ComponentAttributeSource {

	private Map<String, ? extends ComponentAttribute> componentAttributeMap = new HashMap<String, ComponentAttribute>();

	public ComponentAttribute getAttribute(final String componentId) {
		return componentAttributeMap.get(componentId);
	}

	/**
	 * @param componentAttributeMap
	 *            the Map from component id to ComponentAttribute that will back
	 *            this ComponentAttributeSource
	 * @throws IllegalArgumentException
	 *             if the argument is {@code null}
	 */
	public void setComponentAttributeMap(
			final Map<String, ? extends ComponentAttribute> componentAttributeMap) {
		if (componentAttributeMap == null)
			throw new IllegalArgumentException("componentAttributeMap is null");
		this.componentAttributeMap = componentAttributeMap;
	}

	@Override
	public String toString() {
		return "BasicComponentAttributeSource [componentAttributeMap="
				+ componentAttributeMap + "]";
	}

}
