package com.upthescala.viewprotect.tag;

import static com.upthescala.viewprotect.basic.BasicTestSupport.array;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertEquals;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.upthescala.viewprotect.ViewAuthorizationService;

public class ViewProtectTagTest {

	private ViewProtectTag tag;
	private MockPageContext pageContext;
	private MockServletContext servletContext;

	private Authentication user;
	private SecurityContext securityContext;
	private ViewAuthorizationService viewAuthorizationService;

	private Object[] mocks;
	private WebApplicationContext applicationContext;

	@BeforeMethod
	public void setUp() {
		tag = new ViewProtectTag();

		servletContext = new MockServletContext();

		applicationContext = createMock("applicationContext",
				WebApplicationContext.class);

		servletContext.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				applicationContext);
		pageContext = new MockPageContext(servletContext);

		tag.setPageContext(pageContext);

		user = createMock("userAuthentication", Authentication.class);
		securityContext = createMock("securityContext", SecurityContext.class);
		viewAuthorizationService = createMock("viewAuthorizationService",
				ViewAuthorizationService.class);

		mocks = array(user, securityContext, viewAuthorizationService,
				applicationContext);

		reset(mocks);

		SecurityContextHolder.setContext(securityContext);
	}

	@DataProvider(name = "protectTag")
	public Object[][] protectTagDataProvider() {
		return new Object[][]{{"foo", false, Tag.SKIP_BODY},
				{"foo", true, Tag.EVAL_BODY_INCLUDE},};
	}

	@Test(dataProvider = "protectTag")
	public void testProtectTag(final String componentId,
			final boolean isAuthorized, final int expectedDoStartTagResult)
			throws JspException {

		expect(
				applicationContext.getBean("viewAuthorizationService",
						ViewAuthorizationService.class)).andReturn(
				viewAuthorizationService);

		expect(securityContext.getAuthentication()).andReturn(user);
		expect(viewAuthorizationService.isAuthorizedForUser(componentId, user))
				.andReturn(isAuthorized);

		replay(mocks);

		tag.setComponentId(componentId);

		assertEquals(tag.getComponentId(), componentId);

		assertEquals(tag.doStartTag(), expectedDoStartTagResult,
				"doStartTagResult does not match expected");

		verify(mocks);
	}

	@AfterMethod
	public void tearDown() {
		SecurityContextHolder.clearContext();

	}
}
