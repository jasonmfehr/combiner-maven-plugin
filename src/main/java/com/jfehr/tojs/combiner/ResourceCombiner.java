package com.jfehr.tojs.combiner;

import java.util.List;

import com.jfehr.tojs.mojo.Setting;

public interface ResourceCombiner {

	public String combine(final String combiner, final List<String> inputResources, final List<Setting> settings);
}
