package com.github.jasonmfehr.combiner.transformer;

import java.util.Map;

import org.apache.maven.project.MavenProject;

/**
 * <p>Resource transformers are executed in the second stage of the pipeline.  Their purpose is to manipulate the 
 * contents of a single resource before handing it to the third stage.  Examples of transformers are 
 * stripping out any newlines and escaping double quotes.</p>
 * 
 * <p>Implementing classes must not modify the inputs in any way.  Doing so will cause undesired and unpredictable results.</p> 
 * 
 * @author jasonmfehr
 * @since 1.0.0
 * 
 */
public interface ResourceTransformer {

	public String transform(final String resourceName, final String resourceContents, final Map<String, String> settings, final MavenProject mavenProject);
	
}
