package com.upthescala.viewprotect.basic;

import java.util.Set;

import com.upthescala.viewprotect.ComponentAttribute;

class BasicComponentAttributes implements ComponentAttribute {

	private AnyGrantedAttribute anyGrantedAttribute;
	private AllGrantedAttribute allGrantedAttribute;
	private NotGrantedAttribute notGrantedAttribute;

	public boolean isSatisfiedBy(final Set<String> grantedRoles) {

		if (notGrantedAttribute != null
				&& notGrantedAttribute.isNotSatisfiedBy(grantedRoles))
			return false;

		if (allGrantedAttribute != null
				&& allGrantedAttribute.isNotSatisfiedBy(grantedRoles))
			return false;

		if (anyGrantedAttribute != null
				&& anyGrantedAttribute.isNotSatisfiedBy(grantedRoles))
			return false;

		return true;

	}

	public void setAnyGrantedAttribute(
			final AnyGrantedAttribute anyGrantedAttribute) {
		this.anyGrantedAttribute = anyGrantedAttribute;
	}

	public void setAllGrantedAttribute(
			final AllGrantedAttribute allGrantedAttribute) {
		this.allGrantedAttribute = allGrantedAttribute;
	}

	public void setNotGrantedAttribute(
			final NotGrantedAttribute notGrantedAttribute) {
		this.notGrantedAttribute = notGrantedAttribute;
	}

	@Override
	public String toString() {
		return "BasicComponentAttributes [allGrantedAttribute="
				+ allGrantedAttribute + ", anyGrantedAttribute="
				+ anyGrantedAttribute + ", notGrantedAttribute="
				+ notGrantedAttribute + "]";
	}

	public AnyGrantedAttribute getAnyGrantedAttribute() {
		return anyGrantedAttribute;
	}

	public AllGrantedAttribute getAllGrantedAttribute() {
		return allGrantedAttribute;
	}

	public NotGrantedAttribute getNotGrantedAttribute() {
		return notGrantedAttribute;
	}
}