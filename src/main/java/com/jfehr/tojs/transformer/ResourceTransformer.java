package com.jfehr.tojs.transformer;

import java.util.List;

import com.jfehr.tojs.mojo.Setting;

public interface ResourceTransformer {

	public List<String> transform(final List<String> transformers, final List<String> inputResources, final List<Setting> settings);
}
