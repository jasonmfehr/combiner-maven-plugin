package com.jfehr.combiner.transformer;

import com.jfehr.combiner.logging.ParameterizedLogger;

public class EscapeDoubleQuotes extends AbstractResourceTransformer {

	public EscapeDoubleQuotes(final ParameterizedLogger logger) {
		super(logger);
	}

	@Override
	protected String doTransform(String resourceKey, String resourceValue) {
		//replace all non-escaped instances of double quotes with an escaped double quote
		return resourceValue.replaceAll("(?<!\\\\)\"", "\\\\\"");
	}

}
