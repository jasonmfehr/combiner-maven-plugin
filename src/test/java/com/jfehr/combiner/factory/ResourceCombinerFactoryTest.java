package com.jfehr.combiner.factory;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.combiner.MockResourceCombiner;
import com.jfehr.combiner.logging.ParameterizedLogger;

public class ResourceCombinerFactoryTest {

	@Mock private ParameterizedLogger mockLogger;
	private ResourceCombinerFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new ResourceCombinerFactory(mockLogger);
	}
	@Test
	public void testHappyPath() {
		assertTrue(fixture.buildObject("MockResourceCombiner") instanceof MockResourceCombiner);
	}

}
