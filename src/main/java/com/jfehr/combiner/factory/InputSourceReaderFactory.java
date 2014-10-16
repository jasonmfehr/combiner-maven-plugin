package com.jfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.jfehr.combiner.input.InputSourceReader;

@Component(role=InputSourceReaderFactory.class)
public class InputSourceReaderFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.input";

	@Override
	protected Class<?> getObjectClass() {
		return InputSourceReader.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}

}
