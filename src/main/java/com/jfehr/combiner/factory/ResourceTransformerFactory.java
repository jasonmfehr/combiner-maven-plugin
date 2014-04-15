package com.jfehr.combiner.factory;

import java.util.List;

import com.jfehr.combiner.transformer.ResourceTransformer;

public class ResourceTransformerFactory extends ObjectFactory {

	private static final String DEFAULT_PACKAGE = "com.jfehr.combiner.transformer";
	
	public ResourceTransformer buildObject(final String className) {
		return super.buildObject(className, DEFAULT_PACKAGE, ResourceTransformer.class);
	}
	
	public List<ResourceTransformer> buildObjectList(final List<String> classNames) {
		return super.buildObjectList(classNames, DEFAULT_PACKAGE, ResourceTransformer.class);
	}

}
