package com.jfehr.tojs.exception;

public class ObjectInstantiationException extends AbstractParameterizedException {
	
	private static final long serialVersionUID = -874919662148412952L;
	private static final String MESSAGE = "Could not instantiate object with class " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + ".";
	
	public ObjectInstantiationException(final String className, final Exception cause) {
		super(className, cause);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
