package com.jfehr.combiner.transformer;


public class EscapeDoubleQuotes extends AbstractResourceTransformer {

	@Override
	protected String doTransform(String resourceKey, String resourceValue) {
		//replace all non-escaped instances of double quotes with an escaped double quote
		return resourceValue.replaceAll("(?<!\\\\)\"", "\\\\\"");
	}

}
