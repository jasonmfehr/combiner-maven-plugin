package com.jfehr.tojs.logging;

import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;

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
	
	public void log(final LoggerLevel level, final CharSequence content) {
		switch(level){
			case ERROR:
				this.error(content);
				break;
			case WARN:
				this.warn(content);
				break;
			case INFO:
				this.info(content);
				break;
			case DEBUG:
				this.debug(content);
				break;
		}
	}

	public void log(final LoggerLevel level, final CharSequence content, final Throwable error) {
		switch(level){
			case ERROR:
				this.error(content, error);
				break;
			case WARN:
				this.warn(content, error);
				break;
			case INFO:
				this.info(content, error);
				break;
			case DEBUG:
				this.debug(content, error);
				break;
		}
	}

	public void log(final LoggerLevel level, final Throwable error) {
		switch(level){
			case ERROR:
				this.error(error);
				break;
			case WARN:
				this.warn(error);
				break;
			case INFO:
				this.info(error);
				break;
			case DEBUG:
				this.debug(error);
				break;
		}
	}
	
	public void logWithParams(final LoggerLevel level, final CharSequence content, final Object... parameters) {
		switch(level){
			case ERROR:
				this.errorWithParams(content, parameters);
				break;
			case WARN:
				this.warnWithParams(content, parameters);
				break;
			case INFO:
				this.infoWithParams(content, parameters);
				break;
			case DEBUG:
				this.debugWithParams(content, parameters);
				break;
		}
	}
	
	private String charSeqToStr(final CharSequence charSequence) {
		final StringBuilder sb = new StringBuilder(charSequence);
		
		return sb.toString();
	}
	
	private String formatMessage(final CharSequence content, final Object... params) {
		return MessageFormat.format(this.charSeqToStr(content), params);
	}

}
