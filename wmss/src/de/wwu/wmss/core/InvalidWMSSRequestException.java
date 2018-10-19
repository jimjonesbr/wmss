package de.wwu.wmss.core;

public class InvalidWMSSRequestException extends Exception{

	//https://dzone.com/articles/implementing-custom-exceptions-in-java
	public InvalidWMSSRequestException(String message) {
		super(message);
	}

}
