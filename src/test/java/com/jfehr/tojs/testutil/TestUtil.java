package com.jfehr.tojs.testutil;

import java.lang.reflect.Field;

public final class TestUtil {

	private TestUtil() {

	}

	public static void setPrivateField(Object targetObj, String fieldName, Object value) {
		final Field targetField;

		try {
			targetField = targetObj.getClass().getDeclaredField(fieldName);
			targetField.setAccessible(true);
			targetField.set(targetObj, value);
		} catch (SecurityException e) {
			throw new TestUtilException(e);
		} catch (NoSuchFieldException e) {
			throw new TestUtilException(e);
		} catch (IllegalArgumentException e) {
			throw new TestUtilException(e);
		} catch (IllegalAccessException e) {
			throw new TestUtilException(e);
		}
	}
	
}
