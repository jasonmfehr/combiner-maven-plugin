package com.jfehr.tojs.exception;

public class DirectoryNotFoundException extends AbstractParameterizedException {

	private static final long serialVersionUID = 5456054376424569372L;
	private static final String MESSAGE = "The specified directory " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + " does not exist";
	
	public DirectoryNotFoundException(final String location) {
		super(location);
	}
	
	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
