package com.jfehr.tojs.parser;

import static org.junit.Assert.*;

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
		assertEquals("this string has no newlines", fixture.parse("this string has no newlines"));
	}
	
	@Test
	public void testStripLineFeedsOnly() {
		assertEquals("this string has line feed characters", fixture.parse("this string has\n line feed characters"));
	}
	
	@Test
	public void testStripCarriageReturnsOnly() {
		assertEquals("this string has carriage return characters", fixture.parse("this string has\r carriage return characters"));
	}
	
	@Test
	public void testStripCRLF() {
		assertEquals("this string has both carriage return and line feed characters", fixture.parse("this string has\r both\n carriage return\r\n and line feed characters"));
	}
	
	@Test(expected=NullPointerException.class)
	public void testNull() {
		fixture.parse(null);
	}

}
