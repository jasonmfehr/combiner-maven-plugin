package com.jfehr.combiner.transformer;

import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.mojo.Setting;

public interface ResourceTransformer {

	public Map<String, String> transform(final Map<String, String> resourceContents, final List<Setting> settings, final MavenProject mavenProject);
	
}
