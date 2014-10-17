package com.jfehr.combiner.pipeline;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jfehr.combiner.mojo.Combination;
import com.jfehr.combiner.mojo.InputResources;

public class CombinationValidatorTest {

	private CombinationValidator fixture;
	
	@Before
	public void setUp() {
		fixture = new CombinationValidator();
	}
	
	@After
	public void tearDown() {
		fixture = null;
	}
	
	@Test
	public void testEverythingValid() {
		fixture.validate(this.buildValidCombo());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullCharset() {
		this.executeCharsetTest(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBlankCharset() {
		this.executeCharsetTest("");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidCharset() {
		this.executeCharsetTest("invalid charset");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInputSourceReader() {
		final Combination combo = this.buildValidCombo();
		
		combo.setInputSourceReader(null);
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBlankInputSourceReader() {
		final Combination combo = this.buildValidCombo();
		
		combo.setInputSourceReader("");
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInputSources() {
		final Combination combo = this.buildValidCombo();
		
		combo.setInputSources(null);
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullInputSourcesIncludes() {
		final Combination combo = this.buildValidCombo();
		
		combo.getInputSources().setIncludes(null);
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyInputSourcesIncludes() {
		final Combination combo = this.buildValidCombo();
		
		combo.getInputSources().getIncludes().clear();
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullOutputDestination() {
		final Combination combo = this.buildValidCombo();
		
		combo.setOutputDestination(null);
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBlankOutputDestination() {
		final Combination combo = this.buildValidCombo();
		
		combo.setOutputDestination("");
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullOutputSourceWriter() {
		final Combination combo = this.buildValidCombo();
		
		combo.setOutputSourceWriter(null);
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBlankOutputSourceWriter() {
		final Combination combo = this.buildValidCombo();
		
		combo.setOutputSourceWriter("");
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullTransformers() {
		final Combination combo = this.buildValidCombo();
		
		combo.setTransformers(null);
		
		fixture.validate(combo);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyTransformers() {
		final Combination combo = this.buildValidCombo();
		
		combo.getTransformers().clear();
		
		fixture.validate(combo);
	}
	
	private void executeCharsetTest(String testVal) {
		final Combination combo = this.buildValidCombo();
		
		combo.setEncoding(testVal);
		
		fixture.validate(combo);
	}
	
	private Combination buildValidCombo() {
		final Combination combo = new Combination();
		
		combo.setEncoding("UTF-]8");
		combo.setInputSourceReader("inputSourceReader");
		
		combo.setInputSources(new InputResources());
		combo.getInputSources().setIncludes(new ArrayList<String>());
		combo.getInputSources().getIncludes().add("includes");
		
		combo.setOutputDestination("outputDestination");
		combo.setOutputSourceWriter("outputSourceWriter");
		combo.setTransformers(new ArrayList<String>());
		combo.getTransformers().add("optimus prime");
		
		return combo;
	}

}
