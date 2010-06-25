package com.upthescala.viewprotect.basic;

import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.upthescala.viewprotect.ComponentAttribute;
import com.upthescala.viewprotect.ComponentAttributeSource;
import com.upthescala.viewprotect.ViewAuthorizationService;

/**
 * An implementation of {@link ViewAuthorizationService} that delegates to a
 * configured {@link ComponentAttributeSource} in order to determine whether
 * access is allowed.
 * 
 * @author Lloyd Smith II
 */
public class BasicViewAuthorizationService implements ViewAuthorizationService {

	private static final Log logger = LogFactory
			.getLog(BasicViewAuthorizationService.class);

	private ComponentAttributeSource componentAttributeSource;

	private boolean allowAccessIfComponentIsNotConfigured = true;

	/**
	 * @throws IllegalArgumentException
	 *             if the {@link Authentication#getAuthorities()} returns
	 *             {@code null} or if {@link GrantedAuthority#getAuthority()}
	 *             returns {@code null} for any {@link GrantedAuthority} the
	 *             user possesses.
	 */
	public boolean isAuthorizedForUser(final String componentId,
			final Authentication user) {

		ComponentAttribute attribute = componentAttributeSource
				.getAttribute(componentId);

		if (attribute != null) {
			final Set<String> grantedRoles = toRoles(user.getAuthorities());
			final boolean accessGranted = attribute.isSatisfiedBy(grantedRoles);
			if (logger.isTraceEnabled())
				logger.trace("User [" + user + "] with roles [" + grantedRoles
						+ "] requesting access to component with id ["
						+ componentId
						+ "], configured with ComponentAttribute [" + attribute
						+ "] is " + ((accessGranted) ? "ALLOWED" : "DENIED")
						+ " access.");
			return accessGranted;
		}

		if (logger.isTraceEnabled())
			logger.trace("component with id ["
					+ componentId
					+ "] is not configured "
					+ "with access permissions; default in this case is to "
					+ (allowAccessIfComponentIsNotConfigured ? "ALLOW" : "DENY")
					+ " access!");

		return allowAccessIfComponentIsNotConfigured;
	}

	private Set<String> toRoles(final GrantedAuthority[] grantedAuthorities) {

		if (grantedAuthorities == null)
			throw new IllegalArgumentException(
					"grantedAuthorities array is null");

		Set<String> grantedRoles = new HashSet<String>(
				grantedAuthorities.length);

		for (GrantedAuthority grantedAuthority : grantedAuthorities) {
			String role = grantedAuthority.getAuthority();
			if (role == null)
				throw new IllegalArgumentException(
						"GrantedAuthority implementations that return null from getAuthority() are not supported: "
								+ grantedAuthority);
			grantedRoles.add(role);
		}
		return grantedRoles;
	}

	/**
	 * Sets the fallback behavior to use when permissions have not be configured
	 * for a component id. The default is to allow this access.
	 * 
	 * @param allowAccessIfComponentIsNotConfigured
	 *            whether to allow access to the component if permissions for it
	 *            have not been configured in the
	 *            {@link ComponentAttributeSource}
	 */
	public void setAllowAccessIfComponentIsNotConfigured(
			final boolean allowAccessIfComponentIsNotConfigured) {
		this.allowAccessIfComponentIsNotConfigured = allowAccessIfComponentIsNotConfigured;
	}

	public void setComponentAttributeSource(
			final ComponentAttributeSource componentAttributeSource) {
		this.componentAttributeSource = componentAttributeSource;
	}
}
