package com.jfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.jfehr.combiner.combiner.ResourceCombiner;

@Component(role=ResourceCombinerFactory.class)
public class ResourceCombinerFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.combiner";

	@Override
	protected Class<?> getObjectClass() {
		return ResourceCombiner.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}

}
