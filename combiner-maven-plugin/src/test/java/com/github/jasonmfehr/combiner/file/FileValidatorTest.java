package com.github.jasonmfehr.combiner.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;
import com.github.jasonmfehr.tojs.exception.FileSystemLocationNotFound;
import com.github.jasonmfehr.tojs.exception.NotReadableException;
import com.github.jasonmfehr.tojs.exception.NotWriteableException;

@RunWith(MockitoJUnitRunner.class)
public class FileValidatorTest {

	private static final String TEST_LOCATION = "filevalidatortest";
	
	@Mock private ParameterizedLogger mockLogger;
	
	@InjectMocks private FileValidator fixture;
	
	@Rule public TemporaryFolder tmpFolder = new TemporaryFolder();
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadableNullInput() {
		fixture.existsAndReadable(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadableBlankInput() {
		fixture.existsAndReadable("");
	}
	
	@Test(expected=FileSystemLocationNotFound.class)
	public void testReadableNotExists() throws Exception {
		fixture.existsAndReadable(tmpFolder.newFolder().getAbsolutePath() + "/" + TEST_LOCATION);
	}
	
	@Test(expected=NotReadableException.class)
	public void testReadableExistsNotReadable() throws Exception {
		final File notReadableFile = tmpFolder.newFile();
		
		assertThat(notReadableFile.setReadable(false), equalTo(true));
		assertThat(notReadableFile.setWritable(false), equalTo(true));

		fixture.existsAndReadable(notReadableFile.getAbsolutePath());
	}
	
	@Test
	public void testExistsReadable() throws Exception {
		final File tmpFile = tmpFolder.newFile();
		
		fixture.existsAndReadable(tmpFile.getAbsolutePath());
		
		verify(mockLogger).debugWithParams(contains("exists"), any(), any());
		verify(mockLogger).debugWithParams(any(String.class), any(), any(), eq("readable"));
		verify(mockLogger).debugWithParams(contains("valid"), any(), any());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWriteableNullInput() {
		fixture.existsAndWriteable(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWriteableBlankInput() {
		fixture.existsAndWriteable("");
	}
	
	@Test(expected=FileSystemLocationNotFound.class)
	public void testWriteableNotExists() throws Exception {
		fixture.existsAndWriteable(tmpFolder.newFolder().getAbsolutePath() + "/" + TEST_LOCATION);
	}
	
	@Test(expected=NotWriteableException.class)
	public void testWriteableExistsNotWriteable() throws Exception {
		final File notWriteableFile = tmpFolder.newFile();
		
		assertThat(notWriteableFile.setReadable(false), equalTo(true));
		assertThat(notWriteableFile.setWritable(false), equalTo(true));
		
		fixture.existsAndWriteable(notWriteableFile.getAbsolutePath());
	}
	
	@Test
	public void testExistsWriteable() throws Exception {
		final File tmpFile = tmpFolder.newFile();
		
		fixture.existsAndWriteable(tmpFile.getAbsolutePath());
		
		verify(mockLogger).debugWithParams(contains("exists"), any(), any());
		verify(mockLogger).debugWithParams(any(String.class), any(), any(), eq("writeable"));
		verify(mockLogger).debugWithParams(contains("valid"), any(), any());
	}
}
