package com.jfehr.tojs.combiner;

import java.util.List;

import org.apache.maven.project.MavenProject;

import com.jfehr.tojs.mojo.Setting;

public interface ResourceCombiner {

	public String combine(final String combiner, final List<String> inputResources, final List<Setting> settings, final MavenProject mavenProject);
	
}
