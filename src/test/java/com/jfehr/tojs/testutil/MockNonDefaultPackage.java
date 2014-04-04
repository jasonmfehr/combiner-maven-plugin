package com.jfehr.tojs.testutil;

import com.jfehr.combiner.factory.ObjectFactoryTest.MockBaseInterface;

public class MockNonDefaultPackage implements MockBaseInterface {

	public String doNothing(String someInput) {
		return null;
	}

}
