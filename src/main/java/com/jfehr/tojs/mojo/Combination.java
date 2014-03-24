package com.jfehr.tojs.mojo;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

public class Combination {

	/**
	 * list of resources that will be used as the inputs to the combiner
	 */
	@Parameter(required=true)
	private InputSources inputSources;
	
	/**
	 * list of the transformers that will be applied to each input source
	 */
	@Parameter
	private List<String> transformers;
	
	/**
	 * combiner that will be used to combine all the input sources
	 */
	@Parameter(property="combiner.combiner")
	private String combiner;
	
	/**
	 * location of the resource where the combined inputs will be written
	 */
	@Parameter(required=true, property="combiner.destination")
	private String outputDestination;
	
	/**
	 * the charset to use when reading and writing the resources
	 */
	@Parameter(required=true, defaultValue="${project.build.sourceEncoding}", property="combiner.encoding")
	private String encoding;
	
	/**
	 * list of key-value pairs that are used to provide additional 
	 * configuration to the combining pipeline
	 */
	@Parameter
	private List<Setting> settings;

	public InputSources getInputSources() {
		return inputSources;
	}
	public void setInputSources(InputSources inputSources) {
		this.inputSources = inputSources;
	}

	public List<String> getTransformers() {
		return transformers;
	}
	public void setTransformers(List<String> transformers) {
		this.transformers = transformers;
	}

	public String getCombiner() {
		return combiner;
	}
	public void setCombiner(String combiner) {
		this.combiner = combiner;
	}

	public String getOutputDestination() {
		return outputDestination;
	}
	public void setOutputDestination(String outputDestination) {
		this.outputDestination = outputDestination;
	}

	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public List<Setting> getSettings() {
		return settings;
	}
	public void setSettings(List<Setting> settings) {
		this.settings = settings;
	}
	
}
