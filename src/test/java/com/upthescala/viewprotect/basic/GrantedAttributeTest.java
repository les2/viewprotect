package com.upthescala.viewprotect.basic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class GrantedAttributeTest {

	private <T> Set<T> setOf(final T... items) {
		if (items.length == 0)
			return Collections.emptySet();
		return new HashSet<T>(Arrays.asList(items));
	}

	private AnyGrantedAttribute anyGranted(final String... items) {
		return new AnyGrantedAttribute(setOf(items));
	}

	private AllGrantedAttribute allGranted(final String... items) {
		return new AllGrantedAttribute(setOf(items));
	}

	private NotGrantedAttribute notGranted(final String... items) {
		return new NotGrantedAttribute(setOf(items));
	}

	private <T> Set<T> allowed(final T... items) {
		return setOf(items);
	}

	private <T> Set<T> denied(final T... items) {
		return setOf(items);
	}

	@DataProvider(name = "componentAttributeTest")
	public Object[][] anyGrantedTestDataProvider() {
		return new Object[][] {
				{ anyGranted("ROLE_A", "ROLE_B"), allowed("ROLE_A"), denied("ROLE_C") },
				{ anyGranted("ROLE_X"), allowed("ROLE_X"), denied() },
				{ anyGranted("A", "B", "C"), allowed("Z", "X", "B", "F"), denied("X", "y", "a") },
				{ notGranted("a"),allowed("b"),denied("d","e","a","g")},
				{ notGranted("x","y"),allowed("a","b","c"),denied("d","x","a","g")},
				{ notGranted("x","y"),allowed("a","b","c"),denied("d","y","a","g")},
				{ allGranted("a","b","c"),allowed("a","b","c"),denied("a","b")},
				{ allGranted("a","b","c"),allowed("a","y","b","x","c"),denied("b")},
			};
	}

	@Test(dataProvider = "componentAttributeTest")
	public void testAnyGrantedAttribute(
			final AbstractComponentAttribute attribute,
			final Set<String> allowedGrantedRoles,
			final Set<String> deniedGrantedRoles) {

		assertTrue(attribute.isSatisfiedBy(allowedGrantedRoles),
				"granted attribute: " + attribute
						+ " should be satisfied by granted roles: "
						+ allowedGrantedRoles);
		assertFalse(attribute.isNotSatisfiedBy(allowedGrantedRoles),
				"granted attribute: " + attribute
						+ " should be satisfied by granted roles: "
						+ allowedGrantedRoles);
		assertTrue(attribute.isNotSatisfiedBy(deniedGrantedRoles),
				"granted attribute: " + attribute
						+ " should NOT be satisfied by granted roles: "
						+ deniedGrantedRoles);
		assertFalse(attribute.isSatisfiedBy(deniedGrantedRoles),
				"granted attribute: " + attribute
						+ " should NOT be satisfied by granted roles: "
						+ deniedGrantedRoles);

	}
}
