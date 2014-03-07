package com.jfehr.tojs.logging;

import static org.junit.Assert.*;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ParameterizedLoggerTest {

	private static final String TEST_CONTENT = "this is the test logged content";
	private static final String TEST_PARAMETERIZED_CONTENT = "this is [{0}] content";
	private static final String TEST_PARAM_1 = "parameterized";
	private static final String TEST_EXPECTED_PARAMETERIZED_CONTENT = "this is [" + TEST_PARAM_1 + "] content";
	
	
	private ParameterizedLogger fixture;
	private Log mockLogger;
	private Exception testException;;
	
	@Before
	public void setUp() {
		mockLogger = Mockito.mock(Log.class);
		fixture = new ParameterizedLogger(mockLogger);
		testException = new Exception();
	}
	
	//=== BEGIN DEBUG LEVEL TESTS ===\\
	@Test
	public void testIsDebugEnabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(true);
		assertTrue(fixture.isDebugEnabled());
		Mockito.verify(mockLogger).isDebugEnabled();
	}
	
	@Test
	public void testDebugContentOnly() {
		fixture.debug(TEST_CONTENT);
		Mockito.verify(mockLogger).debug(TEST_CONTENT);
	}
	
	@Test
	public void testDebugContentError() {
		fixture.debug(TEST_CONTENT, testException);
		Mockito.verify(mockLogger).debug(TEST_CONTENT, testException);
	}
	
	@Test
	public void testDebugErrorOnly() {
		fixture.debug(testException);
		Mockito.verify(mockLogger).debug(testException);
	}
	
	@Test
	public void testDebugParameterizedContentOnlyEnabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(true);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).debug(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testDebugParameterizedContentOnlyDisabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(false);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger, Mockito.never()).debug(Mockito.anyString());
	}
	
	@Test
	public void testDebugParameterizedContentErrorEnabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(true);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, testException, TEST_PARAM_1);
		Mockito.verify(mockLogger).debug(TEST_EXPECTED_PARAMETERIZED_CONTENT, testException);
	}
	
	@Test
	public void testDebugParameterizedContentErrorDisabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(false);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, testException, TEST_PARAM_1);
		Mockito.verify(mockLogger, Mockito.never()).debug(Mockito.anyString());
	}
	//=== END DEBUG LEVEL TESTS ===\\
	
	//=== BEGIN INFO LEVEL TESTS ===\\
	@Test
	public void testIsInfoEnabled() {
		Mockito.when(mockLogger.isInfoEnabled()).thenReturn(true);
		assertTrue(fixture.isInfoEnabled());
		Mockito.verify(mockLogger).isInfoEnabled();
	}
	
	@Test
	public void testInfoContentOnly() {
		fixture.info(TEST_CONTENT);
		Mockito.verify(mockLogger).info(TEST_CONTENT);
	}
	
	@Test
	public void testInfoContentError() {
		fixture.info(TEST_CONTENT, testException);
		Mockito.verify(mockLogger).info(TEST_CONTENT, testException);
	}
	
	@Test
	public void testInfoErrorOnly() {
		fixture.info(testException);
		Mockito.verify(mockLogger).info(testException);
	}
	
	@Test
	public void testInfoParameterizedContentEnabled() {
		Mockito.when(mockLogger.isInfoEnabled()).thenReturn(true);
		fixture.infoWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).info(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testInfoParameterizedContentDisabled() {
		Mockito.when(mockLogger.isInfoEnabled()).thenReturn(false);
		fixture.infoWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger, Mockito.never()).info(Mockito.anyString());
	}
	//=== END INFO LEVEL TESTS ===\\
	
	//=== BEGIN WARN LEVEL TESTS ===\\
	@Test
	public void testIsWarnEnabled() {
		Mockito.when(mockLogger.isWarnEnabled()).thenReturn(true);
		assertTrue(fixture.isWarnEnabled());
		Mockito.verify(mockLogger).isWarnEnabled();
	}
	
	@Test
	public void testWarnContentOnly() {
		fixture.warn(TEST_CONTENT);
		Mockito.verify(mockLogger).warn(TEST_CONTENT);
	}
	
	@Test
	public void testWarnContentError() {
		fixture.warn(TEST_CONTENT, testException);
		Mockito.verify(mockLogger).warn(TEST_CONTENT, testException);
	}
	
	@Test
	public void testWarnErrorOnly() {
		fixture.warn(testException);
		Mockito.verify(mockLogger).warn(testException);
	}
	
	@Test
	public void testWarnParameterizedContentEnabled() {
		Mockito.when(mockLogger.isWarnEnabled()).thenReturn(true);
		fixture.warnWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).warn(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testWarnParameterizedContentDisabled() {
		Mockito.when(mockLogger.isWarnEnabled()).thenReturn(false);
		fixture.warnWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger, Mockito.never()).warn(Mockito.anyString());
	}
	//=== END INFO LEVEL TESTS ===\\
	
	//=== BEGIN ERROR LEVEL TESTS ===\\
	@Test
	public void testIsErrorEnabled() {
		Mockito.when(mockLogger.isErrorEnabled()).thenReturn(true);
		assertTrue(fixture.isErrorEnabled());
		Mockito.verify(mockLogger).isErrorEnabled();
	}
	
	@Test
	public void testErrorContentOnly() {
		fixture.error(TEST_CONTENT);
		Mockito.verify(mockLogger).error(TEST_CONTENT);
	}
	
	@Test
	public void testErrorContentError() {
		fixture.error(TEST_CONTENT, testException);
		Mockito.verify(mockLogger).error(TEST_CONTENT, testException);
	}
	
	@Test
	public void testErrorErrorOnly() {
		fixture.error(testException);
		Mockito.verify(mockLogger).error(testException);
	}
	
	@Test
	public void testErrorParameterizedContentEnabled() {
		Mockito.when(mockLogger.isErrorEnabled()).thenReturn(true);
		fixture.errorWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).error(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testErrorParameterizedContentDisabled() {
		Mockito.when(mockLogger.isErrorEnabled()).thenReturn(false);
		fixture.errorWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger, Mockito.never()).error(Mockito.anyString());
	}
	//=== END ERROR LEVEL TESTS ===\\
}
