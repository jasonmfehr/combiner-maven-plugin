package com.github.jasonmfehr.combiner.combiner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;

@RunWith(MockitoJUnitRunner.class)
public class JSObjectCombinerTest {

	private static final String LINE_SEPARATOR = "\n";
	private static final String TEST_OBJECT_NAME = "set.object.name";
	private static final String EXPECTED_COMPRESSED_RESULTS = "(function(w){w.combiner={\"file2\"=\"two\",\"file1\"=\"one\"};})(window);";
	private static final String EXPECTED_COMPRESSED_RESULTS_SET_OBJECT_NAME = "(function(w){w." + TEST_OBJECT_NAME + "={\"file2\"=\"two\",\"file1\"=\"one\"};})(window);";
	
	private Map<String, String> testContents = new HashMap<String, String>();
	
	@Mock private ParameterizedLogger mockLogger;
	private JSObjectCombiner fixture;
	
	@Before
	public void setUp() throws Exception {
		testContents.put("/somedir/file1.dat", "one");
		testContents.put("/somedir/file2.dat", "two");
		
		fixture = new JSObjectCombiner(mockLogger);
	}
	
	@Test
	public void testNullSettings() {
		assertThat(this.compressResults(fixture.combine(testContents, null, null)), equalTo(EXPECTED_COMPRESSED_RESULTS));
	}
	
	@Test
	public void testNoObjectNameSetting() {
		final Map<String, String> settings = new HashMap<String, String>();
		
		settings.put("foo", "bar");

		assertThat(this.compressResults(fixture.combine(testContents, settings, null)), equalTo(EXPECTED_COMPRESSED_RESULTS));
	}
	
	@Test
	public void testObjectNameInSettings() {
		final Map<String, String> settings = new HashMap<String, String>();
		
		settings.put("jsObjectName", TEST_OBJECT_NAME);
		
		assertThat(this.compressResults(fixture.combine(testContents, settings, null)), equalTo(EXPECTED_COMPRESSED_RESULTS_SET_OBJECT_NAME));
	}
	
	private String compressResults(final String results) {
		return results.replace(LINE_SEPARATOR, "").replace(" ", "");
	}

}
