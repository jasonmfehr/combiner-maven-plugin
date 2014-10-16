package com.jfehr.combiner.input;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Requirement;

import com.jfehr.combiner.file.FileLocator;
import com.jfehr.combiner.file.MultiFileReader;

public class FileInputSourceReader implements InputSourceReader {

	@Requirement
	private FileLocator fileLocator;
	
	@Requirement
	private MultiFileReader multiFileReader;
	
	public Map<String, String> read(final String encoding, final List<String> includes, final List<String> excludes, final Map<String, String> settings, final MavenProject mavenProject) {
		final Map<String, String> fileContents;
		final List<String> files;
		final Charset encodingCharset;
		
		encodingCharset = this.buildCharset(encoding);
		files = fileLocator.locateFiles(mavenProject.getBasedir().getAbsolutePath(), includes, excludes);
		fileContents = multiFileReader.readInputFiles(encodingCharset, files);
		getParamLogger().debugWithParams("FileInputSource read {0} files", fileContents.size());
		
		return fileContents;
	}
	
	private Charset buildCharset(final String fileEncoding) {
		final Charset cs;
		
		cs = Charset.forName(fileEncoding);
		getParamLogger().infoWithParams("{0} using charset {1} to read files", this.getClass().getSimpleName(), cs.displayName());
		
		return cs;
	}

}
