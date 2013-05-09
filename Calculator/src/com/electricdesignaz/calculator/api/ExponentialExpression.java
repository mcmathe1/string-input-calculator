package com.electricdesignaz.calculator.api;

import org.apache.log4j.Logger;

public class ExponentialExpression extends AbstractExpression {
	
	private static final Logger logger = Logger.getLogger(ExponentialExpression.class);
	
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
			if (expression.charAt(getOperatorIndex()) == '^') {
				double[] operands = getOperands();
				answer = Math.pow(operands[0], operands[1]);
			} else {
				logger.error("Operator is not a \'^\': \"" + expression + "\"");
				throw new ExpressionParseException("Not a exponential expression: \"" + expression + "\"");
			}
			
		} catch (NumberFormatException nfe) {
			logger.error("Error parsing operands as doubles in expression: \"" + expression + "\"");
			throw new ExpressionParseException("Invalid Expression: \"" + expression + "\"", nfe);
		}
		
		return answer;
	}
	
}
