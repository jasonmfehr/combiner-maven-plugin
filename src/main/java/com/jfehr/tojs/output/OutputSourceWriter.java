package com.jfehr.tojs.output;

import java.util.List;

import com.jfehr.tojs.mojo.Setting;


public interface OutputSourceWriter {

	public void write(final String encoding, final String outputDestination, final String combinedResources, final List<Setting> settings);
	
}
