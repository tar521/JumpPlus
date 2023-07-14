package com.cognixia.jump.controller;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.exceptions.UserNotFoundException;
import com.cognixia.jump.model.Credentials;
import com.cognixia.jump.model.User;
import com.cognixia.jump.service.AccountService;
import com.cognixia.jump.service.ExpenseService;
import com.cognixia.jump.service.MonthlyBudgetService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.ColorUtility;
import com.cognixia.jump.util.MenuUtil;

public class ExpenseTrackerApp {
	
	public static Scanner sc = new Scanner(System.in);
	
	private User user = null;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ExpenseService expenseService;
	
	@Autowired
	private MonthlyBudgetService budgetService;
	
	@Autowired
	private UserService userService;
	
	public void entryPoint() {
		
		do {
			MenuUtil.greeting();
			try {
				System.out.print(ColorUtility.CYAN_TEXT);
				int option = sc.nextInt();
				sc.nextLine();
				System.out.print(ColorUtility.TEXT_RESET);
				
				switch (option) {
				case 1: // create user and account
					newUser();
					break;
				case 2: // login
					int login = existingUser();
					if (login == 0) {
						throw new SQLException();
					}
					if (user != null) {
						// proceed to app
					} else {
						throw new UserNotFoundException();
					}
					break;
				case 3: // exit
					return;
				default:
					throw new IllegalOptionException();
				}
				
				
				
			}
			catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println(ColorUtility.RED_TEXT + "Not a listed option. Please input a valid option.\n" + ColorUtility.TEXT_RESET);
			}
			catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "Not a listed option.\n");
				System.out.println(e.getMessage() + ColorUtility.TEXT_RESET);
			}
			catch (UserNotFoundException e) {
				System.out.println(ColorUtility.RED_TEXT + "Username or password incorrect. Please try again.\n");
				System.out.println(e.getMessage() + ColorUtility.TEXT_RESET);
			}
			catch (SQLException e) {
				System.out.println(ColorUtility.RED_TEXT + "Error referencing database. Please email info@dollarsbank.com with this issue.\n");
				System.out.println(ColorUtility.RED_TEXT + "Now exiting the program...\n" + ColorUtility.TEXT_RESET);
				return;
			}
		} while(true);
	}

	private int existingUser() {
		do {
			MenuUtil.loginMenu();
			Credentials cred = new Credentials();
			System.out.println(ColorUtility.PURPLE_TEXT + "[Input 'exit' to return to previous menu]");
			System.out.println(ColorUtility.TEXT_RESET + "Username:" + ColorUtility.CYAN_TEXT);
			cred.setUsername(sc.nextLine());
			if (cred.getUsername().equalsIgnoreCase("exit")) {
				return -1;
			}
			
			System.out.println(ColorUtility.TEXT_RESET + "Password:" + ColorUtility.CYAN_TEXT);
			cred.setPassword(sc.nextLine());
			if (cred.getPassword().equalsIgnoreCase("exit")) {
				return -1;
			}
			
			try {
				user = userService.authenticate(cred);
				if (user == null) {
					throw new UserNotFoundException();
				}
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println(ColorUtility.TEXT_RESET + ColorUtility.RED_TEXT + "Connection Error Occurred: Please try again later.\n" + ColorUtility.TEXT_RESET);
				return 0;
			} catch(UserNotFoundException e) {
				System.out.println(ColorUtility.TEXT_RESET + ColorUtility.RED_TEXT + "Invalid Credentials. Try Again!" + ColorUtility.TEXT_RESET);
				continue;
			} catch (Exception e) {
				
			}
		} while(true);
	}

	private void newUser() {
		// TODO Auto-generated method stub
		
	}

}
