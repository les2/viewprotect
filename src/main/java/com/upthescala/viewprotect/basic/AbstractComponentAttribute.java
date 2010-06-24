package com.upthescala.viewprotect.basic;

import java.util.HashSet;
import java.util.Set;

import com.upthescala.viewprotect.ComponentAttribute;

abstract class AbstractComponentAttribute implements ComponentAttribute {
	protected final Set<String> decisionSet;

	public AbstractComponentAttribute(final Set<String> decisionSet) {
		this.decisionSet = new HashSet<String>(decisionSet);
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

	public final boolean isNotSatisfiedBy(final Set<String> grantedRoles) {
		return !isSatisfiedBy(grantedRoles);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [decisionSet=" + decisionSet
				+ "]";
	}
}