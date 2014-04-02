package com.jfehr.combiner.transformer;

import java.util.List;

import com.jfehr.tojs.mojo.Setting;

public class EscapeDoubleQuotes implements ResourceTransformer {

	public String transform(String resourceContents, List<Setting> settings) {
		//replace all non-escaped instances of double quotes with an escaped double quote
		return resourceContents.replaceAll("(?<!\\\\)\"", "\\\\\"");
	}

}
