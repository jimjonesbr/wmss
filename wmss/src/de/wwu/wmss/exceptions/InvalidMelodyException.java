package de.wwu.wmss.exceptions;

public class InvalidMelodyException extends RuntimeException{

	private static final long serialVersionUID = -4216956738951654372L;
	private String code;
	private String hint;
	
	public InvalidMelodyException(String message) {
		super(message);
	}
	
	public InvalidMelodyException(String message, String code, String hint) {
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
