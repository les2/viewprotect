package com.upthescala.viewprotect;

import org.acegisecurity.Authentication;

/**
 * A ViewAuthorizationService encapsulates decision making for whether to show a
 * component on a JSP or not. A typical implementation will use a properties
 * with entries such as {@code "myComponent.ifAllGranted=ROLE_ADMIN,ROLE_USER"}.
 * file
 * 
 * @author Lloyd Smith II
 */
public interface ViewAuthorizationService {

	/**
	 * Determine whether the user is allowed to access the specified component.
	 * 
	 * @param componentId
	 *            the application-defined id of the protected component
	 * @param user
	 *            the {@link Authentication} object associated with the user
	 * @return {@code true} if the user is allowed to view the component;
	 *         {@code false} otherwise
	 */
	boolean isAuthorizedForUser(String componentId, Authentication user);

}
