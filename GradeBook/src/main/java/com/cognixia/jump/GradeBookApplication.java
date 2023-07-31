package com.cognixia.jump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.cognixia.jump.controller.Gradebook;
import com.cognixia.jump.util.ColorUtility;

@SpringBootApplication
@ComponentScan(basePackages = "com.cognixia.jump")
@EnableAutoConfiguration
public class GradeBookApplication implements CommandLineRunner {

private static Logger log = LoggerFactory.getLogger(GradeBookApplication.class);
	
	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		ConfigurableApplicationContext context = SpringApplication.run(GradeBookApplication.class, args);
		log.info("Gradebook Started!");
		Gradebook app = context.getBean(Gradebook.class);
		app.entryPoint();
		System.out.println(ColorUtility.PURPLE_TEXT + "\n~Goodbye!~\n");
		log.info("Gradebook Ended");
		log.info("APPLICATION FINISHED");
	}
	
	@Override
	public void run(String... args) throws Exception {
		// DO NOTHING
		// CONFIG ALREADY SET UP
	}

}
