package com.upthescala.viewprotect.basic;

import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.upthescala.viewprotect.basic.BasicTestSupport.*;
import static org.testng.Assert.assertEquals;

public class BasicComponentAttributesTest {

	@DataProvider(name = "basicAttributes")
	public Object[][] basicAttributesDataProvider() {
		return new Object[][] {
				{ basicAttributes(notGranted("a"), null, null), false, roles("a") },
				{ basicAttributes(notGranted("a"), null, null), true, roles("b") }, 
				{ basicAttributes(notGranted("a"), null, null), true, roles() }, 
				{ basicAttributes(null, allGranted("a"), null), false, roles("b") }, 
				{ basicAttributes(null, allGranted("a"), null), true, roles("a") }, 
				{ basicAttributes(null, allGranted("a","b","c"), null), false, roles("a","c") }, 
				{ basicAttributes(null, allGranted("a","b","c"), null), false, roles() }, 
				{ basicAttributes(null, null, anyGranted("a","b","c")), false, roles() }, 
				{ basicAttributes(null, null, anyGranted("a","b","c")), false, roles("d") }, 
				{ basicAttributes(null, null, anyGranted("a","b","c")), true, roles("a") }, 
				{ basicAttributes(null, null, anyGranted("a","b","c")), true, roles("b") }, 
				{ basicAttributes(null, null, anyGranted("a","b","c")), true, roles("c") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles() }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles("a") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles("b") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles("c") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles("e","c","a","b") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), true, roles("c","b") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), true, roles("e","c","b") }, 
			};
	}

	@Test(dataProvider = "basicAttributes")
	public void testBasicAttributes(
			final BasicComponentAttributes basicAttributes,
			final boolean expectedIsSatisfiedBy, final Set<String> grantedRoles) {

		assertEquals(
				basicAttributes.isSatisfiedBy(grantedRoles),
				expectedIsSatisfiedBy,
				"attributes: "
						+ basicAttributes
						+ " does not correctly implement isSatisfiedBy for roles: "
						+ grantedRoles);
	}
}
