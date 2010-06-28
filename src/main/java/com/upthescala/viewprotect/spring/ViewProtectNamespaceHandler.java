package com.upthescala.viewprotect.spring;

import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

public class ViewProtectNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("basic",
				new ViewProtectBasicBeanDefinitionParser());
	}

}

class ViewProtectBasicBeanDefinitionParser
		extends
			AbstractSimpleBeanDefinitionParser {

	@Override
	protected Class<?> getBeanClass(final Element element) {
		return BasicViewAuthorizationServiceFactoryBean.class;
	}

}
