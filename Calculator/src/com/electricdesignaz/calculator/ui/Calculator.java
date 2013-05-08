package com.electricdesignaz.calculator.ui;

import com.electricdesignaz.calculator.api.Evaluator;
import com.electricdesignaz.calculator.api.ExpressionParseException;

public class Calculator {
	
	public static double calculate(String stringExpr) throws CalculationException {
		
		Evaluator evaluator = new Evaluator(stringExpr);
		
		try {
			return evaluator.evaluate();
		} catch (ExpressionParseException epe) {
			throw new CalculationException(epe.getMessage(), epe);
		}
	}
	
	public static void main(String[] args) throws Exception {
		String expr = "((pi)) + 5";
		System.out.println("\nThe answer is: " + Calculator.calculate(expr));
	}
}