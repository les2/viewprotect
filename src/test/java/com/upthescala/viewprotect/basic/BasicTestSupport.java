package com.upthescala.viewprotect.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;

/**
 * A container for helper methods us
 */
public class BasicTestSupport {

	public static <T> Set<T> setOf(final T... items) {
		if (items.length == 0)
			return Collections.emptySet();
		return new HashSet<T>(Arrays.asList(items));
	}

	public static AnyGrantedAttribute anyGranted(final String... items) {
		return new AnyGrantedAttribute(setOf(items));
	}

	public static AllGrantedAttribute allGranted(final String... items) {
		return new AllGrantedAttribute(setOf(items));
	}

	public static NotGrantedAttribute notGranted(final String... items) {
		return new NotGrantedAttribute(setOf(items));
	}

	public static BasicComponentAttributes basicAttributes(
			final NotGrantedAttribute notGranted,
			final AllGrantedAttribute allGranted,
			final AnyGrantedAttribute anyGranted) {
		BasicComponentAttributes basicComponentAttributes = new BasicComponentAttributes();
		basicComponentAttributes.setAllGrantedAttribute(allGranted);
		basicComponentAttributes.setAnyGrantedAttribute(anyGranted);
		basicComponentAttributes.setNotGrantedAttribute(notGranted);
		return basicComponentAttributes;
	}

	public static <T> Set<T> allowed(final T... items) {
		return setOf(items);
	}

	public static <T> Set<T> denied(final T... items) {
		return setOf(items);
	}

	public static String[] grantedRoles(final String... roles) {
		return array(roles);
	}

	public static <T> T[] array(final T... items) {
		return items;
	}

	public static GrantedAuthority[] auths(final String... roles) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>(
				roles.length);
		for (String role : roles)
			auths.add(new GrantedAuthorityImpl(role));

		return auths.toArray(new GrantedAuthority[roles.length]);
	}

	public static Set<String> roles(final String... roles) {
		return new HashSet<String>(Arrays.asList(roles));
	}

}
