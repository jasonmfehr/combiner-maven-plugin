package com.jfehr.combiner.file;

import static com.jfehr.combiner.testutil.TestUtil.TMP_TEST_DIR;
import static com.jfehr.combiner.testutil.TestUtil.cleanTmpTestDir;
import static com.jfehr.combiner.testutil.TestUtil.createFile;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.logging.LogHolder;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.testutil.TestUtil;
import com.jfehr.tojs.exception.FileSystemLocationNotFound;
import com.jfehr.tojs.exception.NotReadableException;
import com.jfehr.tojs.exception.NotWriteableException;

public class FileValidatorTest {

	private static final String TEST_LOCATION = "filevalidatortest";
	
	private FileValidator fixture;
	
	@Before
	public void setUp() {
		cleanTmpTestDir();
		initMocks(this);
		fixture = new FileValidator();
	}
	
	@After
	public void deleteTmpTestDir() {
		cleanTmpTestDir();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadableNullInput() {
		fixture.existsAndReadable(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testReadableBlankInput() {
		fixture.existsAndReadable("");
	}
	
	@Test(expected=FileSystemLocationNotFound.class)
	public void testReadableNotExists() {
		fixture.existsAndReadable(TMP_TEST_DIR + TEST_LOCATION);
	}
	
	@Test(expected=NotReadableException.class)
	public void testReadableExistsNotReadable() {
		createFile(TMP_TEST_DIR + TEST_LOCATION, Boolean.FALSE, Boolean.FALSE);
		fixture.existsAndReadable(TMP_TEST_DIR + TEST_LOCATION);
	}
	
	@Test
	public void testExistsReadable() {
		final ParameterizedLogger mockLogger = mock(ParameterizedLogger.class);
		
		createFile(TMP_TEST_DIR + TEST_LOCATION, Boolean.FALSE, Boolean.TRUE);
		TestUtil.setPrivateStaticField(LogHolder.class, "logger", mockLogger);
		fixture.existsAndReadable(TMP_TEST_DIR + TEST_LOCATION);
		
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
	public void testWriteableNotExists() {
		fixture.existsAndWriteable(TMP_TEST_DIR + TEST_LOCATION);
	}
	
	@Test(expected=NotWriteableException.class)
	public void testWriteableExistsNotWriteable() {
		createFile(TMP_TEST_DIR + TEST_LOCATION, Boolean.FALSE, Boolean.FALSE);
		fixture.existsAndWriteable(TMP_TEST_DIR + TEST_LOCATION);
	}
	
	@Test
	public void testExistsWriteable() {
		final ParameterizedLogger mockLogger = mock(ParameterizedLogger.class);
		
		TestUtil.setPrivateStaticField(LogHolder.class, "logger", mockLogger);
		createFile(TMP_TEST_DIR + TEST_LOCATION, Boolean.TRUE, Boolean.FALSE);
		fixture.existsAndWriteable(TMP_TEST_DIR + TEST_LOCATION);
		
		verify(mockLogger).debugWithParams(contains("exists"), any(), any());
		verify(mockLogger).debugWithParams(any(String.class), any(), any(), eq("writeable"));
		verify(mockLogger).debugWithParams(contains("valid"), any(), any());
	}
}
