package com.airwallex.command;

//Command
public interface Operation {
	
	/**
	 * @param symbol 
	 * @param i
	 * @return error msg if any
	 */
	String apply (String symbol, int i);
	
}
