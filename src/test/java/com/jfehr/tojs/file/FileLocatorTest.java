package com.jfehr.tojs.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoAssertionError;

import com.jfehr.tojs.exception.DirectoryNotFoundException;
import com.jfehr.tojs.exception.NotReadableException;
import com.jfehr.tojs.logging.ParameterizedLogger;

public class FileLocatorTest {

	private static final String TMP_TEST_DIR = "src/test/resources/tmp/";
	private static final String NOT_READABLE_DIR = TMP_TEST_DIR + "notreadable";
	private static final String NOT_EXISTS_DIR = TMP_TEST_DIR + "idontexist";
	
	private static final String INCLUDE_PREFIX = "include_";
	private static final String EXCLUDE_PREFIX = "exclude_";
	private static final String FOUND_FILES_PREFIX = "found_";
	private static final int INCLUDE_LENGTH = 3;
	private static final int EXCLUDE_LENGTH = 2;
	private static final int FOUND_FILES_LENGTH = 4;
	
	private FileLocator fixture;
	private List<String> includes;
	private List<String> excludes;
	
	@Mock
	private DirectoryScanner mockScanner;
	private ParameterizedLogger mockLogger;
	
	@Captor
	private ArgumentCaptor<String[]> includesCaptor;
	
	@Captor
	private ArgumentCaptor<String[]> excludesCaptor;
	
	@Before
	public void setUp() {
		fixture = new FileLocator(mockLogger);
		includes = new ArrayList<String>();
		excludes = new ArrayList<String>();
		
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testHappyPathWithExcludes() {
		List<String> foundFilesList;
		String[] foundFilesArr = new String[FOUND_FILES_LENGTH];
		
		for(int i=0; i<INCLUDE_LENGTH; i++){
			includes.add(INCLUDE_PREFIX + Integer.toString(i));
		}
		
		for(int i=0; i<EXCLUDE_LENGTH; i++){
			excludes.add(EXCLUDE_PREFIX + Integer.toString(i));
		}
		
		for(int i=0; i<FOUND_FILES_LENGTH; i++){
			foundFilesArr[i] = FOUND_FILES_PREFIX + Integer.toString(i);
		}
		
		Mockito.when(mockScanner.getIncludedFiles()).thenReturn(foundFilesArr);
		
		foundFilesList = fixture.locateFiles(mockScanner, TMP_TEST_DIR, includes, excludes);
		
		Mockito.verify(mockScanner).addDefaultExcludes();
		Mockito.verify(mockScanner).setBasedir(TMP_TEST_DIR);
		Mockito.verify(mockScanner).setIncludes(includesCaptor.capture());
		Mockito.verify(mockScanner).setExcludes(excludesCaptor.capture());
		
		Mockito.verify(mockScanner).scan();
		
		this.assertArray(INCLUDE_PREFIX, INCLUDE_LENGTH, includesCaptor.getValue());
		this.assertArray(EXCLUDE_PREFIX, EXCLUDE_LENGTH, excludesCaptor.getValue());
		this.assertArray(FOUND_FILES_PREFIX, FOUND_FILES_LENGTH, foundFilesList.toArray(new String[foundFilesList.size()]));
	}
	
	@Test
	public void testHappyPathWithoutExcludes() {
		List<String> foundFilesList;
		String[] foundFilesArr = new String[FOUND_FILES_LENGTH];
		
		for(int i=0; i<INCLUDE_LENGTH; i++){
			includes.add(INCLUDE_PREFIX + Integer.toString(i));
		}
		
		for(int i=0; i<FOUND_FILES_LENGTH; i++){
			foundFilesArr[i] = FOUND_FILES_PREFIX + Integer.toString(i);
		}
		
		Mockito.when(mockScanner.getIncludedFiles()).thenReturn(foundFilesArr);
		
		foundFilesList = fixture.locateFiles(mockScanner, TMP_TEST_DIR, includes, null);
		
		Mockito.verify(mockScanner).addDefaultExcludes();
		Mockito.verify(mockScanner).setBasedir(TMP_TEST_DIR);
		Mockito.verify(mockScanner).setIncludes(includesCaptor.capture());
		Mockito.verify(mockScanner, Mockito.never()).setExcludes(Mockito.any(String[].class));
		
		Mockito.verify(mockScanner).scan();
		
		this.assertArray(INCLUDE_PREFIX, INCLUDE_LENGTH, includesCaptor.getValue());
		this.assertArray(FOUND_FILES_PREFIX, FOUND_FILES_LENGTH, foundFilesList.toArray(new String[foundFilesList.size()]));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullBaseDir() {
		Mockito.doThrow(new MockitoAssertionError("DirectoryScanner scan() method was called but should not have been")).when(mockScanner).scan();
		fixture.locateFiles(mockScanner, null, includes, excludes);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullDirectoryScanner() {
		Mockito.doThrow(new MockitoAssertionError("DirectoryScanner scan() method was called but should not have been")).when(mockScanner).scan();
		fixture.locateFiles(null, TMP_TEST_DIR, includes, excludes);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyBaseDir() {
		Mockito.doThrow(new MockitoAssertionError("DirectoryScanner scan() method was called but should not have been")).when(mockScanner).scan();
		fixture.locateFiles(mockScanner, "", includes, excludes);
	}
	
	@Test(expected=DirectoryNotFoundException.class)
	public void testNonExistantBaseDir() {
		final File nonExistsDir = new File(NOT_EXISTS_DIR);
		
		assertFalse("test pre-condition failed, non-existant directory [" + NOT_EXISTS_DIR + "] actually exists.", nonExistsDir.exists());
		
		Mockito.doThrow(new MockitoAssertionError("DirectoryScanner scan() method was called but should not have been")).when(mockScanner).scan();
		fixture.locateFiles(mockScanner, NOT_EXISTS_DIR, includes, includes);
	}
	
	@Test(expected=NotReadableException.class)
	public void testNonReadableBaseDir() throws Exception {
		final File nonReadableDir = new File(NOT_READABLE_DIR);
		
		assertTrue("test pre-condition failed, could not create non-readable directory [" + NOT_READABLE_DIR + "]", nonReadableDir.mkdir());
		assertTrue("test pre-condition failed, could not set directory [" + NOT_READABLE_DIR + "] as not readable", nonReadableDir.setReadable(false, false));
		nonReadableDir.deleteOnExit();
		
		Mockito.doThrow(new MockitoAssertionError("DirectoryScanner scan() method was called but should not have been")).when(mockScanner).scan();
		fixture.locateFiles(mockScanner, NOT_READABLE_DIR, includes, excludes);
	}
	
	private void assertArray(String entryPrefix, int expectedLength, String[] actual) {
		String tmpEntry;
		List<String> actualList = Arrays.asList(actual);
		
		assertEquals("Length of actual list did not match the expected length", expectedLength, actual.length);
		
		for(int i=0; i<expectedLength; i++){
			tmpEntry = entryPrefix + Integer.toString(i);
			assertTrue("Did not find expected entry [" + tmpEntry + "] in actual list", actualList.contains(tmpEntry));
		}
	}

}
