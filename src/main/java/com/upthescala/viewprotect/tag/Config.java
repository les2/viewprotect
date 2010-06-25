package com.upthescala.viewprotect.tag;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import com.upthescala.viewprotect.ViewAuthorizationService;

/**
 * Internal configuration utility class used by the ViewProtectTag.
 * 
 * @author Lloyd Smith II
 */
class Config {

	private static final Log logger = LogFactory.getLog(Config.class);

	/**
	 * The name of the properties file that will be searched for on the
	 * classpath for ViewProtectTag options.
	 */
	public static final String VIEW_PROTECT_PROPERTIES_FILE = "viewprotect.properties";

	/**
	 * The property key for the bean name of the ViewAuthorizationService.
	 */
	public static final String VIEW_AUTHORIZATION_SERVICE_BEAN_NAME_PROP_KEY = "viewAuthorizationService.beanName";

	/**
	 * The default name used for the Spring bean containing the implementation
	 * of {@link ViewAuthorizationService} that will be used by the
	 * {@link ViewProtectTag}.
	 */
	public static final String DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME = "viewAuthorizationService";

	public static String getViewAuthServiceBeanName(final String propertiesFile) {
		/*
		 * Load the properties file, if present, to determine the name of the
		 * Spring bean that contains the implementation of
		 * ViewAuthorizationService to use:
		 */
		String beanName = DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME;
		try {
			ClassPathResource resource = new ClassPathResource(propertiesFile);

			Properties properties = new Properties();
			properties.load(resource.getInputStream());

			beanName = StringUtils
					.defaultString(
							properties
									.getProperty(VIEW_AUTHORIZATION_SERVICE_BEAN_NAME_PROP_KEY),
							beanName);
		} catch (IOException e) {
			if (logger.isWarnEnabled())
				logger.warn("Could not read '" + propertiesFile
						+ "' on the classpath: " + e);
		}

		if (logger.isInfoEnabled())
			logger.info("viewAuthorizationServiceBeanName: " + beanName);

		return beanName;
	}
}
