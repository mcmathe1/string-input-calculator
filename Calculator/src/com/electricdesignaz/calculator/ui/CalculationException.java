package com.electricdesignaz.calculator.ui;

public class CalculationException extends Exception {
	private static final long serialVersionUID = 1L;

	public CalculationException() {
		super();
	}
	
	public CalculationException(String arg0) {
		super(arg0);
	}
	
	public CalculationException(Throwable arg0) {
		super(arg0);
	}
	
	public CalculationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public CalculationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	
}
