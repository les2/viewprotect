package com.upthescala.viewprotect.basic;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.upthescala.viewprotect.ComponentAttribute;

import static com.upthescala.viewprotect.basic.BasicTestSupport.array;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import static org.easymock.EasyMock.*;

public class BasicComponentAttributeSourceTest {

	private Map<String, ComponentAttribute> attributeMap;

	private Object[] mocks;

	@BeforeMethod
	@SuppressWarnings("unchecked")
	public void setUp() {

		attributeMap = createMock("attributeMap", Map.class);

		mocks = array(attributeMap);

		reset(attributeMap);
	}

	@Test
	public void shouldReadThroughToBackingMap() {

		class MockComponentAttribute implements ComponentAttribute {
			public boolean isSatisfiedBy(final Set<String> grantedRoles) {
				return false;
			}
		}

		BasicComponentAttributeSource source = new BasicComponentAttributeSource();

		source.setComponentAttributeMap(attributeMap);

		MockComponentAttribute expectedAttribute = new MockComponentAttribute();

		expect(attributeMap.get("some.id")).andReturn(expectedAttribute);

		expect(attributeMap.get("another.id")).andReturn(null);

		replay(mocks);

		assertSame(expectedAttribute, source.getAttribute("some.id"));
		assertNull(source.getAttribute("another.id"));

		verify(mocks);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void shouldThrowExceptionIfBackingMapIsNull() {
		new BasicComponentAttributeSource().setComponentAttributeMap(null);
		assert (false) : "unreachable code!";
	}
}
