package com.jfehr.combiner.combiner;

import java.util.Map;

import org.apache.maven.project.MavenProject;

public interface ResourceCombiner {

	public String combine(final Map<String, String> transformedResourceContents, final Map<String, String> settings, final MavenProject mavenProject);
	
}
