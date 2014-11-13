package com.jfehr.combiner.mojo;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.jfehr.combiner.logging.ParameterizedLogger;
import com.jfehr.combiner.pipeline.PipelineExecutor;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 */
@Mojo(name="combine", defaultPhase=LifecyclePhase.PROCESS_SOURCES, threadSafe=true)
public class CombinerMojo extends AbstractMojo {

	/**
	 * list of combinations with each one representing a set of 
	 * resources to combine and the pipeline implementations to 
	 * use when combining them
	 */
	@Parameter(required=true)
	private List<Combination> combinations;
	
	/**
	 * determines if the plugin execution should be skipped
	 */
	@Parameter(property="combiner.skip", defaultValue="false")
	private Boolean skip;
	
	@Component
	private MavenProject mavenProject;
	
	@Component
	private MojoExecution mojoExecution;
	
	@Component
	private PipelineExecutor pipelineExecutor;
	
	@Component
	private ParameterizedLogger logger;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		logger.setLogger(this.getLog());
		
		logger.warnWithParams("this is a parameterized warning in {0}", CombinerMojo.class);
		if(!Boolean.TRUE.equals(this.skip)){
			this.logger.debugWithParams("Entering {0} goal", mojoExecution.getGoal());
			//new PipelineExecutor().execute(this.combinations, this.mavenProject);
			pipelineExecutor.execute(this.combinations, this.mavenProject);
			this.logger.debugWithParams("Exiting {0} goal", mojoExecution.getGoal());
		}else{
			this.logger.info("skipping combiner-maven-plugin execution");
		}
	}

}
