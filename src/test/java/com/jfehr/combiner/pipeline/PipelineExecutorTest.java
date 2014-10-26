package com.jfehr.combiner.pipeline;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.jfehr.combiner.combiner.ResourceCombiner;
import com.jfehr.combiner.factory.InputSourceReaderFactory;
import com.jfehr.combiner.factory.OutputSourceWriterFactory;
import com.jfehr.combiner.factory.ResourceCombinerFactory;
import com.jfehr.combiner.factory.ResourceTransformerFactory;
import com.jfehr.combiner.input.InputSourceReader;
import com.jfehr.combiner.mojo.Combination;
import com.jfehr.combiner.mojo.InputResources;
import com.jfehr.combiner.output.OutputSourceWriter;
import com.jfehr.combiner.transformer.ResourceTransformer;

@RunWith(MockitoJUnitRunner.class)
public class PipelineExecutorTest {

	@Mock private CombinationValidator mockValidator;
	@Mock private CombinationDefaultsManager mockDefaultsManager;
	@Mock private InputSourceReaderFactory mockInputSourceReaderFactory;
	@Mock private ResourceTransformerFactory mockResourceTransformerFactory;
	@Mock private ResourceCombinerFactory mockCombinerFactory;
	@Mock private OutputSourceWriterFactory mockOutputSourceWriterFactory;
	@Mock private MavenProject mockMavenProject;
	@Mock private Build mockMavenBuild;
	@Mock private InputSourceReader mockInputSourceReader;
	@Mock private ResourceTransformer mockResourceTransformer1;
	@Mock private ResourceTransformer mockResourceTransformer2;
	@Mock private ResourceCombiner mockResourceCombiner;
	@Mock private OutputSourceWriter mockOutputSourceWriter;
	
	@InjectMocks private PipelineExecutor fixture;
	
	@Captor ArgumentCaptor<Map<String, String>> transformedResourcesCaptor;
	
	@Test
	public void testHappyPath() {
		List<Combination> combos = new ArrayList<Combination>();
		Combination one = new Combination();
		Map<String, String> resources = new HashMap<String, String>();
		InputResources inputResources = new InputResources();
		Map<String, String> settings = new HashMap<String, String>();
		List<String> inputResourceIncludes = Arrays.asList(new String[]{"inputResource1"});
		List<String> resourceTransformerStrings = Arrays.asList(new String[]{"transformer1"});
		List<Object> resourceTransformerObjects = new LinkedList<Object>();
		
		resources.put("one", "resource1");
		resources.put("two", "resource2");
		
		resourceTransformerObjects.add(mockResourceTransformer1);
		resourceTransformerObjects.add(mockResourceTransformer2);
		
		inputResources.setIncludes(inputResourceIncludes);
		
		one.setInputSourceReader("inputSourceReader1");
		one.setEncoding("UTF-8");
		one.setSettings(settings);
		one.setInputSources(inputResources);
		one.setTransformers(resourceTransformerStrings);
		one.setCombiner("combiner");
		one.setOutputSourceWriter("outputSourceWriter");
		one.setOutputDestination("outputDestination");
		
		combos.add(one);
		
		when(mockMavenProject.getBuild()).thenReturn(mockMavenBuild);
		when(mockInputSourceReaderFactory.buildObject("inputSourceReader1")).thenReturn(mockInputSourceReader);
		when(mockInputSourceReader.read("UTF-8", inputResourceIncludes, null, settings, mockMavenProject)).thenReturn(resources);
		when(mockResourceTransformerFactory.buildObjectList(resourceTransformerStrings)).thenReturn(resourceTransformerObjects);
		when(mockResourceTransformer1.transform("one", "resource1", settings, mockMavenProject)).thenReturn("resource1");
		when(mockResourceTransformer1.transform("two", "resource2", settings, mockMavenProject)).thenReturn("resource2");
		when(mockResourceTransformer2.transform("one", "resource1", settings, mockMavenProject)).thenReturn("resource1");
		when(mockResourceTransformer2.transform("two", "resource2", settings, mockMavenProject)).thenReturn("resource2");
		when(mockCombinerFactory.buildObject("combiner")).thenReturn(mockResourceCombiner);
		when(mockResourceCombiner.combine(transformedResourcesCaptor.capture(), eq(settings), eq(mockMavenProject))).thenReturn("combinedResources");
		when(mockOutputSourceWriterFactory.buildObject("outputSourceWriter")).thenReturn(mockOutputSourceWriter);
		
		fixture.execute(combos, mockMavenProject);
		
		verify(mockDefaultsManager).setupDefaults(combos.get(0), mockMavenProject);
		verify(mockValidator).validate(combos.get(0));
		verify(mockInputSourceReaderFactory).buildObject("inputSourceReader1");
		verify(mockInputSourceReader).read("UTF-8", inputResourceIncludes, null, settings, mockMavenProject);
		verify(mockResourceTransformerFactory).buildObjectList(resourceTransformerStrings);
		verify(mockResourceTransformer1).transform("two", "resource2", settings, mockMavenProject);
		verify(mockResourceTransformer1).transform("one", "resource1", settings, mockMavenProject);
		verify(mockResourceTransformer1).transform("two", "resource2", settings, mockMavenProject);
		verify(mockResourceTransformer2).transform("one", "resource1", settings, mockMavenProject);
		verify(mockResourceTransformer2).transform("two", "resource2", settings, mockMavenProject);
		verify(mockCombinerFactory).buildObject("combiner");
		verify(mockOutputSourceWriterFactory).buildObject("outputSourceWriter");
		verify(mockOutputSourceWriter).write("UTF-8", "outputDestination", "combinedResources", settings, mockMavenProject);
		
		assertEquals(2, transformedResourcesCaptor.getValue().size());
		assertEquals("resource1", transformedResourcesCaptor.getValue().get("one"));
		assertEquals("resource2", transformedResourcesCaptor.getValue().get("two"));
	}

}
