package com.jfehr.combiner.transformer;

import com.jfehr.combiner.logging.ParameterizedLogger;

public class StripNewlines extends AbstractResourceTransformer {

	public StripNewlines(final ParameterizedLogger logger) {
		super(logger);
	}

	@Override
	protected String doTransform(final String resourceKey, final String resourceValue) {
		return resourceValue.replaceAll("\r|\n", "");
	}

}
