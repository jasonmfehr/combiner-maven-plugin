package com.jfehr.combiner.input;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import com.jfehr.combiner.file.FileLocator;
import com.jfehr.combiner.file.MultiFileReader;
import com.jfehr.combiner.logging.ParameterizedLogger;

@Component(role=FileInputSourceReader.class)
public class FileInputSourceReader implements InputSourceReader {

	@Requirement
	private FileLocator fileLocator;
	
	@Requirement
	private MultiFileReader multiFileReader;
	
	@Requirement
	private ParameterizedLogger logger;
	
	public Map<String, String> read(final String encoding, final List<String> includes, final List<String> excludes, final Map<String, String> settings, final MavenProject mavenProject) {
		final Map<String, String> fileContents;
		final List<String> files;
		final Charset encodingCharset;
		
		encodingCharset = this.buildCharset(encoding);
		this.logger.debugWithParams("using base directory {0}", mavenProject.getBasedir().getAbsolutePath());
		files = fileLocator.locateFiles(mavenProject.getBasedir().getAbsolutePath(), includes, excludes);
		fileContents = multiFileReader.readInputFiles(encodingCharset, files);
		this.logger.debugWithParams("FileInputSource read {0} files", fileContents.size());
		
		return fileContents;
	}
	
	private Charset buildCharset(final String fileEncoding) {
		final Charset cs;
		
		cs = Charset.forName(fileEncoding);
		this.logger.infoWithParams("{0} using charset {1} to read files", this.getClass().getSimpleName(), cs.displayName());
		
		return cs;
	}

}
