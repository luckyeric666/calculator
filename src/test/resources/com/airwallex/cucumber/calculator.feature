Feature: Calculator
  A RPN Calculator 
  Will only know operators of +,-,*,/,sqrt,undo,clear
  Will arithmetic to scale of 15
  Will display numbers to scale of 10
  Will stop if meet unknown, insufficient params, or empty calculator
  
	  
  Scenario: unknown symbol
    Given user type "1 2 sqrt + a 3 4 +"
	Then we should see
      """
	  symbol a (position 9): unknown symbol
	  stack: [2.4142135624]
      """

  Scenario: empty calculator
    Given user type "+ 1 2 3"
	Then we should see
      """
	  operator + (position 1): nothing on calculator yet
	  stack: null
      """	
  
  Scenario: undo 
    Given user type "1 2 + 3 * 4 undo undo undo undo undo"
	Then we should see
      """
	  stack: [1]
      """	
	  
  Scenario: undo clear
    Given user type "1 2 sqrt + clear undo undo clear undo undo"
	Then we should see
      """
	  stack: [1, 2]
      """	     
       
  Scenario: undo to a clean calculator and then put numbers
    Given user type "1 2 sqrt undo undo undo 3 4"
	Then we should see
      """
	  stack: [3, 4]
      """	   
	
  Scenario: over undo
    Given user type "1 2 sqrt undo undo undo undo 3 4"
	Then we should see
      """
	  operator undo (position 13): nothing on calculator yet
      stack: null
      """	

  Scenario: insufficient params
    Given user type "1 2 3 * 5 + * * 6 5"
	Then we should see
      """
      operator * (position 15): insufficient parameters
	  stack: [11]
      """	  
	  
  Scenario: clear and insufficient params
    Given user type "1 2 3 + 4 5 clear 6 +"
	Then we should see
      """
	  operator + (position 17): insufficient parameters
      stack: [6]
      """	 
	
	
  Scenario: 15 decimals for arithmetic and 10 decimals for display
    Given user type "1000000000000000000000000000 2 sqrt + 1 3 / + 10000000003333333333333333333333333333333333333333 sqrt 0.000000000000000002 sqrt 0.00000001 3 /"
	Then we should see
      """
	  stack: [1000000000000000000000000001.7475468957, 3162277660695425608649702.9100486267, 0.0000000014, 0.0000000033]
      """	 
  	
 
  
