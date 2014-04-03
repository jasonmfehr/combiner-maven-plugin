package com.jfehr.combiner.combiner;

import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.mojo.Setting;

public interface ResourceCombiner {

	public String combine(final Map<String, String> transformedResourceContents, final List<Setting> settings, final MavenProject mavenProject);
	
}
