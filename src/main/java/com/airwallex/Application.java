package com.airwallex;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) throws BeansException, IOException {

		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		context.getBean(Calculator.class).start();

	}

}
