package com.cognixia.jump.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super("User not found or password incorrect. Please try again");
	}
}