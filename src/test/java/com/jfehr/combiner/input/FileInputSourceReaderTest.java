package com.jfehr.combiner.input;

import static com.jfehr.combiner.testutil.TestUtil.TEST_CHARSET;
import static com.jfehr.combiner.testutil.TestUtil.TEST_CHARSET_STR;
import static com.jfehr.combiner.testutil.TestUtil.TMP_TEST_DIR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.combiner.file.FileLocator;
import com.jfehr.combiner.file.MultiFileReader;

@RunWith(MockitoJUnitRunner.class)
public class FileInputSourceReaderTest {

	@Mock private FileLocator mockLocator;
	@Mock private MultiFileReader mockReader;
	@Mock private MavenProject mockMavenProject;
	@Mock private File mockBaseDir;
	
	@InjectMocks private FileInputSourceReader fixture;
	
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
		
		verify(mockMavenProject, times(2)).getBasedir();
		verify(mockBaseDir, times(2)).getAbsolutePath();
		verify(mockLocator).locateFiles(TMP_TEST_DIR, includes, excludes);
		verify(mockReader).readInputFiles(TEST_CHARSET, locatedFiles);
		
	}
	
}
