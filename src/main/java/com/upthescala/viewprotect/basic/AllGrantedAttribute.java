package com.upthescala.viewprotect.basic;

import java.util.Set;

/**
 * A ComponentAttribute that is satisfied when the user is granted <strong>
 * <em>ALL</em></strong> of the roles in the {@code decisionSet}.
 * 
 * @author Lloyd Smith II
 */
class AllGrantedAttribute extends AbstractComponentAttribute {

	public AllGrantedAttribute(final Set<String> decisionSet) {
		super(decisionSet);
	}

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {
		return grantedRoles.containsAll(decisionSet);
	}
}