package com.jfehr.combiner.mojo;

import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.combiner.ResourceCombiner;
import com.jfehr.combiner.factory.InputSourceReaderFactory;
import com.jfehr.combiner.factory.ObjectFactory;
import com.jfehr.combiner.factory.OutputSourceWriterFactory;
import com.jfehr.combiner.factory.ResourceCombinerFactory;
import com.jfehr.combiner.factory.ResourceTransformerFactory;
import com.jfehr.combiner.input.InputSourceReader;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.output.OutputSourceWriter;
import com.jfehr.combiner.transformer.ResourceTransformer;
import com.jfehr.tojs.file.FileAggregator;
import com.jfehr.tojs.parser.ParserExecutor;
import com.jfehr.tojs.parser.ParserFactory;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
@Mojo(name="combine", defaultPhase=LifecyclePhase.PROCESS_SOURCES, threadSafe=true)
public class CombinerMojo extends AbstractMojo {

	private static final String DEFAULT_PACKAGE_INPUT = "com.jfehr.combiner.input";
	private static final String DEFAULT_PACKAGE_TRANSFORMER = "com.jfehr.combiner.transformer";
	private static final String DEFAULT_PACKAGE_COMBINER = "com.jfehr.combiner.combiner";
	private static final String DEFAULT_PACKAGE_OUTPUT = "com.jfehr.combiner.output";
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
		final ParameterizedLogger logger;
		final ObjectFactory factory;
		/*
		final FileLocator locator;
		final FileAggregator aggregator;
		final List<String> locatedFiles;
		*/
		
		logger = new ParameterizedLogger(this.getLog());
		if(!Boolean.TRUE.equals(this.skip)){
			logger.debugWithParams("Entering {0} goal", mojoExecution.getGoal());
			factory = new ObjectFactory(logger);
			for(Combination c : this.combinations){
				this.executeCombination(c, logger);
			}
			/*
			locator = new FileLocator(logger);
			locatedFiles = locator.locateFiles(new DirectoryScanner(), mavenProject.getBasedir().getAbsolutePath(), fileIncludes, fileExcludes);
			this.debugLogList("list of files matching includes but not matching excludes", locatedFiles, logger);

			aggregator = this.buildFileAggregator(logger);
			aggregator.aggregate(this.jsObjectName, this.fileEncoding, this.outputFile, locatedFiles, parsers);
			*/
			
			logger.debugWithParams("Exiting {0} goal", mojoExecution.getGoal());
		}else{
			logger.info("skipping combiner-maven-plugin execution");
		}
	}
	
	//TODO obviously this method has to be broken apart
	private void executeCombination(final Combination combo, final ParameterizedLogger logger) {
		final String combinedSources;
		Map<String, String> sources;
		this.debugLogInputs(logger, combo);
		
		final InputSourceReaderFactory isFactory = new InputSourceReaderFactory(logger);
		final InputSourceReader isReader;
		isReader = isFactory.buildObject(combo.getInputSourceReader());
		sources = isReader.read(combo.getEncoding(), combo.getInputSources().getIncludes(), combo.getInputSources().getExcludes(), combo.getSettings(), this.mavenProject);
		
		final ResourceTransformerFactory transformerFactory = new ResourceTransformerFactory(logger);
		final List<ResourceTransformer> tranformers;
		tranformers = transformerFactory.buildObjectList(combo.getTransformers());
		for(ResourceTransformer rt : tranformers){
			sources = rt.transform(sources, combo.getSettings(), this.mavenProject);
		}
		
		final ResourceCombinerFactory combinerFactory = new ResourceCombinerFactory(logger);
		final ResourceCombiner combiner;
		combiner = combinerFactory.buildObject(combo.getCombiner());
		combinedSources = combiner.combine(sources, combo.getSettings(), this.mavenProject);
		
		final OutputSourceWriterFactory osFactory = new OutputSourceWriterFactory(logger);
		final OutputSourceWriter osWriter;
		osWriter = osFactory.buildObject(combo.getOutputSourceWriter());
		osWriter.write(combo.getEncoding(), combo.getOutputDestination(), combinedSources, combo.getSettings(), this.mavenProject);
	}
 	
	/**
	 * factory method to build a {@link FileAggregator} object since 
	 * {@link FileAggregator} objects are constructed using an 
	 * inversion of control methodology
	 * 
	 * @return {@link FileAggregator} the newly constructed object
	 */
	private FileAggregator buildFileAggregator(final ParameterizedLogger logger) {
		final ParserFactory parserFactory;
		final ParserExecutor parserExecutor;
		
		parserFactory = new ParserFactory(logger);
		parserExecutor = new ParserExecutor(parserFactory, logger);
		
		return new FileAggregator(parserExecutor, logger);
	}
	
	/**
	 * Writes debug level information about the user configurable 
	 * plugin parameters
	 * 
	 * @param logger {@link Log} where the information will be written
	 */
	private void debugLogInputs(final ParameterizedLogger logger, final Combination combination) {
		if(logger.isDebugEnabled()){
			logger.debugWithParams("{0} combination sets specified", this.combinations.size());
			logger.debug("--- Begin Combination Configuration ---");
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
