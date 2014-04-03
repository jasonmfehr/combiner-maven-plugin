package com.jfehr.tojs.testutil;

import com.jfehr.combiner.mojo.ObjectFactoryTest.MockBaseInterface;

public class MockNonDefaultPackage implements MockBaseInterface {

	public String doNothing(String someInput) {
		return null;
	}

}
