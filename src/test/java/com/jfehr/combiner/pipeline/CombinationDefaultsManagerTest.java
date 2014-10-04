package com.jfehr.combiner.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

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
		fixture = new CombinationDefaultsManager();
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
	}
	
	@Test
	public void testUserValuesNotOverwritten() {
		when(mockProperties.getProperty("project.build.sourceEncoding")).thenReturn(null);
		helperCombo.setEncoding(TEST_ENCODING);
		helperCombo.setInputSourceReader(TEST_INPUT_SOURCE_READER);
		helperCombo.setOutputSourceWriter(TEST_OUTPUT_SOURCE_WRITER);
		
		fixture.setupDefaults(helperCombo, mockMavenProject);
		
		assertEquals(TEST_INPUT_SOURCE_READER, helperCombo.getInputSourceReader());
		assertEquals(TEST_OUTPUT_SOURCE_WRITER, helperCombo.getOutputSourceWriter());
		assertEquals(TEST_ENCODING, helperCombo.getEncoding());
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
