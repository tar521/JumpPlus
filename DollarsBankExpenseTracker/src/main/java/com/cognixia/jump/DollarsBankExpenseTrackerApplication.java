package com.cognixia.jump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.cognixia.jump.controller.ExpenseTrackerApp;
import com.cognixia.jump.util.CsvUtil;
import com.cognixia.jump.util.MenuUtil;

import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@ComponentScan(basePackages = "com.cognixia.jump")
@EnableAutoConfiguration
public class DollarsBankExpenseTrackerApplication implements CommandLineRunner {
	
	private static Logger log = LoggerFactory.getLogger(DollarsBankExpenseTrackerApplication.class);
	
	public static void main(String[] args) {
		log.info("STARTING THE APPLICATION");
		ConfigurableApplicationContext context = SpringApplication.run(DollarsBankExpenseTrackerApplication.class, args);
		log.info("DollarsBank Expense Tracker Started!");
		ExpenseTrackerApp app = context.getBean(ExpenseTrackerApp.class);
		app.entryPoint();
		MenuUtil.exitMessage();
		log.info("DollarsBank Expense Tracker Ended");
		log.info("APPLICATION FINISHED");
	}
	
	@Override
	public void run(String... args) throws Exception {
		// DO NOTHING
		// CONFIG ALREADY SET UP
	}
	

}
