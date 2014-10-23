package com.jfehr.combiner.mojo;

import static com.jfehr.combiner.testutil.TestUtil.setPrivateField;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.apache.maven.plugin.MojoExecution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.combiner.pipeline.PipelineExecutor;

@RunWith(MockitoJUnitRunner.class)
public class CombinerMojoTest {

	@Mock private PipelineExecutor mockPipelineExecutor;
	@Mock private MojoExecution mockMojoExecution;
	
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
