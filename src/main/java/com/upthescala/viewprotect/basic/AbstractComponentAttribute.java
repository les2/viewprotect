package com.upthescala.viewprotect.basic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.upthescala.viewprotect.ComponentAttribute;

/**
 * A base class for {@link ComponentAttribute}s that make a decision based on a
 * set of roles that a user must have, not have, or partially have.
 * 
 * @author Lloyd Smith II
 */
abstract class AbstractComponentAttribute implements ComponentAttribute {
	protected final Set<String> decisionSet;

	public AbstractComponentAttribute(final Set<String> decisionSet) {
		this.decisionSet = Collections.unmodifiableSet(new HashSet<String>(
				decisionSet)); // defensive
	}

	/**
	 * Gets a new set containing the intersection of {@code a} and {@code b} .
	 * The arguments will not be modified.
	 * 
	 * @param <T>
	 *            the type of elements in the set
	 * @param a
	 *            a set
	 * @param b
	 *            a set
	 * @return a new Set containing the intersection of sets {@code a} and
	 *         {@code b}
	 */
	protected final <T> Set<T> intersection(final Set<T> a, final Set<T> b) {
		Set<T> copy = new HashSet<T>(a);
		copy.retainAll(b);
		return copy;
	}

	/**
	 * A fluently-named convenience method for negating
	 * {@link #isSatisfiedBy(Set)}.
	 * 
	 * @param grantedRoles
	 *            the roles the authenticated user has
	 * @return {@code !isSatisfiedBy(grantedRoles)}
	 */
	public final boolean isNotSatisfiedBy(final Set<String> grantedRoles) {
		return !isSatisfiedBy(grantedRoles);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [decisionSet=" + decisionSet
				+ "]";
	}
}