package com.jfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.jfehr.combiner.output.OutputSourceWriter;

@Component(role=OutputSourceWriterFactory.class)
public class OutputSourceWriterFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.output";

	@Override
	protected Class<?> getObjectClass() {
		return OutputSourceWriter.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}

}
