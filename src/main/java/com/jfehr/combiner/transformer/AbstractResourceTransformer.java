package com.jfehr.combiner.transformer;

import static com.jfehr.combiner.mojo.LogHolder.getParamLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.project.MavenProject;

public abstract class AbstractResourceTransformer implements ResourceTransformer {

	private Map<String, String> resourceContents;
	private Map<String, String> settings;
	private MavenProject mavenProject;
	
	public Map<String, String> transform(final Map<String, String> resourceContents, final Map<String, String> settings, final MavenProject mavenProject) {
		getParamLogger().debugWithParams("Executing resource transformer {0}", this.getClass().getName());
		
		this.resourceContents = resourceContents;
		this.settings = settings;
		this.mavenProject = mavenProject;
		
		return this.execute();
	}
	
	protected abstract String doTransform(final String resourceKey, final String resourceValue);
	
	protected Map<String, String> execute() {
		final Map<String, String> transformedResources = new HashMap<String, String>();
		
		for(final Entry<String, String> rcEntry : this.resourceContents.entrySet()){
			transformedResources.put(rcEntry.getKey(), this.doTransform(rcEntry.getKey(), rcEntry.getValue()));
		}
		
		return transformedResources;
	}
	
	protected Map<String, String> getResourceContents() {
		return this.resourceContents;
	}
	
	protected Map<String, String> getSettings() {
		return this.settings;
	}
	
	protected MavenProject getMavenProject() {
		return this.mavenProject;
	}

}
