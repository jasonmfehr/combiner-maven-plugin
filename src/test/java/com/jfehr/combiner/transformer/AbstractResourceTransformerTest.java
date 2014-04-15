package com.jfehr.combiner.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class AbstractResourceTransformerTest {

	@Mock Map<String, String> mockResoureContents;
	@Mock Map<String, String> mockSettings;
	@Mock MavenProject mockMavenProject;
	private AbstractResourceTransformer fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		
		fixture = new AbstractResourceTransformer() {
			@Override
			protected String doTransform(String resourceKey, String resourceValue) {
				return resourceKey + ":" + resourceValue;
			}
		};
	}
	
	@Test
	public void testHappyPath() {
		final Map<String, String> resourceContents = new HashMap<String, String>();
		final Map<String, String> actual;
		
		resourceContents.put("one", "onevalue");
		resourceContents.put("two", "twovalue");
		
		actual = fixture.transform(resourceContents, null, null);
		
		assertNotNull(actual);
		assertEquals(2, actual.size());
		
		assertTrue(actual.containsKey("one"));
		assertEquals("one:onevalue", actual.get("one"));
		
		assertTrue(actual.containsKey("two"));
		assertEquals("two:twovalue", actual.get("two"));
	}
	
	@Test
	public void testGetters() {
		fixture.transform(mockResoureContents, mockSettings, mockMavenProject);
		
		assertEquals(mockResoureContents, fixture.getResourceContents());
		assertEquals(mockSettings, fixture.getSettings());
		assertEquals(mockMavenProject, fixture.getMavenProject());
	}

}
