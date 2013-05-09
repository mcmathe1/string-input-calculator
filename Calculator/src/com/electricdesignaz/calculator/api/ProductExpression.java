package com.electricdesignaz.calculator.api;

public class ProductExpression extends AbstractExpression {
	
	public ProductExpression(String expression) throws ExpressionParseException {
		super(expression);
		if (!isSimpleTwoTermExpression()) {
			throw new ExpressionParseException("Too many terms");
		}
	}
	
	@Override
	public double eval() throws ExpressionParseException {
		double answer = 0.0;

		try {
			double[] operands = getOperands();
			if (expression.charAt(getOperatorIndex()) == '*') {
				answer = operands[0] * operands[1];
			} else if (expression.charAt(getOperatorIndex()) == '/') {
				answer = operands[0] / operands[1];
			}
		} catch (NumberFormatException nfe) {
			throw new ExpressionParseException("Invalid Expression: \"" + expression + "\"", nfe);
		}
		
		return answer;
	}
	
}
