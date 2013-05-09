package com.electricdesignaz.calculator.api;

import org.apache.log4j.Logger;

public abstract class AbstractExpression implements Expression {
	
	private static final Logger logger = Logger.getLogger(AbstractExpression.class);
	
	protected String expression;
	
	protected AbstractExpression() {
		expression = "";
	}
	
	protected AbstractExpression(String expression) {
		this.expression = expression;
	}
	
	protected void setExpression(String expression) {
		this.expression = expression;
	}
	
	protected boolean isNumber() {
		return expression.matches("-?\\d+\\.?\\d*");
	}
	
	/**
	 * A complex two term expression includes a number followed by an operator, then a function involving a number (eg. sin(3.14))
	 * A simple two term expression will also return true
	 * 
	 * NOTE: complex is not intended to mean of or relating to imaginary (complex) numbers
	 * 
	 * @return true if the expression is a 2 term expression
	 * 			false if it is not
	 */
	protected boolean isComplexTwoTermExpression() {
		return expression.matches("^-?\\d+\\.?\\d*[" + Evaluator.operatorCharacterClass + "]-?[^" + Evaluator.operatorCharacterClass + "]+");
	}
	
	/**
	 * A simple two term expression consists of a number followed by a operator followed by another number
	 * 
	 * @return true if the expression is a simple 2 term expression
	 * 			false if it is not
	 */
	protected boolean isSimpleTwoTermExpression() {
		return expression.matches("^-?\\d+\\.?\\d*[" + Evaluator.operatorCharacterClass + "]-?\\d+\\.?\\d*");
	}
	
	protected static boolean isOperator(char character) {
		return (character == '*' || character == '/' || character == '+' || character == '-' || character == '^');
	}
	
	/**
	 * Can be called without a given operator index and the getOperator() method will be called to get the operator index
	 * 
	 * @see #getOperands(int)
	 */
	protected double[] getOperands() throws NumberFormatException, ExpressionParseException {
		return getOperands(getOperatorIndex());
	}
	
	/**
	 * Gets the terms of an arithmetic expression in double form.  
	 * NOTE: ASSUMES FORMAT OF '[operand][operator][operand]' WITH NO ADDITIONAL OPERATORS OR OPERANDS (aside from a possible negative sign)
	 * 
	 * @param operatorIndex
	 * @return an array containing the first and second terms of arithmetic expression, first operand being index 0, second operand being index 1;
	 * @throws NumberFormatException if the operands aren't numbers
	 * @throws ExpressionParseException if there are more than two operands
	 */
	protected double[] getOperands(int operatorIndex) throws NumberFormatException, ExpressionParseException {
		double[] operands = new double[2];

		operands[0] = Double.parseDouble(expression.substring(0, operatorIndex));
		
		if (isSimpleTwoTermExpression()) {
			operands[1] = Double.parseDouble(expression.substring(operatorIndex + 1));
			
		} else if (isComplexTwoTermExpression()) {
			operands[1] = new Evaluator(expression.substring(operatorIndex + 1), true).evaluate();
			
		} else {
			logger.error("Expression is not a two term expression: \"" + expression + "\"");
			throw new ExpressionParseException("Expression \"" + expression + "\" contains more than two operands");
		}
		
		return operands;
	}
	
	/**
	 * If the expression is a two term expression, returns the index of the operator, 
	 * unless it is a negative sign for an operand  
	 * 
	 * @return the index of the operator
	 * @throws ExpressionParseException if no operators are found in the expression or 
	 * 			the expression isn't a two term expression
	 */
	protected int getOperatorIndex() throws ExpressionParseException {
		if (!isComplexTwoTermExpression()) throw new ExpressionParseException("Expression \"" + expression + "\" contains more than 1 operator");
		
		for (int i = 1; i < expression.length(); i++) {
			if (isOperator(expression.charAt(i))) {
				return i;
			}
		}
		
		logger.error("No operators found in expression: \"" + expression + "\"");
		throw new ExpressionParseException("No operators in expression");
	}
}