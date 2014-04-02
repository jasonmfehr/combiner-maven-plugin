package com.jfehr.combiner.combiner;

import java.util.List;
import java.util.Map;

import com.jfehr.tojs.mojo.Setting;

public interface ResourceCombiner {

	public String combine(final Map<String, String> transformedResourceContents, final List<Setting> settings);
	
}
