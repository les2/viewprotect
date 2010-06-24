/**
 * 
 */
package com.upthescala.viewprotect.basic;

import java.util.Set;

class AnyGrantedAttribute extends AbstractComponentAttribute {

	public AnyGrantedAttribute(final Set<String> decisionSet) {
		super(decisionSet);
	}

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {
		return !(intersection(grantedRoles, decisionSet).isEmpty());
	}
}