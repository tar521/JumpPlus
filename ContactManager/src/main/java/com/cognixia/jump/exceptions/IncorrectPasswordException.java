package com.cognixia.jump.exceptions;

public class IncorrectPasswordException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public IncorrectPasswordException() {
		super("Password incorrect. Please try again");
	}
}