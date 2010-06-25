package com.upthescala.viewprotect.basic;

import static com.upthescala.viewprotect.basic.BasicTestSupport.allGranted;
import static com.upthescala.viewprotect.basic.BasicTestSupport.anyGranted;
import static com.upthescala.viewprotect.basic.BasicTestSupport.basicAttributes;
import static com.upthescala.viewprotect.basic.BasicTestSupport.notGranted;
import static com.upthescala.viewprotect.basic.BasicTestSupport.roles;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BasicComponentAttributesTest {

	@DataProvider(name = "basicAttributes")
	public Object[][] basicAttributesDataProvider() {

		// @formatter:off
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
				{ basicAttributes(notGranted("a","z"), allGranted("b","c"), null), false, roles("a") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles("b") }, 
				{ basicAttributes(notGranted("a","y"), allGranted("b","c"), null), false, roles("c") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), false, roles("e","c","a","b") }, 
				{ basicAttributes(notGranted("a"), allGranted("b","c"), null), true, roles("c","b") }, 
				{ basicAttributes(notGranted("a","x"), allGranted("b","c"), null), true, roles("e","c","b") }, 
				{ basicAttributes(notGranted("a"), null, anyGranted("b")), false, roles() }, 
				{ basicAttributes(notGranted("a"), null, anyGranted("b")), false, roles("c") }, 
				{ basicAttributes(notGranted("a","c"), null, anyGranted("b","d")), false, roles("b","c","d") }, 
				{ basicAttributes(notGranted("a","c"), null, anyGranted("b","d")), true, roles("d") }, 
				{ basicAttributes(null, allGranted("1","2","3"), anyGranted("4","5")), false, roles("4") }, 
				{ basicAttributes(null, allGranted("1","2","3"), anyGranted("4","5")), false, roles("4","5") }, 
				{ basicAttributes(null, allGranted("1","2","3"), anyGranted("4","5")), false, roles("1","2","3") }, 
				{ basicAttributes(null, allGranted("1","2","3"), anyGranted("4","5")), true, roles("1","2","3","4") }, 
				{ basicAttributes(null, allGranted("1","2","3"), anyGranted("4","5")), true, roles("1","2","3","5") }, 
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("a") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("b") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("1") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("1","x") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("2","y") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("1","2") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("1","2","x","a") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), false, roles("1","2","x","b") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), true, roles("1","2","x") },
				{ basicAttributes(notGranted("a","b"), allGranted("1","2"), anyGranted("x","y")), true, roles("1","2","y") },
			};
		// @formatter:on
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
