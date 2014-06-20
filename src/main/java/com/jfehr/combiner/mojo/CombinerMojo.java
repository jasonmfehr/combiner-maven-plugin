package com.jfehr.combiner.mojo;

import static com.jfehr.combiner.mojo.LogHolder.getParamLogger;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.combiner.ResourceCombiner;
import com.jfehr.combiner.factory.InputSourceReaderFactory;
import com.jfehr.combiner.factory.OutputSourceWriterFactory;
import com.jfehr.combiner.factory.ResourceCombinerFactory;
import com.jfehr.combiner.factory.ResourceTransformerFactory;
import com.jfehr.combiner.input.InputSourceReader;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.output.OutputSourceWriter;
import com.jfehr.combiner.transformer.ResourceTransformer;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
@Mojo(name="combine", defaultPhase=LifecyclePhase.PROCESS_SOURCES, threadSafe=true)
public class CombinerMojo extends AbstractMojo {

	private static final String DEFAULT_INPUT_SOURCE_READER = "FileInputSourceReader";
	private static final String DEFAULT_OUTPUT_SOURCE_WRITER = "FileOutputSourceWriter";
	private static final String DEFAULT_ENCODING_PROPERTY = "project.build.sourceEncoding";
	
	private static final String INPUT_PARAM_ENCODING = "encoding";
	private static final String INPUT_PARAM_INPUT_SOURCE_READER = "inputSourceReader";
	private static final String INPUT_PARAM_OUTPUT_SOURCE_WRITER = "outputSourceWriter";
	private static final String INPUT_PARAM_INPUT_SOURCES = "inputSources";
	private static final String INPUT_PARAM_INPUT_SOURCES_INCLUDES = "includes";
	private static final String INPUT_PARAM_OUTPUT_DESTINATION = "outputDestination";
	private static final String INPUT_PARAM_TRANSFORMERS = "transformers";
	
	private static final String ID_PROVIDED_MSG = " for combination with id ";
	private static final String INPUT_PARAM_USING_DEFAULT_VALUE_MSG = "{0} parameter not specified, using default of {1}";
	private static final String REQUIRED_PARAM_SPECIFIED_MSG = "The {0} parameter was specified";
	private static final String REQUIRED_PARAM_CHECKING_MSG = "Checking if the required {0} parameter was specified";
	private static final String REQUIRED_PARAM_NOT_SPECIFIED_MSG = "Required {0} parameter was not specified";
	
	/**
	 * list of combinations with each one representing a set of 
	 * resources to combine and the pipeline implementations to 
	 * use when combining them
	 */
	@Parameter(required=true)
	private List<Combination> combinations;
	
	/**
	 * determines if the plugin execution should be skipped
	 */
	@Parameter(property="tojs.skip", defaultValue="false")
	private Boolean skip;
	
	@Component
	private MavenProject mavenProject;
	
	@Component
	private MojoExecution mojoExecution;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		LogHolder.setLog(new ParameterizedLogger(this.getLog()));
		
