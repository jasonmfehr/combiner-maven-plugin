package com.github.jasonmfehr.combiner.factory;

import org.codehaus.plexus.component.annotations.Component;

import com.github.jasonmfehr.combiner.transformer.ResourceTransformer;

@Component(role=ResourceTransformerFactory.class)
public class ResourceTransformerFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.github.jasonmfehr.combiner.transformer";

	@Override
	protected Class<?> getObjectClass() {
		return ResourceTransformer.class;
	}

	@Override
	protected String getDefaultPackage() {
		return DEFAULT_PACKAGE;
	}
	
}
