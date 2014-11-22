package com.github.jasonmfehr.tojs.exception;

public class FileSystemLocationNotFound extends AbstractParameterizedException {

	private static final long serialVersionUID = 758652950402937419L;
	private static final String MESSAGE = "The specified location " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + " does not exist on the file system";
	
	public FileSystemLocationNotFound(final String location) {
		super(location);
	}
	
	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}
	
}
