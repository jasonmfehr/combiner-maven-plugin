package com.jfehr.tojs.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.jfehr.tojs.logging.ParameterizedLogger;

public class ParserExecutorTest {

	private ParserExecutor fixture;
	
	@Mock private ParserFactory mockParserFactory;
	@Mock private ParameterizedLogger mockLogger;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		
		fixture = new ParserExecutor(mockParserFactory, mockLogger);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullParserList() {
		fixture.execute(null, "");
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullContents() {
		fixture.execute(new ArrayList<String>(), null);
	}
	
	@Test
	public void testHappyPath() {
		final List<String> parsersList = new ArrayList<String>();
		final Map<String, ToJsParser> mockParsers = new HashMap<String, ToJsParser>();
		final int numParsers = 4;
		final String actual;
		String parserName;
		
		for(int i=1; i<=numParsers; i++){
			parserName = Integer.toString(i);
			mockParsers.put(parserName, this.buildMockParserObject(i));
			parsersList.add(parserName);
		}
		
		actual = fixture.execute(parsersList, "1");
		assertEquals(Integer.toString(numParsers + 1), actual);
		
		for(Entry<String, ToJsParser> mockParser : mockParsers.entrySet()){
			Mockito.verify(mockParser.getValue()).parse(mockParser.getKey());
		}
	}
	
	@Test
	public void testNoParsers() {
		final String actual;
		
		actual = fixture.execute(new ArrayList<String>(), "");
		
		assertEquals("", actual);
		
		Mockito.verifyZeroInteractions(mockParserFactory);
	}

	private ToJsParser buildMockParserObject(int number) {
		final ToJsParser mockParser;
		final String parserName = Integer.toString(number);
		
		mockParser = Mockito.mock(ToJsParser.class);
		Mockito.when(mockParserFactory.buildParser(parserName)).thenReturn(mockParser);
		Mockito.when(mockParser.parse(parserName)).thenReturn(Integer.toString(number + 1));
		
		return mockParser;
	}

}
