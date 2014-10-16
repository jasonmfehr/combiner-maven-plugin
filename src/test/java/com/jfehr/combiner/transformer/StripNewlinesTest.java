package com.jfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StripNewlinesTest {
	private static final String TEST_KEY = "testKey";
	
	private StripNewlines fixture;
	
	@Before
	public void setUp() {
		fixture = new StripNewlines();
	}
	
	@Test
	public void testNoNewlines() {
		assertEquals("this string has no newlines", fixture.transform(TEST_KEY, "this string has no newlines", null, null));
	}
	
	@Test
	public void testStripLineFeedsOnly() {
		assertEquals("this string has line feed characters", fixture.transform(TEST_KEY, "this string has\n line feed characters", null, null));
	}
	
	@Test
	public void testStripCarriageReturnsOnly() {
		assertEquals("this string has carriage return characters", fixture.transform(TEST_KEY, "this string has\r carriage return characters", null, null));
	}
	
	@Test
	public void testStripCRLF() {
		assertEquals("this string has both carriage return and line feed characters", fixture.transform(TEST_KEY, "this string has\r both\n carriage return\r\n and line feed characters\n", null, null));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNull() {
		fixture.transform(TEST_KEY, null, null, null);
	}

}
