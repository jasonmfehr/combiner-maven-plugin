package com.jfehr.combiner.testutil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import com.google.common.io.Files;

public final class TestUtil {

	public static final String TEST_CHARSET_STR = "UTF-8";
	public static final Charset TEST_CHARSET = Charset.forName(TEST_CHARSET_STR);
	
	private TestUtil() {

	}

	public static Object getPrivateField(Object targetObj, String fieldName) {
		final Field targetField = locateField(targetObj.getClass(), fieldName);
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
		final Field targetField = locateField(targetObj.getClass(), fieldName);

		try {
			targetField.set(targetObj, value);
		} catch (IllegalArgumentException e) {
			throw new TestUtilException(e);
		} catch (IllegalAccessException e) {
			throw new TestUtilException(e);
		}
	}
	
	public static void setPrivateStaticField(Class<?> targetClass, String fieldName, Object value) {
		final Field targetField = locateField(targetClass, fieldName);
		
		try {
			targetField.set(targetClass, value);
		} catch (IllegalArgumentException e) {
			throw new TestUtilException(e);
		} catch (IllegalAccessException e) {
			throw new TestUtilException(e);
		}
	}
	
	public static String readFile(final String filePath) {
		try {
			return Files.toString(new File(filePath), TEST_CHARSET);
		} catch (IOException e) {
			throw new TestUtilException(e);
		}	
	}
	
	private static Field locateField(Class<?> targetClass, String fieldName) {
		final Field targetField;
		
		try {
			targetField = targetClass.getDeclaredField(fieldName);
			targetField.setAccessible(true);
			return targetField;
		} catch (SecurityException e) {
			throw new TestUtilException(e);
		} catch (NoSuchFieldException e) {
			throw new TestUtilException(e);
		}
	}
 	
}
