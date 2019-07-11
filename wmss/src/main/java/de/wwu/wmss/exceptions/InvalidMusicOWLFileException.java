package de.wwu.wmss.exceptions;

public class InvalidMusicOWLFileException extends RuntimeException{

	private static final long serialVersionUID = -4216956738951654372L;
	private String code;
	private String hint;
	
	public InvalidMusicOWLFileException(String message) {
		super(message);
	}
	
	public InvalidMusicOWLFileException(String message, String code, String hint) {
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
