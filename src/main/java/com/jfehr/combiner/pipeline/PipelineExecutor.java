package com.jfehr.combiner.pipeline;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.combiner.ResourceCombiner;
import com.jfehr.combiner.factory.InputSourceReaderFactory;
import com.jfehr.combiner.factory.OutputSourceWriterFactory;
import com.jfehr.combiner.factory.ResourceCombinerFactory;
import com.jfehr.combiner.factory.ResourceTransformerFactory;
import com.jfehr.combiner.input.InputSourceReader;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.mojo.Combination;
import com.jfehr.combiner.mojo.CombinerMojo;
import com.jfehr.combiner.output.OutputSourceWriter;
import com.jfehr.combiner.transformer.ResourceTransformer;

public class PipelineExecutor {

	private static final String ID_PROVIDED_MSG = " for combination with id ";
	
	private final CombinationValidator validator;
	private final CombinationDefaultsManager defaultsManager;
	
	public PipelineExecutor() {
		this.validator = new CombinationValidator();
		this.defaultsManager = new CombinationDefaultsManager();
	}
	
	public void execute(final List<Combination> combinations, final MavenProject mavenProject) {
		getParamLogger().debugWithParams("{0} combination sets specified", combinations.size());
		
		for(Combination c : combinations){
			this.executeCombination(c, mavenProject);
		}
	}
	
	private void executeCombination(final Combination combo, final MavenProject mavenProject) {
		final String combinedResources;
		Map<String, String> sources;
		
		getParamLogger().debugWithParams("Executing pipeline{0}", this.idString(combo));
		
		this.defaultsManager.setupDefaults(combo, mavenProject);
		this.debugLogInputs(combo, mavenProject);

		this.validator.validate(combo, mavenProject);
		
		sources = this.readInputSources(combo, mavenProject);
		sources = this.executeTransformers(combo, sources, mavenProject);
		combinedResources = this.combineResources(combo, sources, mavenProject);
		this.outputResources(combo, combinedResources, mavenProject);
		
		getParamLogger().debugWithParams("Completed executing pipeline{0}", this.idString(combo));
	}
	
	/**
	 * Executes stage one of the pipeline which reads all the input sources.
	 * 
	 * @param combo executes the read input stage of the pipeline on this {@link Combination}
	 * @param mavenProject {@link MavenProject} object that was injected into the {@link CombinerMojo} by maven
	 * @return {@link Map} with a key of the resource name and a value of the resource contents
	 */
	private Map<String, String> readInputSources(final Combination combo, final MavenProject mavenProject) {
		final InputSourceReaderFactory isFactory;
		final InputSourceReader isReader;
		final Map<String, String> resources;
		
		getParamLogger().debug("Executing pipeline stage one - read input sources");
		
		isFactory = new InputSourceReaderFactory();
		isReader = isFactory.buildObject(combo.getInputSourceReader());
		resources = isReader.read(combo.getEncoding(), combo.getInputSources().getIncludes(), combo.getInputSources().getExcludes(), combo.getSettings(), mavenProject);
		
		getParamLogger().debug("Completed execution of pipeline stage one - read input sources");
		
		return resources;
	}
	
	/**
	 * Executes stage two of the pipeline which applies transformers to the resources.
	 * 
	 * @param combo executes the transform stage of the pipeline on this {@link Combination}
	 * @param sources contains the sources that were read in the first stage of the pipeline
	 * @param mavenProject {@link MavenProject} object that was injected into the {@link CombinerMojo} by maven
	 * @return {@link Map} with a key of the resource name and a value of the resource contents that were transformed
	 */
	//TODO figure out the mess with the map of sources being manipulated
	private Map<String, String> executeTransformers(final Combination combo, Map<String, String> sources, final MavenProject mavenProject) {
		final ResourceTransformerFactory transformerFactory;
		final List<ResourceTransformer> tranformers;
		Map<String, String> transformedSources;
		
		getParamLogger().debug("Executing pipeline stage two - transform resources");
		
		transformedSources = new HashMap<String, String>();
		transformedSources.putAll(sources);
		
		transformerFactory = new ResourceTransformerFactory();
		tranformers = transformerFactory.buildObjectList(combo.getTransformers());
		
		for(ResourceTransformer rt : tranformers){
			getParamLogger().debugWithParams("Executing resource transformer with class {0}", rt.getClass().getName());
			for(Entry<String, String> resource : transformedSources.entrySet()){
				getParamLogger().debugWithParams("Executing transformer on resource {0}", resource.getKey());
				transformedSources.put(resource.getKey(), rt.transform(resource.getKey(), resource.getValue(), combo.getSettings(), mavenProject));
			}
			getParamLogger().debugWithParams("Finished executing resource transformer with class {0}", rt.getClass().getName());
		}
		
		getParamLogger().debug("Completed execution of pipeline stage two - transform resources");
		
		return transformedSources;
	}
	
	private String combineResources(final Combination combo, final Map<String, String> resources, final MavenProject mavenProject) {
		final ResourceCombinerFactory combinerFactory;
		final ResourceCombiner combiner;
		final String combinedResources;
		
		getParamLogger().debug("Executing pipeline stage three - combined resources");
		
		combinerFactory = new ResourceCombinerFactory();
		combiner = combinerFactory.buildObject(combo.getCombiner());
		combinedResources = combiner.combine(resources, combo.getSettings(), mavenProject);
		
		getParamLogger().debug("Completed execution of pipeline stage three - combined resources");
		
		return combinedResources;
	}
	
	private void outputResources(final Combination combo, final String combinedResources, final MavenProject mavenProject) {
		final OutputSourceWriterFactory osFactory;
		final OutputSourceWriter osWriter;
		
		getParamLogger().debug("Executing pipeline stage four - output resources");
		
		osFactory = new OutputSourceWriterFactory();
		osWriter = osFactory.buildObject(combo.getOutputSourceWriter());
		osWriter.write(combo.getEncoding(), combo.getOutputDestination(), combinedResources, combo.getSettings(), mavenProject);
		
		getParamLogger().debug("Completed execution of pipeline stage four - output resources");
	}
	
	/**
	 * Writes debug level information about the user configurable plugin parameters
	 * 
	 * @param combination {@link Combination} object who's information will be logged
	 * @param mavenProject {@link MavenProject} object that was injected into the {@link CombinerMojo} by maven
	 */
	private void debugLogInputs(final Combination combination, final MavenProject mavenProject) {
		ParameterizedLogger logger = getParamLogger();
		if(logger.isDebugEnabled()){
			logger.debug("--- Begin Combination Configuration ---");
			logger.debug("all defaults have been applied to parameters that were not specified in the pom");
			logger.debugWithParams("  input source reader: {0}", combination.getInputSourceReader());
			logger.debugWithParams("  transformers: {0}", combination.getTransformers());
			logger.debugWithParams("  combiner: {0}", combination.getCombiner());
			logger.debugWithParams("  output source writer: {0}", combination.getOutputSourceWriter());
			logger.debugWithParams("  encoding: {0}", combination.getEncoding());
			logger.debugWithParams("  output destination: {0}", combination.getOutputDestination());
			logger.debugWithParams("  base directory for input files: {0}", mavenProject.getBasedir());
			logger.debugWithParams("  base build output directory: {0}", mavenProject.getBuild().getDirectory());
			logger.debugWithParams("  input resources: {0}", combination.getInputSources());
			logger.debugWithParams("  settings: {0}", combination.getSettings());
			logger.debug("--- End Combination Set Configuration ---");
		}
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
		return combo.getId() != null && combo.getId().length() > 0 ? ID_PROVIDED_MSG + combo.getId() : "";
	}
}
