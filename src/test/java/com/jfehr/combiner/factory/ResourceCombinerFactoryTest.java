package com.jfehr.combiner.factory;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.combiner.MockResourceCombiner;

public class ResourceCombinerFactoryTest {

	private ResourceCombinerFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new ResourceCombinerFactory();
	}
	@Test
	public void testHappyPath() {
		assertTrue(fixture.buildObject("MockResourceCombiner") instanceof MockResourceCombiner);
	}

}
