package com.jfehr.combiner.factory;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.input.MockInputSourceReader;

public class InputSourceReaderFactoryTest {

	private InputSourceReaderFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new InputSourceReaderFactory();
	}
	@Test
	public void testHappyPath() {
		assertTrue(fixture.buildObject("MockInputSourceReader") instanceof MockInputSourceReader);
	}
	
}
