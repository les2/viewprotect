package com.upthescala.viewprotect.tag;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.upthescala.viewprotect.ViewAuthorizationService;

public class ViewProtectTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(ViewProtectTag.class);

	/**
	 * The name of the properties file that will be searched for on the
	 * classpath for ViewProtectTag options.
	 */
	public static final String VIEW_PROTECT_PROPERTIES_FILE = "viewprotect.properties";

	/**
	 * The default name used for the Spring bean containing the implementation
	 * of {@link ViewAuthorizationService} that will be used by the
	 * {@link ViewProtectTag}.
	 */
	public static final String DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME = "viewAuthorizationService";

	private static final String VIEW_AUTH_SERVICE_BEAN_NAME;

	static {
		/*
		 * Load the properties file, if present, to determine the name of the
		 * Spring bean that contains the implementation of
		 * ViewAuthorizationService to use:
		 */
		String beanName = DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME;
		try {
			ClassPathResource resource = new ClassPathResource(
					VIEW_PROTECT_PROPERTIES_FILE);

			Properties properties = new Properties();
			properties.load(resource.getInputStream());

			beanName = StringUtils
					.defaultString(properties
							.getProperty("viewAuthorizationService.beanName"),
							beanName);
		} catch (IOException e) {
			if (logger.isWarnEnabled())
				logger.warn("Could not read '" + VIEW_PROTECT_PROPERTIES_FILE
						+ "' on the classpath: " + e);
		}

		VIEW_AUTH_SERVICE_BEAN_NAME = beanName;

		if (logger.isInfoEnabled())
			logger.info("viewAuthorizationServiceBeanName: "
					+ VIEW_AUTH_SERVICE_BEAN_NAME);
	}

	private String componentId;

	public String getComponentId() {
		return componentId;
	}

	/**
	 * @param componentId
	 *            the componentId passed to
	 *            {@link ViewAuthorizationService#isAuthorizedForUser(String)}
	 */
	public void setComponentId(final String componentId) {
		this.componentId = componentId;
	}

	@Override
	public int doStartTag() throws JspException {

		ViewAuthorizationService viewAuthService = getViewAuthorizationService();

		if (viewAuthService.isAuthorizedForUser(componentId,
				SecurityContextHolder.getContext().getAuthentication()))
			return Tag.EVAL_BODY_INCLUDE;

		return Tag.SKIP_BODY;
	}

	private ViewAuthorizationService getViewAuthorizationService() {

		WebApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(pageContext
						.getServletContext());

		return (ViewAuthorizationService) applicationContext.getBean(
				VIEW_AUTH_SERVICE_BEAN_NAME, ViewAuthorizationService.class);
	}

}
