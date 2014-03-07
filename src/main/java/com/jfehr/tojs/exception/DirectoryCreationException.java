package com.jfehr.tojs.exception;

public class DirectoryCreationException extends AbstractParameterizedException {

	private static final long serialVersionUID = -8346748631973389529L;
	private static final String MESSAGE = "Cannot create directory " + AbstractParameterizedException.PARAMETER_PLACEHOLDER;
	
	public DirectoryCreationException(final String location) {
		super(location);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}
	

}
