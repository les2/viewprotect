package com.upthescala.viewprotect;

import java.util.Set;

/**
 * A {@link ComponentAttribute} determines what roles are necessary to access a
 * particular component.
 * 
 * @author Lloyd Smith II
 */
public interface ComponentAttribute {

	/**
	 * @param grantedRoles
	 *            the roles the current user has
	 * 
	 * @return {@code true} if access should be given to a user granted the
	 *         specified roles; {@code false} otherwise
	 */
	boolean isSatisfiedBy(Set<String> grantedRoles);
}