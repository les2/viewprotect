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

public class BasicViewAuthorizationService implements ViewAuthorizationService {

	private static final Log logger = LogFactory
			.getLog(BasicViewAuthorizationService.class);

	private ComponentAttributeSource componentAttributeSource;

	private boolean allowAccessIfComponentIsNotConfigured = false;

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
			logger
					.trace("component with id ["
							+ componentId
							+ "] is not configured "
							+ "with access permissions; default in this case is to "
							+ (allowAccessIfComponentIsNotConfigured ? "ALLOW"
									: "DENY") + " access!");

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

	public void setAllowAccessIfComponentIsNotConfigured(
			final boolean allowAccessIfComponentIsNotConfigured) {
		this.allowAccessIfComponentIsNotConfigured = allowAccessIfComponentIsNotConfigured;
	}

	public void setComponentAttributeSource(
			final ComponentAttributeSource componentAttributeSource) {
		this.componentAttributeSource = componentAttributeSource;
	}
}
