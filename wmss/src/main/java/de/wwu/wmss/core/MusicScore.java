package de.wwu.wmss.core;

import java.util.ArrayList;

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
	private String dateIssued;
	private String documentCreationDate;
	private ArrayList<Movement> movements;
	private ArrayList<Format> formats;
	private Provenance provenance;
	private ArrayList<Person> persons;
	private ArrayList<MelodyLocationGroup> melodyLocations;
	//private Collection collection;
	private ArrayList<ScoreResource> resources;
	private ArrayList<Collection> collections;
	
	private String document;
	
	public MusicScore() {
		super();
		
		this.movements = new ArrayList<Movement>();
		this.formats = new ArrayList<Format>();
		this.persons = new ArrayList<Person>();
		this.melodyLocations = new ArrayList<MelodyLocationGroup>();
		this.provenance = new Provenance();
		//this.collection = new Collection();
		this.resources= new ArrayList<ScoreResource>();
		this.collections= new ArrayList<Collection>();
		
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

	public ArrayList<MelodyLocationGroup> getMelodyLocation() {
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

	public String getDocumentCreationDate() {
		return documentCreationDate;
	}

	public void setDocumentCreationDate(String documentCreationDate) {
		this.documentCreationDate = documentCreationDate;
	}
	
	public Provenance getProvenance() {
		return provenance;
	}

	public void setProvenance(Provenance provenance) {
		this.provenance = provenance;
	}

	public String getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public ArrayList<ScoreResource> getResources() {
		return resources;
	}

	public ArrayList<Collection> getCollections() {
		return collections;
	}
	
}


