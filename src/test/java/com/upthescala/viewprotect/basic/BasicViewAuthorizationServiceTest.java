package com.upthescala.viewprotect.basic;

import static com.upthescala.viewprotect.basic.BasicTestSupport.array;
import static com.upthescala.viewprotect.basic.BasicTestSupport.auths;
import static com.upthescala.viewprotect.basic.BasicTestSupport.grantedRoles;
import static com.upthescala.viewprotect.basic.BasicTestSupport.roles;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertEquals;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.upthescala.viewprotect.ComponentAttribute;
import com.upthescala.viewprotect.ComponentAttributeSource;

public class BasicViewAuthorizationServiceTest {

	BasicViewAuthorizationService viewAuthorizationService;

	private ComponentAttributeSource attributeSource;
	private ComponentAttribute attribute;
	private Authentication user;

	private Object[] mocks;

	@BeforeMethod
	public void setUp() {

		attributeSource = createMock("attributeSource",
				ComponentAttributeSource.class);

		attribute = createMock("attribute", ComponentAttribute.class);
		user = createMock("user", Authentication.class);

		mocks = array(attributeSource, attribute, user);
		reset(mocks);

		viewAuthorizationService = new BasicViewAuthorizationService();
		viewAuthorizationService.setComponentAttributeSource(attributeSource);
	}

	@DataProvider(name = "delegate")
	public Object[][] delegateDataProvidor() {
		// @formatter:off
		return new Object[][] {
				{ "some.component.a", grantedRoles("ROLE_A"), true },
				{ "some", grantedRoles("ROLE_A", "b", "ANOTHER"), false },
				{ "x", grantedRoles(), true }, { "x", grantedRoles(), false }, };
		// @formatter:on
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenAuthorityIsNotValid() {
		class NonStringRepresentableRole implements GrantedAuthority {
			private static final long serialVersionUID = 1L;

			public String getAuthority() {
				return null;
			}
		}
		expect(attributeSource.getAttribute("foo")).andReturn(attribute);
		expect(user.getAuthorities()).andReturn(
				array(new NonStringRepresentableRole()));

		replay(mocks);

		viewAuthorizationService.isAuthorizedForUser("foo", user);

		assert (false) : "unreachable code!";
	}

	@Test(dataProvider = "delegate")
	public void shouldDelegateToAttribute(final String componentId,
			final String[] grantedRoles, final boolean isSatisfied) {

		expect(attributeSource.getAttribute(componentId)).andReturn(attribute);

		expect(user.getAuthorities()).andReturn(auths(grantedRoles));

		expect(attribute.isSatisfiedBy(roles(grantedRoles))).andReturn(
				isSatisfied);

		replay(mocks);

		boolean authorizedForUser = viewAuthorizationService
				.isAuthorizedForUser(componentId, user);

		verify(mocks);

		assertEquals(isSatisfied, authorizedForUser);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenUserAuthoritiesIsNull() {

		expect(attributeSource.getAttribute("foo")).andReturn(attribute);
		expect(user.getAuthorities()).andReturn(null);

		replay(mocks);

		viewAuthorizationService.isAuthorizedForUser("foo", user);

		assert (false) : "unreachable code!";
	}

	@DataProvider(name = "defaultBehavior")
	public Object[][] defaultBehaviorDataProvidor() {
		return new Object[][]{{true}, {false}};
	}

	@Test(dataProvider = "defaultBehavior")
	public void shouldUseDefaultBehaviorWhenComponentIdNotMapped(
			final boolean allowAccessIfComponentIsNotConfigured) {

		expect(attributeSource.getAttribute("foo")).andReturn(null);

		replay(mocks);

		viewAuthorizationService
				.setAllowAccessIfComponentIsNotConfigured(allowAccessIfComponentIsNotConfigured);

		boolean authorizedForUser = viewAuthorizationService
				.isAuthorizedForUser("foo", user);

		verify(mocks);

		assertEquals(allowAccessIfComponentIsNotConfigured, authorizedForUser);
	}

}
