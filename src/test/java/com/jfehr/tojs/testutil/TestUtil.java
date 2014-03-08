package com.jfehr.tojs.testutil;

import java.lang.reflect.Field;

public final class TestUtil {

	private TestUtil() {

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
