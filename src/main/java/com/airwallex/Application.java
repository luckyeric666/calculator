package com.airwallex;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.airwallex.service.LineParser;

@SpringBootApplication
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);


	public static void main(String[] args) throws BeansException, IOException {
		
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		LineParser lp = context.getBean(LineParser.class);
		
		log.info("Processing user input");
		Scanner sc = new Scanner(System.in);
		while (true) {
			String l = sc.nextLine();
			if (l.equalsIgnoreCase("q")) {
				break;
			}
			try {
				System.out.println(lp.parse(l));
			} catch (Exception e) {
				// continue program if user input is not valid
				log.error("Error reading user input " + l, e);
				throw new RuntimeException(e);
			}
		}

		log.info("Calculator shutdown");
		SpringApplication.exit(context, () -> 0);		

	}

}
