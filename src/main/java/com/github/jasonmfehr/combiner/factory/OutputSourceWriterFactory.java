package com.github.jasonmfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.github.jasonmfehr.combiner.output.OutputSourceWriter;

@Component(role=OutputSourceWriterFactory.class)
public class OutputSourceWriterFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.github.jasonmfehr.combiner.output";

	@Override
	protected Class<?> getObjectClass() {
		return OutputSourceWriter.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}

}
