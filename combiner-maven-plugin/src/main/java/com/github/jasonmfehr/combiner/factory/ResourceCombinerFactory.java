package com.github.jasonmfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.github.jasonmfehr.combiner.combiner.ResourceCombiner;

@Component(role=ResourceCombinerFactory.class)
public class ResourceCombinerFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.github.jasonmfehr.combiner.combiner";

	@Override
	protected Class<?> getObjectClass() {
		return ResourceCombiner.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}

}
