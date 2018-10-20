package de.wwu.wmss.core;

public class InvalidWMSSRequestException extends RuntimeException{

	//https://dzone.com/articles/implementing-custom-exceptions-in-java
	private static final long serialVersionUID = -153434683569724870L;
	private String code;
	private String hint;
	
	public InvalidWMSSRequestException(String message, Throwable cause, String code) {
		super(message, cause);
		this.code = code;
	}

	public InvalidWMSSRequestException(String message, String code, String hint) {
		super(message);
		this.code = code;
		this.hint = hint;
	}
	
	
	public String getCode() {
		return code;
	}

	public String getHint() {
		return hint;
	}	
}
