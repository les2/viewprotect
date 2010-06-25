package com.upthescala.viewprotect.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.upthescala.viewprotect.ViewAuthorizationService;

/**
 * A simple tag that delegates to a Spring managed
 * {@link ViewAuthorizationService} bean to determine whether or not to process
 * the tags body include. The tag will attempt to read
 * {@link Config#VIEW_PROTECT_PROPERTIES_FILE} to determine the name of the
 * {@link ViewAuthorizationService} bean in the {@link WebApplicationContext}.
 * In the event that the properties file is not found, the default bean name of
 * {@link Config#DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME} is used.
 * 
 * <p>
 * Sample {@code viewprotect.properties} file:
 * 
 * <pre>
 * viewAuthorizationService.beanName = myViewAuthorizationService
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * Sample usage of the ViewProtectTag:
 * 
 * <pre>
 * &lt;%&#64; taglib uri="http://upthescala.com/tags/viewprotect" prefix="viewprotect" %&gt;
 * ...
 * &lt;viewprotect:protect componentId="com.upthescala.sample.deleteButton"&gt;
 *    &lt;button name="deleteButton"&gt;Delete All Data&lt;/button&gt;
 * &lt;/viewprotect:protect&gt;
 * </pre>
 * 
 * </p>
 * 
 * Of course, you would need to register a suitable
 * {@link ViewAuthorizationService} in the web application context.
 * {@link com.upthescala.viewprotect.basic.BasicViewAuthorizationService} is
 * expected to be the most common implementation.
 * 
 * @author Lloyd Smith II
 * 
 * @see com.upthescala.viewprotect.basic.BasicViewAuthorizationService
 */
public class ViewProtectTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private static final String VIEW_AUTH_SERVICE_BEAN_NAME = Config
			.getViewAuthServiceBeanName(Config.VIEW_PROTECT_PROPERTIES_FILE);

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
