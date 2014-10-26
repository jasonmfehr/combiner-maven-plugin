package com.jfehr.combiner.combiner;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AppendingCombinerTest {

	private static final String NEWLINE_CHAR = String.format("%n");
	private static final String NUMBER_NEWLINES_SETTING_KEY = "appendingCombinerNewlines";
	
	private AppendingCombiner fixture;
	@Before
	public void setUp() {
		fixture = new AppendingCombiner();
	}
	
	@Test
	public void testDefaultNewlines() {
		Map<String, String> inputResource = new LinkedHashMap<String, String>();

		inputResource.put("1", "one");
		inputResource.put("2", "two");
		
		assertEquals("onetwo", fixture.combine(inputResource, new HashMap<String, String>(), null));
	}
	
	@Test
	public void testNewlinesSpecified() {
		Map<String, String> inputResource = new LinkedHashMap<String, String>();
		Map<String, String> settings = new HashMap<String, String>();
		
		inputResource.put("1", "one");
		inputResource.put("2", "two");
		
		settings.put(NUMBER_NEWLINES_SETTING_KEY, "2");
		
		assertEquals("one" + NEWLINE_CHAR + NEWLINE_CHAR + "two" + NEWLINE_CHAR + NEWLINE_CHAR, fixture.combine(inputResource, settings, null));
	}
	
	@Test(expected=NumberFormatException.class)
	public void testNonNumericNewline() {
		Map<String, String> settings = new HashMap<String, String>();
		
		settings.put(NUMBER_NEWLINES_SETTING_KEY, "foo");
		fixture.combine(null, settings, null);
	}

}
