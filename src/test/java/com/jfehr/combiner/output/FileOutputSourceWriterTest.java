package com.jfehr.combiner.output;

import static com.jfehr.combiner.testutil.TestUtil.TEST_CHARSET_STR;
import static com.jfehr.combiner.testutil.TestUtil.readFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.tojs.exception.DirectoryCreationException;
import com.jfehr.tojs.exception.FileExistsException;
import com.jfehr.tojs.exception.NotWriteableException;

@RunWith(MockitoJUnitRunner.class)
public class FileOutputSourceWriterTest {

	private static final String TEST_PARENT_DIR_PATH = "file_output_source_dir";
	private static final String TEST_PARENT_SUBDIR_PATH = "/subdir";
	private static final String TEST_FILE_NAME = "/testfile.out";
	
	private static final String TEST_FILE_PATH = TEST_PARENT_SUBDIR_PATH + TEST_FILE_NAME;
	private static final String TEST_FILE_FULL_PATH = TEST_PARENT_DIR_PATH + TEST_PARENT_SUBDIR_PATH + TEST_FILE_NAME;
	
	private static final String TEST_CONTENTS = "these are the test file contents to write";
	
	@Mock private MavenProject mockMavenProject;
	@Mock private Build mockBuild;
	
	@InjectMocks private FileOutputSourceWriter fixture;
	
	@Rule public TemporaryFolder tmpFolder = new TemporaryFolder();
	private File buildRootDir;
	
	@Before
	public void setUp() throws Exception {
		buildRootDir = tmpFolder.newFolder();
		
		when(mockMavenProject.getBuild()).thenReturn(mockBuild);
		when(mockBuild.getDirectory()).thenReturn(buildRootDir.getAbsolutePath());
	}
	
	@Test(expected=UnsupportedCharsetException.class)
	public void testInvalidCharset() {
		fixture.write("foo", null, null, null, mockMavenProject);
	}
	
	@Test(expected=DirectoryCreationException.class)
	public void testMakeParentDirFails() throws Exception {
		final File parentFolder = this.buildParentFolder();
		
		parentFolder.setReadable(true);
		parentFolder.setWritable(false);
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_FULL_PATH, TEST_CONTENTS, null, mockMavenProject);
	}
	
	@Test
	public void testMakeParentDirSucceeds() throws Exception {
		final File parentFolder = this.buildParentFolder();
		final String actualWrittenFileContents;
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_FULL_PATH, TEST_CONTENTS, null, mockMavenProject);
		
		actualWrittenFileContents = readFile(parentFolder.getAbsolutePath() + TEST_FILE_PATH);
		assertThat(actualWrittenFileContents, equalTo(TEST_CONTENTS));
	}
	
	@Test(expected=NotWriteableException.class)
	public void testParentDirExistsNotWriteable() throws Exception {
		final File subFolder = this.buildSubFolder();
		
		subFolder.mkdirs();
		subFolder.setReadable(true);
		subFolder.setWritable(false);
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_FULL_PATH, TEST_CONTENTS, null, mockMavenProject);
	}
	
	@Test
	public void testParentDirExistsAndWriteable() throws Exception {
		final File subFolder = this.buildSubFolder();
		final String actualWrittenFileContents;
		
		subFolder.mkdirs();
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_FULL_PATH, TEST_CONTENTS, null, mockMavenProject);
		
		actualWrittenFileContents = readFile(subFolder.getAbsolutePath() + TEST_FILE_NAME);
		assertThat(actualWrittenFileContents, equalTo(TEST_CONTENTS));
	}
	
	@Test(expected=FileExistsException.class)
	public void testFileExists() throws Exception {
		final File subFolder = this.buildSubFolder();
		
		subFolder.mkdirs();
		new File(subFolder.getAbsolutePath() + TEST_FILE_NAME).createNewFile();
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_FULL_PATH, TEST_CONTENTS, null, mockMavenProject);
	}

	@Test
	public void testFileAndParentDirectoryNotExists() {
		final String actualWrittenFileContents;
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_FULL_PATH, TEST_CONTENTS, null, mockMavenProject);
		
		actualWrittenFileContents = readFile(buildRootDir + "/" + TEST_FILE_FULL_PATH);
		assertThat(actualWrittenFileContents, equalTo(TEST_CONTENTS));
	}
	
	private File buildParentFolder() {
		final File parentFolder = new File(buildRootDir.getAbsolutePath() + "/" + TEST_PARENT_DIR_PATH);
		
		parentFolder.mkdirs();
		
		return parentFolder;
	}
	
	private File buildSubFolder() {
		final File subFolder = new File(buildRootDir.getAbsolutePath() + "/" + TEST_PARENT_DIR_PATH + TEST_PARENT_SUBDIR_PATH);
		
		subFolder.mkdirs();
		
		return subFolder;
	}

}
