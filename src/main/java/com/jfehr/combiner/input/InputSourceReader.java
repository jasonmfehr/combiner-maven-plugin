package com.jfehr.combiner.input;

import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.jfehr.tojs.mojo.Setting;

public interface InputSourceReader {

	public Map<String, String> read(final String encoding, final List<String> includes, final List<String> excludes, final List<Setting> settings, final MavenProject mavenProject);
	
}
