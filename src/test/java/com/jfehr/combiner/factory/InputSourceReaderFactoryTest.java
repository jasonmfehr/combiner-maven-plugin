package com.jfehr.combiner.factory;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.input.MockInputSourceReader;
import com.jfehr.combiner.logging.ParameterizedLogger;

public class InputSourceReaderFactoryTest {

	@Mock private ParameterizedLogger mockLogger;
	private InputSourceReaderFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new InputSourceReaderFactory(mockLogger);
	}
	@Test
	public void testHappyPath() {
		assertTrue(fixture.buildObject("MockInputSourceReader") instanceof MockInputSourceReader);
	}
	
}
