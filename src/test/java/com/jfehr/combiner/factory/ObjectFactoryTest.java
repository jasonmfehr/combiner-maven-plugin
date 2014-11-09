package com.jfehr.combiner.factory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.combiner.logging.LogHolder;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.testutil.TestUtil;
import com.jfehr.tojs.exception.NotAssignableException;
import com.jfehr.tojs.exception.ObjectInstantiationException;

@RunWith(MockitoJUnitRunner.class)
public class ObjectFactoryTest {

	private static final String MOCK_PACKAGE = "com.jfehr.combiner.factory";
	
	private static final String MOCK_IMPLEMENTS_IFACE_CLASSNAME = "ObjectFactoryTest$MockImplementsIface";
	private static final String MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED = MOCK_PACKAGE + "." + MOCK_IMPLEMENTS_IFACE_CLASSNAME;
	
	private static final String MOCK_LOGGER_CTOR = "ObjectFactoryTest$MockImplementsIfaceWithLogger";
	private static final String MOCK_LOGGER_CTOR_FULLY_QUALIFIED = MOCK_PACKAGE + "." + "ObjectFactoryTest$MockImplementsIfaceWithLogger";
	
	private static final String MOCK_IFACE_NAME = "ObjectFactoryTest$MockBaseInterface";
	
	private static final String MOCK_NAME_DOES_NOT_IMPL_IFACE = "ObjectFactoryTest$MockInvalid";
	
	@Mock private PlexusContainer mockContainer;
	
	@InjectMocks private TestObjectFactoryFixture fixture;
	
	@Test(expected=NullPointerException.class)
	public void testNullClassOrRoleName() {
		fixture.buildObject(null);
		
		verifyZeroInteractions(mockContainer);
	}

	@Test
	public void testRetrieveFromPlexusContainer() throws ComponentLookupException {
		final MockImplementsIface expected = new MockImplementsIface();

		when(mockContainer.lookup(expected.getClass().getName())).thenReturn(expected);
		
		assertThat((MockImplementsIface)fixture.buildObject(MOCK_IMPLEMENTS_IFACE_CLASSNAME), sameInstance(expected));
		
		verify(mockContainer).lookup(expected.getClass().getName());
	}
	
	@Test
	public void testRetrieveFromPlexusContainerInvalid() throws ComponentLookupException {
		final MockInvalid plexusComponent = new MockInvalid();
		final MockBaseInterface actual;
		
		when(mockContainer.lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED)).thenReturn(plexusComponent);
		
		actual = fixture.buildObject(MOCK_IMPLEMENTS_IFACE_CLASSNAME);
		assertNotSame(plexusComponent, actual);
		assertThat(actual, CoreMatchers.instanceOf(MockImplementsIface.class));
		
		verify(mockContainer).lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED);
	}
	
	@Test
	public void testFullPackage() throws ComponentLookupException {
		when(mockContainer.lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED)).thenThrow(new ComponentLookupException("", "", ""));
		
		assertThat(fixture.buildObject(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED), instanceOf(MockBaseInterface.class));
		
		verify(mockContainer).lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED);
	}
	
	@Test
	public void testClassOnly() throws ComponentLookupException {
		when(mockContainer.lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED)).thenThrow(new ComponentLookupException("", "", ""));
		
		assertThat(fixture.buildObject(MOCK_IMPLEMENTS_IFACE_CLASSNAME), instanceOf(MockBaseInterface.class));
		
		verify(mockContainer).lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED);
	}
	
	@Test
	public void testLoggerConstructor() throws ComponentLookupException {
		final ParameterizedLogger mockLogger;
		final MockBaseInterface actual;
		
		when(mockContainer.lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED)).thenThrow(new ComponentLookupException("", "", ""));
		
		mockLogger = mock(ParameterizedLogger.class);
		TestUtil.setPrivateStaticField(LogHolder.class, "logger", mockLogger);
		
		actual = fixture.buildObject(MOCK_LOGGER_CTOR);
		
		//assertTrue(actual instanceof MockImplementsIfaceWithLogger);
		assertThat(actual, instanceOf(MockImplementsIfaceWithLogger.class));
		
		assertThat(((MockImplementsIfaceWithLogger)actual).getLogger(), sameInstance(mockLogger));
		
		verify(mockContainer).lookup(MOCK_LOGGER_CTOR_FULLY_QUALIFIED);
	}
	
	@Test(expected=ObjectInstantiationException.class)
	public void testInstantiateIface() throws ComponentLookupException {
		when(mockContainer.lookup(anyString())).thenThrow(new ComponentLookupException("", "", ""));
		
		fixture.buildObject(MOCK_IFACE_NAME);
	}
	
	@Test(expected=NotAssignableException.class)
	public void testDoeNotImplementIface() throws ComponentLookupException {
		when(mockContainer.lookup(anyString())).thenThrow(new ComponentLookupException("", "", ""));
		
		fixture.buildObject(MOCK_NAME_DOES_NOT_IMPL_IFACE);
	}
	
	@Test
	public void testListCreate() throws ComponentLookupException {
		final List<String> inputList = new ArrayList<String>();
		final MockImplementsIface expected = new MockImplementsIface();
		final List<MockBaseInterface> actualList;
		
		inputList.add("plexus.Component");
		inputList.add(MOCK_IMPLEMENTS_IFACE_CLASSNAME);
		
		when(mockContainer.lookup("plexus.Component")).thenReturn(expected);
		when(mockContainer.lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED)).thenThrow(new ComponentLookupException("", "", ""));
		
		actualList = fixture.buildObjectList(inputList);
		
		assertThat(actualList, notNullValue());
		assertThat(actualList.size(), equalTo(2));
		assertThat(actualList, CoreMatchers.hasItem(expected));
		assertThat(actualList.get(1), CoreMatchers.instanceOf(MockImplementsIface.class));
		
		verify(mockContainer).lookup("plexus.Component");
		verify(mockContainer).lookup(MOCK_IMPLEMENTS_IFACE_FULLY_QUALIFIED);
	}
	
	//=== test concrete class instances ===\\
	public static class TestObjectFactoryFixture extends ObjectFactory {
		@Override
		protected Class<?> getObjectClass() {
			return MockBaseInterface.class;
		}

		@Override
		protected String getDefaultPackage() {
			return MockBaseInterface.class.getPackage().getName();
		}
	}
	
	//=== Mock classes for testing ===\\
	public static interface MockBaseInterface {
		public String doNothing(final String someInput);
	}
	
	public static class MockImplementsIface implements MockBaseInterface {
		public String doNothing(final String someInput) { return null; }
	}
	
	public static class MockImplementsIfaceWithLogger implements MockBaseInterface {
		private final ParameterizedLogger logger;
		
		public MockImplementsIfaceWithLogger(final ParameterizedLogger logger) {
			this.logger = logger;
		}
		
		public ParameterizedLogger getLogger() {
			return this.logger;
		}
		
		public String doNothing(final String someInput) { return null; }
	}
	
	public static class MockInvalid {}

}
