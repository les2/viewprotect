package com.upthescala.viewprotect.spring;

import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

import com.upthescala.viewprotect.basic.BasicComponentAttributeSourceFactoryBean;
import com.upthescala.viewprotect.basic.BasicViewAuthorizationService;

public class BasicViewAuthorizationServiceFactoryBean implements FactoryBean {
	private boolean allowAccessByDefault;

	private Resource attributeSourceLocation;

	public boolean isAllowAccessByDefault() {
		return allowAccessByDefault;
	}

	public void setAllowAccessByDefault(final boolean allowAccessByDefault) {
		this.allowAccessByDefault = allowAccessByDefault;
	}

	public BasicViewAuthorizationService getObject() throws Exception {
		BasicViewAuthorizationService vas = new BasicViewAuthorizationService();
		vas.setAllowAccessIfComponentIsNotConfigured(allowAccessByDefault);

		PropertiesFactoryBean properties = new PropertiesFactoryBean();
		properties.setLocation(attributeSourceLocation);
		properties.afterPropertiesSet();

		BasicComponentAttributeSourceFactoryBean source = new BasicComponentAttributeSourceFactoryBean();
		source.setPropertiesAttributeMappings((Properties) properties
				.getObject());

		vas.setComponentAttributeSource(source.getObject());

		return vas;
	}
	public Class<?> getObjectType() {
		return BasicViewAuthorizationService.class;
	}

	public boolean isSingleton() {
		return false;
	}

	public Resource getAttributeSourceLocation() {
		return attributeSourceLocation;
	}

	public void setAttributeSourceLocation(
			final Resource attributeSourceLocation) {
		this.attributeSourceLocation = attributeSourceLocation;
	}
}
