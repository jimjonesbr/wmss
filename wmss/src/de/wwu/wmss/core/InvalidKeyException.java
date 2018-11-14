package de.wwu.wmss.core;

public class InvalidKeyException extends RuntimeException{

	private static final long serialVersionUID = -4216956738951654372L;
	private String code;
	private String hint;
	
	public InvalidKeyException(String message) {
		super(message);
	}
	
	public InvalidKeyException(String message, String code, String hint) {
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
