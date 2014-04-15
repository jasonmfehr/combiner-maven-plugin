package com.jfehr.combiner.input;

import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

public class MockInputSourceReader implements InputSourceReader {

	public Map<String, String> read(final String encoding, List<String> includes, final List<String> excludes, final Map<String, String> settings, final MavenProject mavenProject) {
		return null;
	}

}
