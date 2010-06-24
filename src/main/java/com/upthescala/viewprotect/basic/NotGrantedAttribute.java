/**
 * 
 */
package com.upthescala.viewprotect.basic;

import java.util.Set;

class NotGrantedAttribute extends AbstractComponentAttribute {

	public NotGrantedAttribute(final Set<String> decisionSet) {
		super(decisionSet);
	}

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {
		return intersection(grantedRoles, decisionSet).isEmpty();
	}
}