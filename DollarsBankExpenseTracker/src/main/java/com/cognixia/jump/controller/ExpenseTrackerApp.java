package com.cognixia.jump.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.exceptions.UserNotFoundException;
import com.cognixia.jump.model.Credentials;
import com.cognixia.jump.model.Expense;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.Expense.Category;
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
	
	private void session() {
		do {
			System.out.println();
			MenuUtil.mainMenu(user.getName());
			try {
				System.out.print(ColorUtility.CYAN_TEXT);
				int option = sc.nextInt();
				sc.nextLine();
				System.out.print(ColorUtility.TEXT_RESET);
				
				switch (option) {
				case 1: // New Expense
					//accountAction("Deposit");
					expenseAction("NewExpense");
					break;
				case 2: // Remove Expense
					//accountAction("Withdrawal");
					break;
				case 3: // Set budget
//					if (accDAO.getAccounts().size() < 2) {
//						System.out.println(ColorUtility.RED_TEXT + "\nCannot perform transfer: You have only one account\n" + ColorUtility.TEXT_RESET);
//					} else {
//						//accountAction("Transfer");
//						//accountAction("Transfer");
//					}
					break;
				case 4: // view 5 expenses
//					System.out.println(ColorUtility.BLUE_TEXT + "+-----------------------------+");
//					System.out.println("| 5 Most Recent Transactions: |");
//					System.out.println("+-----------------------------+\n" + ColorUtility.TEXT_RESET);
//					System.out.println(transDAO);
					break;
				case 5: // show months budget
//					System.out.print(ColorUtility.TEXT_RESET);
//					System.out.println(custDAO.getUser());
//					System.out.println(ColorUtility.GREEN_TEXT);
//					System.out.println(accDAO);
//					System.out.println(ColorUtility.TEXT_RESET);
					break;
				case 6: 
					// show years budget
					//openNewAccount();
					break;
				case 7:
					// cust info
//					custDAO.setUser(null);
//					accDAO.signOut();
//					transDAO.signOut();
//					System.out.print(ColorUtility.TEXT_RESET);
//					System.out.println("\nSigning Out...\n");
//					System.out.print(ColorUtility.TEXT_RESET);
					return;
				case 8:
					// sign out
					break;
				default:
					throw new IllegalOptionException();
				}
			}catch(InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nInvalid input - Please input a listed option\n" + ColorUtility.TEXT_RESET);
				sc.nextLine();
			}catch(IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a listed option - Please input a listed option\n" + ColorUtility.TEXT_RESET);
			}
		
		
		}while(true);
		
		
		
	}
	
	private void expenseAction(String action) {
		Map<Integer, Integer> years = expenseService.expenseYears(user);
		if (years.isEmpty() && action.equals("RemoveExpense")) {
			System.out.println(ColorUtility.RED_TEXT + "Cannot Remove Expense - No Existing Expenses.");
			System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
			return;
		}
		String printYears = (years.isEmpty()) ? "No existing years" : ""; 
		if (printYears.isEmpty()) {
			for (Map.Entry<Integer, Integer> i : years.entrySet()) {
				printYears = printYears + "\n"+ i.getKey().toString() + " ";
			}
		}
		System.out.println();
		System.out.println(ColorUtility.BLUE_TEXT + "+----------------+");
		System.out.println("| Expense Wizard |");
		System.out.println("+----------------+" + ColorUtility.TEXT_RESET);
		
		System.out.println(ColorUtility.TEXT_RESET + ColorUtility.PURPLE_TEXT + "[To cancel action, input exit]" + ColorUtility.TEXT_RESET);
		System.out.println(ColorUtility.BLUE_TEXT + "Input year to operate on:" + ColorUtility.CYAN_TEXT);
		System.out.println("Existing years: " + printYears);
		System.out.println(ColorUtility.TEXT_RESET + "Year: " + ColorUtility.CYAN_TEXT);
		
		String option = sc.nextLine();
		
		
		System.out.print(ColorUtility.TEXT_RESET);
		
		if (option.equalsIgnoreCase("exit")) {
			return;
		}
		
		String yearPattern = "^(19|2[0-9])\\d{2}$";
		Pattern p = Pattern.compile(yearPattern);
		Matcher m = p.matcher(option);
		
		if (m.matches()) {
			
			if (action.equals("NewExpense")) {
				// add new expense
				createNewExpense(option);
			} else {
				if (years.isEmpty() || !years.containsKey(option)) {
					System.out.println(ColorUtility.RED_TEXT + "Cannot Remove Expense - No Existing Expenses.");
					System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
					return;
				} else {
					removeExpense(option);
				}
			}
			
		} else {
			System.out.println(ColorUtility.RED_TEXT + "Year entered does not fall in valid range. Please enter a year from 1900-2999");
			System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
		}
	}
	
	private void createNewExpense(String year) {
		do {
			try {
				printExpenses();
				Expense newExpense = new Expense();
				newExpense.setAccount(user.getAccount());
				newExpense.setUser(user);
				System.out.println(ColorUtility.BLUE_TEXT + "What is the category for this expense?");
				System.out.println(ColorUtility.PURPLE_TEXT + "["
						+ Category.CLOTHING + " "
						+ Category.FOOD + " "
						+ Category.HOUSING + " "
						+ Category.INSURANCE + " "
						+ Category.TRANSPORTATION + " "
						+ Category.UTILITIES + " "
						+ Category.DEFAULT + "]");
				System.out.println(ColorUtility.TEXT_RESET + "Category: " + ColorUtility.CYAN_TEXT);
				String category = sc.nextLine();
				boolean validOption = false;
				
				for (Category c : Category.values()) {
					if (category.equalsIgnoreCase(c.toString())) {
						validOption = true;
					}
				}
				
				if (!validOption) {
					throw new IllegalOptionException();
				}
				
				System.out.println(ColorUtility.BLUE_TEXT + "Input the dollar amount: " + ColorUtility.CYAN_TEXT + "\n$ ");
				double amount = sc.nextDouble();
				sc.nextLine();
				
				System.out.println(ColorUtility.BLUE_TEXT + "Input the date for this expense: ");
				System.out.println(ColorUtility.PURPLE_TEXT + "[MM/DD]" + ColorUtility.TEXT_RESET);
				String expenseDate = sc.nextLine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
				LocalDateTime dateOfExpense1 = LocalDateTime.parse(expenseDate, formatter);
				LocalDateTime dateOfExpense2 = LocalDateTime.of(Integer.parseInt(year), dateOfExpense1.getMonth(), dateOfExpense1.getDayOfMonth(), 0, 0, 0, 0);
				
				newExpense.setCategory(Category.valueOf(category.toUpperCase()));
				newExpense.setAmount(amount);
				newExpense.setDate(dateOfExpense2);
				
				Expense created = expenseService.createExpense(newExpense);
				user.getExpenses().add(created);
				user = userService.updateUser(user);
				
				System.out.println(ColorUtility.BLUE_TEXT + "\nWould you like to add more for this year? [Y/n]" + ColorUtility.TEXT_RESET);
				String answer = sc.nextLine();
				if (answer.equalsIgnoreCase("y")) {
					continue;
				} else {
					return;
				}
				
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + e.getMessage());
				continue;
			} catch (DateTimeParseException e) {
				System.out.println(ColorUtility.RED_TEXT + "Invalid date entered - please enter MM/DD format.");
				continue;
			}
		} while(true);
		
	}
	
	private void removeExpense(String year) {
		do {
			try {
				printExpenses();
				Expense newExpense = new Expense();
				newExpense.setAccount(user.getAccount());
				newExpense.setUser(user);
				System.out.println(ColorUtility.BLUE_TEXT + "What is the category for this expense?");
				System.out.println(ColorUtility.PURPLE_TEXT + "["
						+ Category.CLOTHING + " "
						+ Category.FOOD + " "
						+ Category.HOUSING + " "
						+ Category.INSURANCE + " "
						+ Category.TRANSPORTATION + " "
						+ Category.UTILITIES + " "
						+ Category.DEFAULT + "]");
				System.out.println(ColorUtility.TEXT_RESET + "Category: " + ColorUtility.CYAN_TEXT);
				String category = sc.nextLine();
				boolean validOption = false;
				
				for (Category c : Category.values()) {
					if (category.equalsIgnoreCase(c.toString())) {
						validOption = true;
					}
				}
				
				if (!validOption) {
					throw new IllegalOptionException();
				}
				
				System.out.println(ColorUtility.BLUE_TEXT + "Input the dollar amount: " + ColorUtility.CYAN_TEXT + "\n$ ");
				double amount = sc.nextDouble();
				sc.nextLine();
				
				System.out.println(ColorUtility.BLUE_TEXT + "Input the date for this expense: ");
				System.out.println(ColorUtility.PURPLE_TEXT + "[MM/DD]" + ColorUtility.TEXT_RESET);
				String expenseDate = sc.nextLine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
				LocalDateTime dateOfExpense1 = LocalDateTime.parse(expenseDate, formatter);
				LocalDateTime dateOfExpense2 = LocalDateTime.of(Integer.parseInt(year), dateOfExpense1.getMonth(), dateOfExpense1.getDayOfMonth(), 0, 0, 0, 0);
				
				newExpense.setCategory(Category.valueOf(category.toUpperCase()));
				newExpense.setAmount(amount);
				newExpense.setDate(dateOfExpense2);
				
				Expense created = expenseService.createExpense(newExpense);
				user.getExpenses().add(created);
				user = userService.updateUser(user);
				
				System.out.println(ColorUtility.BLUE_TEXT + "\nWould you like to add more for this year? [Y/n]" + ColorUtility.TEXT_RESET);
				String answer = sc.nextLine();
				if (answer.equalsIgnoreCase("y")) {
					continue;
				} else {
					return;
				}
				
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + e.getMessage());
				continue;
			} catch (DateTimeParseException e) {
				System.out.println(ColorUtility.RED_TEXT + "Invalid date entered - please enter MM/DD format.");
				continue;
			}
		} while(true);
	}

	private void printExpenses() {
		System.out.println(ColorUtility.CYAN_TEXT + "+----------+");
		System.out.println("| Expenses |");
		System.out.println("+----------+");
		for (Expense e : user.getExpenses()) {
			System.out.println(e);
		}
		System.out.println(ColorUtility.TEXT_RESET + "\n");
		
	}

}
