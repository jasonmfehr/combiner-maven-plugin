package com.jfehr.combiner.pipeline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.combiner.mojo.Combination;

@RunWith(MockitoJUnitRunner.class)
public class CombinationDefaultsManagerTest {

	private static final String EXPECTED_DEFAULT_INPUT_SOURCE_READER = "FileInputSourceReader";
	private static final String EXPECTED_DEFAULT_OUTPUT_SOURCE_WRITER = "FileOutputSourceWriter";
	private static final String TEST_ENCODING = "test-encoding";
	private static final String TEST_INPUT_SOURCE_READER = "test-input-source-reader";
	private static final String TEST_OUTPUT_SOURCE_WRITER = "test-output-source-writer";

	@Mock private MavenProject mockMavenProject;
	@Mock private Properties mockProperties;
	@InjectMocks private CombinationDefaultsManager fixture;
	
	private Combination helperCombo;
	
	@Before
	public void setUp() {
		helperCombo = new Combination();
		when(mockMavenProject.getProperties()).thenReturn(mockProperties);
	}
	
	@Test
	public void testDefaultsAreSet() {
		when(mockProperties.getProperty("project.build.sourceEncoding")).thenReturn(TEST_ENCODING);
		fixture.setupDefaults(helperCombo, mockMavenProject);
		
		assertEquals(EXPECTED_DEFAULT_INPUT_SOURCE_READER, helperCombo.getInputSourceReader());
		assertEquals(EXPECTED_DEFAULT_OUTPUT_SOURCE_WRITER, helperCombo.getOutputSourceWriter());
		assertEquals(TEST_ENCODING, helperCombo.getEncoding());
		
		assertNotNull(helperCombo.getTransformers());
		assertEquals(0, helperCombo.getTransformers().size());
		
		assertNotNull(helperCombo.getSettings());
		assertEquals(0, helperCombo.getSettings().size());
	}
	
	@Test
	public void testUserValuesNotOverwritten() {
		final List<String> expectedTransformers = new ArrayList<String>();
		final Map<String, String> expectedSettings = new HashMap<String, String>();
		
		expectedTransformers.add("foo");
		expectedSettings.put("foo", "bar");
		
		when(mockProperties.getProperty("project.build.sourceEncoding")).thenReturn(null);
		helperCombo.setEncoding(TEST_ENCODING);
		helperCombo.setInputSourceReader(TEST_INPUT_SOURCE_READER);
		helperCombo.setOutputSourceWriter(TEST_OUTPUT_SOURCE_WRITER);
		helperCombo.setTransformers(expectedTransformers);
		helperCombo.setSettings(expectedSettings);
		
		fixture.setupDefaults(helperCombo, mockMavenProject);
		
		assertEquals(TEST_INPUT_SOURCE_READER, helperCombo.getInputSourceReader());
		assertEquals(TEST_OUTPUT_SOURCE_WRITER, helperCombo.getOutputSourceWriter());
		assertEquals(TEST_ENCODING, helperCombo.getEncoding());
		assertSame(expectedTransformers, helperCombo.getTransformers());
		assertSame(expectedSettings, helperCombo.getSettings());
	}
	
	@Test
	public void testNullFieldsAreSet() {
		this.executeInvalidStringFieldTest(null);
	}
	
	@Test
	public void testEmptyStringFieldsAreSet() {
		this.executeInvalidStringFieldTest("");
	}
	
	private void executeInvalidStringFieldTest(String valToTest) {
		when(mockProperties.getProperty("project.build.sourceEncoding")).thenReturn(TEST_ENCODING);
		
		helperCombo.setEncoding(valToTest);
		fixture.setupDefaults(helperCombo, mockMavenProject);
		
		assertEquals(TEST_ENCODING, helperCombo.getEncoding());
	}

}
