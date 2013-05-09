package com.electricdesignaz.calculator.api;

public abstract class AbstractExpression implements Expression {
	
	protected String expression;
	
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
	
	/*protected void format() {
		setExpression(expression.trim()
						.toLowerCase()
						.replaceAll("\\s+", "")
						.replaceAll("[\\[{]", "\\(")
						.replaceAll("[\\]}]", "\\)"));
		
		// Replace ')(' and '\d(' with ')*(' and '\d*(' to more easily show multiplication
		StringBuilder sb = new StringBuilder(expression);
		String regex = "[^" + operatorCharacterClass + "\\(]\\(";
		Matcher matcher = Pattern.compile(regex).matcher(expression);
		while (matcher.find()) {
			sb.insert(matcher.end() - 1, "*");
			matcher = Pattern.compile(regex).matcher(sb.toString());
		}
		setExpression(sb.toString());
		
		regex = "\\)[^" + operatorCharacterClass + "\\)]";
		matcher = Pattern.compile(regex).matcher(expression);
		while(matcher.find()) {
			sb.insert(matcher.start() + 1, "*");
			matcher = Pattern.compile(regex).matcher(sb.toString());
		}
		setExpression(sb.toString());
		
		// Figure out the implied multiplication by adjacency with functions
		formatFunctions();
		setExpression(expression.replace("pi", "(" + String.valueOf(Math.PI) + ")")
							.replace("e", "(" + String.valueOf(Math.E) + ")"));
	}
	
	protected void formatFunctions() {
		String formattingExpr = expression.toLowerCase();
		Function[] functions = Function.values();
		for (Function f : functions) {
			formattingExpr = formattingExpr.replace(f.toString().toLowerCase(), f.toString());
		}
		setExpression(formattingExpr);
	}*/
	
	/**
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
		throw new ExpressionParseException("No operators in expression");
	}
	/*
		double answer = 0.0;

		Map<Expression, Integer> parentheticExprs = null;
		try {
			parentheticExprs = getParentheticalExpressions();
		} catch (ExpressionParseException e) {
			System.err.println(e.getMessage());
		}
		
		for (Expression expr : parentheticExprs.keySet()) {
			System.out.println("Expression: " + expression + "; Expression piece: " + expr.expression);
			int startPosition = parentheticExprs.get(expr);
			int endPosition = startPosition + expr.expression.length();
			double evaluation = expr.eval();
			replaceEvaluation(startPosition, endPosition, evaluation);
		}
		setExpression(expression.replaceAll("[()]", ""));
		
		// Compute products and quotients
		String regex = "(\\d+\\.?\\d*[/\\*]-?\\d+\\.?\\d*)";
		Matcher productMatcher = Pattern.compile(regex).matcher(expression);
		while (productMatcher.find()) {
			Expression productExpression = new Expression(productMatcher.group(), true);
			double product = productExpression.multiply();
			replaceEvaluation(productMatcher.start(), productMatcher.end(), product);
			productMatcher = Pattern.compile(regex).matcher(expression);
		}
		
		// Compute sums and differences
		regex = "^(-?\\d+\\.?\\d*[+-]-?\\d+\\.?\\d*)";
		Matcher sumMatcher = Pattern.compile(regex).matcher(expression);
		while (sumMatcher.find()) {
			Expression sumExpr = new Expression(sumMatcher.group(), true);
			double sum = sumExpr.sum();
			replaceEvaluation(sumMatcher.start(), sumMatcher.end(), sum);
			sumMatcher = Pattern.compile(regex).matcher(expression);
		}
		
		answer = Double.parseDouble(expression);
		return answer;
	}*/
	
	/*public double sum() throws ExpressionParseException {
		double firstTerm = 0.0;
		double lastTerm = 0.0;
		double answer = 0.0;
		
		try {
			for (int i = 1; i < expression.length(); i++) {
				if (expression.charAt(i) == '+') {
					double[] operands = getOperands(i);
					firstTerm = operands[0];
					lastTerm = operands[1];
					answer = firstTerm + lastTerm;
					break;
				} else if (expression.charAt(i) == '-') {
					double[] operands = getOperands(i);
					firstTerm = operands[0];
					lastTerm = operands[1];
					answer = firstTerm - lastTerm;
					break;
				}
			}
		} catch (NumberFormatException nfe) {
			throw new ExpressionParseException("Invalid Expression", nfe);
		}
		
		return answer;
	}
	
	public double multiply() throws ExpressionParseException {
		double firstTerm = 0.0;
		double lastTerm = 0.0;
		double answer = 0.0;
		
		try {
			for (int i = 1; i < expression.length(); i++) {
				if (expression.charAt(i) == '*') {
					double[] operands = getOperands(i);
					firstTerm = operands[0];
					lastTerm = operands[1];
					answer = firstTerm * lastTerm;
					break;
				} else if (expression.charAt(i) == '/') {
					double[] operands = getOperands(i);
					firstTerm = operands[0];
					lastTerm = operands[1];
					answer = firstTerm / lastTerm;
					break;
				}
			}
		} catch (NumberFormatException nfe) {
			throw new ExpressionParseException("Invalid Expression", nfe);
		}
		
		return answer;
	}*/
}