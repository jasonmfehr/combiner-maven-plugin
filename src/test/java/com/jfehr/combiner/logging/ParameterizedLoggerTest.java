package com.jfehr.combiner.logging;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

//TODO could this be implemented with JUnit's paramaterized runner
@RunWith(MockitoJUnitRunner.class)
public class ParameterizedLoggerTest {

	private static final String TEST_CONTENT = "this is the test logged content";
	private static final String TEST_PARAMETERIZED_CONTENT = "this is [{0}] content";
	private static final String TEST_PARAM_1 = "parameterized";
	private static final String TEST_EXPECTED_PARAMETERIZED_CONTENT = "this is [" + TEST_PARAM_1 + "] content";
	
	@Mock private Log mockLogger;
	@Mock private Exception mockException;
	
	@InjectMocks private ParameterizedLogger fixture;
	
	@Before
	public void setUp() {
		fixture = new ParameterizedLogger(mockLogger);
	}
	
	//=== BEGIN DEBUG LEVEL TESTS ===\\
	@Test
	public void testIsDebugEnabled() {
		when(mockLogger.isDebugEnabled()).thenReturn(true);
		assertTrue(fixture.isDebugEnabled());
		verify(mockLogger).isDebugEnabled();
	}
	
	@Test
	public void testDebugContentOnly() {
		fixture.debug(TEST_CONTENT);
		verify(mockLogger).debug(TEST_CONTENT);
	}
	
	@Test
	public void testDebugContentException() {
		fixture.debug(TEST_CONTENT, mockException);
		verify(mockLogger).debug(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testDebugExceptionOnly() {
		fixture.debug(mockException);
		verify(mockLogger).debug(mockException);
	}
	
	@Test
	public void testDebugParameterizedContentOnlyEnabled() {
		when(mockLogger.isDebugEnabled()).thenReturn(true);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger).debug(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testDebugParameterizedContentOnlyDisabled() {
		when(mockLogger.isDebugEnabled()).thenReturn(false);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger, never()).debug(toString());
	}
	
	@Test
	public void testDebugParameterizedContentExceptionEnabled() {
		when(mockLogger.isDebugEnabled()).thenReturn(true);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, mockException, TEST_PARAM_1);
		verify(mockLogger).debug(TEST_EXPECTED_PARAMETERIZED_CONTENT, mockException);
	}
	
	@Test
	public void testDebugParameterizedContentExceptionDisabled() {
		when(mockLogger.isDebugEnabled()).thenReturn(false);
		fixture.debugWithParams(TEST_PARAMETERIZED_CONTENT, mockException, TEST_PARAM_1);
		verify(mockLogger, never()).debug(toString());
	}
	//=== END DEBUG LEVEL TESTS ===\\
	
	//=== BEGIN INFO LEVEL TESTS ===\\
	@Test
	public void testIsInfoEnabled() {
		when(mockLogger.isInfoEnabled()).thenReturn(true);
		assertTrue(fixture.isInfoEnabled());
		verify(mockLogger).isInfoEnabled();
	}
	
	@Test
	public void testInfoContentOnly() {
		fixture.info(TEST_CONTENT);
		verify(mockLogger).info(TEST_CONTENT);
	}
	
	@Test
	public void testInfoContentException() {
		fixture.info(TEST_CONTENT, mockException);
		verify(mockLogger).info(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testInfoExceptionOnly() {
		fixture.info(mockException);
		verify(mockLogger).info(mockException);
	}
	
	@Test
	public void testInfoParameterizedContentEnabled() {
		when(mockLogger.isInfoEnabled()).thenReturn(true);
		fixture.infoWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger).info(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testInfoParameterizedContentDisabled() {
		when(mockLogger.isInfoEnabled()).thenReturn(false);
		fixture.infoWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger, never()).info(toString());
	}
	//=== END INFO LEVEL TESTS ===\\
	
	//=== BEGIN WARN LEVEL TESTS ===\\
	@Test
	public void testIsWarnEnabled() {
		when(mockLogger.isWarnEnabled()).thenReturn(true);
		assertTrue(fixture.isWarnEnabled());
		verify(mockLogger).isWarnEnabled();
	}
	
	@Test
	public void testWarnContentOnly() {
		fixture.warn(TEST_CONTENT);
		verify(mockLogger).warn(TEST_CONTENT);
	}
	
	@Test
	public void testWarnContentException() {
		fixture.warn(TEST_CONTENT, mockException);
		verify(mockLogger).warn(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testWarnExceptionOnly() {
		fixture.warn(mockException);
		verify(mockLogger).warn(mockException);
	}
	
	@Test
	public void testWarnParameterizedContentEnabled() {
		when(mockLogger.isWarnEnabled()).thenReturn(true);
		fixture.warnWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger).warn(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testWarnParameterizedContentDisabled() {
		when(mockLogger.isWarnEnabled()).thenReturn(false);
		fixture.warnWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger, never()).warn(toString());
	}
	//=== END INFO LEVEL TESTS ===\\
	
	//=== BEGIN Exception LEVEL TESTS ===\\
	@Test
	public void testIsErrorEnabled() {
		when(mockLogger.isErrorEnabled()).thenReturn(true);
		assertTrue(fixture.isErrorEnabled());
		verify(mockLogger).isErrorEnabled();
	}
	
	@Test
	public void testErrorContentOnly() {
		fixture.error(TEST_CONTENT);
		verify(mockLogger).error(TEST_CONTENT);
	}
	
	@Test
	public void testErrorContentException() {
		fixture.error(TEST_CONTENT, mockException);
		verify(mockLogger).error(TEST_CONTENT, mockException);
	}
	
	@Test
	public void testErrorExceptionOnly() {
		fixture.error(mockException);
		verify(mockLogger).error(mockException);
	}
	
	@Test
	public void testErrorParameterizedContentEnabled() {
		when(mockLogger.isErrorEnabled()).thenReturn(true);
		fixture.errorWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger).error(TEST_EXPECTED_PARAMETERIZED_CONTENT);
	}
	
	@Test
	public void testErrorParameterizedContentDisabled() {
		when(mockLogger.isErrorEnabled()).thenReturn(false);
		fixture.errorWithParams(TEST_PARAMETERIZED_CONTENT, TEST_PARAM_1);
		verify(mockLogger, never()).error(toString());
	}
	//=== END ERROR LEVEL TESTS ===\\
}
