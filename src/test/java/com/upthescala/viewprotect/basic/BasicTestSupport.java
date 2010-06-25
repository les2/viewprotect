package com.upthescala.viewprotect.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.testng.annotations.DataProvider;

/**
 * A number of "fluently-named" convenience methods for use in unit tests. These
 * methods should be combined with static imports in a unit test class to
 * produce more readable code. This methods are especially useful for defining
 * the tuples returned by {@link DataProvider} methods.
 * 
 * Example:
 * 
 * <pre>
 * &#64;DataProvider(name="roleTestProvider")
 * public Object[][] roleTestDataProvider() {
 *    // &#64formatter:off
 *    return new Object[][] {
 *       {allowed("ROLE_A"),denied("ROLE_C"), basicAttributes(notGranted("FOO"),null,anyGranted("ROLE_X","ROLE_Y")},
 *       ...
 *    };
 *    // &#64formatter:on
 * }
 * 
 * &#64;Test(dataProvider="roleTestProvider")
 * public void roleTest(Set<String> allowedRoles, Set<String> deniedRoles, BasicComponentAttributes attrs) {
 *     ...
 * }
 * </pre>
 * 
 * As a result of using fluently-named helper methods such as those defined in
 * this class, the data-driven tests become much more readable.
 * 
 * @author Lloyd Smith II
 */
public class BasicTestSupport {

	/**
	 * @param <T>
	 *            the type of the elements
	 * @param items
	 *            the elements to add to the HashSet
	 * @return a new HashSet containing the {@code items}
	 */
	public static <T> Set<T> setOf(final T... items) {
		return new HashSet<T>(Arrays.asList(items));
	}

	/**
	 * @param roles
	 *            the roles
	 * @return a new AnyGrantedAtribute with a decision set initialized to the
	 *         specified roles
	 */
	public static AnyGrantedAttribute anyGranted(final String... roles) {
		return new AnyGrantedAttribute(setOf(roles));
	}

	/**
	 * 
	 * @param roles
	 *            the roles
	 * @return a new AllGrantedAttribute with a decision set initialized to the
	 *         specified roles
	 */
	public static AllGrantedAttribute allGranted(final String... roles) {
		return new AllGrantedAttribute(setOf(roles));
	}
	/**
	 * 
	 * @param roles
	 *            the roles
	 * @return a new NotGrantedAttribute with a decision set initialized to the
	 *         specified roles
	 */
	public static NotGrantedAttribute notGranted(final String... roles) {
		return new NotGrantedAttribute(setOf(roles));
	}

	/**
	 * 
	 * @param notGranted
	 *            a NotGrantedAttribute (possibly {@code null})
	 * @param allGranted
	 *            a AllGrantedAttribute (possibly {@code null})
	 * @param anyGranted
	 *            a AnyGrantedAttribute (possibly {@code null})
	 * @return a new BasicComponentAttributes initialized with the arguments
	 */
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

	/**
	 * 
	 * @param <T>
	 * @param items
	 * @return a new HashSet of items fluently thought of as being "allowed"
	 *         (e.g., allowed user roles)
	 */
	public static <T> Set<T> allowed(final T... items) {
		return setOf(items);
	}
	/**
	 * 
	 * @param <T>
	 * @param items
	 * @return a new HashSet of items fluently thought of as being "denied"
	 *         (e.g., denied user roles)
	 */
	public static <T> Set<T> denied(final T... items) {
		return setOf(items);
	}
	/**
	 * @param <T>
	 * @param roles
	 *            the roles
	 * @return a new String array of roles fluently thought of as being
	 *         "granted roles"
	 */
	public static String[] grantedRoles(final String... roles) {
		return array(roles);
	}

	/**
	 * A convenience method to create an array of elements with less syntax that
	 * new Object[]{ one, two, ..., last }.
	 * 
	 * Example:
	 * 
	 * <pre>
	 * doSomethingWithArray(array("thing1", "thing2", thing3"));
	 * </pre>
	 * 
	 * @param <T>
	 * @param items
	 *            the elements of the array
	 * @return an array of the items
	 */
	public static <T> T[] array(final T... items) {
		return items;
	}

	/**
	 * @param roles
	 *            the roles
	 * @return an array containing a {@code GrantedAuthority} for each role;
	 *         {@link GrantedAuthorityImpl} is used
	 */
	public static GrantedAuthority[] auths(final String... roles) {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>(
				roles.length);
		for (String role : roles)
			auths.add(new GrantedAuthorityImpl(role));

		return auths.toArray(new GrantedAuthority[roles.length]);
	}

	/**
	 * @param roles
	 *            the roles to add to the set
	 * @return a new set consisting of the specified roles
	 */
	public static Set<String> roles(final String... roles) {
		return setOf(roles);
	}

}
