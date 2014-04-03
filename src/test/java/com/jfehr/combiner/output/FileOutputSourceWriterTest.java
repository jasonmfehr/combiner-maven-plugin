package com.jfehr.combiner.output;

import static com.jfehr.tojs.testutil.TestUtil.TEST_CHARSET_STR;
import static com.jfehr.tojs.testutil.TestUtil.TMP_TEST_DIR;
import static com.jfehr.tojs.testutil.TestUtil.cleanTmpTestDir;
import static com.jfehr.tojs.testutil.TestUtil.createDirectory;
import static com.jfehr.tojs.testutil.TestUtil.readFile;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.nio.charset.UnsupportedCharsetException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.tojs.exception.DirectoryCreationException;
import com.jfehr.tojs.exception.FileExistsException;
import com.jfehr.tojs.exception.NotWriteableException;

public class FileOutputSourceWriterTest {

	private static final String TEST_PARENT_DIR_PATH = TMP_TEST_DIR + "file_output_source_dir";
	private static final String TEST_PARENT_SUBDIR_PATH = TEST_PARENT_DIR_PATH + "/subdir";
	private static final String TEST_FILE_PATH = TEST_PARENT_SUBDIR_PATH + "/testfile.out";
	private static final String TEST_CONTENTS = "these are the test file contents to write";
	
	@Mock private ParameterizedLogger mockLogger;
	private FileOutputSourceWriter fixture;
	
	@Before
	public void setUp() {
		cleanTmpTestDir();
		initMocks(this);
		
		fixture = new FileOutputSourceWriter(mockLogger);
	}
	
	@AfterClass
	public static void deleteTmpTestDir() {
		cleanTmpTestDir();
	}
	
	@Test(expected=UnsupportedCharsetException.class)
	public void testInvalidCharset() {
		fixture.write("foo", null, null, null, null);
	}
	
	@Test(expected=DirectoryCreationException.class)
	public void testMakeParentDirFails() {
		createDirectory(TEST_PARENT_DIR_PATH, true, false);
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_PATH, TEST_CONTENTS, null, null);
	}
	
	@Test
	public void testMakeParentDirSucceeds() {
		createDirectory(TEST_PARENT_DIR_PATH, true, true);
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_PATH, TEST_CONTENTS, null, null);
		
		assertEquals(TEST_CONTENTS, readFile(TEST_FILE_PATH));
	}
	
	@Test(expected=NotWriteableException.class)
	public void testParentDirExistsNotWriteable() {
		createDirectory(TEST_PARENT_SUBDIR_PATH, true, false, true);
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_PATH, TEST_CONTENTS, null, null);
	}
	
	@Test
	public void testParentDirExistsAndWriteable() {
		createDirectory(TEST_PARENT_SUBDIR_PATH, true, true, true);
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_PATH, TEST_CONTENTS, null, null);
		
		assertEquals(TEST_CONTENTS, readFile(TEST_FILE_PATH));
	}
	
	
	@Test(expected=FileExistsException.class)
	public void testFileExists() throws Exception {
		createDirectory(TEST_PARENT_SUBDIR_PATH, true, true, true);
		new File(TEST_FILE_PATH).createNewFile();
		
		fixture.write(TEST_CHARSET_STR, TEST_FILE_PATH, TEST_CONTENTS, null, null);
	}
	
	@Test
	public void testFileAndParentDirectoryNotExists() {
		fixture.write(TEST_CHARSET_STR, TEST_FILE_PATH, TEST_CONTENTS, null, null);
		
		assertEquals(TEST_CONTENTS, readFile(TEST_FILE_PATH));
	}

}
