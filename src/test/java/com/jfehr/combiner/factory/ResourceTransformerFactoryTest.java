package com.jfehr.combiner.factory;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.transformer.MockResourceTransformer;

public class ResourceTransformerFactoryTest {

	@Mock private ParameterizedLogger mockLogger;
	private ResourceTransformerFactory fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		fixture = new ResourceTransformerFactory(mockLogger);
	}
	@Test
	public void testHappyPath() {
		System.out.println(MockResourceTransformer.class.getName());
		assertTrue(fixture.buildObject("MockResourceTransformer") instanceof MockResourceTransformer);
	}

}
