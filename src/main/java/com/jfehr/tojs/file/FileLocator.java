package com.jfehr.tojs.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

import com.jfehr.tojs.exception.DirectoryNotFoundException;
import com.jfehr.tojs.exception.NotReadableException;
import com.jfehr.tojs.logging.ParameterizedLogger;

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
	
	public FileLocator(ParameterizedLogger logger) {
		this.logger = logger;
	}
	
	/**
	 * Locate a set of files the match the provided includes patterns 
	 * but do not match the provided excludes patterns.<br /><br />
	 * 
	 * @param directoryScanner {@link DirectoryScanner} used to search for 
	 *                         files
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
	public List<String> locateFiles(final DirectoryScanner directoryScanner, final String baseDir, final List<String> includes, final List<String> excludes) {
		this.validateBaseDir(baseDir);
		
		directoryScanner.addDefaultExcludes();
		directoryScanner.setBasedir(baseDir);
		directoryScanner.setIncludes(includes.toArray(new String[includes.size()]));
		
		if(excludes != null){
			directoryScanner.setExcludes(excludes.toArray(new String[excludes.size()]));
		}
		
		directoryScanner.scan();
		
		return Arrays.asList(directoryScanner.getIncludedFiles());
	}

	//TODO switch to using FileValidator class
	private void validateBaseDir(final String baseDir) {
		final File baseDirFile;
		
		if(baseDir == null || "".equals(baseDir)){
			throw new IllegalArgumentException("The base directory to scan for matching files was not specified.");
		}
		
		baseDirFile = new File(baseDir);
		
		if(!baseDirFile.exists()){
			throw new DirectoryNotFoundException(baseDir);
		}
		
		if(!baseDirFile.canRead()){
			throw new NotReadableException(baseDir);
		}
	}
}
