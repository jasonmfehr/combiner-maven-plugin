package com.github.jasonmfehr.combiner.combiner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

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
		
		assertThat(fixture.combine(inputResource, new HashMap<String, String>(), null), equalTo("onetwo"));
	}
	
	@Test
	public void testNewlinesSpecified() {
		Map<String, String> inputResource = new LinkedHashMap<String, String>();
		Map<String, String> settings = new HashMap<String, String>();
		
		inputResource.put("1", "one");
		inputResource.put("2", "two");
		
		settings.put(NUMBER_NEWLINES_SETTING_KEY, "2");
		
		assertThat(fixture.combine(inputResource, settings, null), equalTo("one" + NEWLINE_CHAR + NEWLINE_CHAR + "two" + NEWLINE_CHAR + NEWLINE_CHAR));
	}
	
	@Test(expected=NumberFormatException.class)
	public void testNonNumericNewline() {
		Map<String, String> settings = new HashMap<String, String>();
		
		settings.put(NUMBER_NEWLINES_SETTING_KEY, "foo");
		fixture.combine(null, settings, null);
	}

}
