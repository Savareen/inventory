package com.igorzaitcev.inventory.exceptions;

public class InvalidArgumentException extends RuntimeException {

	private static final long serialVersionUID = -1377343733476802406L;

	public InvalidArgumentException(String message) {
		super(message);
	}
}
