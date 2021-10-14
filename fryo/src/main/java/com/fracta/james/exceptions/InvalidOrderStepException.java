package com.fracta.james.exceptions;

public class InvalidOrderStepException extends RuntimeException {

	public InvalidOrderStepException() {
	}

	public InvalidOrderStepException(String message) {
		super(message);
	}
	
	public InvalidOrderStepException(String message, Throwable cause) {
		super(message, cause);
	}
}
