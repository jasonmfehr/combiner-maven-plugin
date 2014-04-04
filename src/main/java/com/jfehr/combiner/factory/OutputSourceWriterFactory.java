package com.jfehr.combiner.factory;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.output.OutputSourceWriter;

public class OutputSourceWriterFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.output";

	public OutputSourceWriterFactory(final ParameterizedLogger logger) {
		super(logger);
	}
	
	public OutputSourceWriter buildObject(final String className) {
		return super.buildObject(className, DEFAULT_PACKAGE, OutputSourceWriter.class);
	}

}
