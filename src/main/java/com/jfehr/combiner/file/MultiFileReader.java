package com.jfehr.combiner.file;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;
import com.jfehr.tojs.exception.FileReadException;

public class MultiFileReader {

	private final FileValidator fileValidator;
	
	public MultiFileReader() {
		fileValidator = new FileValidator();
	}
	
	public Map<String, String> readInputFiles(final Charset charSet, final List<String> inputFiles) {
		final Map<String, String> filesContents = new HashMap<String, String>();
		String content;
		File tmpFileObj;
		
		for(final String filePath : inputFiles){
			getParamLogger().debugWithParams("MultiFileReader reading file {0}", filePath);
			fileValidator.existsAndReadable(filePath);
			tmpFileObj = new File(filePath);
			
			try {
				content = Files.toString(tmpFileObj, charSet);
				getParamLogger().debugWithParams("contents of file has length of {0}", content.length());
				filesContents.put(tmpFileObj.getAbsolutePath(), content);
			} catch (IOException e) {
				throw new FileReadException(filePath, e);
			}
		}
		
		return filesContents;
	}
	
}
