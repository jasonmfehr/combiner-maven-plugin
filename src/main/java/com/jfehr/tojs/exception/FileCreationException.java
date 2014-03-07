package com.jfehr.tojs.exception;

public class FileCreationException extends AbstractParameterizedException {

	private static final long serialVersionUID = 3126474484334895019L;
	private static final String MESSAGE = "Could not create file at " + AbstractParameterizedException.PARAMETER_PLACEHOLDER;
	
	public FileCreationException(final String location, final Throwable cause) {
		super(location, cause);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
