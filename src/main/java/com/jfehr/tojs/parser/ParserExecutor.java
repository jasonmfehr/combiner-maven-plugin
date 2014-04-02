package com.jfehr.tojs.parser;

import java.util.ArrayList;
import java.util.List;

import com.jfehr.combiner.logging.ParameterizedLogger;

public class ParserExecutor {

	private static final String NULL_PARSERS_TO_EXECUTE_PARAM_MESSAGE = "parsersToExecute parameter passed to ParserExecutor.execute cannot be null";
	private static final String NULL_CONTENTS_TO_PARSE_PARAM_MESSAGE = "contentsToParse parameter passed to ParserExecutor.execute cannot be null";
	
	private final ParserFactory parserFactory;
	private final ParameterizedLogger logger;
	
	public ParserExecutor(final ParserFactory parserFactory, final ParameterizedLogger logger) {
		this.parserFactory = parserFactory;
		this.logger = logger;
	}
	
	public String execute(final List<String> parsersToExecute, final String contentsToParse) {
		final List<ToJsParser> parserObjects = new ArrayList<ToJsParser>();
		String tmpParsed;
		
		if(parsersToExecute == null){
			throw new NullPointerException(NULL_PARSERS_TO_EXECUTE_PARAM_MESSAGE);
		}
		
		if(contentsToParse == null){
			throw new NullPointerException(NULL_CONTENTS_TO_PARSE_PARAM_MESSAGE);
		}
		
		for(String parser : parsersToExecute){
			logger.debugWithParams("ParserExecutor attempting to instantiate parser with name [{0}]", parser);
			parserObjects.add(parserFactory.buildParser(parser));
		}
		
		tmpParsed = contentsToParse; 
		
		for(ToJsParser parser : parserObjects){
			tmpParsed = parser.parse(tmpParsed);
		}
		
		return tmpParsed;
	}
	
}
