package com.upthescala.viewprotect.basic;

import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.upthescala.viewprotect.ComponentAttribute;
import com.upthescala.viewprotect.ComponentAttributeSource;

import static com.upthescala.viewprotect.basic.BasicTestSupport.array;
import static com.upthescala.viewprotect.basic.BasicTestSupport.roles;
import static org.testng.Assert.*;

public class BasicComponentAttributeSourceFactoryBeanTest {

	@Test
	public void objectTypeShouldBeComponentAttributeSource() {
		assertEquals(ComponentAttributeSource.class,
				new BasicComponentAttributeSourceFactoryBean().getObjectType(),
				"incorrect object type");
	}

	@Test
	public void shouldBeSingleton() {
		assertTrue(
				new BasicComponentAttributeSourceFactoryBean().isSingleton(),
				"should be singleton");
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenNoMapOrPropertiesIsSpecified() {

		new BasicComponentAttributeSourceFactoryBean().getObject();
	}

	@DataProvider(name = "invalidTextFormatDataProvider")
	public Object[][] invalidTextFormatDataProvider() {
		// @formatter:off
		return new Object[][]{
			{array("component1"), array("ROLE_A,ROLE_B")},
			{array(""), array("ROLE")},
			{array("."), array("ROLE")},
			{array("component1.ifNotGranted","component2.ifAllGranted","component1"), 
				array("ROLE_A","ROLE_B,ROLE_C","ROLE_D")},
			{array("foo.bar.component.invalidAttributeType"), array("ROLE")},
		};
		// @formatter:on
	}

	@Test(expectedExceptions = IllegalArgumentException.class, dataProvider = "invalidTextFormatDataProvider")
	public void shouldThrowExceptionWhenInputNotValid(
			final String[] componentIds, final String[] roleStrings) {
		HashMap<String, String> mappings = new HashMap<String, String>();

		for (int i = 0; i < componentIds.length; i++)
			mappings.put(componentIds[i], roleStrings[i]);

		BasicComponentAttributeSourceFactoryBean attributeSourceFactoryBean = new BasicComponentAttributeSourceFactoryBean();
		attributeSourceFactoryBean.setAttributeMappings(mappings);

		attributeSourceFactoryBean.getObject();
	}

	@Test
	public void shouldCreateSourceFromProperties() {
		Properties props = new Properties();
		props.setProperty(
				"com.upthescala.viewprotect.sample.admin.deleteUserButton.ifAnyGranted",
				"ROLE_ADMIN,ROLE_X ROLE_Y,ROLE_Z");

		props.setProperty(
				"com.upthescala.viewprotect.sample.home.secrets.ifAllGranted",
				"ROLE_USER ROLE_ADMIN");
		props.setProperty(
				"com.upthescala.viewprotect.sample.coolOnly.ifNotGranted",
				"ROLE_NINCOMPOOP");

		BasicComponentAttributeSourceFactoryBean attributeSourceFactoryBean = new BasicComponentAttributeSourceFactoryBean();
		attributeSourceFactoryBean.setPropertiesAttributeMappings(props);

		ComponentAttributeSource source = attributeSourceFactoryBean
				.getObject();

		assertNotNull(source);
		assertAttributePresentFor(
				"com.upthescala.viewprotect.sample.admin.deleteUserButton",
				source);
		assertAttributePresentFor(
				"com.upthescala.viewprotect.sample.home.secrets", source);
		assertAttributePresentFor("com.upthescala.viewprotect.sample.coolOnly",
				source);

		assertNull(source.getAttribute("component.foo"));

		assertBasicAttributesMatch(
				source.getAttribute("com.upthescala.viewprotect.sample.admin.deleteUserButton"),
				allGranted(),
				anyGranted("ROLE_ADMIN", "ROLE_X", "ROLE_Y", "ROLE_Z"),
				notGranted());

		assertBasicAttributesMatch(
				source.getAttribute("com.upthescala.viewprotect.sample.home.secrets"),
				allGranted("ROLE_ADMIN", "ROLE_USER"), anyGranted(),
				notGranted());

		assertBasicAttributesMatch(
				source.getAttribute("com.upthescala.viewprotect.sample.coolOnly"),
				allGranted(), anyGranted(), notGranted("ROLE_NINCOMPOOP"));
	}

	@Test
	public void shouldCreateSourceFromMap() {

		HashMap<String, String> attributeMappings = new HashMap<String, String>();

		attributeMappings
				.put("component1.ifAnyGranted", "ROLE_A,ROLE_B,ROLE_D");
		attributeMappings.put("component1.ifNotGranted", "ROLE_Z");
		attributeMappings.put("component1.ifAllGranted", "ROLE_X,ROLE_Y");

		attributeMappings.put("component2.ifNotGranted", "ROLE_X");
		attributeMappings.put("component3.ifAnyGranted", "ROLE_Y");
		attributeMappings.put("component4.ifAllGranted", "ROLE_Z");

		BasicComponentAttributeSourceFactoryBean attributeSourceFactoryBean = new BasicComponentAttributeSourceFactoryBean();
		attributeSourceFactoryBean.setAttributeMappings(attributeMappings);

		ComponentAttributeSource source = attributeSourceFactoryBean
				.getObject();

		assertNotNull(source,
				"factory bean returned null ComponentAttributeSource");
		assertAttributePresentFor("component1", source);
		assertAttributePresentFor("component2", source);
		assertAttributePresentFor("component3", source);
		assertAttributePresentFor("component4", source);

		assertNull(source.getAttribute("component10"));

		assertBasicAttributesMatch(source.getAttribute("component1"),
				allGranted("ROLE_X", "ROLE_Y"),
				anyGranted("ROLE_A", "ROLE_B", "ROLE_D"), notGranted("ROLE_Z"));

		assertBasicAttributesMatch(source.getAttribute("component2"),
				allGranted(), anyGranted(), notGranted("ROLE_X"));

		assertBasicAttributesMatch(source.getAttribute("component3"),
				allGranted(), anyGranted("ROLE_Y"), notGranted());

		assertBasicAttributesMatch(source.getAttribute("component4"),
				allGranted("ROLE_Z"), anyGranted(), notGranted());

	}

	private Set<String> allGranted(final String... roles) {
		if (roles == null || roles.length == 0)
			return null;
		return roles(roles);
	}
	private Set<String> anyGranted(final String... roles) {
		if (roles == null || roles.length == 0)
			return null;
		return roles(roles);
	}
	private Set<String> notGranted(final String... roles) {
		if (roles == null || roles.length == 0)
			return null;
		return roles(roles);
	}

	private void assertBasicAttributesMatch(
			final ComponentAttribute basicAttributes,
			final Set<String> ifAllGranted, final Set<String> ifAnyGranted,
			final Set<String> ifNotGranted) {

		BasicComponentAttributes attributes = (BasicComponentAttributes) basicAttributes;

		assertNotNull(attributes, "basicComponentAttributes is null");

		assertEquals(ifAllGranted == null
				? attributes.getAllGrantedAttribute()
				: attributes.getAllGrantedAttribute().getDecisionSet(),
				ifAllGranted, "ifAllGranted attribute does not match: "
						+ attributes);
		assertEquals(ifAnyGranted == null
				? attributes.getAnyGrantedAttribute()
				: attributes.getAnyGrantedAttribute().getDecisionSet(),
				ifAnyGranted, "ifAnyGranted attribute does not match: "
						+ attributes);
		assertEquals(ifNotGranted == null
				? attributes.getNotGrantedAttribute()
				: attributes.getNotGrantedAttribute().getDecisionSet(),
				ifNotGranted, "ifNotGranted attribute does not match: "
						+ attributes);

	}

	private void assertAttributePresentFor(final String componentId,
			final ComponentAttributeSource source) {
		assertNotNull(source.getAttribute(componentId),
				"componentAttributeSource does not contain attributes for component ["
						+ componentId + "] : componentAttributeSource => "
						+ source);
	}

}
