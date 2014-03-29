package com.jfehr.tojs.exception;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 *
 */
public class InvalidParserException extends AbstractParameterizedException {

	private static final long serialVersionUID = -5238054214469781883L;
	private static final String MESSAGE = "Invalid parser " + AbstractParameterizedException.PARAMETER_PLACEHOLDER;
	
	private final String concatMessage;
	
	public InvalidParserException(String additionalMessage, String parameter) {
		super(parameter);
		this.concatMessage = MESSAGE + additionalMessage;
	}

	@Override
	protected String getExceptionMessage() {
		return this.concatMessage;
	}

}
