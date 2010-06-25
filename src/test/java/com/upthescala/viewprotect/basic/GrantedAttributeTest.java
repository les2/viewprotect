package com.upthescala.viewprotect.basic;

import static com.upthescala.viewprotect.basic.BasicTestSupport.allGranted;
import static com.upthescala.viewprotect.basic.BasicTestSupport.allowed;
import static com.upthescala.viewprotect.basic.BasicTestSupport.anyGranted;
import static com.upthescala.viewprotect.basic.BasicTestSupport.denied;
import static com.upthescala.viewprotect.basic.BasicTestSupport.notGranted;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GrantedAttributeTest {

	@DataProvider(name = "componentAttributeTest")
	public Object[][] anyGrantedTestDataProvider() {
		// @formatter:off
		return new Object[][] {
				{ anyGranted("ROLE_A", "ROLE_B"), allowed("ROLE_A"), denied("ROLE_C") },
				{ anyGranted("ROLE_X"), allowed("ROLE_X"), denied() },
				{ anyGranted("A", "B", "C"), allowed("Z", "X", "B", "F"), denied("X", "y", "a") },
				{ notGranted("a"), allowed("b"), denied("d", "e", "a", "g") },
				{ notGranted("x", "y"), allowed("a", "b", "c"),	denied("d", "x", "a", "g") },
				{ notGranted("x", "y"), allowed("a", "b", "c"),	denied("d", "y", "a", "g") },
				{ allGranted("a", "b", "c"), allowed("a", "b", "c"), denied("a", "b") },
				{ allGranted("a", "b", "c"), allowed("a", "y", "b", "x", "c"), denied("b") }, 
			};
		// @formatter:on
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
