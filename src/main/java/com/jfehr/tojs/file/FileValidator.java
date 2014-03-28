package com.jfehr.tojs.file;

import java.io.File;

import com.jfehr.tojs.exception.FileSystemLocationNotFound;
import com.jfehr.tojs.exception.NotReadableException;
import com.jfehr.tojs.exception.NotWriteableException;
import com.jfehr.tojs.logging.ParameterizedLogger;


public class FileValidator {

	private final ParameterizedLogger logger;
	
	public FileValidator(ParameterizedLogger logger) {
		this.logger = logger;
	}
	
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
