package com.jfehr.combiner.combiner;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.mojo.Setting;

public class JSObjectCombinerTest {

	private static final String LINE_SEPARATOR = "\n";
	private static final String TEST_OBJECT_NAME = "set.object.name";
	private static final String EXPECTED_COMPRESSED_RESULTS = "(function(w){w.combiner={\"file2\"=\"two\";\"file1\"=\"one\";};})(window);";
	private static final String EXPECTED_COMPRESSED_RESULTS_SET_OBJECT_NAME = "(function(w){w." + TEST_OBJECT_NAME + "={\"file2\"=\"two\";\"file1\"=\"one\";};})(window);";
	
	@Mock private ParameterizedLogger mockLogger;
	private JSObjectCombiner fixture;
	private Map<String, String> testContents = new HashMap<String, String>();
	
	@Before
	public void setUp() {
		initMocks(this);
		testContents.put("/somedir/file1.dat", "one");
		testContents.put("/somedir/file2.dat", "two");
		
		fixture = new JSObjectCombiner(mockLogger);
	}
	
	@Test
	public void testNullSettings() {
		assertEquals(EXPECTED_COMPRESSED_RESULTS, this.compressResults(fixture.combine(testContents, null, null)));
	}
	
	@Test
	public void testNoObjectNameSetting() {
		final List<Setting> settings = new ArrayList<Setting>();
		final Setting objectNameSetting = new Setting();
		
		objectNameSetting.setKey("foo");
		objectNameSetting.setValue("bar");
		settings.add(objectNameSetting);

		assertEquals(EXPECTED_COMPRESSED_RESULTS, this.compressResults(fixture.combine(testContents, settings, null)));
	}
	
	@Test
	public void testObjectNameInSettings() {
		final List<Setting> settings = new ArrayList<Setting>();
		final Setting objectNameSetting = new Setting();
		
		objectNameSetting.setKey("jsObjectName");
		objectNameSetting.setValue(TEST_OBJECT_NAME);
		settings.add(objectNameSetting);
		
		assertEquals(EXPECTED_COMPRESSED_RESULTS_SET_OBJECT_NAME, this.compressResults(fixture.combine(testContents, settings, null)));
	}
	
	private String compressResults(final String results) {
		return results.replace(LINE_SEPARATOR, "").replace(" ", "");
	}

}
