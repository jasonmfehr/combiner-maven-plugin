package com.jfehr.tojs.file;

import static com.jfehr.tojs.testutil.TestUtil.TMP_TEST_DIR;
import static com.jfehr.tojs.testutil.TestUtil.buildFailMsg;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.io.Files;
import com.jfehr.tojs.exception.FileSystemLocationNotFound;
import com.jfehr.tojs.logging.ParameterizedLogger;
import com.jfehr.tojs.testutil.TestUtil;

public class MultiFileReaderTest {

	private static final String TEST_FILE_1 = TMP_TEST_DIR + "multifilereader.1";
	private static final String TEST_FILE_2 = TMP_TEST_DIR + "multifilereader.2";
	
	@Mock private FileValidator mockValidator;
	@Mock private ParameterizedLogger mockLogger;
	private MultiFileReader fixture;
	
	@Before
	public void setUp() {
		initMocks(this);
		
		fixture = new MultiFileReader(mockLogger);
		TestUtil.setPrivateField(fixture, "fileValidator", mockValidator);
	}
	
	@Test
	public void testHappyPath() throws Exception {
		final Charset charSet = Charset.forName("UTF-8");
		final File file1 = this.setupFile(TEST_FILE_1);
		final File file2 = this.setupFile(TEST_FILE_2);
		final Map<String, String> actual;
		
		Files.write(TEST_FILE_1, file1, charSet);
		Files.write(TEST_FILE_2, file2, charSet);

		actual = fixture.readInputFiles(charSet, Arrays.asList(TEST_FILE_1, TEST_FILE_2));
		
		assertEquals(2, actual.size());
		this.assertFile(file1, TEST_FILE_1, actual);
		this.assertFile(file2, TEST_FILE_2, actual);
	}
	
	@Test(expected=FileSystemLocationNotFound.class)
	public void testFileNotFound() throws Exception {
		final Charset charSet = Charset.forName("UTF-8");

		doThrow(new FileSystemLocationNotFound("")).when(mockValidator).existsAndReadable(TEST_FILE_1);
		
		fixture.readInputFiles(charSet, Arrays.asList(TEST_FILE_1, TEST_FILE_2));
	}
	
	private File setupFile(final String filePath) {
		final File fileObj = new File(filePath);
		
		if(fileObj.exists()){
			assertTrue(buildFailMsg("could not delete file " + filePath), fileObj.delete());
		}
		fileObj.deleteOnExit();
		
		return fileObj;
	}
	
	private void assertFile(final File expectedFile, final String expectedContents, Map<String, String> actual) {
		assertTrue("File " + expectedFile.getAbsolutePath() + " not found in actual results Map", actual.containsKey(expectedFile.getAbsolutePath()));
		assertEquals(expectedContents, actual.get(expectedFile.getAbsolutePath()));
	}

}
