package com.jfehr.tojs.parser;

import static com.jfehr.combiner.testutil.TestUtil.getPrivateField;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.tojs.exception.InvalidParserException;
import com.jfehr.tojs.exception.ParserNotFoundException;
import com.jfehr.tojs.testutil.MockParserNonDefaultPackage;

public class ParserFactoryTest {

	private static final String MOCK_PARSER_DEFAULT_PKG = "ParserFactoryTest$MockParserImplementsIface";
	private static final String MOCK_PARSER_INVALID = "ParserFactoryTest$MockInvalidParser";
	private static final String MOCK_PARSER_INHERITS_IFACE = "ParserFactoryTest$MockParserInheritsIface";
	private static final String MOCK_PARSER_NON_DEFAULT_PKG = "com.jfehr.tojs.testutil.MockParserNonDefaultPackage";
	private static final String MOCK_PARSER_IFACE = "ParserFactoryTest$ToJsParserSubIface";
	private static final String MOCK_PARSER_NONEXISTANT = "NonExistantParser";
	
	private ParserFactory fixture;
	
	@Mock private ParameterizedLogger mockLogger;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		fixture = new ParserFactory(mockLogger);
	}
	
	@Test(expected=NullPointerException.class)
	public void testParserNullParserNamesList() {
		fixture.buildParsers(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void testParserNullParserName() {
		fixture.buildParser(null);
	}
	
	@Test
	public void testParserFullPackage() {
		assertEquals(MockParserNonDefaultPackage.class, fixture.buildParser(MOCK_PARSER_NON_DEFAULT_PKG).getClass());
	}
	
	@Test
	public void testParserDefaultPackage() {
		assertEquals(MockParserImplementsIface.class, fixture.buildParser(MOCK_PARSER_DEFAULT_PKG).getClass());
	}
	
	@Test
	public void testParserInheritsIface() {
		assertEquals(MockParserInheritsIface.class, fixture.buildParser(MOCK_PARSER_INHERITS_IFACE).getClass());
	}
	
	@Test(expected=InvalidParserException.class)
	public void testParserIface() {
		fixture.buildParser(MOCK_PARSER_IFACE);
	}
	
	@Test(expected=InvalidParserException.class)
	public void testParserInvalid() {
		fixture.buildParser(MOCK_PARSER_INVALID);
	}
	
	@Test(expected=ParserNotFoundException.class)
	public void testParserNonExistant() {
		fixture.buildParser(MOCK_PARSER_NONEXISTANT);
	}
	
	@Test
	public void testParserListHappyPath() {
		final List<String> parserList = new ArrayList<String>();
		final List<Class<?>> expectedClasses = new ArrayList<Class<?>>();
		final List<ToJsParser> actualParsers;
		
		expectedClasses.add(MockParserImplementsIface.class);
		expectedClasses.add(MockParserInheritsIface.class);
		expectedClasses.add(MockParserNonDefaultPackage.class);
		expectedClasses.add(MockParserInheritsIface.class);
		
		parserList.add(MOCK_PARSER_DEFAULT_PKG);
		parserList.add(MOCK_PARSER_INHERITS_IFACE);
		parserList.add(MOCK_PARSER_NON_DEFAULT_PKG);
		parserList.add(MOCK_PARSER_INHERITS_IFACE);
		actualParsers = fixture.buildParsers(parserList);
		
		assertEquals(4, actualParsers.size());
		for(ToJsParser parser : actualParsers){
			for(Class<?> clazz : expectedClasses){
				if(clazz == parser.getClass()){
					expectedClasses.remove(clazz);
					break;
				}
			}
		}
		
		assertTrue(expectedClasses.isEmpty());
	}
	
	@Test(expected=ParserNotFoundException.class)
	public void testParserListInvalidParsers() {
		final List<String> parserList = new ArrayList<String>();
		
		parserList.add(MOCK_PARSER_DEFAULT_PKG);
		parserList.add(MOCK_PARSER_NONEXISTANT);
		fixture.buildParsers(parserList);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testParserCaching() {
		final Map<String, ToJsParser> parserCache;
		final ToJsParser parser;
		
		assertEquals(MockParserNonDefaultPackage.class, fixture.buildParser(MOCK_PARSER_NON_DEFAULT_PKG).getClass());
		
		parserCache = (Map<String, ToJsParser>)getPrivateField(fixture, "parserCache");
		assertEquals(1, parserCache.size());
		assertTrue(parserCache.containsKey(MOCK_PARSER_NON_DEFAULT_PKG));
		parser = parserCache.get(MOCK_PARSER_NON_DEFAULT_PKG);
		assertEquals(MockParserNonDefaultPackage.class, parser.getClass());
	}
	
	//=== Mock classes for testing ===\\
	public static class MockParserImplementsIface implements ToJsParser {
		public String parse(String value) { return null; }
	}
	
	public static class MockInvalidParser {}
	
	public static interface ToJsParserSubIface extends ToJsParser {}
	
	public static class MockParserInheritsIface implements ToJsParserSubIface {
		public String parse(String value) { return null; }
	}

}
