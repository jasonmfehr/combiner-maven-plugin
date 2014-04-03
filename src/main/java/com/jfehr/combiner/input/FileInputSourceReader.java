package com.jfehr.combiner.input;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.file.FileLocator;
import com.jfehr.combiner.file.MultiFileReader;
import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.mojo.Setting;

public class FileInputSourceReader implements InputSourceReader {

	private final ParameterizedLogger logger;
	private final FileLocator fileLocator;
	private final MultiFileReader multiFileReader;
	
	public FileInputSourceReader(final ParameterizedLogger logger) {
		this.logger = logger;
		fileLocator = new FileLocator(logger);
		multiFileReader = new MultiFileReader(logger);
	}
	
	public Map<String, String> read(final String encoding, final List<String> includes, final List<String> excludes, final List<Setting> settings, final MavenProject mavenProject) {
		final Map<String, String> fileContents;
		final List<String> files;
		
		files = fileLocator.locateFiles(mavenProject.getBasedir().getAbsolutePath(), includes, excludes);
		fileContents = multiFileReader.readInputFiles(this.buildCharset(encoding), files);
		this.logger.debugWithParams("FileInputSource read {0} files", fileContents.size());
		
		return fileContents;
	}
	
	private Charset buildCharset(final String fileEncoding) {
		final Charset cs;
		
		cs = Charset.forName(fileEncoding);
		this.logger.infoWithParams("FileInputSource using charset {0} to read files", cs.displayName());
		
		return cs;
	}

}
