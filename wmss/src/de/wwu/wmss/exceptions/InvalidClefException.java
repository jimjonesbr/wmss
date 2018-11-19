package de.wwu.wmss.exceptions;

public class InvalidClefException extends RuntimeException{

	private static final long serialVersionUID = -4216956738951654372L;
	private String code;
	private String hint;
	
	public InvalidClefException(String message) {
		super(message);
	}
	
	public InvalidClefException(String message, String code, String hint) {
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
