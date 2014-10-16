package com.jfehr.combiner.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.input.InputSourceReader;

public class InputSourceReaderFactoryTest {

	private static final String EXPECTED_DEFAULT_PACKAGE = "com.jfehr.combiner.input";
	
	private InputSourceReaderFactory fixture;
	
	@Before
	public void setUp() {
		fixture = new InputSourceReaderFactory();
	}
	
	@Test
	public void testDefaultPackage() {
		assertEquals(EXPECTED_DEFAULT_PACKAGE, fixture.getDefaultPackage());
	}
	
	@Test
	public void testObjectInterface() {
		assertEquals(InputSourceReader.class, fixture.getObjectClass());
	}
	
}
