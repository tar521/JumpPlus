package com.cognixia.jump.exceptions;

public class IllegalEmailException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public IllegalEmailException() {
		super("Email input is not a valid email.");
	}

}
