package com.github.jasonmfehr.combiner.combiner;

import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;

/**
 * {@link ResourceCombiner} implementation that appends the contents of each resource onto a single String.
 * 
 * This class has a setting that can be placed in the configuration of the combiner-maven-plugin.  The setting is named 
 * appendingCombinerNewlines and, if specified, contains an {@link Integer} that determines the number of platform specific 
 * newline characters inserted between the contents of each resource.  The default of this value is zero.
 * 
 * Example of specifying the value to set the number of newlines between resources: 
 * <appendingCombinerNewlines>1</appendingCombinerNewlines>
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
@Component(role=AppendingCombiner.class)
public class AppendingCombiner implements ResourceCombiner {

	private static final String NUMBER_NEWLINES_KEY = "appendingCombinerNewlines";
	
	public String combine(Map<String, String> transformedResourceContents, Map<String, String> settings, MavenProject mavenProject) {
		final StringBuilder combinedResources = new StringBuilder();
		String numberOfNewlines = "";
		
		if(settings.containsKey(NUMBER_NEWLINES_KEY)){
			int newlines = Integer.parseInt(settings.get(NUMBER_NEWLINES_KEY));
			for(int i=0; i<newlines; i++){
				numberOfNewlines += String.format("%n");
			}
		}
		
		for(String resource : transformedResourceContents.values()){
			combinedResources.append(resource).append(numberOfNewlines);
		}
		
		return combinedResources.toString();
	}


}
