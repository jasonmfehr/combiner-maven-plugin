package com.jfehr.combiner.transformer;

import java.util.List;

import com.jfehr.tojs.mojo.Setting;

public class StripNewlines implements ResourceTransformer {

	public String transform(String resourceContents, List<Setting> settings) {
		return resourceContents.replaceAll("\r|\n", "");
	}

}
