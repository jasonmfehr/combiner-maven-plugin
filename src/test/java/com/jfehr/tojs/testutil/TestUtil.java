package com.jfehr.tojs.testutil;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public final class TestUtil {

	public static final String TMP_TEST_DIR = "src/test/resources/tmp/";
	public static final String FAIL_MSG = "test pre-condition failed -- ";
	
	private TestUtil() {

	}

	public static void createNotReadableDirectory(final String directoryPath) {
		createDirectory(directoryPath, false, true);
	}
	
	public static void createNotWriteableDirectory(final String directoryPath) {
		createDirectory(directoryPath, true, false);
	}
	
	public static void createDirectory(final String directoryPath, boolean isReadable, boolean isWriteable) {
		final File directory = new File(directoryPath);

		assertTrue(buildFailMsg("could not create directory [" + directoryPath + "]"), directory.mkdir());
		directory.deleteOnExit();
		
		assertTrue(buildFailMsg("could not set directory [" + directoryPath + "] as readable=" + Boolean.toString(isReadable)), directory.setReadable(isReadable, false));
		assertTrue(buildFailMsg("could not set directory [" + directoryPath + "] as writeable=") + Boolean.toString(isReadable), directory.setWritable(isWriteable, false));
	}
	
	public static void createFile(final String filePath, boolean isWriteable, boolean isReadable) {
		final File file = new File(filePath);
		
		try{
			assertTrue(buildFailMsg("file [" + filePath + "] already exists"), file.createNewFile());
		}catch(IOException ioe){
			fail(buildFailMsg("file [" + filePath + "] could not be created" + ioe.getMessage()));
		}
		
		file.deleteOnExit();
		assertTrue(buildFailMsg("could not set file [" + filePath + "] as readable=" + Boolean.toString(isReadable)), file.setReadable(isReadable, false));
		assertTrue(buildFailMsg("could not set file [" + filePath + "] as writeable=" + Boolean.toString(isWriteable)), file.setWritable(isWriteable, false));
	}
	
	public static Object getPrivateField(Object targetObj, String fieldName) {
		final Field targetField = locateField(targetObj, fieldName);
		final Object retrievedValue;
		
		try {
			retrievedValue = targetField.get(targetObj);
			return retrievedValue;
		} catch (IllegalArgumentException e) {
			throw new TestUtilException(e);
		} catch (IllegalAccessException e) {
			throw new TestUtilException(e);
		}
	}
	
	public static void setPrivateField(Object targetObj, String fieldName, Object value) {
		final Field targetField = locateField(targetObj, fieldName);

		try {
			targetField.set(targetObj, value);
		} catch (IllegalArgumentException e) {
			throw new TestUtilException(e);
		} catch (IllegalAccessException e) {
			throw new TestUtilException(e);
		}
	}
	
	public static void cleanTmpTestDir() {
		final File testDir = new File(TMP_TEST_DIR);
		
		deleteDirectory(testDir);
		
		assertTrue(buildFailMsg("could not create tmp test directory [" + testDir.getPath() + "]"), testDir.mkdir());
		assertTrue(buildFailMsg("could not set tmp test directory [" + testDir.getPath() + "] as readable"), testDir.setReadable(true));
		assertTrue(buildFailMsg("could not set tmp test directory [" + testDir.getPath() + "] as writeable"), testDir.setWritable(true));
		assertTrue(buildFailMsg("could not set tmp test directory [" + testDir.getPath() + "] as executable"), testDir.setExecutable(true));
	}
	
	public static void deleteDirectory(File dir) {
		if(dir.exists()){
			dir.setReadable(true);
			dir.setExecutable(true);
			dir.setWritable(true);
		}
		
		for(final File f : dir.listFiles()){
			if(f.isFile()){
				assertTrue(buildFailMsg("could not delete file [" + f.getPath() + "]"), f.delete());
			}else{
				deleteDirectory(f);
			}
		}
		
		assertTrue(buildFailMsg("could not delete file [" + dir.getPath() + "]"), dir.delete());
	}
	
	public static String buildFailMsg(final String msg) {
		return FAIL_MSG + msg;
	}
	
	private static Field locateField(Object targetObj, String fieldName) {
		final Field targetField;
		
		try {
			targetField = targetObj.getClass().getDeclaredField(fieldName);
			targetField.setAccessible(true);
			return targetField;
		} catch (SecurityException e) {
			throw new TestUtilException(e);
		} catch (NoSuchFieldException e) {
			throw new TestUtilException(e);
		}
	}
 	
}
