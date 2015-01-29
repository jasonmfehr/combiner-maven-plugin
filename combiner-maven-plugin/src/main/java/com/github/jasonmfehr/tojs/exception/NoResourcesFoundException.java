package com.github.jasonmfehr.tojs.exception;

public class NoResourcesFoundException extends AbstractParameterizedException {

	private static final long serialVersionUID = -5294515904070078969L;
	private static final String MESSAGE = "No input resources were found for combination with id " + AbstractParameterizedException.PARAMETER_PLACEHOLDER;

	public NoResourcesFoundException(final String parameter) {
		super(parameter);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
