package com.github.jasonmfehr.combiner.input;

import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

public interface InputSourceReader {

	public Map<String, String> read(final String encoding, final List<String> includes, final List<String> excludes, final Map<String, String> settings, final MavenProject mavenProject);
	
}
