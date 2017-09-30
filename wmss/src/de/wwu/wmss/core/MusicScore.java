package de.wwu.wmss.core;

import java.util.ArrayList;

public class MusicScore {

	private String scoreIdentifier;
	private String source;
	private String title;
	private String composer;
	private String groupDescription;
	private String groupId;
	private String tonalityTonic;
	private String tonalityMode;
	
	private int creationDateFrom;
	private int creationDateTo;
	private ArrayList<Movement> movements;
	private ArrayList<Format> formats;
	private ArrayList<Person> persons;
	
	public MusicScore() {
		super();
		
		this.movements = new ArrayList<Movement>();
		this.formats = new ArrayList<Format>();
		this.persons = new ArrayList<Person>();
	}


	public String getScoreIdentifier() {
		return scoreIdentifier;
	}


	public void setScoreIdentifier(String identifier) {
		this.scoreIdentifier = identifier;
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


	public int getCreationDateFrom() {
		return creationDateFrom;
	}


	public void setCreationDateFrom(int creationDateFrom) {
		this.creationDateFrom = creationDateFrom;
	}


	public int getCreationDateTo() {
		return creationDateTo;
	}


	public void setCreationDateTo(int creationDateTo) {
		this.creationDateTo = creationDateTo;
	}


	public ArrayList<Movement> getMovements() {
		return movements;
	}


	public ArrayList<Format> getFormats() {
		return formats;
	}


	public ArrayList<Person> getPersons() {
		return persons;
	}


	public String getGroupDescription() {
		return groupDescription;
	}


	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
		
	
	
}


