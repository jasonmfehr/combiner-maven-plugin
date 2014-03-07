package com.jfehr.tojs.exception;

public class FileWriteException extends AbstractParameterizedException {

	private static final long serialVersionUID = -9060515377747871619L;
	private static final String MESSAGE = "Exception happened while writing file at {}";
	
	public FileWriteException(final String location, final Throwable cause) {
		super(location, cause);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
