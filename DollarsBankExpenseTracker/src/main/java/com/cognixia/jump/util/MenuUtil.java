package com.cognixia.jump.util;

import java.text.DecimalFormat;

import com.cognixia.jump.model.MonthlyBudget;

public class MenuUtil {

	public static void greeting() {
		System.out.println(ColorUtility.BLUE_TEXT + "+-------------------------------------------+");
		System.out.println("| DOLLARSBANK Expense Tracker Welcomes You! |");
		System.out.println("+-------------------------------------------+" + ColorUtility.TEXT_RESET);
		System.out.println("1. Create New Account");
		System.out.println("2. Login");
		System.out.println("3. Exit\n");
		
		System.out.println(ColorUtility.GREEN_TEXT + "Enter Choice (1, 2, or 3)" + ColorUtility.TEXT_RESET);
	}

	
	public static void menuMakeAccount() {
		System.out.println(ColorUtility.BLUE_TEXT + "+-------------------------------+");
		System.out.println("| Enter Details For New Account |");
		System.out.println("+-------------------------------+" + ColorUtility.TEXT_RESET);
		
	}
		
	// Method to print header for account login
	public static void loginMenu() {
		System.out.println(ColorUtility.BLUE_TEXT + "+---------------------+");
		System.out.println("| Enter Login Details |");
		System.out.println("+---------------------+" + ColorUtility.TEXT_RESET);
		
	}
	
	// Method to print header for the main menu
	public static void mainMenu(String name) {
		System.out.println(ColorUtility.BLUE_TEXT + "+---------------------+");
		System.out.println("| WELCOME Customer!!! |");
		System.out.println("+---------------------+" + ColorUtility.TEXT_RESET);
		System.out.println("1. Create New Expense");
		System.out.println("2. Remove Expense");
		System.out.println("3. Set Monthly or Yearly Budget");
		System.out.println("4. View 5 Upcoming Expenses");
		System.out.println("5. Show Month's Budget");
		System.out.println("6. Show Year's Budget");
		System.out.println("7. Display Customer Information");
		System.out.println("8. Export Expenses and Budgets to CSV");
		System.out.println("9. Sign Out\n");
		
		System.out.println(ColorUtility.GREEN_TEXT + name + ", please enter an option (1, 2, 3, 4, 5, 6, or 7)" + ColorUtility.TEXT_RESET);
	}
	
	public static void exitMessage() {
		System.out.println(ColorUtility.PURPLE_TEXT + "\n################################################");
		System.out.println("# Thank you for using our expense application! #");
		System.out.println("#               See you soon!                  #");
		System.out.println("################################################\n" + ColorUtility.TEXT_RESET);
	}


	public static double printMonthlyBudget(String month, DecimalFormat df, MonthlyBudget budget) {
		month.toUpperCase();
		double monthlyBudget = 0;
		switch (month) {
			case "JANUARY":
				monthlyBudget = budget.getJanuary().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getJanuary().doubleValue()));
				break;
			case "FEBRUARY":
				monthlyBudget = budget.getFebruary().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getFebruary().doubleValue()));
				break;
			case "MARCH":
				monthlyBudget = budget.getMarch().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getMarch().doubleValue()));
				break;
			case "APRIL":
				monthlyBudget = budget.getApril().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getApril().doubleValue()));
				break;
			case "MAY":
				monthlyBudget = budget.getMay().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getMay().doubleValue()));
				break;
			case "JUNE":
				monthlyBudget = budget.getJune().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getJune().doubleValue()));
				break;
			case "JULY":
				monthlyBudget = budget.getJuly().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getJuly().doubleValue()));
				break;
			case "AUGUST":
				monthlyBudget = budget.getAugust().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getAugust().doubleValue()));
				break;
			case "SEPTEMBER":
				monthlyBudget = budget.getSeptember().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getSeptember().doubleValue()));
				break;
			case "OCTOBER":
				monthlyBudget = budget.getOctober().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getOctober().doubleValue()));
				break;
			case "NOVEMBER":
				monthlyBudget = budget.getNovember().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getNovember().doubleValue()));
				break;
			case "DECEMBER":
				monthlyBudget = budget.getDecember().doubleValue();
				System.out.println("Monthly Budget: \t$" + df.format(budget.getDecember().doubleValue()));
				break;
			default:
				break;
		}
		return monthlyBudget;
	}
	
}