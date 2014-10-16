package com.jfehr.combiner.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.combiner.ResourceCombiner;

public class ResourceCombinerFactoryTest {

	private static final String EXPECTED_DEFAULT_PACKAGE = "com.jfehr.combiner.combiner";
	
	private ResourceCombinerFactory fixture;
	
	@Before
	public void setUp() {
		fixture = new ResourceCombinerFactory();
	}

	@Test
	public void testDefaultPackage() {
		assertEquals(EXPECTED_DEFAULT_PACKAGE, fixture.getDefaultPackage());
	}
	
	@Test
	public void testObjectInterface() {
		assertEquals(ResourceCombiner.class, fixture.getObjectClass());
	}

}
