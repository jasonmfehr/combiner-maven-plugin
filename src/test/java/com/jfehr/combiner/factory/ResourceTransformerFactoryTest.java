package com.jfehr.combiner.factory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
		assertThat(fixture.getDefaultPackage(), equalTo(EXPECTED_DEFAULT_PACKAGE));
	}
	
	@Test
	public void testObjectInterface() {
		assertEquals(ResourceTransformer.class, fixture.getObjectClass());
	}

}
