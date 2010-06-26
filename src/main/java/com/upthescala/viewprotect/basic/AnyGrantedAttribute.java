package com.upthescala.viewprotect.basic;

import java.util.Set;

/**
 * A ComponentAttribute that is satisfied when the user is granted <strong>
 * <em>ANY</em></strong> of the roles in the {@code decisionSet}.
 * 
 * @author Lloyd Smith II
 */
class AnyGrantedAttribute extends AbstractComponentAttribute {

	public AnyGrantedAttribute(final Set<String> decisionSet) {
		super(decisionSet);
	}

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {
		return containsSomeOfDecisionSet(grantedRoles);
	}
}