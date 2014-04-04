package com.jfehr.combiner.factory;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.transformer.ResourceTransformer;

public class ResourceTransformerFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.transformer";
	
	public ResourceTransformerFactory(final ParameterizedLogger logger) {
		super(logger);
	}
	
	public ResourceTransformer buildObject(final String className) {
		return super.buildObject(className, DEFAULT_PACKAGE, ResourceTransformer.class);
	}

}
