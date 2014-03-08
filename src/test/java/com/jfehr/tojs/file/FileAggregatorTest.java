package com.jfehr.tojs.file;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jfehr.tojs.logging.ParameterizedLogger;
import com.jfehr.tojs.parser.ParserExecutor;

public class FileAggregatorTest {

	private ParameterizedLogger mockLogger;
	private ParserExecutor mockParserExecutor;
	private FileAggregator fixture;
	
	@Before
	public void setUp() {
		mockLogger = Mockito.mock(ParameterizedLogger.class);
		mockParserExecutor = Mockito.mock(ParserExecutor.class);
		
		fixture = new FileAggregator(mockParserExecutor, mockLogger);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
