package com.electricdesignaz.calculator.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluator {
	
	protected static final String operatorCharacterClass = "+\\-\\*/\\^";
	
	protected String expression;
	
	public Evaluator(String expression) {
		this(expression, false);
	}
	
	protected Evaluator(String expression, boolean formatted) {
		this.expression = expression;
		if (!formatted) {
			format();
		}
	}
	
	public void setExpression(String expression) {
		setExpression(expression, false);
	}
	
	protected void setExpression(String expression, boolean formatted) {
		this.expression = expression;
		if (!formatted) {
			format();
		}
	}
	
	protected void format() {
		setExpression(expression.trim()
				.toLowerCase()
				.replaceAll("\\s+", "")
				.replaceAll("[\\[{]", "\\(")
				.replaceAll("[\\]}]", "\\)"), true);
		
		// Replace ')(' and '\d(' with ')*(' and '\d*(' to more easily show multiplication
		StringBuilder sb = new StringBuilder(expression);
		String regex = "[^" + operatorCharacterClass + "\\(]\\(";
		Matcher matcher = Pattern.compile(regex).matcher(expression);
		while (matcher.find()) {
			sb.insert(matcher.end() - 1, "*");
			matcher = Pattern.compile(regex).matcher(sb.toString());
		}
		setExpression(sb.toString(), true);
		
		regex = "\\)[^" + operatorCharacterClass + "\\)]";
		matcher = Pattern.compile(regex).matcher(expression);
		while (matcher.find()) {
			sb.insert(matcher.start() + 1, "*");
			matcher = Pattern.compile(regex).matcher(sb.toString());
		}
		setExpression(sb.toString(), true);
		
		// Figure out the implied multiplication by adjacency with functions
		
		formatFunctions();
		setExpression(expression.replace("pi", "(" + String.valueOf(Math.PI) + ")")
				.replace("e", "(" + String.valueOf(Math.E) + ")"), true);
	}
	
	protected void formatFunctions() {
		String formattingExpr = expression.toLowerCase();
		Function[] functions = Function.values();
		for (Function f : functions) {
			formattingExpr = formattingExpr.replace(f.toString().toLowerCase(), f.toString());
		}
		setExpression(formattingExpr, true);
	}
	
	/**
	 * Takes the expression and looks through it to find every where parentheses are used to enclose a section of an expression
	 * that should be evaluated first. 
	 * 
	 * @return Map containing the indices of matching paren pairs
	 * @throws ExpressionParseException
	 */
	private Map<Integer, Integer> getParenIndices() throws ExpressionParseException {
		Map<Integer, Integer> parenPairs = new HashMap<Integer, Integer>();
		
		int tempIndex = 0;
		int parenCounter = 0;
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '(') {
				if (parenCounter == 0) {
					tempIndex = i;
				}
				parenCounter++;
			}
			else if (expression.charAt(i) == ')') {
				parenCounter--;
				if (parenCounter == 0) {
					parenPairs.put(tempIndex, i);
				}
			}
		}
		
		if (parenCounter != 0) throw new ExpressionParseException("Uneven number of parentheses");
		
		return parenPairs;
	}
	
	protected void replaceEvaluation(int startPosition, int endPosition, double evaluation) {
		System.out.println("start position: " + startPosition + ", end position: " + endPosition);
		StringBuilder sb = new StringBuilder(expression);
		sb.replace(startPosition, endPosition, String.valueOf(evaluation));
		System.out.println(sb.toString());
		setExpression(sb.toString(), true);
	}
	
	public double evaluate() throws ExpressionParseException {
		double answer = 0.0;
		String regex = null;
		
		Map<Integer, Integer> parenIndices = getParenIndices();
		for (int firstParen : parenIndices.keySet()) {
			String parenEnclosedExpr = expression.substring(firstParen + 1, parenIndices.get(firstParen));
			System.out.println("Expression: " + expression + "; Expression piece: " + parenEnclosedExpr);
			Evaluator parenEvaluator = new Evaluator(parenEnclosedExpr, true);
			double evaluation = parenEvaluator.evaluate();
			replaceEvaluation(firstParen, parenIndices.get(firstParen) + 1, evaluation);
		}

		// Compute products and quotients
		regex = "(\\d+\\.?\\d*[/\\*]-?\\d+\\.?\\d*)";
		Matcher productMatcher = Pattern.compile(regex).matcher(expression);
		while (productMatcher.find()) {
			Expression productExpression = new ProductExpression(productMatcher.group());
			double product = productExpression.eval();
			replaceEvaluation(productMatcher.start(), productMatcher.end(), product);
			productMatcher = Pattern.compile(regex).matcher(expression);
		}
		
		// Compute sums and differences
		regex = "^(-?\\d+\\.?\\d*[+\\-]-?\\d+\\.?\\d*)";
		Matcher sumMatcher = Pattern.compile(regex).matcher(expression);
		while (sumMatcher.find()) {
			Expression sumExpr = new SumExpression(sumMatcher.group());
			double sum = sumExpr.eval();
			replaceEvaluation(sumMatcher.start(), sumMatcher.end(), sum);
			sumMatcher = Pattern.compile(regex).matcher(expression);
		}
		
		answer = Double.parseDouble(expression);
		return answer;
	}
}