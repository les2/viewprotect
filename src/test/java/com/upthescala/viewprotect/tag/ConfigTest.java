package com.upthescala.viewprotect.tag;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ConfigTest {

	@DataProvider(name = "loadProps")
	public Object[][] loadPropsDataProvider() {
		return new Object[][]{
				{"viewprotect.test.properties", "sillyName"},
				{"nonExistentFile.properties",
						Config.DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME},
				{"viewprotect.test2.properties",
						Config.DEFAULT_VIEW_AUTH_SERVICE_BEAN_NAME}};
	}

	@Test(dataProvider = "loadProps")
	public void shouldLoadViewAuthServiceBeanNameFromPropertiesFile(
			final String fileName, final String beanName) {
		Assert.assertEquals(Config
				.getViewAuthServiceBeanName("viewprotect.test.properties"),
				"sillyName");
	}
}
