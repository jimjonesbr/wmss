package de.wwu.wmss.core;

import java.util.ArrayList;

public class MusicScore {

	private String identifier;
	private String source;
	private String title;
	private String composer;
	private String group;
	private String tonalityTonic;
	private String tonalityMode;
	private String creationDate;
	private String creationDateFrom;
	private String creationDateTo;
	private ArrayList<Movement> movements;
	private ArrayList<String> formats;
	private ArrayList<Person> persons;
	
	public MusicScore() {
		super();
		
		this.movements = new ArrayList<Movement>();
		this.formats = new ArrayList<String>();
		this.persons = new ArrayList<Person>();
	}


	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getComposer() {
		return composer;
	}


	public void setComposer(String composer) {
		this.composer = composer;
	}


	public String getGroup() {
		return group;
	}


	public void setGroup(String group) {
		this.group = group;
	}


	public String getTonalityTonic() {
		return tonalityTonic;
	}


	public void setTonalityTonic(String tonalityTonic) {
		this.tonalityTonic = tonalityTonic;
	}


	public String getTonalityMode() {
		return tonalityMode;
	}


	public void setTonalityMode(String tonalityMode) {
		this.tonalityMode = tonalityMode;
	}


	public String getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


	public String getCreationDateFrom() {
		return creationDateFrom;
	}


	public void setCreationDateFrom(String creationDateFrom) {
		this.creationDateFrom = creationDateFrom;
	}


	public String getCreationDateTo() {
		return creationDateTo;
	}


	public void setCreationDateTo(String creationDateTo) {
		this.creationDateTo = creationDateTo;
	}


	public ArrayList<Movement> getMovements() {
		return movements;
	}


	public ArrayList<String> getFormats() {
		return formats;
	}


	public ArrayList<Person> getPersons() {
		return persons;
	}
		
}


