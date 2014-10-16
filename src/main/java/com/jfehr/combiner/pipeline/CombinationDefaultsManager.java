package com.jfehr.combiner.pipeline;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.util.List;
import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;

import com.jfehr.combiner.mojo.Combination;
import com.jfehr.combiner.mojo.CombinerMojo;

@Component(role=CombinationDefaultsManager.class)
public class CombinationDefaultsManager {

	private static final String INPUT_PARAM_USING_DEFAULT_VALUE_MSG = "{0} parameter not specified, using default of {1}";
	private static final String DEFAULT_INPUT_SOURCE_READER = "FileInputSourceReader";
	private static final String DEFAULT_OUTPUT_SOURCE_WRITER = "FileOutputSourceWriter";
	private static final String DEFAULT_ENCODING_PROPERTY = "project.build.sourceEncoding";
	
	//TODO figure out how to use the defaultValue from the Parameter annotation instead of directly setting fields
	/**
	 * Since maven does not set the default value from the {@link Parameter} annotation any place other than top level parameters, this 
	 * function sets the default values for it.
	 * 
	 * @param combo {@link Combination} combo that will have default values set in its fields
	 * @param mavenProject {@link MavenProject} object that was injected into the {@link CombinerMojo} by maven
	 */
	public void setupDefaults(final Combination combo, final MavenProject mavenProject) {
		final String encoding;
		
		getParamLogger().debug("Setting default values");
		
		if(!this.isFieldValid(combo.getInputSourceReader())){
			getParamLogger().debugWithParams(INPUT_PARAM_USING_DEFAULT_VALUE_MSG, CombinationValidator.INPUT_PARAM_INPUT_SOURCE_READER, DEFAULT_INPUT_SOURCE_READER);
			combo.setInputSourceReader(DEFAULT_INPUT_SOURCE_READER);
		}
		
		if(!this.isFieldValid(combo.getOutputSourceWriter())){
			getParamLogger().debugWithParams(INPUT_PARAM_USING_DEFAULT_VALUE_MSG, CombinationValidator.INPUT_PARAM_OUTPUT_SOURCE_WRITER, DEFAULT_OUTPUT_SOURCE_WRITER);
			combo.setOutputSourceWriter(DEFAULT_OUTPUT_SOURCE_WRITER);
		}
		
		if(!this.isFieldValid(combo.getEncoding())){
			encoding = mavenProject.getProperties().getProperty(DEFAULT_ENCODING_PROPERTY);
			getParamLogger().debugWithParams(INPUT_PARAM_USING_DEFAULT_VALUE_MSG, CombinationValidator.INPUT_PARAM_ENCODING, encoding);
			combo.setEncoding(encoding);
		}
		
		getParamLogger().debug("Finished setting default values");
	}
	
	/**
	 * Determines if a provided {@code value} is empty or not using three rules.  If the {@code value} is null, then it is not valid.  
	 * If the provided {@code value} is a {@link String} and its length is 0, then the {@code value} is not valid.  If the {@code value} 
	 * is a {@link List} or {@link Map} and it contains zero entries, then the {@code value} is not valid.  Otherwise, the 
	 * {@code value} is considered valid;
	 * 
	 * @param value {@link Object} to check for validity
	 * @return {@link Boolean#TRUE} is the {@code value} is valid or {@link Boolean#FALSE} if it is not valid
	 */
	private Boolean isFieldValid(final Object value) {
		if(value == null){
			return Boolean.FALSE;
		}
		
		if(value instanceof String && value.toString().length() == 0){
			return Boolean.FALSE;
		}
		
		/*
		if(value instanceof List<?> && ((List<?>)value).size() == 0){
			return Boolean.FALSE;
		}
		
		if(value instanceof Map<?, ?> && ((Map<?, ?>)value).size() == 0){
			return Boolean.FALSE;
		}
		*/
		
		return Boolean.TRUE;
	}
}
