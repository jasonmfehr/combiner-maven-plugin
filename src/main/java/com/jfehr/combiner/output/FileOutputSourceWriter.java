package com.jfehr.combiner.output;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.maven.project.MavenProject;

import com.google.common.io.Files;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.mojo.Setting;
import com.jfehr.tojs.exception.DirectoryCreationException;
import com.jfehr.tojs.exception.FileCreationException;
import com.jfehr.tojs.exception.FileExistsException;
import com.jfehr.tojs.exception.FileWriteException;
import com.jfehr.tojs.exception.NotWriteableException;

public class FileOutputSourceWriter implements OutputSourceWriter {

	private final ParameterizedLogger logger;
	
	public FileOutputSourceWriter(final ParameterizedLogger logger) {
		this.logger = logger;
	}
	
	public void write(final String encoding, final String outputDestination, final String combinedResources, final List<Setting> settings, final MavenProject mavenProject) {
		final Charset charSet = this.buildCharset(encoding);
		final File outputFile = this.createOutputFile(outputDestination);
		
		this.writeParsedContents(outputFile, charSet, combinedResources);
	}
	
	private void writeParsedContents(final File outputFile, final Charset charSet, final String contents) {
		try{
			Files.write(contents, outputFile, charSet);
		}catch(IOException e){
			throw new FileWriteException(outputFile.getPath(), e);
		}
	}
	
	private File createOutputFile(final String outputFilePath) {
		final File outputFile;
		final File parentFile;
		
		outputFile = new File(outputFilePath);
		
		//create the directory structure (if it does not exist) where the 
		//output file will reside
		parentFile = outputFile.getParentFile();
		if(!parentFile.exists() && !parentFile.mkdirs()){
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
		final Charset cs;
		
		cs = Charset.forName(fileEncoding);
		this.logger.infoWithParams("FileInputSource using charset {0} to read files", cs.displayName());
		
		return cs;
	}

}
