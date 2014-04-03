package com.jfehr.combiner.output;

import java.util.List;

import org.apache.maven.project.MavenProject;

import com.jfehr.tojs.mojo.Setting;


public interface OutputSourceWriter {

	public void write(final String encoding, final String outputDestination, final String combinedResources, final List<Setting> settings, final MavenProject mavenProject);
	
}
