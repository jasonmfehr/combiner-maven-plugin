package com.jfehr.combiner.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.google.common.io.Files;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.tojs.exception.FileReadException;

@Component(role=MultiFileReader.class)
public class MultiFileReader {

	@Requirement
	private FileValidator fileValidator;
	
	@Requirement
	private ParameterizedLogger logger;
	
	public Map<String, String> readInputFiles(final Charset charSet, final List<String> inputFiles) {
		final Map<String, String> filesContents = new HashMap<String, String>();
		String content;
		File tmpFileObj;
		
		for(final String filePath : inputFiles){
			this.logger.debugWithParams("MultiFileReader reading file {0}", filePath);
			fileValidator.existsAndReadable(filePath);
			tmpFileObj = new File(filePath);
			
			try {
				content = Files.toString(tmpFileObj, charSet);
				this.logger.debugWithParams("contents of file has length of {0}", content.length());
				filesContents.put(tmpFileObj.getAbsolutePath(), content);
			} catch (IOException e) {
				throw new FileReadException(filePath, e);
			}
		}
		
		return filesContents;
	}
	
}
