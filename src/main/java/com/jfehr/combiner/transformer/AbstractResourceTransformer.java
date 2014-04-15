package com.jfehr.combiner.transformer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.logging.ParameterizedLogger;

public abstract class AbstractResourceTransformer implements ResourceTransformer {

	private final ParameterizedLogger logger;
	private Map<String, String> resourceContents;
	private Map<String, String> settings;
	private MavenProject mavenProject;
	
	public AbstractResourceTransformer(final ParameterizedLogger logger) {
		this.logger = logger;
	}

	public Map<String, String> transform(final Map<String, String> resourceContents, final Map<String, String> settings, final MavenProject mavenProject) {
		logger.debugWithParams("Executing resource transformer {0}", this.getClass().getName());
		
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
	
	protected ParameterizedLogger getLogger() {
		return this.logger;
	}

}
