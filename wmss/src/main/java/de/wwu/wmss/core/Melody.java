package de.wwu.wmss.core;

import java.util.ArrayList;

public class Melody {

	private String format = "";
	private String query = "";
	private String mediumCode = "";
	private String mediumType = "";
	private String key = "";
	private String time = "";
	private String clef = "";
	private ArrayList<Note> noteSequence = new ArrayList<Note>(); 
	
	public Melody() {
		
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getMediumCode() {
		return mediumCode;
	}

	public void setMediumCode(String mediumCode) {
		this.mediumCode = mediumCode;
	}

	public String getMediumType() {
		return mediumType;
	}

	public void setMediumType(String mediumType) {
		this.mediumType = mediumType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getClef() {
		return clef;
	}

	public void setClef(String clef) {
		this.clef = clef;
	}

	public ArrayList<Note> getNoteSequence() {
		return noteSequence;
	}

	public void setNoteSequence(ArrayList<Note> noteSequence) {
		this.noteSequence = noteSequence;
	}
		
}
