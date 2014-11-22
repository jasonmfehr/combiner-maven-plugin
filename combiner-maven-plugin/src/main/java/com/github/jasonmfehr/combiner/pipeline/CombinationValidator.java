package com.github.jasonmfehr.combiner.pipeline;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;
import com.github.jasonmfehr.combiner.mojo.Combination;

@Component(role=CombinationValidator.class)
public class CombinationValidator {

	protected static final String INPUT_PARAM_INPUT_SOURCE_READER = "inputSourceReader";
	protected static final String INPUT_PARAM_OUTPUT_SOURCE_WRITER = "outputSourceWriter";
	protected static final String INPUT_PARAM_ENCODING = "encoding";
	protected static final String INPUT_PARAM_TRANSFORMERS = "transformers";
	
	private static final String INPUT_PARAM_INPUT_SOURCES = "inputSources";
	private static final String INPUT_PARAM_INPUT_SOURCES_INCLUDES = "includes";
	private static final String INPUT_PARAM_OUTPUT_DESTINATION = "outputDestination";
	
	private static final String REQUIRED_PARAM_SPECIFIED_MSG = "The {0} parameter was specified";
	private static final String REQUIRED_PARAM_CHECKING_MSG = "Checking if the required {0} parameter was specified";
	private static final String REQUIRED_PARAM_NOT_SPECIFIED_MSG = "Required {0} parameter was not specified";
	
	@Requirement
	private ParameterizedLogger logger;
	
	/**
	 * Validates that all required parameters have been specified (or appropriate defaults have been set) in the pom.  
	 * Missing or invalid  parameters will result in an exception being thrown.
	 * 
	 * @param combo {@link Combination} combo that will have its default values set
	 */
	public void validate(final Combination combo) {
		this.validateRequiredInputs(combo);
	}
	
	/**
	 * Verifies that all required fields are present.  It does check parameters that have a default value set since the checks are 
	 * quick and it guards against accidental removal of the default values.
	 * 
	 * @param combo {@link Combination} combo that will have its required fields checked
	 */
	private void validateRequiredInputs(final Combination combo) {
		this.logger.debugWithParams("Validating that required input parameters are present");
		
		try{
			this.validateField(INPUT_PARAM_ENCODING, combo.getEncoding(), combo);
			Charset.isSupported(combo.getEncoding());
			this.logger.debugWithParams(REQUIRED_PARAM_SPECIFIED_MSG + " and is a valid charset", INPUT_PARAM_ENCODING);
		}catch(IllegalCharsetNameException icne){
			this.logger.debugWithParams("Provided charset of {0} is not a valid charset name according to the rules specified in the javadoc of the Charset class", combo.getEncoding());
			throw new IllegalArgumentException("The specified encoding [" + combo.getEncoding() + "] is invalid", icne);
		}
		
		this.validateField(INPUT_PARAM_INPUT_SOURCE_READER, combo.getInputSourceReader(), combo);
		this.validateField(INPUT_PARAM_INPUT_SOURCES, combo.getInputSources(), combo);
		this.validateField(INPUT_PARAM_INPUT_SOURCES_INCLUDES, combo.getInputSources().getIncludes(), combo);
		this.validateField(INPUT_PARAM_OUTPUT_DESTINATION, combo.getOutputDestination(), combo);
		this.validateField(INPUT_PARAM_OUTPUT_SOURCE_WRITER, combo.getOutputSourceWriter(), combo);
		//this.validateField(INPUT_PARAM_TRANSFORMERS, combo.getTransformers(), combo);
		
		this.logger.debugWithParams("Finished validating required input parameters.  All parameters are present.");
	}
	
	private void validateField(final String fieldName, final Object fieldValue, final Combination combo) {
		final Integer length;
		
		this.logger.debugWithParams(REQUIRED_PARAM_CHECKING_MSG, fieldName);
		
		if(fieldValue instanceof String){
			length = fieldValue.toString().length();
		}else if(fieldValue instanceof List){
			length = ((List<?>)fieldValue).size();
		}else if(fieldValue instanceof Map){
			length = ((Map<?, ?>)fieldValue).size();
		}else{
			//set length to 1 which will cause only the null check to matter
			length = Integer.valueOf(1);
		}
		
		if(fieldValue == null || length == 0){
			this.logger.debugWithParams(REQUIRED_PARAM_NOT_SPECIFIED_MSG, fieldName);
			throw new IllegalArgumentException("The " + fieldName + " parameter is required");
		}
		this.logger.debugWithParams(REQUIRED_PARAM_SPECIFIED_MSG, fieldValue);
	}
}
