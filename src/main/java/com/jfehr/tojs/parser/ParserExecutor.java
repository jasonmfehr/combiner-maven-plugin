package com.jfehr.tojs.parser;

import java.util.ArrayList;
import java.util.List;

public class ParserExecutor {

	private List<String> parsersToExecute;
	private List<ToJsParser> parserObjects;
	private ParserFactory parserFactory;
	private boolean parsersInitialized;
	
	public ParserExecutor() {
		parsersInitialized = false;
	}
	
	public ParserExecutor(ParserFactory parserFactory) {
		super();
		this.parserFactory = parserFactory;
	}
	
	public String executeParsers(String contentsToParse) {
		final List<ToJsParser> parserObjects;
		String tmpParsed;
		
		parserObjects = this.buildParserObjects();
		
		tmpParsed = contentsToParse; 
		
		for(ToJsParser parser : parserObjects){
			tmpParsed = parser.parse(tmpParsed);
		}
		
		return tmpParsed;
	}
	
	public List<String> getParsersToExecute() {
		return parsersToExecute;
	}
	public void setParsersToExecute(List<String> parsersToExecute) {
		this.parsersInitialized = false;
		this.parsersToExecute = parsersToExecute;
	}
	
	public ParserFactory getParserFactory() {
		return parserFactory;
	}
	public void setParserFactory(ParserFactory parserFactory) {
		this.parserFactory = parserFactory;
	}

	private List<ToJsParser> buildParserObjects() {
		if(!this.parsersInitialized){
			this.parserObjects = new ArrayList<ToJsParser>();
			
			for(String parser : this.parsersToExecute){
				this.parserObjects.add(parserFactory.buildParser(parser));
			}
			
			this.parsersInitialized = true;
		}
		
		return this.parserObjects;
	}
	
}
