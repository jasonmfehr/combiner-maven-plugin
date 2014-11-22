package com.github.jasonmfehr.combiner.logging;

import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.annotations.Component;

import com.github.jasonmfehr.combiner.mojo.Combination;

@Component(role=ParameterizedLogger.class)
public class ParameterizedLogger implements Log {

	private Log decoratedLogger;
	
	public ParameterizedLogger() {
		this.decoratedLogger = null;
	}
	
	public ParameterizedLogger(final Log decoratedLogger) {
		this.decoratedLogger = decoratedLogger;
	}
	
	public void setLogger(final Log logger) {
		this.decoratedLogger = logger;
	}
	
	public boolean isDebugEnabled() {
		return this.internalGetLogger().isDebugEnabled();
	}

	public void debug(final CharSequence content) {
		this.internalGetLogger().debug(content);
	}

	public void debug(final CharSequence content, final Throwable error) {
		this.internalGetLogger().debug(content, error);
	}

	public void debug(final Throwable error) {
		this.internalGetLogger().debug(error);
	}
	
	public void debugWithParams(final CharSequence content, final Object... parameters) {
		if(this.isDebugEnabled()){
			this.internalGetLogger().debug(this.formatMessage(content, parameters));
		}
	}
	
	public void debugWithParams(final CharSequence content, final Throwable error, final Object... parameters) {
		if(this.isDebugEnabled()){
			this.internalGetLogger().debug(this.formatMessage(content, parameters), error);
		}
	}

	public boolean isInfoEnabled() {
		return this.internalGetLogger().isInfoEnabled();
	}

	public void info(final CharSequence content) {
		this.internalGetLogger().info(content);
	}

	public void info(final CharSequence content, final Throwable error) {
		this.internalGetLogger().info(content, error);
	}

	public void info(final Throwable error) {
		this.internalGetLogger().info(error);
	}
	
	public void infoWithParams(final CharSequence content, final Object... parameters) {
		if(this.isInfoEnabled()){
			this.internalGetLogger().info(this.formatMessage(content, parameters));
		}
	}

	public boolean isWarnEnabled() {
		return this.internalGetLogger().isWarnEnabled();
	}

	public void warn(final CharSequence content) {
		this.internalGetLogger().warn(content);
	}

	public void warn(final CharSequence content, final Throwable error) {
		this.internalGetLogger().warn(content, error);
	}

	public void warn(final Throwable error) {
		this.internalGetLogger().warn(error);
	}
	
	public void warnWithParams(final CharSequence content, final Object... parameters) {
		if(this.isWarnEnabled()){
			this.internalGetLogger().warn(this.formatMessage(content, parameters));
		}
	}

	public boolean isErrorEnabled() {
		return this.internalGetLogger().isErrorEnabled();
	}

	public void error(final CharSequence content) {
		this.internalGetLogger().error(content);
	}

	public void error(final CharSequence content, final Throwable error) {
		this.internalGetLogger().error(content, error);
	}

	public void error(final Throwable error) {
		this.internalGetLogger().error(error);
	}
	
	public void errorWithParams(final CharSequence content, final Object... parameters) {
		if(this.isErrorEnabled()){
			this.internalGetLogger().error(this.formatMessage(content, parameters));
		}
	}
	
	/**
	 * Determines if the {@code id} field within the provided {@link Combination} exists or not.  If it exists, then a {@link String} is created 
	 * by concatenating an id provided message String with the {@link Combination#getId()} field.  If it does not exist, 
	 * then an empty {@link String} is returned.
	 *  
	 * @param combo {@link Combination} to check if its {@code id} field has been specified
	 * @return {@link String}
	 */
	public String buildCombinationIDString(final Combination combo) {
		return combo.getId() != null && combo.getId().length() > 0 ? " for combination with id " + combo.getId() : "";
	}

	private String charSeqToStr(final CharSequence charSequence) {
		final StringBuilder sb = new StringBuilder(charSequence);
		
		return sb.toString();
	}
	
	private String formatMessage(final CharSequence content, final Object... params) {
		return MessageFormat.format(this.charSeqToStr(content), params);
	}
	
	private Log internalGetLogger() {
		if(this.decoratedLogger == null){
			throw new NullPointerException("Internal logger not set.  This exception may be caused by attempting to use the plexus component before the CombinerMojo class sets the decorated logger");
		}else{
			return this.decoratedLogger;
		}
	}

}
