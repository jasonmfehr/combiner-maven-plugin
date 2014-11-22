package com.github.jasonmfehr.combiner.testutil;

import com.github.jasonmfehr.combiner.factory.ObjectFactoryTest.MockBaseInterface;

public class MockNonDefaultPackage implements MockBaseInterface {

	public String doNothing(String someInput) {
		return null;
	}

}
