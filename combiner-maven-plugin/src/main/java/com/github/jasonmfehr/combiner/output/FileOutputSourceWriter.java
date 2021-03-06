package com.github.jasonmfehr.combiner.output;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.google.common.io.Files;
import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;
import com.github.jasonmfehr.tojs.exception.DirectoryCreationException;
import com.github.jasonmfehr.tojs.exception.FileCreationException;
import com.github.jasonmfehr.tojs.exception.FileExistsException;
import com.github.jasonmfehr.tojs.exception.FileWriteException;
import com.github.jasonmfehr.tojs.exception.NotWriteableException;

@Component(role=FileOutputSourceWriter.class)
public class FileOutputSourceWriter implements OutputSourceWriter {
	
	@Requirement
	private ParameterizedLogger logger;

	//TODO the unit tests for this method never assert that the correct fullOutputDestination is created
	public void write(final String encoding, final String outputDestination, final String combinedResources, final Map<String, String> settings, final MavenProject mavenProject) {
		final Charset charSet;
		final File outputFile;
		final String fullOutputDestination;
		
		fullOutputDestination = mavenProject.getBuild().getDirectory() + "/" + outputDestination;
		this.logger.debugWithParams("{0} starting execution with charset {1} and output destination {2}", this.getClass().getName(), encoding, fullOutputDestination);
		
		charSet = this.buildCharset(encoding);
		outputFile = this.createOutputFile(fullOutputDestination);
		
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
		this.logger.infoWithParams("{0} using charset {1} to read files", this.getClass().getSimpleName(), cs.displayName());
		
		return cs;
	}

}
