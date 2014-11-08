package com.jfehr.combiner.file;

import static com.jfehr.combiner.testutil.TestUtil.TEST_CHARSET;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.io.Files;
import com.jfehr.tojs.exception.FileSystemLocationNotFound;

@RunWith(MockitoJUnitRunner.class)
public class MultiFileReaderTest {

	private static final String TEST_FILE_1 = "multifilereader.1";
	private static final String TEST_FILE_2 = "multifilereader.2";
	
	@Mock private FileValidator mockValidator;
	@InjectMocks private MultiFileReader fixture;
	
	@Rule public TemporaryFolder tmpFolder = new TemporaryFolder();
	
	@Test
	public void testHappyPath() throws Exception {
		final File file1 = tmpFolder.newFile(TEST_FILE_1);
		final File file2 = tmpFolder.newFile(TEST_FILE_2);
		final Map<String, String> actual;
		
		Files.write(TEST_FILE_1, file1, TEST_CHARSET);
		Files.write(TEST_FILE_2, file2, TEST_CHARSET);

		actual = fixture.readInputFiles(TEST_CHARSET, Arrays.asList(file1.getAbsolutePath(), file2.getAbsolutePath()));
		
		assertThat(actual.size(), equalTo(2));
		this.assertFile(file1, TEST_FILE_1, actual);
		this.assertFile(file2, TEST_FILE_2, actual);
	}
	
	@Test(expected=FileSystemLocationNotFound.class)
	public void testFileNotFound() throws Exception {
		doThrow(new FileSystemLocationNotFound("")).when(mockValidator).existsAndReadable(TEST_FILE_1);
		
		fixture.readInputFiles(TEST_CHARSET, Arrays.asList(TEST_FILE_1, TEST_FILE_2));
	}
	
	private void assertFile(final File expectedFile, final String expectedContents, Map<String, String> actual) {
		assertThat(actual.keySet(), hasItem(expectedFile.getAbsolutePath()));
		assertThat(actual.get(expectedFile.getAbsolutePath()), equalTo(expectedContents));
	}

}
