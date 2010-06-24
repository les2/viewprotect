package com.upthescala.viewprotect;

import java.util.Set;

public interface ComponentAttribute {
	boolean isSatisfiedBy(Set<String> grantedRoles);
}