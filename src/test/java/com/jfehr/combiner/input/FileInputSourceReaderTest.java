package com.jfehr.combiner.input;

import static com.jfehr.combiner.testutil.TestUtil.TEST_CHARSET;
import static com.jfehr.combiner.testutil.TestUtil.TEST_CHARSET_STR;
import static com.jfehr.combiner.testutil.TestUtil.TMP_TEST_DIR;
import static com.jfehr.combiner.testutil.TestUtil.setPrivateField;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.file.FileLocator;
import com.jfehr.combiner.file.MultiFileReader;

public class FileInputSourceReaderTest {

	@Mock private FileLocator mockLocator;
	@Mock private MultiFileReader mockReader;
	@Mock private MavenProject mockMavenProject;
	@Mock private File mockBaseDir;
	private FileInputSourceReader fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		
		fixture = new FileInputSourceReader();
		setPrivateField(fixture, "fileLocator", mockLocator);
		setPrivateField(fixture, "multiFileReader", mockReader);
	}
	
	@Test
	public void testHappyPath() {
		final List<String> includes = new ArrayList<String>();
		final List<String> excludes = new ArrayList<String>();
		final List<String> locatedFiles = new ArrayList<String>();
		final Map<String, String> readFiles = new HashMap<String, String>();
		
		when(mockMavenProject.getBasedir()).thenReturn(mockBaseDir);
		when(mockBaseDir.getAbsolutePath()).thenReturn(TMP_TEST_DIR);
		when(mockLocator.locateFiles(TMP_TEST_DIR, includes, excludes)).thenReturn(locatedFiles);
		when(mockReader.readInputFiles(TEST_CHARSET, locatedFiles)).thenReturn(readFiles);
		
		assertEquals(readFiles, fixture.read(TEST_CHARSET_STR, includes, excludes, new HashMap<String, String>(), mockMavenProject));
	}

}
