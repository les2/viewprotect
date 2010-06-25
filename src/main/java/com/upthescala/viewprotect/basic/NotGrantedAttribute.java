package com.upthescala.viewprotect.basic;

import java.util.Set;

/**
 * A ComponentAttribute that is satisfied when the user is <strong><em>NOT</em>
 * </strong> granted any of the roles in the {@code decisionSet}.
 * 
 * @author Lloyd Smith II
 */
class NotGrantedAttribute extends AbstractComponentAttribute {

	public NotGrantedAttribute(final Set<String> decisionSet) {
		super(decisionSet);
	}

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {
		return intersection(grantedRoles, decisionSet).isEmpty();
	}
}