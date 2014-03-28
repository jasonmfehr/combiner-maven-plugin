package com.jfehr.tojs.logging;

import static org.junit.Assert.*;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ParameterizedLoggerTest {

	private static final String TEST_CONTENT = "this is the test logged content";
	private static final String TEST_PARAMETERIZED_CONTENT = "this is [{0}] content";
	private static final String TEST_PARAM_1 = "parameterized";
	private static final String TEST_EXPECTED_PARAMETERIZED_CONTENT = "this is [" + TEST_PARAM_1 + "] content";
	
	
	private ParameterizedLogger fixture;
	@Mock private Log mockLogger;
	@Mock private Exception mockException;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		fixture = new ParameterizedLogger(mockLogger);
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
	public void testDebugContentOnlyLogLevel() {
		fixture.log(LoggerLevel.DEBUG, TEST_CONTENT);
		Mockito.verify(mockLogger).debug(TEST_CONTENT);
	}
	
	@Test
	public void testDebugContentException() {
		fixture.debug(TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).debug(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testDebugContentExceptionLogLevel() {
		fixture.log(LoggerLevel.DEBUG, TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).debug(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testDebugExceptionOnly() {
		fixture.debug(mockException);
		Mockito.verify(mockLogger).debug(mockException);
	}
	
	@Test
	public void testDebugExceptionOnlyLogLevel() {
		fixture.log(LoggerLevel.DEBUG, mockException);
		Mockito.verify(mockLogger).debug(mockException);
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
	public void testDebugParameterizedContentOnlyLogLevel() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(true);
		fixture.logWithParams(LoggerLevel.DEBUG, TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).debug(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testDebugParameterizedContentExceptionEnabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(true);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, mockException, TEST_PARAM_1);
		Mockito.verify(mockLogger).debug(TEST_EXPECTED_PARAMETERIZED_CONTENT, mockException);
	}
	
	@Test
	public void testDebugParameterizedContentExceptionDisabled() {
		Mockito.when(mockLogger.isDebugEnabled()).thenReturn(false);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, mockException, TEST_PARAM_1);
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
	public void testInfoContentOnlyLogLevel() {
		fixture.log(LoggerLevel.INFO, TEST_CONTENT);
		Mockito.verify(mockLogger).info(TEST_CONTENT);
	}
	
	@Test
	public void testInfoContentException() {
		fixture.info(TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).info(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testInfoContentExceptionLogLevel() {
		fixture.log(LoggerLevel.INFO, TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).info(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testInfoExceptionOnly() {
		fixture.info(mockException);
		Mockito.verify(mockLogger).info(mockException);
	}
	
	@Test
	public void testInfoExceptionOnlyLogLevel() {
		fixture.log(LoggerLevel.INFO, mockException);
		Mockito.verify(mockLogger).info(mockException);
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
	
	@Test
	public void testInfoParameterizedContentLogLevel() {
		Mockito.when(mockLogger.isInfoEnabled()).thenReturn(true);
		fixture.logWithParams(LoggerLevel.INFO, TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).info(TEST_EXPECTED_PARAMETERIZED_CONTENT);
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
	public void testWarnContentOnlyLogLevel() {
		fixture.log(LoggerLevel.WARN, TEST_CONTENT);
		Mockito.verify(mockLogger).warn(TEST_CONTENT);
	}
	
	@Test
	public void testWarnContentException() {
		fixture.warn(TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).warn(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testWarnContentExceptionLogLevel() {
		fixture.log(LoggerLevel.WARN, TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).warn(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testWarnExceptionOnly() {
		fixture.warn(mockException);
		Mockito.verify(mockLogger).warn(mockException);
	}
	
	@Test
	public void testWarnExceptionOnlyLogLevel() {
		fixture.log(LoggerLevel.WARN, mockException);
		Mockito.verify(mockLogger).warn(mockException);
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
	
	@Test
	public void testWarnParameterizedContentLogLevel() {
		Mockito.when(mockLogger.isWarnEnabled()).thenReturn(true);
		fixture.logWithParams(LoggerLevel.WARN, TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).warn(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	//=== END INFO LEVEL TESTS ===\\
	
	//=== BEGIN Exception LEVEL TESTS ===\\
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
	public void testErrorContentOnlyLogLevel() {
		fixture.log(LoggerLevel.ERROR, TEST_CONTENT);
		Mockito.verify(mockLogger).error(TEST_CONTENT);
	}
	
	@Test
	public void testErrorContentException() {
		fixture.error(TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).error(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testErrorContentExceptionLogLevel() {
		fixture.log(LoggerLevel.ERROR, TEST_CONTENT, mockException);
		Mockito.verify(mockLogger).error(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testErrorExceptionOnly() {
		fixture.error(mockException);
		Mockito.verify(mockLogger).error(mockException);
	}
	
	@Test
	public void testErrorExceptionOnlyLogLevel() {
		fixture.log(LoggerLevel.ERROR, mockException);
		Mockito.verify(mockLogger).error(mockException);
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
	
	@Test
	public void testErrorParameterizedContentLogLevel() {
		Mockito.when(mockLogger.isErrorEnabled()).thenReturn(true);
		fixture.logWithParams(LoggerLevel.ERROR, TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		Mockito.verify(mockLogger).error(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	//=== END ERROR LEVEL TESTS ===\\
}
