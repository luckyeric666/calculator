package com.airwallex.cucumber;

import static org.assertj.core.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;

import com.airwallex.Application;
import com.airwallex.service.Calculator;
import com.airwallex.service.LineParser;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * @author yuancongjia
 * This class represents integration test by specification/behavior
 * Senarios are written in Gherkin at "calculator.feature"
 * 
 */
@ContextConfiguration(classes = Application.class, loader = SpringBootContextLoader.class)
public class CalculatorStepDefs {

	@Autowired
	private LineParser lp;
	
	@Autowired
	private Calculator calculator;

	private String result;

	@Before
	public void setup() {
		calculator.restart();
	}

	

	@Given("^user type \"([^\"]*)\"$")
	public void user_type(String type) throws Throwable {
		result = lp.parse(type);
	}

	@Then("^we should see$")
	public void we_should_see(String see) throws Throwable {
		assertThat(result).isEqualToIgnoringWhitespace(see);
	}

}
