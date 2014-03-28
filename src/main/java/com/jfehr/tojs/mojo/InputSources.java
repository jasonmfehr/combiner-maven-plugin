package com.jfehr.tojs.mojo;

import java.util.List;

public class InputSources {

	private List<String> includes;
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
		return new StringBuilder().append("[includes=").append(this.includes.toString()).append(", excludes=").append(this.excludes.toString()).append("]").toString();
	}
	
}
