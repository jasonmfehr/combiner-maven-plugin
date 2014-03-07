package com.jfehr.tojs.file;

import java.io.File;

import com.jfehr.tojs.exception.DirectoryNotFoundException;
import com.jfehr.tojs.exception.NotReadableException;


public class FileValidator {

	public void fileExistsAndReadable(final String fileToValidate, final String fileEmptyMsg) {
		final File file;
		
		if(fileToValidate == null || "".equals(fileToValidate)){
			throw new IllegalArgumentException(fileEmptyMsg);
		}
		
		file = new File(fileToValidate);
		
		if(!file.exists()){
			throw new DirectoryNotFoundException(fileToValidate);
		}
		
		if(!file.canRead()){
			throw new NotReadableException(fileToValidate);
		}
		
	}
	
	public boolean validateWriteable() {
		return false;
	}

}
