package com.cognixia.jump.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.exceptions.UserNotFoundException;
import com.cognixia.jump.model.Account;
import com.cognixia.jump.model.Credentials;
import com.cognixia.jump.model.Expense;
import com.cognixia.jump.model.User;
import com.cognixia.jump.model.Expense.Category;
import com.cognixia.jump.model.MonthlyBudget;
import com.cognixia.jump.service.AccountService;
import com.cognixia.jump.service.ExpenseService;
import com.cognixia.jump.service.MonthlyBudgetService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.ColorUtility;
import com.cognixia.jump.util.CsvUtil;
import com.cognixia.jump.util.MenuUtil;

@Component
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
						session();
					} else {
						throw new UserNotFoundException();
					}
					break;
				case 3: // exit
					return;
				default:
					throw new IllegalOptionException();
				}

			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println(ColorUtility.RED_TEXT + "Not a listed option. Please input a valid option.\n"
						+ ColorUtility.TEXT_RESET);
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "Not a listed option.\n");
				System.out.println(e.getMessage() + ColorUtility.TEXT_RESET);
			} catch (UserNotFoundException e) {
				System.out.println(ColorUtility.RED_TEXT + "Username or password incorrect. Please try again.\n");
				System.out.println(e.getMessage() + ColorUtility.TEXT_RESET);
			} catch (SQLException e) {
				System.out.println(ColorUtility.RED_TEXT
						+ "Error referencing database. Please email info@dollarsbank.com with this issue.\n");
				System.out.println(ColorUtility.RED_TEXT + "Now exiting the program...\n" + ColorUtility.TEXT_RESET);
				return;
			}
		} while (true);
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
				user.setBudget(budgetService.getBudgetByUser(user));
				user.setAccount(accountService.getAccount(user));
				user.setExpenses(expenseService.getExpensesByUser(user));
				if (user.getExpenses().isEmpty()) {
					user.setExpenses(new ArrayList<Expense>());
				} else {
					expenseService.sortExpenses(user);
				}

				return 1;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(ColorUtility.TEXT_RESET + ColorUtility.RED_TEXT
						+ "Connection Error Occurred: Please try again later.\n" + ColorUtility.TEXT_RESET);
				return 0;
			} catch (UserNotFoundException e) {
				System.out.println(ColorUtility.TEXT_RESET + ColorUtility.RED_TEXT + "Invalid Credentials. Try Again!"
						+ ColorUtility.TEXT_RESET);
				continue;
			} catch (Exception e) {

			}
		} while (true);
	}

	private void newUser() {
		user = new User();
		String phonePattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
		String passPattern = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%^&+=])" + "(?=\\S+$).{8,20}$";

		MenuUtil.menuMakeAccount();
		System.out.println("Customer Name:" + ColorUtility.CYAN_TEXT);
		user.setName(sc.nextLine());

		System.out.println(ColorUtility.TEXT_RESET + "Customer Address:" + ColorUtility.CYAN_TEXT);
		user.setAddress(sc.nextLine());

		// USERNAME
		do {
			System.out.println(ColorUtility.TEXT_RESET + "Customer Username:" + ColorUtility.CYAN_TEXT);
			String temp = sc.nextLine();

			if (userService.uniqueUsername(temp)) {
				user.setUsername(temp);
				break;
			} else {
				System.out.println(
						ColorUtility.RED_TEXT + "Username not available - Try Again!" + ColorUtility.TEXT_RESET);
			}
		} while (true);

		// PHONE
		do {
			System.out.println(ColorUtility.TEXT_RESET + "Customer Contact Number:" + ColorUtility.CYAN_TEXT);
			String temp = sc.nextLine();

			if (temp.matches(phonePattern)) {
				user.setPhone(temp);
				break;
			} else {
				System.out.println(ColorUtility.TEXT_RESET + ColorUtility.RED_TEXT
						+ "Not valid phone number - Try Again!" + ColorUtility.TEXT_RESET);
			}

		} while (true);

		// PASSWORD
		Pattern p = Pattern.compile(passPattern);
		do {
			System.out.println(ColorUtility.TEXT_RESET + "Password: " + ColorUtility.PURPLE_TEXT
					+ "8 Characters Min With Lower, Upper, Number, and Special" + ColorUtility.CYAN_TEXT);
			String temp = sc.nextLine();
			temp = temp.replaceAll("\n", "");
			temp = temp.replaceAll("\\s", "");
			Matcher m = p.matcher(temp);

			if (m.matches()) {
				user.setPassword(Base64.getEncoder().encodeToString(temp.getBytes()));
				break;
			} else {
				System.out.println(ColorUtility.TEXT_RESET + ColorUtility.RED_TEXT + "Invalid Password - Try Again!"
						+ ColorUtility.TEXT_RESET);
			}

		} while (true);

		// DEFAULT ACCOUNT AND BUDGET
		user.setExpenses(new ArrayList<Expense>());
		user = userService.createUser(user);

		Account account = new Account(null, null, user);
		user.setAccount(accountService.createAccount(account));

		MonthlyBudget budget = new MonthlyBudget(null, Integer.toString(LocalDateTime.now().getYear()),
				Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0),
				Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0),
				Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), user);
		user.setBudget(new ArrayList<MonthlyBudget>());
		user.getBudget().add(budgetService.createExpense(budget));
		userService.updateUser(user);
		user = null;

		System.out.println(ColorUtility.GREEN_TEXT + "\nAccount Created!\n");
		System.out.println("Default budget has been created for year " + LocalDateTime.now().getYear());
		System.out.println("Values set to zero.\n" + ColorUtility.TEXT_RESET);

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
					expenseAction("NewExpense");
					break;
				case 2: // Remove Expense
					expenseAction("RemoveExpense");
					break;
				case 3: // Set budget
					String budgetYear = validateYear();
					setMonthlyYearlyBudget(budgetYear);
					break;
				case 4: // view 5 upcoming expenses
					if (user.getExpenses().size() == 0) {
						System.out.println(ColorUtility.RED_TEXT + "\nThere are no expenses to display.");
						System.out.println("Please add expenses before displaying.\n" + ColorUtility.TEXT_RESET);
					} else {
						upcomingExpenseDisplay();
					}
					break;
				case 5: // show months budget
					String year = validateYear();
					String month = validateMonth();
					displayBudget(year, month);
					break;
				case 6:
					// show years budget
					String yearBudget = validateYear();
					displayBudget(yearBudget);
					break;
				case 7:
					// cust info
					System.out.print(ColorUtility.TEXT_RESET);
					System.out.println(user.toString());
					System.out.println(ColorUtility.GREEN_TEXT);
					System.out.println(user.getAccount().toString());
					System.out.println(ColorUtility.TEXT_RESET);
					break;
				case 8:
					try {
						CsvUtil.writeExpensesToCsv(user);
						CsvUtil.writeBudgetsToCsv(user);
						System.out.println(
								ColorUtility.GREEN_TEXT + "\nSuccessfully wrote expenses and budgets to csv files!");
						System.out.println("They can be found in the base directory.\n" + ColorUtility.TEXT_RESET);
					} catch (IOException e) {
						System.out.println(ColorUtility.RED_TEXT + "Error writing to file\n" + ColorUtility.TEXT_RESET);
					}
					break;
				case 9:
					// sign out
					userService.updateUser(user);
					user = null;
					return;
				default:
					throw new IllegalOptionException();
				}
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nInvalid input - Please input a listed option\n"
						+ ColorUtility.TEXT_RESET);
				sc.nextLine();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a listed option - Please input a listed option\n"
						+ ColorUtility.TEXT_RESET);
			}

		} while (true);

	}

	private void setMonthlyYearlyBudget(String year) {
		boolean yearlyBudgetExists = budgetService.yearExists(year, user);
		if (!yearlyBudgetExists) {
			// NEW BUDGET
			do {
				try {
					MonthlyBudget newBudget = new MonthlyBudget(null, year, Double.valueOf(0.0), Double.valueOf(0.0),
							Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0),
							Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0),
							Double.valueOf(0.0), Double.valueOf(0.0), Double.valueOf(0.0), user);

					System.out.println(ColorUtility.GREEN_TEXT + "\nDEFAULT BUDGET:");
					System.out.println(newBudget);
					System.out.print(ColorUtility.TEXT_RESET);
					double yearlyBudget = 0;
					// JAN
					do {
						System.out.print("\nJanuary Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setJanuary(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// FEB
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nFebruary Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setFebruary(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// MAR
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nMarch Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setMarch(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// APR
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nApril Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setApril(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// MAY
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nMay Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setMay(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// JUN
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nJune Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setJune(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// JUL
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nJuly Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setJuly(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// AUG
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nAugust Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setAugust(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// SEP
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nSeptember Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setSeptember(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// OCT
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nOctober Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setOctober(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// NOV
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nNovember Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setNovember(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// DEC
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nDecember Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							newBudget.setDecember(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					newBudget.setId(null);
					newBudget.setUser(user);
					newBudget.setYearlyBudget(yearlyBudget);
					newBudget = budgetService.createExpense(newBudget);
					user.getBudget().add(newBudget);
					break;
				} catch (InputMismatchException e) {
					System.out.println(ColorUtility.RED_TEXT + "Invalid input - Please input a number\n"
							+ ColorUtility.TEXT_RESET);
					sc.nextLine();
					continue;
				} catch (IllegalOptionException e) {
					System.out.println(ColorUtility.RED_TEXT + "Invalid input - Please input positive numbers\n"
							+ ColorUtility.TEXT_RESET);
					continue;
				}
			} while (true);

		} else {
			// EXISTING BUDGET
			do {
				try {
					MonthlyBudget existingBudget = null;
					for (MonthlyBudget b : user.getBudget()) {
						if (b.getYear().equals(year)) {
							existingBudget = b;
						}
					}

					if (existingBudget == null) {
						System.out.println(ColorUtility.RED_TEXT + "\nBudget not found for year: " + year);
						System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
						return;
					}

					System.out.println(ColorUtility.GREEN_TEXT + "\n" + year + " BUDGET:");
					System.out.println(existingBudget);
					System.out.print(ColorUtility.TEXT_RESET);
					double yearlyBudget = 0;
					// JAN
					do {
						System.out.print("\nJanuary Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setJanuary(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// FEB
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nFebruary Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setFebruary(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// MAR
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nMarch Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setMarch(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// APR
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nApril Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setApril(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// MAY
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nMay Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setMay(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// JUN
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nJune Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setJune(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// JUL
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nJuly Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setJuly(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// AUG
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nAugust Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setAugust(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// SEP
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nSeptember Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setSeptember(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// OCT
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nOctober Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setOctober(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// NOV
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nNovember Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setNovember(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					// DEC
					do {
						System.out.print(ColorUtility.TEXT_RESET + "\nDecember Budget: $" + ColorUtility.CYAN_TEXT);
						double amount = sc.nextDouble();
						sc.nextLine();

						if (amount >= 0) {
							existingBudget.setDecember(amount);
							yearlyBudget = yearlyBudget + amount;
						} else {
							throw new IllegalOptionException();
						}
						break;
					} while (true);

					existingBudget.setUser(user);
					existingBudget.setYearlyBudget(yearlyBudget);
					existingBudget = budgetService.updateExpense(existingBudget);

					break;
				} catch (InputMismatchException e) {
					System.out.println(ColorUtility.RED_TEXT + "Invalid input - Please input a number\n"
							+ ColorUtility.TEXT_RESET);
					sc.nextLine();
					continue;
				} catch (IllegalOptionException e) {
					System.out.println(ColorUtility.RED_TEXT + "Invalid input - Please input positive numbers\n"
							+ ColorUtility.TEXT_RESET);
					continue;
				}
			} while (true);
		}

	}

	private void upcomingExpenseDisplay() {
		System.out.println(ColorUtility.BLUE_TEXT + "+-------------------+");
		System.out.println("| Upcoming Expenses |");
		System.out.println("+-------------------+\n" + ColorUtility.GREEN_TEXT);
		int count = 5;
		for (Expense e : user.getExpenses()) {
			int diff = e.getDate().compareTo(LocalDateTime.now());
			if (diff > 0) {
				System.out.println(e);
				count--;
				if (count <= 0) {
					System.out.println(ColorUtility.TEXT_RESET);
					return;
				}
			}
		}

	}

	private void displayBudget(String year) {
		MonthlyBudget budget = null;
		for (MonthlyBudget b : user.getBudget()) {
			if (b.getYear().equals(year)) {
				budget = b;
				break;
			}
		}
		if (budget == null) {
			System.out.println(ColorUtility.RED_TEXT + "Year entered does not have a budget.");
			System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
			return;
		}

		double totalExpenses = 0;
		for (Expense e : user.getExpenses()) {
			if (year.equals(Integer.toString(e.getDate().getYear()))) {
				totalExpenses = totalExpenses + e.getAmount();
				System.out.println(e);
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");

		System.out.println(ColorUtility.BLUE_TEXT + "\n+------------+");
		System.out.println("| Budget " + year + " |");
		System.out.println("+------------+" + ColorUtility.TEXT_RESET);
		System.out.println(ColorUtility.GREEN_TEXT + "\n" + budget);
		System.out.println("\nTotal Expenses: \t$" + df.format(totalExpenses));
		System.out.println("Total Year Budget: \t$" + df.format(budget.getYearlyBudget().doubleValue()));

		if (totalExpenses - budget.getYearlyBudget() > 0.00) {
			System.out.println(ColorUtility.RED_TEXT + "\nYou are over budget!");
			System.out.println("Amount Over Budget: $" + df.format(totalExpenses - budget.getYearlyBudget()));
			System.out.println("Please consider minimizing expenses\n" + ColorUtility.TEXT_RESET);
		} else {
			System.out.println(ColorUtility.GREEN_TEXT + "\nYou are on track for your budget!");
			System.out.println("Amount In Excess: $" + df.format(-1.0 * (totalExpenses - budget.getYearlyBudget())));
			System.out.println("Consider saving the money left in excess of your budget!\n" + ColorUtility.TEXT_RESET);
		}

	}

	private void displayBudget(String year, String month) {
		MonthlyBudget budget = null;
		for (MonthlyBudget b : user.getBudget()) {
			if (b.getYear().equals(year)) {
				budget = b;
				break;
			}
		}
		if (budget == null) {
			System.out.println(ColorUtility.RED_TEXT + "Year entered does not have a budget.");
			System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
			return;
		}

		double totalExpenses = 0;
		Month budgetMonth = Month.of(Integer.parseInt(month));
		for (Expense e : user.getExpenses()) {
			if (year.equals(Integer.toString(e.getDate().getYear()))
					&& month.equals(Integer.toString(e.getDate().getMonth().getValue()))) {
				totalExpenses = totalExpenses + e.getAmount();
				System.out.println(e);
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		String monthFormat = budgetMonth.toString();
		String printHelper = "";
		for (int i = 0; i < monthFormat.length(); i++) {
			printHelper = printHelper + "-";
		}

		System.out.println(ColorUtility.BLUE_TEXT + "\n+-" + printHelper + "--------+");
		System.out.println("| " + monthFormat + " Budget |");
		System.out.println("+-" + printHelper + "--------+" + ColorUtility.TEXT_RESET);
		System.out.println("\nTotal Expenses: \t$" + df.format(totalExpenses));
		double monthlyBudget = MenuUtil.printMonthlyBudget(monthFormat, df, budget);

		if (totalExpenses - monthlyBudget > 0.00) {
			System.out.println(ColorUtility.RED_TEXT + "\nYou are over budget!");
			System.out.println("Amount Over Budget: $" + df.format(totalExpenses - monthlyBudget));
			System.out.println("Please consider minimizing expenses\n" + ColorUtility.TEXT_RESET);
		} else {
			System.out.println(ColorUtility.GREEN_TEXT + "\nYou are on track for your budget!");
			System.out.println("Amount In Excess: $" + df.format(-1.0 * (totalExpenses - monthlyBudget)));
			System.out.println("Consider saving the money left in excess of your budget!\n" + ColorUtility.TEXT_RESET);
		}

	}

	private void expenseAction(String action) {
		Map<Integer, Integer> years = expenseService.expenseYears(user);
		if (years == null) {
			years = new HashMap<Integer, Integer>();
		}
		if (years.isEmpty() && action.equals("RemoveExpense")) {
			System.out.println(ColorUtility.RED_TEXT + "Cannot Remove Expense - No Existing Expenses.");
			System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
			return;
		}
		String printYears = (years.isEmpty()) ? "No existing years" : "";
		if (printYears.isEmpty()) {
			for (Map.Entry<Integer, Integer> i : years.entrySet()) {
				printYears = printYears + "\n" + i.getKey().toString() + " ";
			}
		}
		System.out.println();
		System.out.println(ColorUtility.BLUE_TEXT + "+----------------+");
		System.out.println("| Expense Wizard |");
		System.out.println("+----------------+" + ColorUtility.TEXT_RESET);

		System.out.println(ColorUtility.TEXT_RESET + ColorUtility.PURPLE_TEXT + "[To cancel action, input exit]"
				+ ColorUtility.TEXT_RESET);
		System.out.println(ColorUtility.BLUE_TEXT + "Input year to operate on:" + ColorUtility.CYAN_TEXT);
		System.out.println("Existing years: " + printYears);
		System.out.print(ColorUtility.TEXT_RESET + "Year: " + ColorUtility.CYAN_TEXT);

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
				if (years.isEmpty() || !years.containsKey(Integer.valueOf(option))) {
					System.out.println(ColorUtility.RED_TEXT + "Cannot Remove Expense - No Existing Expenses.");
					System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
					return;
				} else {
					removeExpense(option);
				}
			}

		} else {
			System.out.println(ColorUtility.RED_TEXT
					+ "Year entered does not fall in valid range. Please enter a year from 1900-2999");
			System.out.println("Returning to menu...\n" + ColorUtility.TEXT_RESET);
		}
	}

	private void createNewExpense(String year) {
		do {
			try {
				printExpenses();
				Expense newExpense = new Expense();
				newExpense.setUser(user);
				System.out.println(ColorUtility.BLUE_TEXT + "What is the category for this expense?");
				System.out.println(ColorUtility.PURPLE_TEXT + "[" + Category.CLOTHING + " " + Category.FOOD + " "
						+ Category.HOUSING + " " + Category.INSURANCE + " " + Category.TRANSPORTATION + " "
						+ Category.UTILITIES + " " + Category.DEFAULT + "]");
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

				System.out
						.print(ColorUtility.BLUE_TEXT + "Input the dollar amount: " + ColorUtility.CYAN_TEXT + "\n$ ");
				double amount = sc.nextDouble();
				sc.nextLine();

				System.out.println(ColorUtility.BLUE_TEXT + "Input the date for this expense: ");
				System.out.println(ColorUtility.PURPLE_TEXT + "[MM-DD]" + ColorUtility.TEXT_RESET);
				String expenseDate = sc.nextLine();
				expenseDate = expenseDate.trim().replace(" ", "").replace("\n", "");
				expenseDate = year + "-" + expenseDate + " 00:00";

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				LocalDateTime dateOfExpense1 = LocalDateTime.parse(expenseDate, formatter);
				LocalDateTime dateOfExpense2 = LocalDateTime.of(Integer.parseInt(year), dateOfExpense1.getMonth(),
						dateOfExpense1.getDayOfMonth(), 0, 0, 0, 0);

				newExpense.setCategory(Category.valueOf(category.toUpperCase()));
				newExpense.setAmount(amount);
				newExpense.setDate(dateOfExpense2);

				Expense created = expenseService.createExpense(newExpense);
				user.getExpenses().add(created);
				expenseService.sortExpenses(user);

				System.out.println(ColorUtility.BLUE_TEXT + "\nWould you like to add more for this year? [Y/n]"
						+ ColorUtility.TEXT_RESET);
				String answer = sc.nextLine();
				if (answer.equalsIgnoreCase("y")) {
					continue;
				} else if (answer.equalsIgnoreCase("n")) {
					return;
				} else {
					System.out.println(ColorUtility.RED_TEXT + "Not a valid input.");
					System.out.println("Returning to menu anyway.");
					return;
				}

			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + e.getMessage());
				continue;
			} catch (DateTimeParseException e) {
				System.out.println(ColorUtility.RED_TEXT + "Invalid date entered - please enter MM/DD format.");
				continue;
			}
		} while (true);

	}

	private void removeExpense(String year) {
		do {
			try {
				printExpenses();
				System.out.println(ColorUtility.BLUE_TEXT + "Select an ID from above:");
				System.out.println(ColorUtility.TEXT_RESET + "ID: " + ColorUtility.CYAN_TEXT);
				int expenseId = sc.nextInt();
				sc.nextLine();

				boolean expenseExists = false;
				Expense expenseToRemove = null;
				for (Expense e : user.getExpenses()) {
					if (e.getId().intValue() == expenseId) {
						expenseExists = true;
						expenseToRemove = e;
						break;
					}
				}

				if (!expenseExists) {
					throw new IllegalOptionException();
				}

				user.getExpenses().remove(expenseToRemove);
				expenseService.removeExpenseById(expenseId);
				userService.updateUser(user);

				System.out.println(ColorUtility.GREEN_TEXT + "\nExpense Successfully Removed!");
				System.out.println(ColorUtility.BLUE_TEXT + "\nWould you like to remove more for this year? [Y/n]"
						+ ColorUtility.TEXT_RESET);
				String answer = sc.nextLine();
				if (answer.equalsIgnoreCase("y")) {
					continue;
				} else {
					return;
				}

			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "Invalid expense ID - please enter an ID from the list.");
				sc.nextLine();
				continue;
			} catch (IllegalOptionException e) {

				System.out.println(ColorUtility.RED_TEXT + e.getMessage());
				continue;
			} catch (DateTimeParseException e) {
				System.out.println(ColorUtility.RED_TEXT + "Invalid date entered - please enter MM/DD format.");
				continue;
			}
		} while (true);
	}

	private void printExpenses() {
		System.out.println(ColorUtility.GREEN_TEXT + "+----------+");
		System.out.println("| Expenses |");
		System.out.println("+----------+");
		if (user.getExpenses().size() > 0) {
			for (Expense e : user.getExpenses()) {
				System.out.println(e);
			}
		} else {
			System.out.println("***NO EXPENSES TO SHOW***");
		}
		System.out.println(ColorUtility.TEXT_RESET);

	}

	private String validateYear() {
		do {
			try {
				System.out.println(ColorUtility.BLUE_TEXT + "Please input a year for budget:");
				System.out.println(ColorUtility.TEXT_RESET + "Year: " + ColorUtility.CYAN_TEXT);
				String year = sc.nextLine();

				String yearPattern = "^(19|2[0-9])\\d{2}$";
				Pattern p = Pattern.compile(yearPattern);
				Matcher m = p.matcher(year);

				if (m.matches()) {
					return year;
				}

				throw new IllegalOptionException();
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "Please input a valid year | 1900-2999");
				continue;
			}
		} while (true);
	}

	private String validateMonth() {
		do {
			try {
				System.out.println(ColorUtility.BLUE_TEXT + "Please input a month [MM] for budget:");
				System.out.println(ColorUtility.TEXT_RESET + "Month: " + ColorUtility.CYAN_TEXT);
				String month = sc.nextLine();
				int monthValue = Integer.parseInt(month.replace("0", ""));

				if (monthValue > 12 || monthValue < 1) {
					throw new IllegalOptionException();
				}

				return month.replace("0", "");
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "Please input a valid month | [MM]");
				continue;
			}
		} while (true);

	}
}
