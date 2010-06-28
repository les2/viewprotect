package com.upthescala.viewprotect.spring;

import static org.easymock.EasyMock.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.*;

import com.upthescala.viewprotect.ViewAuthorizationService;

public class SpringNamespaceHandlerTest {

	private ViewAuthorizationService viewAuthorizationService;
	private Authentication user;

	@BeforeClass
	public void beforeClass() {
		viewAuthorizationService = (ViewAuthorizationService) new ClassPathXmlApplicationContext(
				"com/upthescala/viewprotect/spring/springNamespaceTestContext.xml")
				.getBean("viewAuthorizationService",
						ViewAuthorizationService.class);
	}

	@BeforeMethod
	public void setUp() {
		assertNotNull(viewAuthorizationService,
				"viewAuthorizationService is null");

		user = createMock("user", Authentication.class);

		reset(user);
	}

	@Test(dataProvider = "integrationTestDataProvider", dataProviderClass = com.upthescala.viewprotect.basic.BasicViewProtectIntegrationTest.class)
	public void shouldLoadViewAuthorizationServiceInSpringContext(
			final String componentId,
			final GrantedAuthority[] grantedAuthorities,
			final boolean expectedIsAuthorized) {

		expect(user.getAuthorities()).andReturn(grantedAuthorities);

		replay(user);

		assertEquals(
				viewAuthorizationService.isAuthorizedForUser(componentId, user),
				expectedIsAuthorized, "componentId [" + componentId
						+ " should be authorized for a user with roles: "
						+ Arrays.toString(grantedAuthorities));

		verify(user);
	}
}
