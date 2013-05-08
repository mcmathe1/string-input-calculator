package com.electricdesignaz.calculator.api;

public class ExpressionParseException extends Exception {
	private static final long serialVersionUID = 1L;

	public ExpressionParseException() {
		super();
	}
	
	public ExpressionParseException(String message) {
		super(message);
	}
	
	public ExpressionParseException(Throwable cause) {
		super(cause);
	}
	
	public ExpressionParseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ExpressionParseException(String message, Throwable cause, boolean arg2, boolean arg3) {
		super(message, cause, arg2, arg3);
	}
	
}
