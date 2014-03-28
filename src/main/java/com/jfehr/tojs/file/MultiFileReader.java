package com.jfehr.tojs.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;
import com.jfehr.tojs.exception.FileReadException;
import com.jfehr.tojs.logging.ParameterizedLogger;

public class MultiFileReader {

	private final ParameterizedLogger logger;
	
	public MultiFileReader(final ParameterizedLogger logger) {
		this.logger = logger;
	}
	
	public Map<String, String> readInputFiles(final Charset charSet, final List<String> inputFiles) {
		final Map<String, String> filesContents = new HashMap<String, String>();
		File tmpFileObj;
		
		for(final String filePath : inputFiles){
			try {
				logger.debugWithParams("MultiFileReader reading file {0}", filePath);
				tmpFileObj = new File(filePath);
				filesContents.put(tmpFileObj.getName(), Files.toString(tmpFileObj, charSet));
			} catch (IOException e) {
				throw new FileReadException(filePath, e);
			}
		}
		
		return filesContents;
	}
	
}
