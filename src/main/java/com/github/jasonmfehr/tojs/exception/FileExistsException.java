package com.github.jasonmfehr.tojs.exception;

public class FileExistsException extends AbstractParameterizedException {

	private static final long serialVersionUID = 7651711430948008788L;
	private static final String MESSAGE = "Output file " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + " exists.  It must not exist.";
	
	public FileExistsException(final String location) {
		super(location);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}
}
