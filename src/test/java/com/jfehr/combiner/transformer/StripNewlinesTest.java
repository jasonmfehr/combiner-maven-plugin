package com.jfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.logging.ParameterizedLogger;


public class StripNewlinesTest {
	private static final String TEST_KEY = "testKey";
	
	@Mock private ParameterizedLogger mockLogger;
	private StripNewlines fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new StripNewlines(mockLogger);
	}
	
	@Test
	public void testNoNewlines() {
		assertEquals("this string has no newlines", fixture.doTransform(TEST_KEY, "this string has no newlines"));
	}
	
	@Test
	public void testStripLineFeedsOnly() {
		assertEquals("this string has line feed characters", fixture.doTransform(TEST_KEY, "this string has\n line feed characters"));
	}
	
	@Test
	public void testStripCarriageReturnsOnly() {
		assertEquals("this string has carriage return characters", fixture.doTransform(TEST_KEY, "this string has\r carriage return characters"));
	}
	
	@Test
	public void testStripCRLF() {
		assertEquals("this string has both carriage return and line feed characters", fixture.doTransform(TEST_KEY, "this string has\r both\n carriage return\r\n and line feed characters"));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNull() {
		fixture.doTransform(TEST_KEY, null);
	}

}
