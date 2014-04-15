package com.jfehr.combiner.input;

import static com.jfehr.combiner.mojo.LogHolder.getParamLogger;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.file.FileLocator;
import com.jfehr.combiner.file.MultiFileReader;

public class FileInputSourceReader implements InputSourceReader {

	private final FileLocator fileLocator;
	private final MultiFileReader multiFileReader;
	
	public FileInputSourceReader() {
		fileLocator = new FileLocator();
		multiFileReader = new MultiFileReader();
	}
	
	public Map<String, String> read(final String encoding, final List<String> includes, final List<String> excludes, final Map<String, String> settings, final MavenProject mavenProject) {
		final Map<String, String> fileContents;
		final List<String> files;
		
		files = fileLocator.locateFiles(mavenProject.getBasedir().getAbsolutePath(), includes, excludes);
		fileContents = multiFileReader.readInputFiles(this.buildCharset(encoding), files);
		getParamLogger().debugWithParams("FileInputSource read {0} files", fileContents.size());
		
		return fileContents;
	}
	
	private Charset buildCharset(final String fileEncoding) {
		final Charset cs;
		
		cs = Charset.forName(fileEncoding);
		getParamLogger().infoWithParams("FileInputSource using charset {0} to read files", cs.displayName());
		
		return cs;
	}

}
