package com.jfehr.combiner.factory;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.output.MockOutputSourceWriter;

public class OutputSourceWriterFactoryTest {

	private OutputSourceWriterFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new OutputSourceWriterFactory();
	}
	@Test
	public void testHappyPath() {
		assertTrue(fixture.buildObject("MockOutputSourceWriter") instanceof MockOutputSourceWriter);
	}

}
