package com.jfehr.tojs.exception;

import java.text.MessageFormat;

/**
 * Base class for any exceptions that have a single variable parameter 
 * within the exception message.  Builds the exception message using the 
 * subclass's unique error message obtained by calling 
 * {@link AbstractParameterizedException#getExceptionMessage()}.  Any 
 * instances of the string {@link AbstractParameterizedException#PARAMETER_PLACEHOLDER} 
 * will be replaced by the location provided in the constructor. <br /><br />
 * 
 * Subclasses dealing with file system operations are encouraged to include either 
 * the word Directory or File in their class names to help the user distinguish 
 * if the issue is with a directory or a non-directory normal file location.
 * 
 * @author jasonmfehr
 * @since 1.0.0
 * 
 */
public abstract class AbstractParameterizedException extends RuntimeException {

	protected static final String PARAMETER_PLACEHOLDER = "{0}";
	
	private static final long serialVersionUID = 7319642112102383672L;
	
	protected abstract String getExceptionMessage();
	
	private final String parameter;
	
	/**
	 * Constructor.
	 *   
	 * @param parameter {@link String} containing the value of the 
	 *                  parameter for the message
	 */
	public AbstractParameterizedException(final String parameter) {
		super();
		this.parameter = parameter;
	}
	
	/**
	 * Constructor.
	 *   
	 * @param parameter {@link String} containing the value of the 
	 *                  parameter for the message
	 * @param cause {@link Throwable} containing the exception that was 
	 *              the reason for this exception being created
	 */
	public AbstractParameterizedException(final String parameter, final Throwable cause) {
		super(cause);
		this.parameter = parameter;
	}
	
	public String getMessage() {
		return MessageFormat.format(this.getExceptionMessage(), this.parameter);
	}

	public String getParameter() {
		return parameter;
	}
	
}
