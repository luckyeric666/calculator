package com.airwallex;

public enum Operator {

	ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"), SQRT("sqrt"), UNDO("undo"), CLEAR("clear");

	protected String symbol;

	private Operator(String symbol) {
		this.symbol = symbol;
	}

	public static Operator ofSymbol(String symbol) {
		for (Operator o : values()) {
			if (o.symbol.equals(symbol)) {
				return o;
			}
		}
		return null;
	}

}