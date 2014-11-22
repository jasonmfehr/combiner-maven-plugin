package com.github.jasonmfehr.combiner.pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.github.jasonmfehr.combiner.combiner.ResourceCombiner;
import com.github.jasonmfehr.combiner.factory.InputSourceReaderFactory;
import com.github.jasonmfehr.combiner.factory.OutputSourceWriterFactory;
import com.github.jasonmfehr.combiner.factory.ResourceCombinerFactory;
import com.github.jasonmfehr.combiner.factory.ResourceTransformerFactory;
import com.github.jasonmfehr.combiner.input.InputSourceReader;
import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;
import com.github.jasonmfehr.combiner.mojo.Combination;
import com.github.jasonmfehr.combiner.mojo.CombinerMojo;
import com.github.jasonmfehr.combiner.output.OutputSourceWriter;
import com.github.jasonmfehr.combiner.transformer.ResourceTransformer;

@Component(role=PipelineExecutor.class)
public class PipelineExecutor {

	@Requirement
	private CombinationValidator validator;
	
	@Requirement
	private CombinationDefaultsManager defaultsManager;
	
	@Requirement
	private InputSourceReaderFactory inputSourceReaderFactory;
	
	@Requirement
	private ResourceTransformerFactory transformerFactory;
	
	@Requirement
	private ResourceCombinerFactory combinerFactory;
	
	@Requirement
	private OutputSourceWriterFactory osFactory;
	
	@Requirement
	private ParameterizedLogger logger;
	
	public void execute(final List<Combination> combinations, final MavenProject mavenProject) {
		logger.warnWithParams("this is a parameterized warning in {0}", PipelineExecutor.class);
		this.logger.debugWithParams("{0} combination sets specified", combinations.size());
		
		for(Combination c : combinations){
			this.executeCombination(c, mavenProject);
		}
	}
	
	private void executeCombination(final Combination combo, final MavenProject mavenProject) {
		final String combinedResources;
		Map<String, String> sources;
		
		this.logger.debugWithParams("Executing pipeline{0}", this.logger.buildCombinationIDString(combo));
		
		this.defaultsManager.setupDefaults(combo, mavenProject);
		this.debugLogInputs(combo, mavenProject);

		this.validator.validate(combo);
		
		sources = this.readInputSources(combo, mavenProject);
		sources = this.executeTransformers(combo, sources, mavenProject);
		combinedResources = this.combineResources(combo, sources, mavenProject);
		this.outputResources(combo, combinedResources, mavenProject);
		
		this.logger.debugWithParams("Completed executing pipeline{0}", this.logger.buildCombinationIDString(combo));
	}
	
	/**
	 * Executes stage one of the pipeline which reads all the input sources.
	 * 
	 * @param combo executes the read input stage of the pipeline on this {@link Combination}
	 * @param mavenProject {@link MavenProject} object that was injected into the {@link CombinerMojo} by maven
	 * @return {@link Map} with a key of the resource name and a value of the resource contents
	 */
	private Map<String, String> readInputSources(final Combination combo, final MavenProject mavenProject) {
		final InputSourceReader isReader;
		final Map<String, String> resources;
		
		this.logger.debug("Executing pipeline stage one - read input sources");
		
		isReader = inputSourceReaderFactory.buildObject(combo.getInputSourceReader());
		resources = isReader.read(combo.getEncoding(), combo.getInputSources().getIncludes(), combo.getInputSources().getExcludes(), combo.getSettings(), mavenProject);
		
		this.logger.debug("Completed execution of pipeline stage one - read input sources");
		
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
	private Map<String, String> executeTransformers(final Combination combo, Map<String, String> sources, final MavenProject mavenProject) {
		final List<ResourceTransformer> tranformers;
		Map<String, String> transformedSources;
		
		this.logger.debug("Executing pipeline stage two - transform resources");
		
		transformedSources = new HashMap<String, String>();
		transformedSources.putAll(sources);
		
		tranformers = transformerFactory.buildObjectList(combo.getTransformers());
		
		for(ResourceTransformer rt : tranformers){
			this.logger.debugWithParams("Executing resource transformer with class {0}", rt.getClass().getName());
			for(Entry<String, String> resource : transformedSources.entrySet()){
				this.logger.debugWithParams("Executing transformer on resource {0}", resource.getKey());
				transformedSources.put(resource.getKey(), rt.transform(resource.getKey(), resource.getValue(), combo.getSettings(), mavenProject));
			}
			this.logger.debugWithParams("Finished executing resource transformer with class {0}", rt.getClass().getName());
		}
		
		this.logger.debug("Completed execution of pipeline stage two - transform resources");
		
		return transformedSources;
	}
	
	private String combineResources(final Combination combo, final Map<String, String> resources, final MavenProject mavenProject) {
		final ResourceCombiner combiner;
		final String combinedResources;
		
		this.logger.debug("Executing pipeline stage three - combined resources");
		
		combiner = combinerFactory.buildObject(combo.getCombiner());
		combinedResources = combiner.combine(resources, combo.getSettings(), mavenProject);
		
		this.logger.debug("Completed execution of pipeline stage three - combined resources");
		
		return combinedResources;
	}
	
	private void outputResources(final Combination combo, final String combinedResources, final MavenProject mavenProject) {
		final OutputSourceWriter osWriter;
		
		this.logger.debug("Executing pipeline stage four - output resources");
		
		osWriter = osFactory.buildObject(combo.getOutputSourceWriter());
		osWriter.write(combo.getEncoding(), combo.getOutputDestination(), combinedResources, combo.getSettings(), mavenProject);
		
		this.logger.debug("Completed execution of pipeline stage four - output resources");
	}
	
	/**
	 * Writes debug level information about the user configurable plugin parameters
	 * 
	 * @param combination {@link Combination} object who's information will be logged
	 * @param mavenProject {@link MavenProject} object that was injected into the {@link CombinerMojo} by maven
	 */
	private void debugLogInputs(final Combination combination, final MavenProject mavenProject) {
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
	
}
