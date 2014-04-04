package com.jfehr.combiner.factory;

import com.jfehr.combiner.combiner.ResourceCombiner;
import com.jfehr.combiner.logging.ParameterizedLogger;

public class ResourceCombinerFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.combiner";
	
	public ResourceCombinerFactory(final ParameterizedLogger logger) {
		super(logger);
	}
	
	public ResourceCombiner buildObject(final String className) {
		return super.buildObject(className, DEFAULT_PACKAGE, ResourceCombiner.class);
	}

}
