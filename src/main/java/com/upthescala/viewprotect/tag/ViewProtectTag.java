package com.upthescala.viewprotect.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.upthescala.viewprotect.ViewAuthorizationService;

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
