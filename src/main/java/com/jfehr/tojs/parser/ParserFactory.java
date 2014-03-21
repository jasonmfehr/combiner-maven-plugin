package com.jfehr.tojs.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfehr.tojs.exception.InvalidParserException;
import com.jfehr.tojs.exception.ParserNotFoundException;
import com.jfehr.tojs.logging.ParameterizedLogger;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 *
 */
public class ParserFactory {
	private static final String DEFAULT_PARSER_PACKAGE = "com.jfehr.tojs.parser.";
	private static final String PARSER_IS_INTERFACE_MESSAGE = "The parser is an interface.  It must be a class that implements the " + ToJsParser.class.getName() + " interface.";
	private static final String PARSER_DOES_NOT_IMPLEMENT_INTERFACE_MESSAGE = "The parser does not implement the " + ToJsParser.class.getName() + " interface.  This interface must be implemented.";
	private static final String NULL_PARSER_NAMES_PARAM = "parserNamesList parameter passed to ParserFactory.buildParsers cannot be null";
	private static final String NULL_PARSER_NAME_PARAM = "parserName parameter passed to ParserFactory.buildParser cannot be null";
	
	private final ParameterizedLogger logger;
	private final Map<String, ToJsParser> parserCache;
	
	public ParserFactory(final ParameterizedLogger logger) {
		this.logger = logger;
		this.parserCache = new HashMap<String, ToJsParser>();
	}
	
	public List<ToJsParser> buildParsers(final List<String> parserNamesList) {
		final List<ToJsParser> parserObjs;
		
		//TODO build default list of parsers if not specified
		if(parserNamesList != null){
			logger.debugWithParams("ParserFactory.buildParsers called with list containing [{0}] items", parserNamesList.size());
			
			parserObjs = new ArrayList<ToJsParser>();
			
			for(String parser : parserNamesList){
				parserObjs.add(this.buildParser(parser));
			}
		}else{
			throw new NullPointerException(NULL_PARSER_NAMES_PARAM);
		}
		
		return parserObjs;
	}
	
	public ToJsParser buildParser(final String parserName) {
		ToJsParser parserObj;
		
		if(parserName != null){
			logger.debugWithParams("ParserFactory.buildParser called with parser named [{0}]", " + parserName + ");
			
			if(parserCache.containsKey(parserName)){
				parserObj = parserCache.get(parserName);
			}else{
				//TODO catch exceptions coming from instantiateParser instead
				//of using these if statements
				parserObj = this.instantiateParserUnchanged(parserName);
				
				if(parserObj == null){
					logger.debugWithParams("Coud not instatiate class with the exact package and name of [{0}]", parserName);
					parserObj = this.instantiateParserDefaultPackage(parserName);
				}
				
				if(parserObj == null){
					logger.debugWithParams("Could not instantiate parser with a name of [{0}] in the default package of [{1}]", parserName, DEFAULT_PARSER_PACKAGE);
					throw new ParserNotFoundException(parserName);
				}
				
				parserCache.put(parserName, parserObj);
			}
		}else{
			throw new NullPointerException(NULL_PARSER_NAME_PARAM);
		}
		
		return parserObj;
	}
	
	private ToJsParser instantiateParserUnchanged(final String parserName) {
		return this.instantiateParser(parserName);
	}
	
	private ToJsParser instantiateParserDefaultPackage(final String parserName) {
		return this.instantiateParser(DEFAULT_PARSER_PACKAGE + parserName);
	}
	
	private ToJsParser instantiateParser(final String parserName) {
		Object parserObj = null;
		Class<?> clazz = null;
		
		logger.debugWithParams("Attempting to instantiate a class with the exact package and name of [{0}]", parserName);
		
		try {
			clazz = Class.forName(parserName);
		} catch (ClassNotFoundException e) {
			logger.debugWithParams("Could not find class [{0}]", e, parserName);
			return null;
		}
		
		if(clazz.isInterface()){
			throw new InvalidParserException(PARSER_IS_INTERFACE_MESSAGE, parserName);
		}
		
		if(!this.implementsParserInterface(clazz)){
			throw new InvalidParserException(PARSER_DOES_NOT_IMPLEMENT_INTERFACE_MESSAGE, parserName);
		}
		
		try {
			parserObj = clazz.newInstance();
		} catch (InstantiationException e) {
			logger.debugWithParams("Could not instantiate an object of class [{0}]", e, parserName);
		} catch (IllegalAccessException e) {
			logger.debugWithParams("Could not instantiate an object of class [{0}]", e, parserName);
		}
		
		return (ToJsParser)parserObj;
	}
	
	private boolean implementsParserInterface(Class<?> clazz) {
		boolean doesImplement = false;
		
		for(Class<?> interf : clazz.getInterfaces()){
			if(interf == ToJsParser.class){
				doesImplement = true;
				break;
			}else if(this.implementsParserInterface(interf)){
				doesImplement = true;
				break;
			}
		}
		
		return doesImplement;
	}
}
