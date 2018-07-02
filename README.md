#Calculator Coding Task
 
A RPN Calculator 
Will only know operators of +,-,*,/,sqrt,undo,clear
Will arithmetic to scale of 15
Will display numbers to scale of 10
Will stop if meet unknown, insufficient params, or empty calculator


#Running the code

##### Running from maven
cd \{project_root\}  
mvn spring-boot:run

##### Running in a microservice
cd \{project_root\}  
mvn clean install  
cd \{project_root\}/target  
java -jar calculator-0.0.1.jar 

##### Running in eclipse
Right click com.airwallex.Application
Run As / Java Application  


#Running the tests

#####Integration Test
Right click com.airwallex.cucumber.CalculatorIntegrationTest.java 
Run As / Junit Test
Feature is defined at calculator.feature

#####Mock Test
Omitted here. It will be almost same as intergation test, as calculator dont have dependency.


![alt text](file:test_result.jpg "Test Result")


