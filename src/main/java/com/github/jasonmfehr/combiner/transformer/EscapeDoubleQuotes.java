package com.github.jasonmfehr.combiner.transformer;

import java.util.Map;

import org.apache.maven.project.MavenProject;


public class EscapeDoubleQuotes implements ResourceTransformer {

	public String transform(final String resourceName, String resourceContents, Map<String, String> settings, MavenProject mavenProject) {
		//replace all non-escaped instances of double quotes with an escaped double quote
		return resourceContents.replaceAll("(?<!\\\\)\"", "\\\\\"");
	}

}
