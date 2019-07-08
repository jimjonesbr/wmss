package de.wwu.wmss.core;

public class Provenance {
	
	private String generatedAtTime;
	private String wasGeneratedBy; 
	private String wasAssociatedWith;
	private String comments;
	
	public Provenance() {
		super();
	}

	public String getGeneratedAtTime() {
		return generatedAtTime;
	}

	public void setGeneratedAtTime(String generatedAtTime) {
		this.generatedAtTime = generatedAtTime;
	}

	public String getWasGeneratedBy() {
		return wasGeneratedBy;
	}

	public void setWasGeneratedBy(String wasGeneratedBy) {
		this.wasGeneratedBy = wasGeneratedBy;
	}

	public String getWasAssociatedWith() {
		return wasAssociatedWith;
	}

	public void setWasAssociatedWith(String wasAssociatedWith) {
		this.wasAssociatedWith = wasAssociatedWith;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
	
}
