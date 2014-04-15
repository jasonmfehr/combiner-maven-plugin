package com.jfehr.combiner.factory;

import com.jfehr.combiner.input.InputSourceReader;

public class InputSourceReaderFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.input";
	
	public InputSourceReader buildObject(final String className) {
		return super.buildObject(className, DEFAULT_PACKAGE, InputSourceReader.class);
	}

}
