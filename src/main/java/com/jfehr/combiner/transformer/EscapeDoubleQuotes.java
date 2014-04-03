package com.jfehr.combiner.transformer;

import java.util.List;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.mojo.Setting;

public class EscapeDoubleQuotes implements ResourceTransformer {

	public String transform(String resourceContents, List<Setting> settings, final MavenProject mavenProject) {
		//replace all non-escaped instances of double quotes with an escaped double quote
		return resourceContents.replaceAll("(?<!\\\\)\"", "\\\\\"");
	}

}
