package de.wwu.wmss.core;

public class DeletedRecord {

	private String scoreIdentifier;
	private String scoreTitle;
	private String collectionLabel;
	
	public DeletedRecord() {
		super();
	}

	public String getScoreIdentifier() {
		return scoreIdentifier;
	}

	public void setScoreIdentifier(String scoreIdentifier) {
		this.scoreIdentifier = scoreIdentifier;
	}

	public String getTitle() {
		return scoreTitle;
	}

	public void setTitle(String title) {
		this.scoreTitle = title;
	}

	public String getCollection() {
		return collectionLabel;
	}

	public void setCollection(String collection) {
		this.collectionLabel = collection;
	}

	

}
