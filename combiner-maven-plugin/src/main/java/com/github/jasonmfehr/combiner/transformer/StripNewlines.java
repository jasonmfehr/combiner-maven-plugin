package com.github.jasonmfehr.combiner.transformer;

import java.util.Map;

import org.apache.maven.project.MavenProject;


public class StripNewlines implements ResourceTransformer {

	public String transform(final String resourceName, String resourceContents, Map<String, String> settings, MavenProject mavenProject) {
		return resourceContents.replaceAll("\r|\n", "");
	}

}
