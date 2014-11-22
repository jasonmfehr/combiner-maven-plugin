package com.github.jasonmfehr.combiner.combiner;

import java.util.Map;

import org.apache.maven.project.MavenProject;

public class MockResourceCombiner implements ResourceCombiner {

	public String combine(final Map<String, String> transformedResourceContents, final Map<String, String> settings, final MavenProject mavenProject) {
		return null;
	}

}
