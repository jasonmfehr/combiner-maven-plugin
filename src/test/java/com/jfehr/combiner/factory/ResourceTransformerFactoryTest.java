package com.jfehr.combiner.factory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.transformer.ResourceTransformer;

public class ResourceTransformerFactoryTest {

	private static final String EXPECTED_DEFAULT_PACKAGE = "com.jfehr.combiner.transformer";
	
	private ResourceTransformerFactory fixture;
	
	@Before
	public void setUp() {
		fixture = new ResourceTransformerFactory();
	}
	
	@Test
	public void testDefaultPackage() {
		assertEquals(EXPECTED_DEFAULT_PACKAGE, fixture.getDefaultPackage());
	}
	
	@Test
	public void testObjectInterface() {
		assertEquals(ResourceTransformer.class, fixture.getObjectClass());
	}

}
