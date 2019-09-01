package de.wwu.wmss.core;

public class Key {

	private String format = "";
	private String key = "";
	
	public Key() {
		super();
	}

	public Key(String format, String key) {
		super();
		this.format = format;
		this.key = key;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
