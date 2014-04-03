package com.jfehr.combiner.transformer;

import java.util.List;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.mojo.Setting;

public interface ResourceTransformer {

	public String transform(final String resourceContents, final List<Setting> settings, final MavenProject mavenProject);
	
}
