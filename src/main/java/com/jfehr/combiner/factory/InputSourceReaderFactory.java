package com.jfehr.combiner.factory;

import com.jfehr.combiner.input.InputSourceReader;
import com.jfehr.combiner.logging.ParameterizedLogger;

public class InputSourceReaderFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.input";
	
	public InputSourceReaderFactory(final ParameterizedLogger logger) {
		super(logger);
	}
	
	public InputSourceReader buildObject(final String className) {
		return super.buildObject(className, DEFAULT_PACKAGE, InputSourceReader.class);
	}

}
