package com.cognixia.jump.util;

import com.cognixia.jump.model.Contact;
import com.cognixia.jump.model.User;

public class MenuUtil {
	
	public static void greeting() {
		System.out.println(ColorUtility.YELLOW_TEXT);
		System.out.println("--------------------------------------");
		System.out.println("| Welcome to Contact Manager System  |");
		System.out.println("--------------------------------------");
		System.out.println("   1: Login  2: New User  3: Exit" + ColorUtility.TEXT_RESET);
	}
	
	public static void exitMessage() {
		System.out.println(ColorUtility.YELLOW_TEXT);
		System.out.println("------------------------------------------------");
		System.out.println("| Thanks for using the Contact Manager System  |");
		System.out.println("------------------------------------------------");
		System.out.println(ColorUtility.TEXT_RESET);
	}

	public static void session(User user) {
		contactList(user);
		
		System.out.println("Please input an option:");
		System.out.println("  1: Add Contact");
		System.out.println("  2: Update Contact");
		System.out.println("  3: Delete Contact");
		System.out.println("  4: Sort Contacts");
		System.out.println("  5: Logout");
		System.out.print("Option: " + ColorUtility.CYAN_TEXT);
	}
	
	public static void contactList(User user) {
		System.out.println(ColorUtility.YELLOW_TEXT);
		
		if (user.getContact().size() > 0) {
			System.out.printf("----------------------------------------------------------------------------------------%n");
			System.out.printf("         Your Contacts - Contact Management System                                     |%n");
			System.out.printf("----------------------------------------------------------------------------------------%n");
			System.out.printf("| %-3s | %-12s | %-12s | %-15s | %-30s |%n", "ID", "First Name", "Last Name", "Phone", "Email");
			System.out.printf("----------------------------------------------------------------------------------------%n");
			
			for (Contact c : user.getContact()) {
				System.out.printf("| %03d | %-12s | %-12s | %-15s | %-30s |%n", c.getId().intValue(), c.getFirstName(), c.getLastName(), c.getPhone(), c.getEmail());
			}
			
			System.out.printf("----------------------------------------------------------------------------------------%n");
			System.out.println();
		}
	}

}
