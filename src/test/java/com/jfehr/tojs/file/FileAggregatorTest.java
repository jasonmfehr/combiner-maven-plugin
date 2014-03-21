package com.jfehr.tojs.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jfehr.tojs.exception.DirectoryCreationException;
import com.jfehr.tojs.exception.FileExistsException;
import com.jfehr.tojs.exception.NotWriteableException;
import com.jfehr.tojs.logging.ParameterizedLogger;
import com.jfehr.tojs.parser.ParserExecutor;
import com.jfehr.tojs.testutil.TestUtil;

public class FileAggregatorTest {

	private static final String INVALID_CHARSET = "MyFooCharSet";
	private static final String NOT_WRITEABLE_DIR = TestUtil.TMP_TEST_DIR + "not_writeable_dir/";
	private static final String NOT_WRITEABLE_SUBDIR = NOT_WRITEABLE_DIR + "subdir/";
	private static final String NOT_WRITEABLE_DIR_2 = TestUtil.TMP_TEST_DIR + "not_writeable_dir_2/";
	private static final String WRITEABLE_DIR = TestUtil.TMP_TEST_DIR + "writeable_dir/";
	private static final String WRITEABLE_FILE_NAME = WRITEABLE_DIR + "writeable_file";
	private static final String UTF_8 = "UTF-8";
	
	private FileAggregator fixture;
	
	@Mock private ParameterizedLogger mockLogger;
	@Mock private ParserExecutor mockParserExecutor;
	
	@Before
	public void setUp() {
		TestUtil.cleanTmpTestDir();
		MockitoAnnotations.initMocks(this);
		
		fixture = new FileAggregator(mockParserExecutor, mockLogger);
		
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullJsObjectNameParam() {
		fixture.aggregate(null, " ", " ", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyJsObjectNameParam() {
		fixture.aggregate("", " ", " ", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullFileEncodingParam() {
		fixture.aggregate(" ", null, " ", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyFileEncodingParam() {
		fixture.aggregate(" ", "", " ", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullOutputFilePathParam() {
		fixture.aggregate(" ", " ", null, new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyOutputFilePathParam() {
		fixture.aggregate(" ", " ", "", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInputFilesParam() {
		fixture.aggregate(" ", " ", " ", null, new ArrayList<String>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullParsersParam() {
		fixture.aggregate(" ", " ", " ", new ArrayList<String>(), null);
	}
	
	@Test(expected=IllegalCharsetNameException.class)
	public void testInvalidCharset() {
		fixture.aggregate(" ", INVALID_CHARSET, " ", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=DirectoryCreationException.class)
	public void testOutputFileDirNotCreatable() {
		TestUtil.createNotWriteableDirectory(NOT_WRITEABLE_DIR);
		fixture.aggregate(" ", UTF_8, NOT_WRITEABLE_SUBDIR + "test", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=NotWriteableException.class)
	public void testOutputFileNotWriteable() {
		TestUtil.createNotWriteableDirectory(NOT_WRITEABLE_DIR_2);
		fixture.aggregate(" ", UTF_8, NOT_WRITEABLE_DIR_2  + "test.file", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=FileExistsException.class)
	public void testFileExists() {
		TestUtil.createDirectory(WRITEABLE_DIR, true, true);
		TestUtil.createFile(WRITEABLE_FILE_NAME, true, true);
		
		fixture.aggregate(" ", UTF_8, WRITEABLE_FILE_NAME, new ArrayList<String>(), new ArrayList<String>());
	}
	
}
