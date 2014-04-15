package com.jfehr.combiner.transformer;

import java.util.Map;

import org.apache.maven.project.MavenProject;

public interface ResourceTransformer {

	public Map<String, String> transform(final Map<String, String> resourceContents, final Map<String, String> settings, final MavenProject mavenProject);
	
}
