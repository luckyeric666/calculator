package com.airwallex;

import static com.airwallex.Operator.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;
import java.util.Stack;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Calculator {

	private static final Logger log = LoggerFactory.getLogger(Calculator.class);

	/**
	 * Stack of operators performed so far
	 * Each operator is itself a stack recording numbers just before the operator applies
	 * A new stack with result will be pushed onto calculator after the operator applies
	 * The top most stack gives the latest result of this calculator
	 */
	protected Stack<Stack<RPNDecimal>> calculator = new Stack<Stack<RPNDecimal>>();

	protected Stack<RPNDecimal> stack; //top most stack

	@Autowired
	private ApplicationContext context;

	/**
	 * Will take a line of input, and update calculator accordingly.
	 * A new stack with result will be pushed after arithmetic operator "+,-,*,/,sqrt" applies.
	 * "undo" will either take a number off the stack or pop the stack off the calculator
	 * "clear" will push an empty stack onto calculator, so that itself can also be undo-ed 
	 * 
	 * @param input
	 * @return msgs that should be printed out
	 */
	public String calculate(String[] input) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < input.length; i++) {

			String symbol = input[i];
			Operator op = Operator.ofSymbol(symbol);
			boolean isNum = NumberUtils.isParsable(symbol);

			//unknown symbol
			if (!isNum && op == null) {
				String msg = String.format("symbol %s (position %s): unknown symbol", symbol,
						i * 2 + 1);
				log.error(msg);
				sb.append(msg + "\n");
				break;
			}

			//over undo or calculator not yet have numbers
			if (!isNum && calculator.isEmpty()) {
				String msg = String.format("operator %s (position %s): nothing on calculator yet",
						symbol, i * 2 + 1);
				log.error(msg);
				sb.append(msg + "\n");
				break;
			}

			if (CLEAR == op) {
				//push an empty stack onto calculator, so that can undo clear if wanted
				stack = new Stack<RPNDecimal>();
				calculator.push(stack);

			} else if (UNDO == op) {
				if (stack.isEmpty()) {
					//undo clear
					calculator.pop();
					stack = calculator.isEmpty() ? null : calculator.peek();
				} else {
					//undo number
					RPNDecimal d = stack.pop();
					if (d.isResult()) {
						//undo operator that produce this result
						calculator.pop();
						stack = calculator.peek();
					} else if (stack.isEmpty()) {
						//undo number leads to empty stack
						calculator.pop();
						stack = calculator.isEmpty() ? null : calculator.peek();
					}
				}
			} else if (ADD == op || SUBTRACT == op || MULTIPLY == op || DIVIDE == op || SQRT == op) {//arithmetic
				//check sufficient params, sqrt must have 1, arithmatic must have 2
				if ((SQRT == op && stack.size() < 1) || (SQRT != op && stack.size() < 2)) {
					String msg = String
							.format("operator %s (position %s): insufficient parameters", symbol,
									i * 2 + 1);
					log.error(msg);
					sb.append(msg + "\n");
					break;
				}

				//clone to a new stack whenever need to perform an arithmetic operator
				stack = (Stack<RPNDecimal>) stack.clone();
				calculator.push(stack);
				if (SQRT == op) {
					stack.push(stack.pop().operator(null, op)); //push result back to stack
				} else {
					RPNDecimal d2 = stack.pop();
					RPNDecimal d1 = stack.pop();
					stack.push(d1.operator(d2, op));
				}

			} else if (isNum) {//number				
				//first number
				if (stack == null) {
					stack = new Stack<RPNDecimal>();
					calculator.push(stack);
				}
				//push number onto stack
				RPNDecimal decimal = new RPNDecimal(new BigDecimal(symbol), false);
				stack.push(decimal);

			}

			log.info("symbol {} (position {}): performed", symbol, i * 2 + 1);
		}

		//print stack
		log.info("stack: {}", stack);
		sb.append("stack: " + stack + "\n");

		return sb.toString();

	}

	public void restart() {
		this.calculator = new Stack<Stack<RPNDecimal>>();
		this.stack = null;
	}

	public void start() throws IOException {

		log.info("Processing user input");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String l = sc.nextLine();
			if (l.equalsIgnoreCase("q")) {
				break;
			}
			try {
				System.out.println(calculate(l.split(" ")));
			} catch (Exception e) {
				// continue program if user input is not valid
				log.error("Error reading user input " + l, e);
				throw new RuntimeException(e);
			}
		}

		log.info("Calculator shutdown");
		SpringApplication.exit(context, () -> 0);

	}

}
