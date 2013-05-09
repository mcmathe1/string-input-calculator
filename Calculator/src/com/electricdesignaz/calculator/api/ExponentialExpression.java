package com.electricdesignaz.calculator.api;

public class ExponentialExpression extends AbstractExpression {
	
	protected ExponentialExpression() {
		super();
	}
	
	public ExponentialExpression(String expression) {
		super(expression);
	}
	
	@Override
	public double eval() throws ExpressionParseException {
		double answer = 0.0;

		try {
			double[] operands = getOperands();
			answer = Math.pow(operands[0], operands[1]);
			
		} catch (NumberFormatException nfe) {
			throw new ExpressionParseException("Invalid Expression: \"" + expression + "\"", nfe);
		}
		
		return answer;
	}
	
}
