package com.cognixia.jump.controller;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognixia.jump.exceptions.IllegalOptionException;
import com.cognixia.jump.exceptions.IncorrectPasswordException;
import com.cognixia.jump.exceptions.UserNotFoundException;
import com.cognixia.jump.model.Contact;
import com.cognixia.jump.model.Credentials;
import com.cognixia.jump.model.User;
import com.cognixia.jump.service.ContactService;
import com.cognixia.jump.service.UserService;
import com.cognixia.jump.util.ColorUtility;
import com.cognixia.jump.util.MenuUtil;

@Component
public class ContactManagerApp {
	
	public static Scanner sc = new Scanner(System.in);
	
	private User user = null;
	
	private String sortMethod = "first";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
	public void entryPoint() {
		do {
			try {
			MenuUtil.greeting();
			System.out.print(ColorUtility.CYAN_TEXT);
			int option = sc.nextInt();
			sc.nextLine();
			
			switch (option) {
				case 1: 
					login();
					break;
				case 2:
					newUser();
					break;
				case 3:
					return;
				default:
					throw new IllegalOptionException();
			}
			
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid input. Please try again.");
				sc.nextLine();
				continue;
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again.");
				continue;
			}
		} while (true);
	}

	private void newUser() {
		System.out.println(ColorUtility.YELLOW_TEXT + "-------------------");
		System.out.println("| Create New User |");
		System.out.println("-------------------");
		User newUser = new User();
		do {
			System.out.print(ColorUtility.YELLOW_TEXT + "Please input your email: ");
			System.out.print(ColorUtility.CYAN_TEXT);
			newUser.setEmail(sc.nextLine());
			
			if (newUser.getEmail() == null || newUser.getEmail().isEmpty() || newUser.getEmail().isBlank() ) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
				continue;
			}
			
			Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = emailPattern.matcher(newUser.getEmail());
			if (!matcher.find()) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
				continue;
			}
			break;
		} while (true);
		
		do {
			System.out.print(ColorUtility.YELLOW_TEXT + "\nPlease input a password: ");
			System.out.print(ColorUtility.CYAN_TEXT);
			newUser.setPassword(sc.nextLine());
			
			if (newUser.getPassword() == null || newUser.getPassword().isEmpty() || newUser.getPassword().isBlank() ) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted password is empty/blank. Please try again.\n");
				continue;
			}
			
			System.out.print(ColorUtility.YELLOW_TEXT + "\nPlease confirm password: ");
			System.out.print(ColorUtility.CYAN_TEXT);
			String temp = sc.nextLine();
			System.out.println();
			
			if (!newUser.getPassword().equals(temp) ) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted password is empty/blank. Please try again.\n");
				continue;
			}
			break;
			
		} while (true);
		
		userService.createUser(newUser);
		System.out.println(ColorUtility.GREEN_TEXT + "User Created!\n");
	}

	private void login() {
		do {
			Credentials login = new Credentials();
			System.out.println(ColorUtility.YELLOW_TEXT + "\n---------");
			System.out.println("| Login |");
			System.out.println("---------");
			System.out.print("Email: " + ColorUtility.CYAN_TEXT);
			login.setEmail(sc.nextLine());
			
			if (login.getEmail() == null || login.getEmail().isEmpty() || login.getEmail().isBlank() ) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
				continue;
			}
			
			Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = emailPattern.matcher(login.getEmail());
			if (!matcher.find()) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
				continue;
			}
			
			System.out.print(ColorUtility.YELLOW_TEXT + "\nPassword: " + ColorUtility.CYAN_TEXT);
			login.setPassword(sc.nextLine());
			
			if (login.getPassword() == null || login.getPassword().isEmpty() || login.getPassword().isBlank() ) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted password is empty/blank. Please try again.\n");
				continue;
			}
			try {
				user = userService.authenticate(login);
				user.setContact(contactService.getContacts(user));
				if (user.getContact().size() > 1) {
					contactService.sortContacts(user, sortMethod);
				}
				
				session();
				return;
				
			} catch (IncorrectPasswordException e) {
				System.out.println(ColorUtility.RED_TEXT + "Password is incorrect. Please try again.");
				continue;
			} catch (UserNotFoundException e) {
				System.out.println(ColorUtility.RED_TEXT + "User information not found. Please try again.");
			}
		} while(true);
		
	}

	private void session() {
		do {
			try {
				MenuUtil.session(user);
				int option = sc.nextInt();
				sc.nextLine();
				
				switch (option) {
					case 1: // Add Contact
						newContact();
						break;
					case 2: // Update
						methodOption("update");
						break;
					case 3: // Delete
						methodOption("delete");
						break;
					case 4: // Sort
						if (user.getContact().size() > 1) {
							sortOption();
						} else {
							System.out.println(ColorUtility.RED_TEXT + "\nCannot sort contacts.");
							System.out.println("Must have 2 or more contacts\n" + ColorUtility.TEXT_RESET);
						}
						
						break;
					case 5: // Logout
						user = null;
						return;
					default:
						throw new IllegalOptionException();
				}
				
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid input. Please try again.");
				sc.nextLine();
				continue;
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again.");
				continue;
			}
		} while(true);
	}

	private void sortOption() {
		try {
			System.out.println(ColorUtility.YELLOW_TEXT + "\nSelect Contact Sorting Field: (Current = " + sortMethod + ")");
			System.out.println("  1. First Name");
			System.out.println("  2. Last Name");
			System.out.println("  3. Email");
			System.out.println("  4. Recent");
			System.out.print("Option: " + ColorUtility.CYAN_TEXT);
			int sortUpdate = sc.nextInt();
			sc.nextLine();
			System.out.println();
			
			switch (sortUpdate) {
				case 1: 
					sortMethod = "first";
					break;
				case 2: 
					sortMethod = "last";
					break;
				case 3: 
					sortMethod = "email";
					break;
				case 4: 
					sortMethod = "recent";
					break;
				default: 
					throw new InputMismatchException();
			}
			
			contactService.sortContacts(user, sortMethod);
			System.out.println(ColorUtility.GREEN_TEXT + "\nSorting method updated!\n");
			
			
		} catch (InputMismatchException e) {
			System.out.println(ColorUtility.RED_TEXT + "\nNot a valid input. Sorting method unchanged (" + sortMethod + ")\n");
		}
		
	}

	private void deleteContact(Contact delete) {
		contactService.deleteContact(delete);
		user.getContact().remove(delete);
		System.out.println(ColorUtility.GREEN_TEXT + "\nContact deleted!\n" + ColorUtility.TEXT_RESET);
		
	}

	private void updateDeleteFindContact(int option, String method) {
		do {
			try {
				
				MenuUtil.contactList(user);
				
				switch (option) {
					case 1:
						System.out.print(ColorUtility.YELLOW_TEXT + "Input contact ID: " + ColorUtility.CYAN_TEXT);
						int contactId = sc.nextInt();
						sc.nextLine();
						
						Contact byId = null;
						for (Contact c : user.getContact() ) {
							if (contactId == c.getId().intValue()) {
								byId = c;
								break;
							}
						}
						if (byId == null) {
							throw new UserNotFoundException();
						}
						
						if (method.equals("update")) {
							updateUtil(byId);
						} else {
							deleteContact(byId);
						}
						return;
					case 2:
						System.out.print(ColorUtility.YELLOW_TEXT + "Input contact First Name: " + ColorUtility.CYAN_TEXT);
						String contactFirst = sc.nextLine();
						
						Contact byFirst = null;
						for (Contact c : user.getContact() ) {
							if (contactFirst.startsWith(c.getFirstName())) {
								System.out.println(ColorUtility.YELLOW_TEXT + "Is this the contact you are looking for?[Y/n]");
								System.out.println(c);
								String answer = sc.nextLine();
								if (answer.equalsIgnoreCase("n")) {
									continue;
								} else if (answer.equalsIgnoreCase("y")) {
									byFirst = c;
									break;
								} else {
									System.out.println(ColorUtility.RED_TEXT + "\nNot a valid answer. Restarting search.\n");
									byFirst = null;
									break;
								}
							}
						}
						if (byFirst == null) {
							throw new UserNotFoundException();
						}
						
						if (method.equals("update")) {
							updateUtil(byFirst);
						} else {
							deleteContact(byFirst);
						}
						return;
					case 3:
						System.out.print(ColorUtility.YELLOW_TEXT + "Input contact Last Name: " + ColorUtility.CYAN_TEXT);
						String contactLast = sc.nextLine();
						
						Contact byLast = null;
						for (Contact c : user.getContact() ) {
							if (contactLast.startsWith(c.getLastName())) {
								System.out.println(ColorUtility.YELLOW_TEXT + "Is this the contact you are looking for?[Y/n]");
								System.out.println(c);
								String answer = sc.nextLine();
								if (answer.equalsIgnoreCase("n")) {
									continue;
								} else if (answer.equalsIgnoreCase("y")) {
									byLast = c;
									break;
								} else {
									System.out.println(ColorUtility.RED_TEXT + "\nNot a valid answer. Restarting search.\n");
									byLast = null;
									break;
								}
							}
						}
						if (byLast == null) {
							throw new UserNotFoundException();
						}
						
						if (method.equals("update")) {
							updateUtil(byLast);
						} else {
							deleteContact(byLast);
						}
						return;
					default:
						throw new IllegalOptionException();
				}
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid input. Please try again.");
				sc.nextLine();
				continue;
			} catch (IllegalOptionException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid option. Please try again.");
				continue;
			} catch (UserNotFoundException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nContact by that ID not found. Please try again.");
				continue;
			}
		} while(true);
		
	}

	private void methodOption(String method) {
		if (user.getContact().size() <= 0) {
			System.out.println(ColorUtility.RED_TEXT + "\nNo contacts! Cannot update or delete.");
			System.out.println("Returning to menu.\n" + ColorUtility.TEXT_RESET);
			return;
		}
		do {
			try {
				System.out.println(ColorUtility.YELLOW_TEXT + "\nEnter option to look up contact for " + method + ": ");
				System.out.println("  1: ID  2: First Name  3: Last Name");
				System.out.print("Option: " + ColorUtility.CYAN_TEXT);
				int option = sc.nextInt();
				sc.nextLine();
				System.out.println();
				
				updateDeleteFindContact(option, method);
				return;
				
			} catch (InputMismatchException e) {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid input. Please try again.");
				sc.nextLine();
				continue;
			}
		} while(true);
				
			
		
	}

	private void newContact() {
		Contact newContact = new Contact();
		
		System.out.println(ColorUtility.YELLOW_TEXT + "\n-----------------");
		System.out.println("|  New Contact  |");
		System.out.println("-----------------");
		System.out.print("First Name: " + ColorUtility.CYAN_TEXT);
		newContact.setFirstName(sc.nextLine());
		
		if ( newContact.getFirstName() == null || newContact.getFirstName().isEmpty() ) {
			newContact.setFirstName("DEFAULT");
		}
		
		System.out.print(ColorUtility.YELLOW_TEXT + "\nLast Name: " + ColorUtility.CYAN_TEXT);
		newContact.setLastName(sc.nextLine());
		
		if ( newContact.getLastName() == null || newContact.getLastName().isEmpty() || newContact.getLastName().isBlank() ) {
			newContact.setLastName("");
		}
		
		do {
			System.out.print(ColorUtility.YELLOW_TEXT + "\nPhone Number: " + ColorUtility.CYAN_TEXT);
			String temp = sc.nextLine();
			Pattern emailPattern = Pattern.compile("^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", Pattern.CASE_INSENSITIVE);
			Matcher matcher = emailPattern.matcher(temp);
			if (!matcher.find()) {
				System.out.println(ColorUtility.RED_TEXT + "\nInputted phone number is invalid. Please try again.\n");
				continue;
			}
			newContact.setPhone(temp);
			break;
		} while(true);
		
		newContact.setEmail("");
		System.out.println(ColorUtility.YELLOW_TEXT + "\nInput an email for contact? [Y/n}" + ColorUtility.CYAN_TEXT);
		String answer = sc.nextLine();
		if (answer.equalsIgnoreCase("y")) {
			do {
				System.out.print(ColorUtility.YELLOW_TEXT + "\nEmail: " + ColorUtility.CYAN_TEXT);
				String temp = sc.nextLine();
				Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
				Matcher matcher = emailPattern.matcher(temp);
				if (!matcher.find()) {
					System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
					continue;
				}
				newContact.setEmail(temp);
				break;
			} while(true);
		} else if (answer.equalsIgnoreCase("n")) {
			// DO NOTHING
		} else {
			System.out.println(ColorUtility.RED_TEXT + "\nNot a valid answer. Returning to menu");
		}
		
		newContact.setUser(user);
		user.getContact().add(contactService.newContact(newContact));
		contactService.sortContacts(user, sortMethod);
	}
	
	private void updateUtil(Contact update) {
		do {
			System.out.println(ColorUtility.YELLOW_TEXT + "\nWhat field do you want to edit?");
			System.out.println("# Contact: " + update.getFirstName() + " " + update.getLastName() + ", " + update.getEmail() + ", " + update.getPhone());
			System.out.println("  First Name  \t(first)");
			System.out.println("  Last Name   \t(last)");
			System.out.println("  Email Addr. \t(email)");
			System.out.println("  Phone Number\t(phone)");
			System.out.println(ColorUtility.PURPLE_TEXT + "[Input 'update' to update contact]");
			System.out.print(ColorUtility.YELLOW_TEXT + "Field: " + ColorUtility.CYAN_TEXT);
			String field = sc.nextLine().trim();
			
			if (field.equalsIgnoreCase("update")) {
				contactService.updateContact(update);
				contactService.sortContacts(user, sortMethod);
				System.out.println(ColorUtility.GREEN_TEXT + "\nContact Updated!\n");
				return;
			} else if (field.equalsIgnoreCase("first")) {
				System.out.print(ColorUtility.YELLOW_TEXT + "\nNew First Name: " + ColorUtility.CYAN_TEXT);
				update.setFirstName(sc.nextLine());
				continue;
			} else if (field.equalsIgnoreCase("last")) {
				System.out.print(ColorUtility.YELLOW_TEXT + "\nNew Last Name: " + ColorUtility.CYAN_TEXT);
				update.setLastName(sc.nextLine());
				continue;
			} else if (field.equalsIgnoreCase("email")) {
				System.out.print(ColorUtility.YELLOW_TEXT + "\nNew Email: " + ColorUtility.CYAN_TEXT);
				String temp = sc.nextLine();
				Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
				Matcher matcher = emailPattern.matcher(temp);
				if (!matcher.find()) {
					System.out.println(ColorUtility.RED_TEXT + "\nInputted email is invalid. Please try again.\n");
					continue;
				}
				update.setEmail(temp);
				continue;
			} else if (field.equalsIgnoreCase("phone")) {
				System.out.print(ColorUtility.YELLOW_TEXT + "\nNew Phone Number: " + ColorUtility.CYAN_TEXT);
				String temp = sc.nextLine();
				Pattern emailPattern = Pattern.compile("^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", Pattern.CASE_INSENSITIVE);
				Matcher matcher = emailPattern.matcher(temp);
				if (!matcher.find()) {
					System.out.println(ColorUtility.RED_TEXT + "\nInputted phone number is invalid. Please try again.\n");
					continue;
				}
				update.setPhone(temp);
				continue;
			} else {
				System.out.println(ColorUtility.RED_TEXT + "\nNot a valid field. Try again.\n");
				continue;
			}
		} while(true);
	}
	

}
