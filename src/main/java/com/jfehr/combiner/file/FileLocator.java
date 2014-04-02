package com.jfehr.combiner.file;

import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

import com.jfehr.combiner.logging.ParameterizedLogger;

/**
 * Searches through a base directory locating files that match a 
 * set of includes patterns but do not match a set of excludes patterns.<br /><br />
 * 
 * Objects of this class are not thread safe.
 * 
 * @author jasonmfehr
 * @since 1.0.0
 * 
 */
public class FileLocator {

	private final ParameterizedLogger logger;
	private final DirectoryScanner directoryScanner;
	private final FileValidator fileValidator;
	
	public FileLocator(final ParameterizedLogger logger) {
		this.logger = logger;
		this.directoryScanner = new DirectoryScanner();
		this.fileValidator = new FileValidator(logger);
	}
	
	/**
	 * Locate a set of files the match the provided includes patterns 
	 * but do not match the provided excludes patterns.<br /><br />
	 * 
	 * @param baseDir {@link String} containing an absolute path to a 
	 *                base directory that will be searched
	 * @param includes {@link List} containing file patterns to include in the 
	 *                 results set
	 * @param excludes {@link List} containing file patterns to exclude from the 
	 *                 results set
	 * 
	 * @return {@link List} of file paths relative to the baseDir
	 * 
	 * @throws NullPointerException if the baseDir parameter is null
	 * @throws IllegalArgumentException if the directory at this path 
	 *                                  specified in the baseDir parameter 
	 *                                  does not exist or is not readable 
	 */
	public List<String> locateFiles(final String baseDir, final List<String> includes, final List<String> excludes) {
		final List<String> filesList;
		
		fileValidator.existsAndReadable(baseDir);
		
		logger.debugWithParams("FileLocator scanning directory {0} for input files", baseDir);
		
		directoryScanner.addDefaultExcludes();
		directoryScanner.setBasedir(baseDir);
		directoryScanner.setIncludes(includes.toArray(new String[includes.size()]));
		
		if(excludes != null){
			directoryScanner.setExcludes(excludes.toArray(new String[excludes.size()]));
		}
		
		directoryScanner.scan();
		
		filesList = Arrays.asList(directoryScanner.getIncludedFiles());
		
		logger.infoWithParams("FileLocator located {0} files", filesList.size());
		if(logger.isDebugEnabled()){
			for(final String s : filesList){
				logger.debug("  " + s);
			}
		}
		
		return filesList;
	}

}
