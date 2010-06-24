/**
 * 
 */
package com.upthescala.viewprotect.basic;

import java.util.Set;

class AllGrantedAttribute extends AbstractComponentAttribute {

	public AllGrantedAttribute(final Set<String> decisionSet) {
		super(decisionSet);
	}

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {
		return grantedRoles.containsAll(decisionSet);
	}
}