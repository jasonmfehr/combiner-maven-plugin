package com.github.jasonmfehr.combiner.factory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.jasonmfehr.combiner.combiner.ResourceCombiner;

public class ResourceCombinerFactoryTest {

	private static final String EXPECTED_DEFAULT_PACKAGE = "com.github.jasonmfehr.combiner.combiner";
	
	private ResourceCombinerFactory fixture;
	
	@Before
	public void setUp() {
		fixture = new ResourceCombinerFactory();
	}

	@Test
	public void testDefaultPackage() {
		assertThat(fixture.getDefaultPackage(), equalTo(EXPECTED_DEFAULT_PACKAGE));
	}
	
	@Test
	public void testObjectInterface() {
		assertEquals(ResourceCombiner.class, fixture.getObjectClass());
	}

}
