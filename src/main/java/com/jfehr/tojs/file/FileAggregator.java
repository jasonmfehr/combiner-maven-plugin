package com.jfehr.tojs.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.io.Files;
import com.jfehr.tojs.exception.DirectoryCreationException;
import com.jfehr.tojs.exception.FileCreationException;
import com.jfehr.tojs.exception.FileExistsException;
import com.jfehr.tojs.exception.FileReadException;
import com.jfehr.tojs.exception.FileWriteException;
import com.jfehr.tojs.exception.NotWriteableException;
import com.jfehr.tojs.logging.ParameterizedLogger;
import com.jfehr.tojs.parser.ParserExecutor;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 * 
 */
public class FileAggregator {
	private static final char LINE_SEPARATOR = '\n';
	
	//TODO this class may be doing too much and may need to be broken apart
	private final ParserExecutor parserExecutor;
	private final ParameterizedLogger logger;
	
	public FileAggregator(final ParserExecutor parserExecutor, final ParameterizedLogger logger) {
		this.parserExecutor = parserExecutor;
		this.logger = logger;
	}

	public void aggregate(final String jsObjectName, final String fileEncoding, final String outputFilePath, final List<String> inputFiles, List<String> parsers) {
		final Charset charSet;
		final File outputFile;
		final Map<String, String> inputFileContents;
		final Map<String, String> parsedContents;
		
		//build the charset first so its exceptions are thrown 
		//before we start messing with the file system
		charSet = this.buildCharset(fileEncoding);
		
		//try to create the output file before taking the time and 
		//memory to read in all the input files
		outputFile = this.createOutputFile(outputFilePath);
		
		inputFileContents = this.readInputFiles(charSet, inputFiles);
		
		parsedContents = new HashMap<String, String>();
		
		for(Entry<String, String> fileContents : inputFileContents.entrySet()){
			parsedContents.put(fileContents.getKey(), parserExecutor.execute(parsers, fileContents.getValue()));
		}
		
		this.writeParsedContents(jsObjectName, outputFile, charSet, parsedContents);
	}
	
	private File createOutputFile(final String outputFilePath) {
		final File outputFile;
		
		//ensure the output file was specified
		if(outputFilePath == null || "".equals(outputFilePath)){
			throw new IllegalArgumentException("Required parameter output file was not specified.");
		}
		
		outputFile = new File(outputFilePath);
		
		//create the directory structure (if it does not exist) where the 
		//output file will reside
		if(!outputFile.getParentFile().mkdirs()){
			throw new DirectoryCreationException(outputFile.getParentFile().getPath());
		}
		
		//ensure the output file's containing directory is writeable
		if(!outputFile.getParentFile().canWrite()){
			throw new NotWriteableException(outputFilePath);
		}
		
		//last but not least actually create the file
		try{
			if(!outputFile.createNewFile()){
				//file already exists, throw an exception since we do not want to overwrite anything
				throw new FileExistsException(outputFilePath);
			}
		}catch(IOException ioe){
			throw new FileCreationException(outputFilePath, ioe);
		}
		
		return outputFile;
	}
	
	private Charset buildCharset(final String fileEncoding) {
		//TODO use platform default encoding if not specified
		if(!Charset.isSupported(fileEncoding)){
			throw new IllegalCharsetNameException(fileEncoding);
		}
		
		return Charset.forName(fileEncoding);
	}
	
	private Map<String, String> readInputFiles(final Charset charSet, final List<String> inputFiles) {
		final Map<String, String> filesContents = new HashMap<String, String>();
		File tmpFileObj;
		
		for(String filePath : inputFiles){
			try {
				tmpFileObj = new File(filePath);
				filesContents.put(tmpFileObj.getName(), Files.toString(tmpFileObj, charSet));
			} catch (IOException e) {
				throw new FileReadException(filePath, e);
			}
		}
		
		return filesContents;
	}
	
	private void writeParsedContents(final String jsObjectName, final File outputFile, final Charset charSet, final Map<String, String> parsedContents) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("(function(w){").append(LINE_SEPARATOR);
		sb.append("    w.").append(jsObjectName).append(" = {").append(LINE_SEPARATOR);
		
		for(Entry<String, String> contentsEntry : parsedContents.entrySet()){
			sb.append("        ").append(contentsEntry.getKey()).append(" = ").append(contentsEntry.getValue()).append(";").append(LINE_SEPARATOR);
		}
		
		sb.append("    };").append(LINE_SEPARATOR);
		sb.append("})(window);").append(LINE_SEPARATOR);
		
		try{
			Files.write(sb.toString(), outputFile, charSet);
		}catch(IOException e){
			throw new FileWriteException(outputFile.getPath(), e);
		}
	}
	
	/*
	private FileOutputStream buildOutputStream(final File outputFile, final Charset charSet) {
		FileOutputStream fileStream = null;
		
		try {
			fileStream = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			throw new FileWriteException(outputFile.getPath(), e);
		}

		return fileStream;
	}
	
	private FileInputStream buildInputStream(final File inputFile, final Charset charSet) {
		FileInputStream fileStream = null;
		
		try {
			fileStream = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			throw new FileReadException(inputFile.getPath(), e);
		}
		
		return fileStream;
	}
	*/
	
}
