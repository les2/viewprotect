package com.upthescala.viewprotect.basic;

import java.util.HashMap;
import java.util.Map;

import com.upthescala.viewprotect.ComponentAttribute;
import com.upthescala.viewprotect.ComponentAttributeSource;

public class BasicComponentAttributeSource implements ComponentAttributeSource {

	private Map<String, ComponentAttribute> componentAttributeMap = new HashMap<String, ComponentAttribute>();

	public ComponentAttribute getAttribute(final String componentId) {
		return componentAttributeMap.get(componentId);
	}

	public void setComponentAttributeMap(
			final Map<String, ComponentAttribute> componentAttributeMap) {
		if (componentAttributeMap == null)
			throw new IllegalArgumentException("componentAttributeMap is null");
		this.componentAttributeMap = componentAttributeMap;
	}

}
