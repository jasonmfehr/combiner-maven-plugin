package com.jfehr.combiner.logging;

import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;

import com.jfehr.combiner.mojo.Combination;

public class ParameterizedLogger implements Log {

	private Log decoratedLogger;
	
	public ParameterizedLogger(final Log decoratedLogger) {
		this.decoratedLogger = decoratedLogger;
	}
	
	public boolean isDebugEnabled() {
		return this.decoratedLogger.isDebugEnabled();
	}

	public void debug(final CharSequence content) {
		this.decoratedLogger.debug(content);
	}

	public void debug(final CharSequence content, final Throwable error) {
		this.decoratedLogger.debug(content, error);
	}

	public void debug(final Throwable error) {
		this.decoratedLogger.debug(error);
	}
	
	public void debugWithParams(final CharSequence content, final Object... parameters) {
		if(this.isDebugEnabled()){
			this.decoratedLogger.debug(this.formatMessage(content, parameters));
		}
	}
	
	public void debugWithParams(final CharSequence content, final Throwable error, final Object... parameters) {
		if(this.isDebugEnabled()){
			this.decoratedLogger.debug(this.formatMessage(content, parameters), error);
		}
	}

	public boolean isInfoEnabled() {
		return this.decoratedLogger.isInfoEnabled();
	}

	public void info(final CharSequence content) {
		this.decoratedLogger.info(content);
	}

	public void info(final CharSequence content, final Throwable error) {
		this.decoratedLogger.info(content, error);
	}

	public void info(final Throwable error) {
		this.decoratedLogger.info(error);
	}
	
	public void infoWithParams(final CharSequence content, final Object... parameters) {
		if(this.isInfoEnabled()){
			this.decoratedLogger.info(this.formatMessage(content, parameters));
		}
	}

	public boolean isWarnEnabled() {
		return this.decoratedLogger.isWarnEnabled();
	}

	public void warn(final CharSequence content) {
		this.decoratedLogger.warn(content);
	}

	public void warn(final CharSequence content, final Throwable error) {
		this.decoratedLogger.warn(content, error);
	}

	public void warn(final Throwable error) {
		this.decoratedLogger.warn(error);
	}
	
	public void warnWithParams(final CharSequence content, final Object... parameters) {
		if(this.isWarnEnabled()){
			this.decoratedLogger.warn(this.formatMessage(content, parameters));
		}
	}

	public boolean isErrorEnabled() {
		return this.decoratedLogger.isErrorEnabled();
	}

	public void error(final CharSequence content) {
		this.decoratedLogger.error(content);
	}

	public void error(final CharSequence content, final Throwable error) {
		this.decoratedLogger.error(content, error);
	}

	public void error(final Throwable error) {
		this.decoratedLogger.error(error);
	}
	
	public void errorWithParams(final CharSequence content, final Object... parameters) {
		if(this.isErrorEnabled()){
			this.decoratedLogger.error(this.formatMessage(content, parameters));
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

}
