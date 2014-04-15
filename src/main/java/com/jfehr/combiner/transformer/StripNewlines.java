package com.jfehr.combiner.transformer;


public class StripNewlines extends AbstractResourceTransformer {

	@Override
	protected String doTransform(final String resourceKey, final String resourceValue) {
		return resourceValue.replaceAll("\r|\n", "");
	}

}
