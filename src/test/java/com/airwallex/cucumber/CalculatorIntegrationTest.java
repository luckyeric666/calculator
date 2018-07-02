package com.airwallex.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;



@RunWith(Cucumber.class)
@CucumberOptions(glue = {"com.airwallex.cucumber"})
public class CalculatorIntegrationTest {
}
