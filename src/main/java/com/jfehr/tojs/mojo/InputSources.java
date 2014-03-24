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
		final StringBuilder sb = new StringBuilder();

		sb.append("InputSources").append(System.getProperty("line.separator")).append(this.buildList("includes", this.includes)).append(this.buildList("excludes", this.excludes));
		
		return sb.toString();
	}
	
	private String buildList(final String listName, final List<String> list) {
		final StringBuilder sb = new StringBuilder();
		
		if(list != null){
			sb.append("List [").append(listName).append("] with ").append(list.size()).append(" entries").append(System.getProperty("line.separator"));
			for(String s : list){
				sb.append("    ").append(s).append(System.getProperty("line.separator"));
			}
			
		}else{
			sb.append("List [").append(listName).append("] is null").append(System.getProperty("line.separator"));
		}
		
		return sb.toString();
	}
	
}
