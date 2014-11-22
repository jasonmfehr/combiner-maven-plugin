package com.github.jasonmfehr.combiner.mojo;

import static com.github.jasonmfehr.combiner.testutil.TestUtil.setPrivateField;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.jasonmfehr.combiner.logging.ParameterizedLogger;
import com.github.jasonmfehr.combiner.pipeline.PipelineExecutor;

@RunWith(MockitoJUnitRunner.class)
public class CombinerMojoTest {

	@Mock private PipelineExecutor mockPipelineExecutor;
	@Mock private MojoExecution mockMojoExecution;
	@Mock private ParameterizedLogger mockLogger;
	
	@InjectMocks private CombinerMojo fixture;
	
	@Test
	public void testSkipTrue() throws Exception {
		setPrivateField(fixture, "skip", Boolean.TRUE);
		
		fixture.execute();
		
		verifyZeroInteractions(mockPipelineExecutor);
	}
	
	@Test
	public void testSkipFalse() throws Exception {
		setPrivateField(fixture, "skip", Boolean.FALSE);
		when(mockMojoExecution.getGoal()).thenReturn("goal");
		
		fixture.execute();
		
		verify(mockPipelineExecutor).execute(null, null);
		
	}

}
