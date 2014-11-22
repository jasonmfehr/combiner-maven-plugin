package com.github.jasonmfehr.combiner.transformer;

import java.util.Map;

import org.apache.maven.project.MavenProject;

public class MockResourceTransformer implements ResourceTransformer {

	public String transform(final String resourceName, final String resourceContents, final Map<String, String> settings, final MavenProject mavenProject) {
		return null;
	}

}
