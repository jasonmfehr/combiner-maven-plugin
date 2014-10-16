package com.jfehr.combiner.file;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.util.DirectoryScanner;

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
@Component(role=FileLocator.class)
public class FileLocator {

	@Requirement
	private FileValidator fileValidator;
	private final DirectoryScanner directoryScanner;
	
	public FileLocator() {
		this.directoryScanner = new DirectoryScanner();
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
		
		getParamLogger().debugWithParams("FileLocator scanning directory {0} for input files", baseDir);
		
		directoryScanner.addDefaultExcludes();
		directoryScanner.setBasedir(baseDir);
		directoryScanner.setIncludes(includes.toArray(new String[includes.size()]));
		
		if(excludes != null){
			directoryScanner.setExcludes(excludes.toArray(new String[excludes.size()]));
		}
		
		directoryScanner.scan();
		
		filesList = Arrays.asList(directoryScanner.getIncludedFiles());
		
		getParamLogger().infoWithParams("FileLocator located {0} files", filesList.size());
		if(getParamLogger().isDebugEnabled()){
			for(final String s : filesList){
				getParamLogger().debug("  " + s);
			}
		}
		
		return filesList;
	}

}
