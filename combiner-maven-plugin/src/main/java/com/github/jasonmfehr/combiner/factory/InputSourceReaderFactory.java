package com.github.jasonmfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.github.jasonmfehr.combiner.input.InputSourceReader;

@Component(role=InputSourceReaderFactory.class)
public class InputSourceReaderFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.github.jasonmfehr.combiner.input";

	@Override
	protected Class<?> getObjectClass() {
		return InputSourceReader.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}

}
