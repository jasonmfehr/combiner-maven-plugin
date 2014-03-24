package com.jfehr.tojs.input;

import java.util.List;

import com.jfehr.tojs.mojo.Setting;

public interface InputSourceReader {

	public String read(final String encoding, final List<String> includes, final List<String> excludes, final List<Setting> settings);
}
