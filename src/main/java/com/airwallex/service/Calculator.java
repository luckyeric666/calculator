package com.airwallex.service;

import static com.airwallex.model.Operator.*;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.airwallex.model.Operator;
import com.airwallex.model.RPNDecimal;

//Command receiver
@Service
public class Calculator {

	private static final Logger log = LoggerFactory.getLogger(Calculator.class);

	/**
	 * Stack of operation performed so far
	 * Each operation is itself a stack recording numbers just before operation applies
	 * A new stack with result will be pushed onto calculator after operation applies
	 * The top most stack gives the latest result of this calculator
	 */
	protected Stack<Stack<RPNDecimal>> calculator = new Stack<Stack<RPNDecimal>>();

	protected Stack<RPNDecimal> stack; //top most stack

	public void clear() {
		//push an empty stack onto calculator, so that can undo clear if wanted
		stack = new Stack<RPNDecimal>();
		calculator.push(stack);
	}

	public void undo() {
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
				//if undo number leads to an empty stack
				calculator.pop();
				stack = calculator.isEmpty() ? null : calculator.peek();
			}
		}
	}

	public String add(int i) {
		return arithmetic(ADD, i);
	}

	public String subtract(int i) {
		return arithmetic(SUBTRACT, i);
	}

	public String multiply(int i) {
		return arithmetic(MULTIPLY, i);
	}

	public String divide(int i) {
		return arithmetic(DIVIDE, i);
	}

	public String sqrt(int i) {
		return arithmetic(SQRT, i);
	}

	/**
	 * @param op
	 * @param i
	 * @return error msg if any
	 */
	private String arithmetic(Operator op, int i) {

		//check sufficient params
		//sqrt must have 1, arithmatic must have 2
		if ((SQRT == op && stack.size() < 1) || (SQRT != op && stack.size() < 2)) {
			String msg = String.format("operator %s (position %s): insufficient parameters", op.symbol, i * 2 + 1);
			log.error(msg);
			return msg + "\n";
		}

		//clone to a new stack whenever arithmetic operator
		stack = (Stack<RPNDecimal>) stack.clone();
		calculator.push(stack);
		if (SQRT == op) {
			stack.push(stack.pop().arithmetic(null, op)); //push result back to stack
		} else {
			RPNDecimal d2 = stack.pop();
			RPNDecimal d1 = stack.pop();
			stack.push(d1.arithmetic(d2, op));
		}

		return null;
	}

	public void number(String symbol) {
		//first number
		if (stack == null) {
			stack = new Stack<RPNDecimal>();
			calculator.push(stack);
		}
		//push number onto stack
		RPNDecimal decimal = new RPNDecimal(new BigDecimal(symbol), false);
		stack.push(decimal);
	}

	public void restart() {
		calculator = new Stack<Stack<RPNDecimal>>();
		stack = null;
	}

	public boolean isEmpty() {
		return calculator.isEmpty();
	}
	
	@Override
	public String toString() {
		return "stack: "+ (stack == null ? null : stack.stream().map(d->d.toString()).collect(Collectors.joining(" ")));
	}

}
