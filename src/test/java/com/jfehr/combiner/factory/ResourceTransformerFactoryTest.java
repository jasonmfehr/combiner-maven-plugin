package com.jfehr.combiner.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.transformer.MockResourceTransformer;
import com.jfehr.combiner.transformer.ResourceTransformer;

public class ResourceTransformerFactoryTest {

	private ResourceTransformerFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new ResourceTransformerFactory();
	}
	
	@Test
	public void testHappyPath() {
		assertTrue(fixture.buildObject("MockResourceTransformer") instanceof MockResourceTransformer);
	}
	
	@Test
	public void testHappyPathList() {
		final List<String> inputList = new ArrayList<String>();
		final List<ResourceTransformer> actualList;
		
		inputList.add("MockResourceTransformer");
		inputList.add("MockResourceTransformer");
		
		actualList = fixture.buildObjectList(inputList);
		
		assertNotNull(actualList);
		assertEquals(2, actualList.size());
		assertTrue(actualList.get(0) instanceof MockResourceTransformer);
		assertTrue(actualList.get(1) instanceof MockResourceTransformer);
	}

}
