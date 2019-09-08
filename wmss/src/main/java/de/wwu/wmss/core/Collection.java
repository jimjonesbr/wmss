package de.wwu.wmss.core;

public class Collection {

	private String identifier = "";
	private String label = "";
	private String action = "";
	
	public Collection() {
		super();

	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String id) {
		this.identifier = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String description) {
		this.label = description;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
