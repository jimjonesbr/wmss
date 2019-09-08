package de.wwu.wmss.core;

public class Person {

	private String name;
	private String role;
	private String scoreId; 
	private String identifier;
	private String action = "";
	
	public Person() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String score_id) {
		this.scoreId = score_id;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String url) {
		this.identifier = url;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	
}
