package com.jfehr.tojs.exception;

public class FileReadException extends AbstractParameterizedException {

	private static final long serialVersionUID = -9060515377747871619L;
	private static final String MESSAGE = "Exception happened while reading file at {}";
	
	public FileReadException(final String location, final Throwable cause) {
		super(location, cause);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
