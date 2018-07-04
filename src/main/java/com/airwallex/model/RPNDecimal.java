package com.airwallex.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class RPNDecimal {

	public static final int SCALE = 15; //arithmetic to 15 decimal

	private BigDecimal decimal;

	private boolean isResult; //is this decimal a result of arithmetic operator  or from input

	public RPNDecimal(BigDecimal decimal, boolean isResult) {
		this.decimal = decimal;
		this.isResult = isResult;
	}

	/**
	 * Apply arithmetic operator with given decimal
	 * @param rd
	 * @param op
	 * @return
	 */
	public RPNDecimal arithmetic(RPNDecimal rd, Operator op) {
		switch (op) {
		case ADD:
			return add(rd);
		case SUBTRACT:
			return subtract(rd);
		case MULTIPLY:
			return multiply(rd);
		case DIVIDE:
			return divide(rd);
		case SQRT:
			return sqrt();
		default:
			return null;
		}
	}

	private RPNDecimal divide(RPNDecimal rd) {
		return new RPNDecimal(decimal.divide(rd.decimal, SCALE, RoundingMode.FLOOR), true);
	}

	private RPNDecimal multiply(RPNDecimal rd) {
		return new RPNDecimal(decimal.multiply(rd.decimal), true);
	}

	private RPNDecimal subtract(RPNDecimal rd) {
		return new RPNDecimal(decimal.subtract(rd.decimal), true);
	}

	private RPNDecimal add(RPNDecimal rd) {
		return new RPNDecimal(decimal.add(rd.decimal), true);
	}

	private RPNDecimal sqrt() {
		return new RPNDecimal(sqrt(decimal, SCALE), true);
	}

	//babylonian method for high scale sqrt
	private static BigDecimal sqrt(BigDecimal in, int scale) {
		BigDecimal sqrt = new BigDecimal(1);
		sqrt.setScale(scale + 3, RoundingMode.FLOOR);
		BigDecimal store = new BigDecimal(in.toString());
		boolean first = true;
		do {
			if (!first) {
				store = new BigDecimal(sqrt.toString());
			} else
				first = false;
			store.setScale(scale + 3, RoundingMode.FLOOR);
			sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store)
					.divide(BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
		} while (!store.equals(sqrt));
		return sqrt.setScale(scale, RoundingMode.FLOOR);
	}

	public boolean isResult() {
		return isResult;
	}

	@Override
	public String toString() {
		//display to 10 decimal
		DecimalFormat df = new DecimalFormat("#.##########");
		return df.format(this.decimal);
	}

}
