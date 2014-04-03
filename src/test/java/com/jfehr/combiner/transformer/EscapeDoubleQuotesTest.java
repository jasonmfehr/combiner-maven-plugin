package com.jfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EscapeDoubleQuotesTest {

	private EscapeDoubleQuotes fixture;
	
	@Before
	public void setUp() {
		fixture = new EscapeDoubleQuotes();
	}
	
	@Test
	public void testEscapeQuotes() {
		assertEquals("this \\\"word\\\" is in escaped double quotes", fixture.transform("this \"word\" is in escaped double quotes", null));
	}
	
	@Test
	public void testNoEscapeQuotes() {
		assertEquals("this word is not in double quotes", fixture.transform("this word is not in double quotes", null));
	}
	
	@Test
	public void testSkipAlreadyEscapedQuotes() {
		assertEquals("this \\\"word\\\" is not double escaped", fixture.transform("this \\\"word\\\" is not double escaped", null));
	}

}