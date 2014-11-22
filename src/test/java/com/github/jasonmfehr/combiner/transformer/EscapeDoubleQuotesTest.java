package com.github.jasonmfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EscapeDoubleQuotesTest {

	private static final String TEST_KEY = "testKey";
	
	private EscapeDoubleQuotes fixture;
	
	@Before
	public void setUp() {
		fixture = new EscapeDoubleQuotes();
	}
	
	@Test
	public void testEscapeQuotes() {
		assertEquals("this \\\"word\\\" is in escaped double quotes", fixture.transform(TEST_KEY, "this \"word\" is in escaped double quotes", null, null));
	}
	
	@Test
	public void testNoEscapeQuotes() {
		assertEquals("this word is not in double quotes", fixture.transform(TEST_KEY, "this word is not in double quotes", null, null));
	}
	
	@Test
	public void testSkipAlreadyEscapedQuotes() {
		assertEquals("this \\\"word\\\" is not double escaped", fixture.transform(TEST_KEY, "this \\\"word\\\" is not double escaped", null, null));
	}

}
