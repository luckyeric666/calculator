package com.airwallex.service;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airwallex.command.Operation;
import com.airwallex.model.Operator;

//Command invoker
@Service
public class LineParser {

	private static final Logger log = LoggerFactory.getLogger(LineParser.class);

	@Autowired
	private Calculator calculator;

	
	
	public String parse(String l) {

		String[] symbols = l.split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < symbols.length; i++) {
			String error = processSymbol(symbols[i], i);
			if (error != null) {
				sb.append(error);
				break;
			}
			
		}

		//print stack
		log.info(calculator.toString());
		sb.append(calculator);
		return sb.toString();
	}

	public String processSymbol(String symbol, int i) {
		
		log.info("symbol {} (position {}): processing", symbol, i * 2 + 1);

		Operator op = Operator.ofSymbol(symbol);
		boolean isNum = NumberUtils.isParsable(symbol);

		//unknown symbol
		if (!isNum && op == null) {
			String msg = String.format("symbol %s (position %s): unknown symbol", symbol, i * 2 + 1);
			log.error(msg);
			return msg + "\n";
		}

		//calculator not yet have numbers or over undo
		if (!isNum && calculator.isEmpty()) {
			String msg = String.format("operator %s (position %s): nothing on calculator yet", symbol, i * 2 + 1);
			log.error(msg);
			return msg + "\n";
		}
		
		return getOperation(symbol).apply(symbol, i);

	}
	
	//Command factory
	private Operation getOperation(String symbol) {
		Operator op = Operator.ofSymbol(symbol);
		if(op == null){ //number
			return (s,p)->{calculator.number(s); return null;};			
		}else{
			switch (op) { //operator
				case CLEAR: return (s,p)->{calculator.clear(); return null;};
				case UNDO: return (s,p)->{calculator.undo(); return null;};
				case ADD: return (s,p)->calculator.add(p);
				case SUBTRACT: return (s,p)->calculator.subtract(p);
				case MULTIPLY: return (s,p)->calculator.multiply(p);
				case DIVIDE: return (s,p)->calculator.divide(p);
				case SQRT: return (s,p)->calculator.sqrt(p);		
				default: return null;
		    }	
		}
	}

}
