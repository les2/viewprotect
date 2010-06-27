package com.upthescala.viewprotect.basic;

import static com.upthescala.viewprotect.basic.BasicTestSupport.auths;
import static org.easymock.EasyMock.*;
import static org.testng.Assert.*;

import java.util.Arrays;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.*;

import com.upthescala.viewprotect.ViewAuthorizationService;

public class BasicViewProtectIntegrationTest {

	private ViewAuthorizationService viewAuthorizationService;
	private Authentication user;

	@BeforeClass
	public void setUpBeforeClass() {
		viewAuthorizationService = (ViewAuthorizationService) new ClassPathXmlApplicationContext(
				"com/upthescala/viewprotect/basic/basicIntegrationTests.xml")
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

	@DataProvider(name = "integrationTestDataProvider")
	public Object[][] integrationTestDataProvider() {
		// @formatter:off
		return new Object[][]{
			//{"unconfiguredComponent", auths(), false}, 
			{"com.foo.component1", auths("ROLE_K"), false}, 
			{"com.foo.component1", auths(), false}, 
			{"com.foo.component1", auths("ROLE_A"), true}, 
			{"com.foo.component1", auths("ROLE_B"), true}, 
			{"com.foo.component1", auths("ROLE_C"), true}, 
			{"com.bar.component2", auths("ROLE_X","ROLE_Y"), false}, 
			{"com.bar.component2", auths("ROLE_X","ROLE_Y","ROLE_XYZ"), false}, 
			{"com.bar.component2", auths("ROLE_X","ROLE_Y","ROLE_Z"), true}, 
			{"com.foo.bar.component3", auths("ROLE_M","ROLE_A"), false}, 
			{"com.foo.bar.component3", auths("ROLE_A"), true}, 
			{"component4", auths("ROLE_A"), false}, 
			{"component4", auths("ROLE_ANY_1"), false}, 
			{"component4", auths("ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3"), false}, 
			{"component4", auths("ROLE_ANY_1","ROLE_ANY_2",
								 "ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3",
								 "ROLE_NOT_1"), false}, 
			{"component4", auths("ROLE_ANY_1","ROLE_ANY_2",
								 "ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3",
								 "ROLE_NOT_2"), false}, 
			{"component4", auths("ROLE_ANY_1","ROLE_ANY_2",
								 "ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3",
								 "ROLE_NOT_3"), false}, 
			{"component4", auths("ROLE_ANY_1","ROLE_ANY_2",
								 "ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3",
								 "ROLE_NOT_4"), false}, 
			{"component4", auths("ROLE_ANY_1", "ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3"), true}, 
			{"component4", auths("ROLE_ANY_2", "ROLE_ALL_1","ROLE_ALL_2","ROLE_ALL_3"), true}, 
		};
		// @formatter:on
	}

	@DataProvider(name = "unconfiguredComponentDataProvider")
	public Object[][] unconfiguredComponentDataProvider() {

		// @formatter:off
		return new Object[][]{
			{"notConfigured1", auths("ROLE_ADMIN", "ROLE_SUPER_USER")},
			{"component4.almost", auths("ROLE_ADMIN", "ROLE_SUPER_USER")},
			{"component1.almost", auths()},
		};
		// @formatter:on
	}

	@Test(dataProvider = "unconfiguredComponentDataProvider")
	public void shouldNotAllowAccessToUnconfiguredComponents(
			final String componentId,
			final GrantedAuthority[] grantedAuthorities) {

		replay(user);

		assertFalse(
				viewAuthorizationService.isAuthorizedForUser(componentId, user),
				"componentId ["
						+ componentId
						+ "] should not be authorized for anyone since it was not configured: granted authorities => "
						+ Arrays.toString(grantedAuthorities));

		verify(user);
	}

	@Test(dataProvider = "integrationTestDataProvider")
	public void shouldAllowOrDisallowAccessBasedOnXmlConfig(
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
