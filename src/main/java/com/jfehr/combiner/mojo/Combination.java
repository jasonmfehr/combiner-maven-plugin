package com.jfehr.combiner.mojo;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

import com.jfehr.combiner.combiner.ResourceCombiner;
import com.jfehr.combiner.input.InputSourceReader;
import com.jfehr.combiner.output.OutputSourceWriter;
import com.jfehr.combiner.transformer.ResourceTransformer;

public class Combination {

	/**
	 * List of resources that will be used as the inputs to the combiner.
	 */
	@Parameter(required=true)
	private InputSources inputSources;
	
	/**
	 * {@link InputSourceReader} class to use when reading the inputSources.  If no package is specified, 
	 * defaults to {@code com.jfehr.combiner.input} 
	 */
	@Parameter(required=true, property="combiner.inputSourceReader", defaultValue="FileInputSourceReader")
	private String inputSourceReader;
	
	/**
	 * List of the {@link ResourceTransformer} classes that will be applied to each input source.  
	 * If no package is specified, defaults to {@code com.jfehr.combiner.transformer}.  
	 * Since this is an optional parameter, if no transformers are specified, then 
	 * no transformations will be applied.
	 */
	@Parameter
	private List<String> transformers;
	
	/**
	 * {@link ResourceCombiner} that will be used to combine all the input sources.  
	 * If no package is specified, defaults to {@code com.jfehr.combiner.combiner}
	 */
	@Parameter(required=true, property="combiner.combiner")
	private String combiner;
	
	/**
	 * {@link OutputSourceWriter} class to use to write out the combined contents.  If no package is specified, 
	 * defaults to {@code com.jfehr.combiner.output} 
	 */
	@Parameter(required=true, property="combiner.outputSourceWriter", defaultValue="FileOutputSourceWriter")
	private String outputSourceWriter;
	
	/**
	 * Location of the resource where the combined inputs will be written.
	 */
	@Parameter(required=true, property="combiner.destination")
	private String outputDestination;
	
	/**
	 * The charset to use when reading and writing the resources.
	 */
	@Parameter(required=true, defaultValue="${project.build.sourceEncoding}", property="combiner.encoding")
	private String encoding;
	
	/**
	 * List of key-value pairs that are used to provide additional 
	 * configuration to the combining pipeline.  There are no restrictions on 
	 * what can be put here.  Each pipeline stage implementation chooses what 
	 * settings it needs (if any).
	 */
	@Parameter
	private List<Setting> settings;

	public InputSources getInputSources() {
		return inputSources;
	}
	public void setInputSources(InputSources inputSources) {
		this.inputSources = inputSources;
	}

	public String getInputSourceReader() {
		return inputSourceReader;
	}
	public void setInputSourceReader(String inputSourceReader) {
		this.inputSourceReader = inputSourceReader;
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
	
	public String getOutputSourceWriter() {
		return outputSourceWriter;
	}
	public void setOutputSourceWriter(String outputSourceWriter) {
		this.outputSourceWriter = outputSourceWriter;
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
