package com.jfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.logging.ParameterizedLogger;

public class EscapeDoubleQuotesTest {

	private static final String TEST_KEY = "testKey";
	
	@Mock private ParameterizedLogger mockLogger;
	private EscapeDoubleQuotes fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new EscapeDoubleQuotes(mockLogger);
	}
	
	@Test
	public void testEscapeQuotes() {
		assertEquals("this \\\"word\\\" is in escaped double quotes", fixture.doTransform(TEST_KEY, "this \"word\" is in escaped double quotes"));
	}
	
	@Test
	public void testNoEscapeQuotes() {
		assertEquals("this word is not in double quotes", fixture.doTransform(TEST_KEY, "this word is not in double quotes"));
	}
	
	@Test
	public void testSkipAlreadyEscapedQuotes() {
		assertEquals("this \\\"word\\\" is not double escaped", fixture.doTransform(TEST_KEY, "this \\\"word\\\" is not double escaped"));
	}

}
