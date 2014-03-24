package com.jfehr.tojs.mojo;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.jfehr.tojs.file.FileAggregator;
import com.jfehr.tojs.file.FileLocator;
import com.jfehr.tojs.logging.ParameterizedLogger;
import com.jfehr.tojs.parser.ParserExecutor;
import com.jfehr.tojs.parser.ParserFactory;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
@Mojo(name="combine", defaultPhase=LifecyclePhase.PROCESS_SOURCES, threadSafe=true)
public class ToJs extends AbstractMojo {

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
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		final FileLocator locator;
		final FileAggregator aggregator;
		final ParameterizedLogger logger;
		final List<String> locatedFiles;
		
		logger = new ParameterizedLogger(this.getLog());
		if(!Boolean.TRUE.equals(this.skip)){
			//TODO switch to standard maven layout: --- artifactId:version:goal (???) @ consuming project name
			logger.debug("Entering tojs jsfile goal");
			this.debugLogInputs(logger);
			/*
			locator = new FileLocator(logger);
			locatedFiles = locator.locateFiles(new DirectoryScanner(), mavenProject.getBasedir().getAbsolutePath(), fileIncludes, fileExcludes);
			this.debugLogList("list of files matching includes but not matching excludes", locatedFiles, logger);

			aggregator = this.buildFileAggregator(logger);
			aggregator.aggregate(this.jsObjectName, this.fileEncoding, this.outputFile, locatedFiles, parsers);
			*/
			
			logger.debug("Exiting tojs jsfile goal");
		}else{
			logger.debug("skipping tojs-maven-plugin execution");
		}
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
	private void debugLogInputs(final ParameterizedLogger logger) {
		if(logger.isDebugEnabled()){
			logger.debugWithParams("{0} combination sets specified", this.combinations.size());
			for(Combination c : this.combinations){
				logger.debug("--- Begin Combination Set ---");
				logger.debugWithParams("  encoding: {0}", c.getEncoding());
				logger.debugWithParams("  output destination: {0}", c.getOutputDestination());
				logger.debugWithParams("  combiner: {0}", c.getCombiner());
				logger.debugWithParams("  base directory for input files: {0}", mavenProject.getBasedir());
				logger.debugWithParams("  base build output directory: {0}", mavenProject.getBuild().getDirectory());
				logger.debugWithParams("  input resources {0}", c.getInputSources());
				
				this.debugLogList("  transformers list", c.getTransformers(), logger);
				this.debugLogList("  settings", c.getSettings(), logger);
				logger.debug("--- End Combination Set ---");
			}
		}
	}
	
	/**
	 * Write debug level information about a list to a logger.  
	 * Does a check to see if debug is enabled before executing  
	 * any code.
	 * 
	 * @param message {@link String} to write out before writing the 
	 *                contents of the list
	 * @param list {@link List} to write out, the toString() method 
	 *             of each item will be written
	 * @param logger {@link Log} where the list information will 
	 *               be written
	 */
	private void debugLogList(final String message, final List<?> list, final ParameterizedLogger logger) {
		final int listSize;
		
		if(logger.isDebugEnabled()){
			logger.debug(message);
			if(list == null){
				logger.debug("list is null");
			}else{
				listSize = list.size();
				logger.debugWithParams("list contains {0} items", listSize);
				for(int i=0; i<listSize; i++){
					logger.debugWithParams("item {0} of {1}: {2}", (i+1), listSize, list.get(i).toString());
				}
			}
		}
	}

}
