import java.util.ArrayList;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Driver {

	static char[] validCharsInFix = { '(', ')', '-', '+', '*', '/' };
	static char[] validCharsPostFix = { '-', '+', '*', '/' };
	static char[] validDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	static char[] validOps = { '*', '/', '+', '-' };
	
	public static String characterArrayListToString(ArrayList<Character> l) {
		String s = "";
		for (Character ch : l) {
			s += String.valueOf(ch.charValue());
		}
		return s;
	}

	public static boolean isOperand(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	public static boolean isOperator(String s) {
		for (char validOp : validOps) {
			if (s.equals(String.valueOf(validOp))) {
				return true;
			}
		}
		return false;
	}

	// Both parameters must be valid for a proper return value
	public static boolean isHigherOrEqualPrecedence(char a, char b) {
		int aIndex = -1, bIndex = -1;
		for (int i = 0; i < validOps.length; i++) {
			if (a == validOps[i]) {
				aIndex = i;
			}

			if (b == validOps[i]) {
				bIndex = i;
			}
		}
		if (bIndex <= aIndex && aIndex > -1 && bIndex > -1) {
			return true;
		}
		return false;
	}

	// returns each line of infix as element in String array
	public static String[] parseInfixExpression(String s) {
		// Remove all spaces
		s = s.replace(" ", "");
		char[] c = s.toCharArray();
		ArrayList<String> elements = new ArrayList<String>();
		ArrayList<Character> number = new ArrayList<Character>();
		for (int i = 0; i < c.length; i++) {
			boolean digitFound = false;
			
			// parse multicharacter numbers
			for (char digit : validDigits) {
				if (c[i] == digit) {
					digitFound = true;
					number.add(c[i]);
					break;
				}
			}

			// stop multicharacter number parsing when operand is found
			if (!digitFound) {
				if (!number.isEmpty()) {
					elements.add(characterArrayListToString(number));
					number.clear();
				}
				elements.add(String.valueOf(c[i]));
			}
		}
		
		// add remaining numbers in number workspace after processing all characters
		if (!number.isEmpty()) {
			elements.add(characterArrayListToString(number));
			number.clear();
		}
		String[] elementsArr = new String[elements.size()];
		return elements.toArray(elementsArr);
	}

	public static boolean areExpressionElementsValid(String[] s, boolean infix) {
		if (s.length == 0) {
			return false;
		}

		int openParensCounter = 0, closeParensCounter = 0;
		for (String e : s) {
			try {
				Integer.parseInt(e);
			} catch (NumberFormatException ex) {
				boolean charIsValid = false;
				char[] validChars;
				if (infix) {
					validChars = validCharsInFix;
				} else {
					validChars = validCharsPostFix;
				}
				if (e.equals("(")) {
					openParensCounter++;
				} else if (e.equals(")")) {
					closeParensCounter++;
				}
				for (char validChar : validChars) {
					if (e.equals(String.valueOf(validChar))) {
						charIsValid = true;
						continue;
					}
				}
				if (!charIsValid) {
					return false;
				}
			}
		}

		// when checking infix notation return invalid  when the number of closing 
		// parentheses don't match the number of open parentheses 
		if (infix && openParensCounter != closeParensCounter) {
			return false;
		}
		return true;
	}

	public static String infixToPostfix(String s) {
		// infix to postfix conversion is done via the following rules:
		// 1. if operand add to output list
		// 2. if open parenthese push it to operator stack
		// 3. if close parenthese keep popping values off operator stack to output list 
		// up until you reach a left parenthese
		// 4. if operator going in has less precedence than current top of operator stack
		// pop off the top of stack and add to output list 
		// and push new operator onto operator stack
		// 5.  any remaining values in operator stack pop to output list
		// and return result of output list as string
		
		
		String[] elements = parseInfixExpression(s);
		if (!areExpressionElementsValid(elements, true)) {
			return null;
		}
		Stack<String> operatorStack = new Stack<>();
		ArrayList<String> postfixChars = new ArrayList<>();
		
		for (int i = 0; i < elements.length; i++) {
			if (isOperand(elements[i])) {
				postfixChars.add(elements[i]);
				postfixChars.add(" ");
				continue;
			}
			if (elements[i].equals("(")) {
				operatorStack.push(elements[i]);
			}
			if (elements[i].equals(")")) {
				while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
					postfixChars.add(operatorStack.pop());
					postfixChars.add(" ");
				}
				if (!operatorStack.isEmpty()) {
					operatorStack.pop();
				}
			}
			if (isOperator(elements[i])) {
				while (!operatorStack.isEmpty()
						&& isHigherOrEqualPrecedence(elements[i].charAt(0), operatorStack.peek().charAt(0))) {
					postfixChars.add(operatorStack.pop());
					postfixChars.add(" ");
				}
				operatorStack.push(elements[i]);
			}
		}
		while (!operatorStack.isEmpty()) {
			postfixChars.add(operatorStack.pop());
			postfixChars.add(" ");
		}
		String postfixString = "";
		for (String e : postfixChars) {
			postfixString += e;
		}
		return postfixString;
	}

	public static int evaluatePostfixExpression(String s) {
		StringTokenizer st = new StringTokenizer(s);

		ArrayList<String> eArr = new ArrayList<>();
		int countTokens = st.countTokens();
		
		for (int i = 0; i < countTokens; i++) {
			eArr.add(st.nextToken());
		}

		String[] elements = new String[st.countTokens()];
		elements = eArr.toArray(elements);
		if (!areExpressionElementsValid(elements, false)) {
			return Integer.MIN_VALUE;
		}
		Stack<Integer> result = new Stack<>();
		
		// iterates through all postfix elements
		// if element is number  add to result stack 
		// and keep looking for numbers until element is operator
		// if operator there has to be 2 operands in stack 
		// to get operation result back in stack
		// and must be one value
		for (String e : elements) {
			if (isOperand(e)) {
				result.add(Integer.parseInt(e));
				continue;
			}
			if (isOperator(e)) {
				if (result.size() < 2) {
					return Integer.MIN_VALUE;
				}
				int opResult = performOperation(result.pop(), result.pop(), e);
				if (opResult == Integer.MIN_VALUE) {
					return Integer.MIN_VALUE;
				}
				result.add(opResult);
			}
		}
		if (!result.isEmpty() && result.size() == 1) {
			return result.pop();
		} else {
			return Integer.MIN_VALUE;
		}
	}

	public static int performOperation(int a, int b, String op) {
		switch (op) {
		case "*":
			return b * a;
		case "/":
			if (a == 0) {
				return Integer.MIN_VALUE;
			}
			return b / a;
		case "+":
			return b + a;
		case "-":
			return b - a;
		default:
			return Integer.MIN_VALUE;
		}
	}

	public static void main(String[] args) {
		File infixFile = new File("infix.txt");
		
		// Reads and Evaluate Infix file
		try {
			BufferedReader input = new BufferedReader(new FileReader(infixFile));
			String txt;
			while ((txt = input.readLine()) != null) {
				System.out.println(txt);
				String[] expressionElements = parseInfixExpression(txt);
				if (!areExpressionElementsValid(expressionElements, true)) {
					System.out.println("Invalid Expression\n");
					continue;
				}
				String postfixString = infixToPostfix(txt);
				int result = evaluatePostfixExpression(postfixString);
				if (result == Integer.MIN_VALUE) {
					System.out.println("Invalid Expression\n");
					continue;
				}
				System.out.printf("expression = %d\n\n", result);
			}
			input.close();
		} catch (IOException e) {
			System.out.printf("error opening infix.txt file: %s", e.getMessage());
		}

		// Reads and Evaluate Postfix file
		File postfixFile = new File("postfix.txt");
		try {
			BufferedReader input = new BufferedReader(new FileReader(postfixFile));
			String txt;
			while ((txt = input.readLine()) != null) {
				System.out.println(txt);
				String[] expressionElements = parseInfixExpression(txt);
				if (!areExpressionElementsValid(expressionElements, false)) {
					System.out.println("Invalid Expression\n");
					continue;
				}
				int result = evaluatePostfixExpression(txt);
				if (result == Integer.MIN_VALUE) {
					System.out.println("Invalid Expression\n");
					continue;
				}
				System.out.printf("expression = %d\n\n", result);
			}
			input.close();
		} catch (IOException e) {
			System.out.printf("error opening postfix.txt file: %s", e.getMessage());
		}
	}
}
