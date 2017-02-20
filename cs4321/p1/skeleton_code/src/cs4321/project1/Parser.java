package cs4321.project1;

import cs4321.project1.tree.*;

/**
 * Class for a parser that can parse a string and produce an expression tree. To
 * keep the code simple, this does no input checking whatsoever so it only works
 * on correct input.
 * 
 * An expression is one or more terms separated by + or - signs. A term is one
 * or more factors separated by * or / signs. A factor is an expression in
 * parentheses (), a factor with a unary - before it, or a number.
 * 
 * @author Lucja Kot
 * @author Your names and netids go here
 */
public class Parser {

	private String[] tokens;
	private int currentToken; // pointer to next input token to be processed

	/**
	 * @precondition input represents a valid expression with all tokens
	 *               separated by spaces, e.g. "3.0 - ( 1.0 + 2.0 ) / - 5.0. All
	 *               tokens must be either numbers that parse to Double, or one
	 *               of the symbols +, -, /, *, ( or ), and all parentheses must
	 *               be matched and properly nested.
	 */
	public Parser(String input) {
		this.tokens = input.split("\\s+");
		currentToken = 0;
	}

	/**
	 * Parse the input and build the expression tree
	 * 
	 * @return the (root node of) the resulting tree
	 */
	public TreeNode parse() {
		return expression();
	}

	/**
	 * Parse the remaining input as far as needed to get the next factor
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode factor() {
		String token = tokens[currentToken];
		System.out.println("parsing factor. " + "current token: " + token);
		currentToken++;
		if (token.equals("(")) return expression();
		//else if (token.equals(")")) return term();
		else if (token.equals("-")) {
			UnaryMinusTreeNode umtn = new UnaryMinusTreeNode(parse());
			return umtn;
		}
		else {
			try {
				Double d = new Double(token);
				LeafTreeNode ltn = new LeafTreeNode(d.doubleValue());
				//currentToken++;
				return ltn;
			}
			catch (NumberFormatException e) {
				return expression();
			}
		}
	}

	/**
	 * Parse the remaining input as far as needed to get the next term
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode term() {
		TreeNode left = factor();

		while (currentToken < tokens.length && 
				(tokens[currentToken].equals("*") || tokens[currentToken].equals("/"))) {
			String operand = tokens[currentToken];
			System.out.println("parsing term. " + "current token: " + operand);
			currentToken++;
			TreeNode right = factor();
			if (operand.equals("*")) {
				TreeNode mtn = new MultiplicationTreeNode(left, right);
				left = mtn;
			}
			else if (operand.equals("/")) {
				TreeNode dtn = new DivisionTreeNode(left, right);
				left = dtn;
			}
			// consume next token if close paren
			currentToken = (currentToken < tokens.length && tokens[currentToken].equals(")")) 
					? (currentToken+1) : currentToken;
		}
		return left;
	}

	/**
	 * Parse the remaining input as far as needed to get the next expression
	 * 
	 * @return the (root node of) the resulting subtree
	 */ 
	
	private TreeNode expression() {
		TreeNode left = term();

		while (currentToken < tokens.length && 
				(tokens[currentToken].equals("+") || tokens[currentToken].equals("-"))) {
			String operand = tokens[currentToken];
			System.out.println("parsing expr. " + "current token: " + operand);
			currentToken++;
			TreeNode right = term();
			if (operand.equals("+")) {
				TreeNode atn = new AdditionTreeNode(left, right);
				left = atn;
			}
			else if (operand.equals("-")) {
				TreeNode stn = new SubtractionTreeNode(left, right);
				left = stn;
			}
			// consume next token if close paren
			currentToken = (currentToken < tokens.length && tokens[currentToken].equals(")")) 
					? (currentToken+1) : currentToken;
		}
		return left;
	}
}
