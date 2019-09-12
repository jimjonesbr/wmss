package de.wwu.wmss.core;

public class Person {

	private String personName ="";
	private String personRole ="";
	private String scoreId; 
	private String personIdentifier ="";
	private String action = "";
	
	public Person() {
		super();
	}
	public String getName() {
		return personName;
	}
	public void setName(String name) {
		this.personName = name;
	}
	public String getRole() {
		return personRole;
	}
	public void setRole(String role) {
		this.personRole = role;
	}
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String score_id) {
		this.scoreId = score_id;
	}
	public String getIdentifier() {
		return personIdentifier;
	}
	public void setIdentifier(String url) {
		this.personIdentifier = url;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}

	
}