		if(!Boolean.TRUE.equals(this.skip)){
			getParamLogger().debugWithParams("Entering {0} goal", mojoExecution.getGoal());
			for(Combination c : this.combinations){
				//this.executeCombinationOld(c, logger);
				this.executeCombination(c);
			}
			
			getParamLogger().debugWithParams("Exiting {0} goal", mojoExecution.getGoal());
		}else{
			getParamLogger().info("skipping combiner-maven-plugin execution");
		}
	}
	
	//TODO start here by moving functionality from executeCombinationOld into this function
	private void executeCombination(final Combination combo) {
		final String combinedResources;
		Map<String, String> sources;
		
		getParamLogger().debugWithParams("Executing pipeline{0}", this.idString(combo));
		
		this.debugLogInputs(combo);
		this.setDefaults(combo);
		this.validateRequiredInputs(combo);
		
		sources = this.readInputSources(combo);
		sources = this.executeTransformers(combo, sources);
		combinedResources = this.combineResources(combo, sources);
		this.outputResources(combo, combinedResources);
		
		getParamLogger().debugWithParams("Completed executing pipeline{0}", this.idString(combo));
	}
	
	/**
	 * Executes stage one of the pipeline which reads all the input sources.
	 * 
	 * @param combo executes the read input stage of the pipeline on this {@link Combination} 
	 * @return {@link Map} with a key of the resource name and a value of the resource contents
	 */
	private Map<String, String> readInputSources(final Combination combo) {
		final InputSourceReaderFactory isFactory;
		final InputSourceReader isReader;
		final Map<String, String> resources;
		
		getParamLogger().debug("Executing pipeline stage one - read input sources");
		
		isFactory = new InputSourceReaderFactory();
		isReader = isFactory.buildObject(combo.getInputSourceReader());
		resources = isReader.read(combo.getEncoding(), combo.getInputSources().getIncludes(), combo.getInputSources().getExcludes(), combo.getSettings(), this.mavenProject);
		
		getParamLogger().debug("Completed execution of pipeline stage one - read input sources");
		
		return resources;
	}
	
	/**
	 * Executes stage two of the pipeline which applies transformers to the resources.
	 * 
	 * @param combo executes the transform stage of the pipeline on this {@link Combination}
	 * @param sources in-out parameter that <b>will be modified</b> containing the sources that were 
	 *                read in the first stage of the pipeline
	 * @return {@link Map} with a key of the resource name and a value of the resource contents that were transformed
	 */
	private Map<String, String> executeTransformers(final Combination combo, Map<String, String> sources) {
		final ResourceTransformerFactory transformerFactory;
		final List<ResourceTransformer> tranformers;
		Map<String, String> transformedSources;
		
		getParamLogger().debug("Executing pipeline stage two - transform resources");
		
		transformerFactory = new ResourceTransformerFactory();
		tranformers = transformerFactory.buildObjectList(combo.getTransformers());
		transformedSources = sources;
		
		for(ResourceTransformer rt : tranformers){
			getParamLogger().debugWithParams("Executing resource transformer with class {0}", rt.getClass().getName());
			transformedSources = rt.transform(transformedSources, combo.getSettings(), this.mavenProject);
			getParamLogger().debugWithParams("Finished executing resource transformer with class {0}", rt.getClass().getName());
		}
		
		getParamLogger().debug("Completed execution of pipeline stage two - transform resources");
		
		return transformedSources;
	}
	
	private String combineResources(final Combination combo, final Map<String, String> resources) {
		final ResourceCombinerFactory combinerFactory;
		final ResourceCombiner combiner;
		final String combinedResources;
		
		getParamLogger().debug("Executing pipeline stage three - combined resources");
		
		combinerFactory = new ResourceCombinerFactory();
		combiner = combinerFactory.buildObject(combo.getCombiner());
		combinedResources = combiner.combine(resources, combo.getSettings(), this.mavenProject);
		
		getParamLogger().debug("Completed execution of pipeline stage three - combined resources");
		
		return combinedResources;
	}
	
	private void outputResources(final Combination combo, final String combinedResources) {
		final OutputSourceWriterFactory osFactory;;
		final OutputSourceWriter osWriter;
		
		getParamLogger().debug("Executing pipeline stage four - output resources");
		
		osFactory = new OutputSourceWriterFactory();
		osWriter = osFactory.buildObject(combo.getOutputSourceWriter());
		osWriter.write(combo.getEncoding(), combo.getOutputDestination(), combinedResources, combo.getSettings(), this.mavenProject);
		
		getParamLogger().debug("Completed execution of pipeline stage four - output resources");
	}
	
	//TODO figure out how to use the defaultValue from the Parameter annotation instead of directly setting fields
	/**
	 * Since maven does not set the default value from the {@link Parameter} annotation any place other than top level parameters, this 
	 * function sets the default values for it.
	 * 
	 * @param combo {@link Combination} combo that will have its default values set
	 */
	private void setDefaults(final Combination combo) {
		final String encoding;
		
		getParamLogger().debugWithParams("Setting default values{0}", this.idString(combo));
		
		if(this.isFieldEmpty(combo.getInputSourceReader())){
			getParamLogger().debugWithParams(INPUT_PARAM_USING_DEFAULT_VALUE_MSG, INPUT_PARAM_INPUT_SOURCE_READER, DEFAULT_INPUT_SOURCE_READER);
			combo.setInputSourceReader(DEFAULT_INPUT_SOURCE_READER);
		}
		
		if(this.isFieldEmpty(combo.getOutputSourceWriter())){
			getParamLogger().debugWithParams(INPUT_PARAM_USING_DEFAULT_VALUE_MSG, INPUT_PARAM_OUTPUT_SOURCE_WRITER, DEFAULT_OUTPUT_SOURCE_WRITER);
			combo.setOutputSourceWriter(DEFAULT_OUTPUT_SOURCE_WRITER);
		}
		
		if(this.isFieldEmpty(combo.getEncoding())){
			encoding = this.mavenProject.getProperties().getProperty(DEFAULT_ENCODING_PROPERTY);
			getParamLogger().debugWithParams(INPUT_PARAM_USING_DEFAULT_VALUE_MSG, INPUT_PARAM_ENCODING, encoding);
			combo.setEncoding(encoding);
		}
		
		getParamLogger().debugWithParams("Finished setting default values{0}", this.idString(combo));
	}
	
	/**
	 * Verifies that all required fields are present.  It does check parameters that have a default value set since the checks are 
	 * quick and it guards against accidental removal of the default values.
	 * 
	 * @param combo {@link Combination} combo that will have its required fields checked
	 */
	//TODO this should not check default parameters, the unit tests should guard against their accidental removal
	private void validateRequiredInputs(final Combination combo) {
		getParamLogger().debugWithParams("Validating that required input parameters are present");
		
		try{
			getParamLogger().debugWithParams(REQUIRED_PARAM_CHECKING_MSG, INPUT_PARAM_ENCODING);
			this.validateField(INPUT_PARAM_ENCODING, combo.getEncoding(), combo);
			Charset.isSupported(combo.getEncoding());
			getParamLogger().debugWithParams(REQUIRED_PARAM_SPECIFIED_MSG + " and is a valid charset", INPUT_PARAM_ENCODING);
		}catch(IllegalCharsetNameException icne){
			getParamLogger().debugWithParams("Provided charset of {0} is not a valid charset name according to the rules specified in the javadoc of the Charset class", combo.getEncoding());
			throw new IllegalArgumentException("The specified encoding [" + combo.getEncoding() + "] is invalid" + this.idString(combo), icne);
		}
		
		this.validateField(INPUT_PARAM_INPUT_SOURCE_READER, combo.getInputSourceReader(), combo);
		this.validateField(INPUT_PARAM_INPUT_SOURCES, combo.getInputSources(), combo);
		this.validateField(INPUT_PARAM_INPUT_SOURCES_INCLUDES, combo.getInputSources().getIncludes(), combo);
		this.validateField(INPUT_PARAM_OUTPUT_DESTINATION, combo.getOutputDestination(), combo);
		this.validateField(INPUT_PARAM_OUTPUT_SOURCE_WRITER, combo.getOutputSourceWriter(), combo);
		this.validateField(INPUT_PARAM_TRANSFORMERS, combo.getTransformers(), combo);
		
		getParamLogger().debugWithParams("Finished validating required input parameters.  All parameters are present.");
	}
	
	private void validateField(final String fieldName, final Object fieldValue, final Combination combo) {
		final Integer length;
		
		getParamLogger().debugWithParams(REQUIRED_PARAM_CHECKING_MSG, fieldName);
		
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
			getParamLogger().debugWithParams(REQUIRED_PARAM_NOT_SPECIFIED_MSG, fieldName);
			throw new IllegalArgumentException("The " + fieldName + " parameter is required" + this.idString(combo));
		}
		getParamLogger().debugWithParams(REQUIRED_PARAM_SPECIFIED_MSG, fieldValue);
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
	private Boolean isFieldEmpty(final Object value) {
		Boolean valid = Boolean.TRUE;
		
		if(value == null){
			valid = Boolean.FALSE;
		}
		
		if(value instanceof String && value.toString().length() == 0){
			valid = Boolean.FALSE;
		}
		
		if(value instanceof List<?> && ((List<?>)value).size() == 0){
			valid = Boolean.FALSE;
		}
		
		if(value instanceof Map<?, ?> && ((Map<?, ?>)value).size() == 0){
			valid = Boolean.FALSE;
		}
		
		return valid;
	}
	
	/**
	 * Determines if the {@code id} field within the provided {@link Combination} exists or not.  If it exists, then a {@link String} is created 
	 * by concatenating the {@link CombinerMojo#ID_PROVIDED_MSG} with the {@link Combination#getId()} field.  If it does not exist, 
	 * then an empty {@link String} is returned.
	 *  
	 * @param combo {@link Combination} to check if its {@code id} field has been specified
	 * @return {@link String}
	 */
	private String idString(final Combination combo) {
		return combo.hasId() ? ID_PROVIDED_MSG + combo.getId() : "";
	}
 	
	/**
	 * Writes debug level information about the user configurable 
	 * plugin parameters
	 * 
	 */
	private void debugLogInputs(final Combination combination) {
		if(getParamLogger().isDebugEnabled()){
			getParamLogger().debugWithParams("{0} combination sets specified", this.combinations.size());
			getParamLogger().debug("--- Begin Combination Configuration ---");
			getParamLogger().debugWithParams("  input source reader: {0}", combination.getInputSourceReader());
			getParamLogger().debugWithParams("  transformers: {0}", combination.getTransformers());
			getParamLogger().debugWithParams("  combiner: {0}", combination.getCombiner());
			getParamLogger().debugWithParams("  output source writer: {0}", combination.getOutputSourceWriter());
			getParamLogger().debugWithParams("  encoding: {0}", combination.getEncoding());
			getParamLogger().debugWithParams("  output destination: {0}", combination.getOutputDestination());
			getParamLogger().debugWithParams("  base directory for input files: {0}", mavenProject.getBasedir());
			getParamLogger().debugWithParams("  base build output directory: {0}", mavenProject.getBuild().getDirectory());
			getParamLogger().debugWithParams("  input resources: {0}", combination.getInputSources());
			getParamLogger().debugWithParams("  settings: {0}", combination.getSettings());
			getParamLogger().debug("--- End Combination Set Configuration ---");
		}
	}

}
