package de.wwu.wmss.core;

public class PersonDescription extends Person {

	private int totalRelatedScores;
	
	public PersonDescription() {
		super();
	}

	public int getTotalRelatedScores() {
		return totalRelatedScores;
	}
	public void setTotalRelatedScores(int totalRelatedScores) {
		this.totalRelatedScores = totalRelatedScores;
	}
	
}
