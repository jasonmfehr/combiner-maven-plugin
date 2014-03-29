package com.jfehr.tojs.exception;

/**
 * 
 * @author jasonmfehr
 * @since 1.0.0
 *
 */
public class ParserNotFoundException extends AbstractParameterizedException {

	private static final long serialVersionUID = -4289402631882865199L;
	private static final String MESSAGE = "Could not instantiate parser named " + AbstractParameterizedException.PARAMETER_PLACEHOLDER + ".  Execute in debug mode for more information";
	
	public ParserNotFoundException(final String parserName) {
		super(parserName);
	}

	@Override
	protected String getExceptionMessage() {
		return MESSAGE;
	}

}
