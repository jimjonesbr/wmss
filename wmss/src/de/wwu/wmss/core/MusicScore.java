package de.wwu.wmss.core;

import java.util.ArrayList;
import java.util.Date;

public class MusicScore {

	private String scoreIdentifier;
	private String source;
	private String title;
	private String composer;
	private String collectionDescription;
	private String collectionId;
	private String tonalityTonic;
	private String tonalityMode;
	private String thumbnail;
	private String printResource;
	private String onlineResource;
	private Date creationDateFrom;
	private Date creationDateTo;
	private ArrayList<Movement> movements;
	private ArrayList<Format> formats;
	private ArrayList<Person> persons;
	//private ArrayList<MelodyLocation> melodyLocation;
	private ArrayList<MelodyLocationGroup> melodyLocations;
	
	public MusicScore() {
		super();
		
		this.movements = new ArrayList<Movement>();
		this.formats = new ArrayList<Format>();
		this.persons = new ArrayList<Person>();
		//this.melodyLocation = new ArrayList<MelodyLocation>();
		this.melodyLocations = new ArrayList<MelodyLocationGroup>();
		
	}


	public String getScoreId() {
		return scoreIdentifier;
	}


	public void setScoreId(String identifier) {
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


	public Date getCreationDateFrom() {
		return creationDateFrom;
	}


	public void setCreationDateFrom(Date creationDateFrom) {
		this.creationDateFrom = creationDateFrom;
	}


	public Date getCreationDateTo() {
		return creationDateTo;
	}


	public void setCreationDateTo(Date creationDateTo) {
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


	public String getCollectionDescription() {
		return collectionDescription;
	}


	public void setCollectionDescription(String groupDescription) {
		this.collectionDescription = groupDescription;
	}


	public String getCollectionId() {
		return collectionId;
	}


	public void setCollectionId(String groupId) {
		this.collectionId = groupId;
	}


//	public ArrayList<MelodyLocation> getMelodyLocation() {
//		return melodyLocation;
//	}
	
	public ArrayList<MelodyLocationGroup> getMelodyLocation2() {
		return melodyLocations;
	}


	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getPrintResource() {
		return printResource;
	}


	public void setPrintResource(String printResource) {
		this.printResource = printResource;
	}


	public String getOnlineResource() {
		return onlineResource;
	}


	public void setOnlineResource(String onlineResource) {
		this.onlineResource = onlineResource;
	}
		
	
	
}


