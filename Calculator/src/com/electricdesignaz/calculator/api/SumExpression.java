package com.electricdesignaz.calculator.api;

import org.apache.log4j.Logger;

public class SumExpression extends AbstractExpression {
	
	private static final Logger logger = Logger.getLogger(SumExpression.class);
	
	protected SumExpression() {
		super();
	}
	
	protected SumExpression(String expression) throws ExpressionParseException {
		super(expression);
		if (!isSimpleTwoTermExpression()) {
			logger.error("Expression is not a simple two term expression: \"" + expression + "\"");
			throw new ExpressionParseException("Too many terms");
		}
	}
	
	@Override
	public double eval() throws ExpressionParseException {
		double answer = 0.0;

		if (!isSimpleTwoTermExpression()) {
			logger.error("Expression is not a simple two term expression: \"" + expression + "\"");
			throw new ExpressionParseException("Too many terms");
		}
		
		try {
			double[] operands = getOperands();
			if (expression.charAt(getOperatorIndex()) == '+') {
				answer = operands[0] + operands[1];
			} else if (expression.charAt(getOperatorIndex()) == '-') {
				answer = operands[0] - operands[1];
			} else {
				logger.error("Operator is not a \'+\' or \'-\': \"" + expression + "\"");
				throw new ExpressionParseException("Not a sum or differece expression: \"" + expression + "\"");
			}
		} catch (NumberFormatException nfe) {
			logger.error("Error parsing operands as doubles in expression: \"" + expression + "\"");
			throw new ExpressionParseException("Invalid Expression: \"" + expression + "\"", nfe);
		}
		
		return answer;
	}
	
}
