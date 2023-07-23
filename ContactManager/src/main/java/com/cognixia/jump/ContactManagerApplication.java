package com.cognixia.jump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.cognixia.jump.controller.ContactManagerApp;
import com.cognixia.jump.util.MenuUtil;

@SpringBootApplication
@ComponentScan(basePackages = "com.cognixia.jump")
@EnableAutoConfiguration
public class ContactManagerApplication implements CommandLineRunner{

	private static Logger log = LoggerFactory.getLogger(ContactManagerApplication.class);
	
	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		ConfigurableApplicationContext context = SpringApplication.run(ContactManagerApplication.class, args);
		log.info("Contact Manager Started!");
		ContactManagerApp app = context.getBean(ContactManagerApp.class);
		app.entryPoint();
		MenuUtil.exitMessage();
		log.info("Contact Manager Ended");
		log.info("APPLICATION FINISHED");
	}
	
	@Override
	public void run(String... args) throws Exception {
		// DO NOTHING
		// CONFIG ALREADY SET UP
	}

}
