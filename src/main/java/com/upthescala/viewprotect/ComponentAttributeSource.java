package com.upthescala.viewprotect;

/**
 * A {@link ComponentAttributeSource} is a repository for
 * {@link ComponentAttribute}s. Simple implementations could be backed by a
 * simple HashMap and more complex ones by a relational database or web service.
 * 
 * @author Lloyd Smith II
 */
public interface ComponentAttributeSource {

	/**
	 * Looks up the {@link ComponentAttribute} for the given id.
	 * 
	 * @param componentId
	 *            the application defined component id
	 * @return a {@link ComponentAttribute} or {@code null} if none is contained
	 *         in this {@link ComponentAttributeSource}
	 */
	ComponentAttribute getAttribute(String componentId);
}