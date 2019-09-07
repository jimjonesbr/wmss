package de.wwu.wmss.core;

public class Collection {

	private String identifier = "";
	private String name = "";
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
	public String getName() {
		return name;
	}
	public void setName(String description) {
		this.name = description;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
