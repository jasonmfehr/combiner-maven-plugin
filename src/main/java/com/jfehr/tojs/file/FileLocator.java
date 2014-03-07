package com.jfehr.tojs.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

import com.jfehr.tojs.exception.DirectoryNotFoundException;
import com.jfehr.tojs.exception.NotReadableException;

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

	private DirectoryScanner directoryScanner;
	private String baseDir;
	private List<String> includes;
	private List<String> excludes;
	
	public FileLocator() {
		directoryScanner = new DirectoryScanner();
	}
	
	/**
	 * Locate a set of files the match the provided includes patterns 
	 * but do not match the provided excludes patterns.<br /><br />
	 * 
	 * <b>Note:</b> This implementation is NOT thread safe!<br /><br />
	 * 
	 * @return {@link List} of file paths relative to the baseDir
	 * 
	 * @throws NullPointerException if the baseDir parameter is null
	 * @throws IllegalArgumentException if the directory at this path 
	 *                                  specified in the baseDir parameter 
	 *                                  does not exist or is not readable 
	 */
	public List<String> locateFiles() {
		this.validateBaseDir();
		
		directoryScanner.addDefaultExcludes();
		directoryScanner.setBasedir(this.baseDir);
		directoryScanner.setIncludes(this.includes.toArray(new String[this.includes.size()]));
		
		if(this.excludes != null){
			this.directoryScanner.setExcludes(this.excludes.toArray(new String[this.excludes.size()]));
		}
		
		this.directoryScanner.scan();
		
		return Arrays.asList(this.directoryScanner.getIncludedFiles());
	}
	
	public String getBaseDir() {
		return baseDir;
	}
	
	/**
	 * sets the base directory that will be searched during 
	 * a {@link FileLocator#locateFiles()} execution
	 * 
	 * @param baseDir {@link String} containing an absolute path to a 
	 *                base directory that will be searched
	 */
	public void setBaseDir(final String baseDir) {
		this.baseDir = baseDir;
	}

	public List<String> getIncludes() {
		return includes;
	}
	
	/**
	 * sets the patterns for file names that will be included during 
	 * a {@link FileLocator#locateFiles()} execution
	 * @param includes {@link List} containing file patterns to include in the 
	 *                 results set
	 */
	public void setIncludes(final List<String> includes) {
		this.includes = includes;
	}

	public List<String> getExcludes() {
		return excludes;
	}
	
	/**
	 * sets the patterns for file names that will be excluded during 
	 * a {@link FileLocator#locateFiles()} execution
	 * @param excludes {@link List} containing file patterns to exclude from the 
	 *                 results set
	 */
	public void setExcludes(final List<String> excludes) {
		this.excludes = excludes;
	}

	//TODO switch to using FileValidator class
	private void validateBaseDir() {
		final File baseDirFile;
		
		if(this.baseDir == null || "".equals(baseDir)){
			throw new IllegalArgumentException("The base directory to scan for matching files was not specified.");
		}
		
		baseDirFile = new File(this.baseDir);
		
		if(!baseDirFile.exists()){
			throw new DirectoryNotFoundException(this.baseDir);
		}
		
		if(!baseDirFile.canRead()){
			throw new NotReadableException(this.baseDir);
		}
	}
}
