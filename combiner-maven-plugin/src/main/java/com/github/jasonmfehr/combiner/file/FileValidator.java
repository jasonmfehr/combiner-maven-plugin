package com.github.jasonmfehr.combiner.file;

import java.io.File;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;
import com.github.jasonmfehr.tojs.exception.FileSystemLocationNotFound;
import com.github.jasonmfehr.tojs.exception.NotReadableException;
import com.github.jasonmfehr.tojs.exception.NotWriteableException;

@Component(role=FileValidator.class)
public class FileValidator {

	@Requirement
	private ParameterizedLogger logger;
	
	public void existsAndReadable(final String location) {
		final File file;
		
		this.validateInput(location);
		file = this.validateExists(location);
		
		if(!file.canRead()){
			throw new NotReadableException(location);
		}
		
		this.logValid(location, "readable");
	}
	
	public void existsAndWriteable(final String location) {
		final File file;
		
		this.validateInput(location);
		file = this.validateExists(location);
		
		if(!file.canWrite()){
			throw new NotWriteableException(location);
		}
		
		this.logValid(location, "writeable");
	}
	
	private void validateInput(final String input) {
		if(input == null || "".equals(input)){
			throw new IllegalArgumentException("No path was specified to validate");
		}
	}
	
	private File validateExists(final String location) {
		final File file = new File(location);
		
		if(!file.exists()){
			throw new FileSystemLocationNotFound(location);
		}
		this.logger.debugWithParams("{0} - location {1} exists", this.getClass().getSimpleName(), location);
		
		return file;
	}
	
	private void logValid(final String location, final String type) {
		this.logger.debugWithParams("{0} - location {1} is {2}", this.getClass().getSimpleName(), location, type);
		this.logger.debugWithParams("{0} - location {1} is valid", this.getClass().getSimpleName(), location);
	}

}
