package com.github.jasonmfehr.tojs.exception;

public class NotWriteableException extends AbstractParameterizedException {

	private static final long serialVersionUID = -1733763273463180683L;
	private static final String MESSAGE = "Could not write file system location " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + ".  The location is not writeable by the executing user.  Ensure that the location is writeable by the executing user.";
	
	public NotWriteableException(final String location) {
		super(location);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}
}
