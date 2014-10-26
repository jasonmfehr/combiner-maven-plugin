package com.jfehr.combiner.combiner;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.project.MavenProject;

public class JSObjectCombiner implements ResourceCombiner {

	private static final String JS_OBJECT_NAME_KEY = "jsObjectName";
	private static final String DEFAULT_JS_OBJECT_NAME = "combiner";
	
	private static final char LINE_SEPARATOR = '\n';
	
	public String combine(final Map<String, String> transformedResourceContents, final Map<String, String> settings, final MavenProject mavenProject) {
		final String combined;
		
		getParamLogger().debugWithParams("{0} starting execution", this.getClass().getName());
		combined = this.doCombine(this.determineJSObjectName(settings), transformedResourceContents);
		getParamLogger().debugWithParams("{0} finished with a combined contents having length {1}", this.getClass().getName(), combined.length());
		
		return combined;
	}
	
	private String determineJSObjectName(final Map<String, String> settings) {
		String determinedName = DEFAULT_JS_OBJECT_NAME;
		
		if(settings != null && settings.containsKey(JS_OBJECT_NAME_KEY)){
			return settings.get(JS_OBJECT_NAME_KEY);
		}
		
		return determinedName;
	}
	
	private String doCombine(final String jsObjectName, final Map<String, String> contents) {
		final StringBuilder sb = new StringBuilder();
		final List<String> concatedLines = new ArrayList<String>(contents.size());
		
		sb.append("(function(w){").append(LINE_SEPARATOR);
		sb.append("    w.").append(jsObjectName).append(" = {").append(LINE_SEPARATOR);
		
		for(Entry<String, String> contentsEntry : contents.entrySet()){
			concatedLines.add("        \"" + this.findFieldName(contentsEntry.getKey()) + "\" = \"" + contentsEntry.getValue() + "\"");
		}
		sb.append(StringUtils.join(concatedLines, "," + LINE_SEPARATOR)).append(LINE_SEPARATOR);
		
		sb.append("    };").append(LINE_SEPARATOR);
		sb.append("})(window);").append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		
		return sb.toString();
	}

	private String findFieldName(final String resourceKey) {
		return new File(resourceKey).getName().replaceFirst("\\.[^\\.]*?$", "");
	}

}
