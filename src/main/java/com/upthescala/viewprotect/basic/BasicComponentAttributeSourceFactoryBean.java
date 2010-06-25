package com.upthescala.viewprotect.basic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;

import com.upthescala.viewprotect.ComponentAttributeSource;

/**
 * A {@link FactoryBean} for creating a {@link BasicComponentAttributeSource},
 * possibly for use with a {@link BasicViewAuthorizationService}. Either the
 * {@code popertiesAttributeMappings} or {@code attributeMappings} property must
 * be provided.
 * 
 * @author Lloyd Smith II
 */
public class BasicComponentAttributeSourceFactoryBean implements FactoryBean {

	private Properties propertiesAttributeMappings;

	private Map<String, String> attributeMappings;

	public ComponentAttributeSource getObject() {

		Map<String, BasicComponentAttributes> attributeMap;
		if (attributeMappings != null) {
			attributeMap = parseAttributesMapFromMap(attributeMappings);
		} else if (propertiesAttributeMappings != null)
			attributeMap = parseAttributesMapFromMap(propertiesAttributeMappings);
		else
			throw new IllegalArgumentException(
					"attributeMappings or propertiesAttributeMappings must be set!");

		BasicComponentAttributeSource attributeSource = new BasicComponentAttributeSource();
		attributeSource.setComponentAttributeMap(attributeMap);
		return attributeSource;
	}

	private Map<String, BasicComponentAttributes> parseAttributesMapFromMap(
			final Map<?, ?> map) {

		Map<String, BasicComponentAttributes> attributeMap = new HashMap<String, BasicComponentAttributes>();

		for (Map.Entry<?, ?> mapping : map.entrySet()) {

			String componentAttribute = (String) mapping.getKey();

			int indexOfDot = componentAttribute.lastIndexOf(".");
			if (indexOfDot < 0)
				throw new IllegalArgumentException("Invalid key: "
						+ componentAttribute);

			String attributeType = componentAttribute.substring(indexOfDot + 1,
					componentAttribute.length());

			String componentId = componentAttribute.substring(0, indexOfDot);

			BasicComponentAttributes attributes = getAttribute(attributeMap,
					componentId);

			Set<String> roles = toRoles((String) mapping.getValue());

			if ("ifNotGranted".equals(attributeType))
				attributes
						.setNotGrantedAttribute(new NotGrantedAttribute(roles));
			else if ("ifAllGranted".equals(attributeType))
				attributes
						.setAllGrantedAttribute(new AllGrantedAttribute(roles));
			else if ("ifAnyGranted".equals(attributeType))
				attributes
						.setAnyGrantedAttribute(new AnyGrantedAttribute(roles));
			else
				throw new IllegalArgumentException(
						"Unrecognized attribute type [" + attributeType
								+ "] : accepted values are: "
								+ "ifNotGranted, ifAnyGranted, or ifAllGranted");

			attributeMap.put(componentId, attributes);
		}
		return attributeMap;
	}

	private BasicComponentAttributes getAttribute(
			final Map<String, BasicComponentAttributes> attributeMap,
			final String componentId) {
		BasicComponentAttributes attributes;
		if (attributeMap.containsKey(componentId))
			attributes = attributeMap.get(componentId);
		else
			attributes = new BasicComponentAttributes();
		return attributes;
	}

	private Set<String> toRoles(final String value) {
		String[] rolesArray = value.split("[,\\s]");
		return new HashSet<String>(Arrays.asList(rolesArray));
	}

	public Class<?> getObjectType() {
		return ComponentAttributeSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setPropertiesAttributeMappings(final Properties properties) {
		this.propertiesAttributeMappings = properties;
	}

	public void setAttributeMappings(final Map<String, String> attributeMappings) {
		this.attributeMappings = attributeMappings;
	}

}
