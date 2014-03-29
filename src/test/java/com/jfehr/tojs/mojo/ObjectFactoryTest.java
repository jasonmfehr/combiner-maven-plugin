package com.jfehr.tojs.mojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jfehr.tojs.exception.NotAssignableException;
import com.jfehr.tojs.exception.ObjectInstantiationException;
import com.jfehr.tojs.logging.ParameterizedLogger;
import com.jfehr.tojs.testutil.MockNonDefaultPackage;

public class ObjectFactoryTest {

	private static final String DEFAULT_PACKAGE_NAME = ObjectFactoryTest.class.getPackage().getName();
	private static final String MOCK_DEFAULT_PKG = "ObjectFactoryTest$MockImplementsIface";
	private static final String MOCK_LOGGER_CTOR = "ObjectFactoryTest$MockImplementsIfaceWithLogger";
	private static final String MOCK_INVALID = "ObjectFactoryTest$MockInvalid";
	private static final String MOCK_INHERITS_IFACE = "ObjectFactoryTest$MockInheritsIface";
	private static final String MOCK_NON_DEFAULT_PKG = "com.jfehr.tojs.testutil.MockNonDefaultPackage";
	private static final String MOCK_IFACE = "ObjectFactoryTest$MockSubIface";
	private static final String MOCK_NONEXISTANT = "NonExistant";
	
	@Mock private ParameterizedLogger mockLogger;
	private ObjectFactory fixture;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		fixture = new ObjectFactory(mockLogger);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullClassName() {
		this.runTest(null, null);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullDefaultPackage() {
		this.runTest(MOCK_DEFAULT_PKG, null);
	}
	
	@Test(expected=NotAssignableException.class)
	public void testNullImplementsInterface() {
		fixture.buildObject(MockImplementsIface.class.getName(), MOCK_DEFAULT_PKG, null);
	}
	
	@Test
	public void testFullPackage() {
		assertTrue(this.runTest(MOCK_NON_DEFAULT_PKG, "test") instanceof MockNonDefaultPackage);
	}
	
	@Test
	public void testDefaultPackage() {
		assertTrue(this.runTest(MOCK_DEFAULT_PKG) instanceof MockImplementsIface);
	}
	
	@Test
	public void testDefaultPackageEndsWithDot() {
		assertTrue(this.runTest(MOCK_DEFAULT_PKG, DEFAULT_PACKAGE_NAME + ".") instanceof MockImplementsIface);
	}
	
	@Test
	public void testLoggerConstructor() {
		final MockBaseInterface actual = (MockBaseInterface)this.runTest(MOCK_LOGGER_CTOR);
		
		assertTrue(actual instanceof MockImplementsIfaceWithLogger);
		assertEquals(mockLogger, ((MockImplementsIfaceWithLogger)actual).getLogger());
	}
	
	@Test
	public void testParserInheritsIface() {
		assertTrue(this.runTest(MOCK_INHERITS_IFACE) instanceof MockInheritsIface);
	}
	
	@Test(expected=ObjectInstantiationException.class)
	public void testParserIface() {
		this.runTest(MOCK_IFACE);
	}
	
	@Test(expected=NotAssignableException.class)
	public void testParserInvalid() {
		this.runTest(MOCK_INVALID);
	}
	
	@Test(expected=ObjectInstantiationException.class)
	public void testParserNonExistant() {
		this.runTest(MOCK_NONEXISTANT);
	}
	
	private Object runTest(final String className, final String defaultPackage) {
		return fixture.buildObject(className, defaultPackage, MockBaseInterface.class);
	}
	
	private Object runTest(final String className) {
		return this.runTest(className, DEFAULT_PACKAGE_NAME);
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
	
	public static interface MockSubIface extends MockBaseInterface {}
	
	public static class MockInheritsIface implements MockSubIface {
		public String doNothing(final String someInput) { return null; }
	}

}
