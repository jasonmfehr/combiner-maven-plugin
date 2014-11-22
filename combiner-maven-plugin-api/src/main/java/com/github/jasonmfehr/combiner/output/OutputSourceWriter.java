package com.github.jasonmfehr.combiner.output;

import java.util.Map;

import org.apache.maven.project.MavenProject;


public interface OutputSourceWriter {

	public void write(final String encoding, final String outputDestination, final String combinedResources, final Map<String, String> settings, final MavenProject mavenProject);
	
}
