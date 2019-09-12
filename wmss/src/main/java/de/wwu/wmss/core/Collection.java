package de.wwu.wmss.core;

public class Collection {

	private String collectionIdentifier = "";
	private String collectionLabel = "";
	private String action = "";
	
	public Collection() {
		super();

	}
	public String getIdentifier() {
		return collectionIdentifier;
	}
	public void setIdentifier(String id) {
		this.collectionIdentifier = id;
	}
	public String getLabel() {
		return collectionLabel;
	}
	public void setLabel(String description) {
		this.collectionLabel = description;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
