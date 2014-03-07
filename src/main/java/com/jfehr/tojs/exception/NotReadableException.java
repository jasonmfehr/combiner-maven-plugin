package com.jfehr.tojs.exception;

public class NotReadableException extends AbstractParameterizedException {

	private static final long serialVersionUID = -1733763273463180683L;
	private static final String MESSAGE = "Could not read file system location " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + ".  The location is not readable by the executing user.  Ensure that the location is readable by the executing user.";
	
	public NotReadableException(final String location) {
		super(location);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}
}
