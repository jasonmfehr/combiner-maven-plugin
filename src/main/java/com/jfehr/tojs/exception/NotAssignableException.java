package com.jfehr.tojs.exception;

public class NotAssignableException extends AbstractParameterizedException {

	private static final long serialVersionUID = -3952778049113290917L;
	private static final String MESSAGE = "Object either does not implement expected interface or does not inherit from expected class " + AbstractParameterizedException.PARAMETER_PLACEHOLDER;
	
	public NotAssignableException(final String className) {
		super(className);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
