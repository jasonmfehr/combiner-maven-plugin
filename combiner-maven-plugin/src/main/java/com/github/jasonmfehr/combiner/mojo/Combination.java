package com.github.jasonmfehr.combiner.mojo;

import java.util.List;
import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;

import com.github.jasonmfehr.combiner.combiner.ResourceCombiner;
import com.github.jasonmfehr.combiner.input.InputSourceReader;
import com.github.jasonmfehr.combiner.output.OutputSourceWriter;
import com.github.jasonmfehr.combiner.transformer.ResourceTransformer;

/**
 * Specifies a single combination of all configuration required to run a full pipeline cycle.
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
public class Combination {

	/**
	 * String that uniquely identifies this particular combination.  This 
	 * parameter is optional and only used in log messages.  Specifying 
	 * a value is recommended when using more than one combination as it  
	 * will be much easier to identify which combination set has the problem.
	 */
	@Parameter
	private String id;
	
	/**
	 * List of resources that will be used as the inputs to the combiner.
	 */
	@Parameter(required=true)
	private InputResources inputResources;
	
	/**
	 * {@link InputSourceReader} class to use when reading the inputSources.  If no package is specified, 
	 * defaults to {@code com.github.jasonmfehr.combiner.input} 
	 */
	@Parameter(required=true, defaultValue="FileInputSourceReader")
	private String inputSourceReader;
	
	/**
	 * List of the {@link ResourceTransformer} classes that will be applied to each input source.  
	 * If no package is specified, defaults to {@code com.github.jasonmfehr.combiner.transformer}.  
	 * Since this is an optional parameter, if no transformers are specified, then 
	 * no transformations will be applied.
	 */
	@Parameter
	private List<String> transformers;
	
	/**
	 * {@link ResourceCombiner} that will be used to combine all the input sources.  
	 * If no package is specified, defaults to {@code com.github.jasonmfehr.combiner.combiner}
	 */
	@Parameter(required=true)
	private String combiner;
	
	/**
	 * {@link OutputSourceWriter} class to use to write out the combined contents.  If no package is specified, 
	 * defaults to {@code com.github.jasonmfehr.combiner.output} 
	 */
	@Parameter(required=true, defaultValue="FileOutputSourceWriter")
	private String outputSourceWriter;
	
	/**
	 * Location of the resource where the combined inputs will be written.
	 */
	@Parameter(required=true)
	private String outputDestination;
	
	/**
	 * The charset to use when reading and writing the resources.
	 */
	@Parameter(required=true, defaultValue="${project.build.sourceEncoding}")
	private String encoding;
	
	/**
	 * List of key-value pairs that are used to provide additional 
	 * configuration to the combining pipeline.  There are no restrictions on 
	 * what can be put here.  Each pipeline stage implementation chooses what 
	 * settings it needs (if any).  Format is &lt;key&gt;value&lt;/key&gt;
	 */
	@Parameter
	private Map<String, String> settings;

	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public InputResources getInputSources() {
		return inputResources;
	}
	public void setInputSources(InputResources inputSources) {
		this.inputResources = inputSources;
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

	public Map<String, String> getSettings() {
		return settings;
	}
	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
	
}
