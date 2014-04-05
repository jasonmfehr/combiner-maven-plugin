package com.jfehr.combiner.mojo;

import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

public class InputSources {

	/**
	 * list of patterns that will determine what resources are put into 
	 * the combiner pipeline
	 */
	@Parameter(required=true)
	private List<String> includes;
	
	/**
	 * list of patterns that will determine what resources are not put 
	 * into the combiner pipeline
	 */
	@Parameter
	private List<String> excludes;
	
	public List<String> getIncludes() {
		return includes;
	}
	public void setIncludes(final List<String> includes) {
		this.includes = includes;
	}
	
	public List<String> getExcludes() {
		return excludes;
	}
	public void setExcludes(final List<String> excludes) {
		this.excludes = excludes;
	}
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("[includes=");
		if(this.includes != null){
			sb.append(this.includes.toString());
		}else{
			sb.append("null");
		}
		
		sb.append(", excludes=");
		if(this.excludes != null){
			sb.append(this.excludes.toString());
		}else{
			sb.append("null");
		}
		
		return sb.append("]").toString();
	}
	
}
