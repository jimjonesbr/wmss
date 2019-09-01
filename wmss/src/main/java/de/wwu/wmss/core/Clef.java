package de.wwu.wmss.core;

public class Clef {

	private String clef = "";
	private String format = "";
	
	public Clef(String clef, String format) {
		super();
		this.clef = clef;
		this.format = format;
	}
	public Clef() {
		super();
	}
	public String getClef() {
		return clef;
	}
	public void setClef(String clef) {
		this.clef = clef;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	
}
