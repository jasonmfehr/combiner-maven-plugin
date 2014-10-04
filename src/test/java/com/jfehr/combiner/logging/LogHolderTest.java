package com.jfehr.combiner.logging;

import static com.jfehr.combiner.testutil.TestUtil.getPrivateField;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

public class LogHolderTest {

	@Test
	public void testSetLog() {
		final Log mockLogger = mock(Log.class);
		final ParameterizedLogger actualLogger;
		
		LogHolder.setLog(mockLogger);
		actualLogger = LogHolder.getParamLogger();
		
		assertEquals(mockLogger, getPrivateField(actualLogger, "decoratedLogger"));
	}

}
