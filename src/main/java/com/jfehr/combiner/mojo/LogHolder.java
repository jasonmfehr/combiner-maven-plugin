package com.jfehr.combiner.mojo;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.jfehr.combiner.logging.ParameterizedLogger;

/**
 * <p>Stores the maven configured {@link Log} used for all plugin logging.  This class is provided in anticipation 
 * of switching to JSR-330 dependency injection in the future.  Maven currently does not support injection of the 
 * configured {@link Log}, so a static reference to the configured {@link Log} object is maintained by this class.</p>
 * 
 * <p>The static {@link Log} reference is set by the {@link CombinerMojo} class as the very first statement it executes.  
 * Therefore, any object can count on the {@link LogHolder#getParamLogger()} method returning the correct logger</p>
 * 
 * @author jasonfehr
 * @since 1.0.0
 */
public final class LogHolder {

	private static ParameterizedLogger logger = null;
	
	private LogHolder() {
	}
	
	static void setLog(Log log) {
		logger = new ParameterizedLogger(log);
	}
	
	/**
	 * Returns a {@link ParameterizedLogger} object that decorates the {@link Log} object that  was injected into the mojo by maven.  
	 * If the {@link LogHolder#setLog(Log)} method has not been called, sets up a new {@link ParameterizedLogger} that decorates a 
	 * new {@link SystemStreamLog}.  <b>Note:</b> the maven documentation says  that instantiating a {@link Log} in this manner will 
	 * not result in a correctly configured logger, thus this functionality should only be used for unit testing when the logger does not matter.
	 * 
	 * @return {@link ParameterizedLogger}
	 */
	public static ParameterizedLogger getParamLogger() {
		if(logger == null){
			logger = new ParameterizedLogger(new SystemStreamLog());
		}
		
		return logger;
	}

}
