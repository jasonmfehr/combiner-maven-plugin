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

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
@Mojo(name="jsfile", defaultPhase=LifecyclePhase.GENERATE_TEST_RESOURCES, threadSafe=true)
public class ToJs extends AbstractMojo {
	//TODO add lots of debug logging
	/**
	 * list of files to included using ant patter syntax
	 */
	@Parameter(required=true)
	private List<String> fileIncludes;
	
	/**
	 * list of files to exclude using ant patter syntax, applied 
	 * to all files matching the fileIncludes patterns
	 */
	@Parameter
	private List<String> fileExcludes;
	
	/**
	 * name of the file where the aggregated files will be written, 
	 * relative to the ${project.build.directory}
	 */
	@Parameter(required=true, property="tojs.outputFile")
	private String outputFile;
	
	@Parameter
	private List<String> parsers;
	
	@Parameter(required=true, property="tojs.jsObjectName")
	private String jsObjectName;
	
	@Parameter(defaultValue="${project.build.sourceEncoding}", property="tojs.fileEncoding")
	private String fileEncoding;
	
	@Parameter(property="tojs.skip")
	private Boolean skip;
	
	@Component
	private MavenProject mavenProject;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		final FileLocator locator = new FileLocator();
		final FileAggregator aggregator = new FileAggregator();
		final Log logger = this.getLog();
		final List<String> locatedFiles;
		
		if(!Boolean.TRUE.equals(this.skip)){
			logger.debug("Entering tojs jsfile goal");
			this.debugLogInputs(logger);
			
			locator.setBaseDir(mavenProject.getBasedir().getAbsolutePath());
			locator.setIncludes(fileIncludes);
			locator.setExcludes(fileExcludes);
			locatedFiles = locator.locateFiles();
			this.debugLogList("list of files matching includes but not matching excludes", locatedFiles, logger);
			
			aggregator.setFileEncoding(fileEncoding);
			aggregator.setOutputFilePath(outputFile);
			aggregator.setInputFiles(locatedFiles);
			aggregator.setParsers(parsers);
			aggregator.setJsObjectName(jsObjectName);
			aggregator.aggregate();
			
			logger.debug("Exiting tojs jsfile goal");
		}else{
			logger.debug("skipping tojs-maven-plugin execution");
		}
	}
	
	/**
	 * Writes debug level information about the user configurable 
	 * plugin parameters
	 * 
	 * @param logger {@link Log} where the information will be written
	 */
	private void debugLogInputs(Log logger) {
		if(logger.isDebugEnabled()){
			logger.debug("file encoding: " + fileEncoding);
			logger.debug("output file: " + outputFile);
			logger.debug("javascript object name: " + jsObjectName);
			logger.debug("base directory for input files: " + mavenProject.getBasedir());
			logger.debug("base build output directory: " + mavenProject.getBuild().getDirectory());
			
			this.debugLogList("file include patterns list", fileIncludes, logger);
			this.debugLogList("file exclude patterns list", fileExcludes, logger);
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
	private <T> void debugLogList(String message, List<T> list, Log logger) {
		int listSize;
		
		if(logger.isDebugEnabled()){
			logger.debug(message);
			if(list == null){
				logger.debug("list is null");
			}else{
				listSize = list.size();
				logger.debug("list contains " + listSize + " items");
				for(int i=0; i<listSize; i++){
					logger.debug("item " + (i+1) + " of " + listSize + ": " + list.get(i));
				}
			}
		}
	}

}
