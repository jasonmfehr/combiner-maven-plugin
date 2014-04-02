package com.jfehr.tojs.file;

import static com.jfehr.tojs.testutil.TestUtil.TEST_CHARSET_STR;
import static com.jfehr.tojs.testutil.TestUtil.TMP_TEST_DIR;
import static com.jfehr.tojs.testutil.TestUtil.cleanTmpTestDir;
import static com.jfehr.tojs.testutil.TestUtil.createDirectory;
import static com.jfehr.tojs.testutil.TestUtil.createFile;
import static com.jfehr.tojs.testutil.TestUtil.createNotWriteableDirectory;

import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.tojs.exception.DirectoryCreationException;
import com.jfehr.tojs.exception.FileExistsException;
import com.jfehr.tojs.exception.NotWriteableException;
import com.jfehr.tojs.parser.ParserExecutor;

public class FileAggregatorTest {

	private static final String INVALID_CHARSET = "MyFooCharSet";
	private static final String NOT_WRITEABLE_DIR = TMP_TEST_DIR + "not_writeable_dir/";
	private static final String NOT_WRITEABLE_SUBDIR = NOT_WRITEABLE_DIR + "subdir/";
	private static final String NOT_WRITEABLE_DIR_2 = TMP_TEST_DIR + "not_writeable_dir_2/";
	private static final String WRITEABLE_DIR = TMP_TEST_DIR + "writeable_dir/";
	private static final String WRITEABLE_FILE_NAME = WRITEABLE_DIR + "writeable_file";
	
	private FileAggregator fixture;
	
	@Mock private ParameterizedLogger mockLogger;
	@Mock private ParserExecutor mockParserExecutor;
	
	@Before
	public void setUp() {
		cleanTmpTestDir();
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
		createNotWriteableDirectory(NOT_WRITEABLE_DIR);
		fixture.aggregate(" ", TEST_CHARSET_STR, NOT_WRITEABLE_SUBDIR + "test", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=NotWriteableException.class)
	public void testOutputFileNotWriteable() {
		createNotWriteableDirectory(NOT_WRITEABLE_DIR_2);
		fixture.aggregate(" ", TEST_CHARSET_STR, NOT_WRITEABLE_DIR_2  + "test.file", new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Test(expected=FileExistsException.class)
	public void testFileExists() {
		createDirectory(WRITEABLE_DIR, true, true);
		createFile(WRITEABLE_FILE_NAME, true, true);
		
		fixture.aggregate(" ", TEST_CHARSET_STR, WRITEABLE_FILE_NAME, new ArrayList<String>(), new ArrayList<String>());
	}
	
}
