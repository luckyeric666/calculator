#Calculator Coding Task
 
A RPN Calculator 
Will only know operations of +,-,*,/,sqrt,undo,clear,numbers
Will arithmetic to scale of 15
Will display numbers to scale of 10
Will stop if meet unknown, insufficient params, or empty calculator

Demoed bdd test with minimal codes and testing logics
Demoed sqrt to high scale
Demoed using Command pattern in Spring way



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

##### Integration Test
Right click com.airwallex.cucumber.CalculatorIntegrationTest.java 
Run As / Junit Test
Feature is defined at calculator.feature

##### Mock Test
Omitted here.


![alt text](file:test_result.jpg "Test Result")


