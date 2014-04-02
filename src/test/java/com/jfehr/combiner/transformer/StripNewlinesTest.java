package com.jfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class StripNewlinesTest {

	private StripNewlines fixture;
	
	@Before
	public void setUp() {
		fixture = new StripNewlines();
	}
	
	@After
	public void tearDown() {
		fixture = null;
	}
	
	@Test
	public void testNoNewlines() {
		assertEquals("this string has no newlines", fixture.transform("this string has no newlines", null));
	}
	
	@Test
	public void testStripLineFeedsOnly() {
		assertEquals("this string has line feed characters", fixture.transform("this string has\n line feed characters", null));
	}
	
	@Test
	public void testStripCarriageReturnsOnly() {
		assertEquals("this string has carriage return characters", fixture.transform("this string has\r carriage return characters", null));
	}
	
	@Test
	public void testStripCRLF() {
		assertEquals("this string has both carriage return and line feed characters", fixture.transform("this string has\r both\n carriage return\r\n and line feed characters", null));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNull() {
		fixture.transform(null, null);
	}

}
