package com.jfehr.tojs.parser;

public class StripNewlines implements ToJsParser {

	public String parse(final String value) {
		return value.replaceAll("\r|\n", "");
	}

}
