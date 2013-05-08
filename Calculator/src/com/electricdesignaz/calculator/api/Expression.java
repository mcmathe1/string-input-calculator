package com.electricdesignaz.calculator.api;

public interface Expression {
	
	public double eval() throws ExpressionParseException;
	
}
