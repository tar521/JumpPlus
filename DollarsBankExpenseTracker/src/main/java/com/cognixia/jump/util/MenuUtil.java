package com.cognixia.jump.util;


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
	public static void mainMenu() {
		System.out.println(ColorUtility.BLUE_TEXT + "+---------------------+");
		System.out.println("| WELCOME Customer!!! |");
		System.out.println("+---------------------+" + ColorUtility.TEXT_RESET);
		System.out.println("1. Deposit Amount");
		System.out.println("2. Withdraw Amount");
		System.out.println("3. Funds Transfer");
		System.out.println("4. View 5 Recent Transactions");
		System.out.println("5. Display Customer Information");
		System.out.println("6. Open New Account");
		System.out.println("7. Sign Out\n");
		
		System.out.println(ColorUtility.GREEN_TEXT + "Enter Choice (1, 2, 3, 4, 5, 6, or 7)" + ColorUtility.TEXT_RESET);
	}
	
	public static void exitMessage() {
		System.out.println(ColorUtility.PURPLE_TEXT + "\n################################################");
		System.out.println("# Thank you for using our expense application! #");
		System.out.println("#               See you soon!                  #");
		System.out.println("################################################\n" + ColorUtility.TEXT_RESET);
	}
	
}