package de.wwu.wmss.core;

import java.util.ArrayList;

public class Movement {

	private String movementIdentifier;
	private String movementName;	
	private String tempo;
	private String scoreId;

	private ArrayList<PerformanceMediumType> performanceMediumList;
	
	public Movement() {
		super();
		this.performanceMediumList = new ArrayList<PerformanceMediumType>();
	}
	
	public String getMovementId() {
		return movementIdentifier;
	}

	public void setMovementIdentifier(String identifier) {
		this.movementIdentifier = identifier;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

//	public ArrayList<PerformanceMedium> getPerformanceMediumList() {
//		return performanceMediumList;
//	}

//	public void setPerformanceMediumList(ArrayList<PerformanceMedium> performanceMediumList) {
//		this.performanceMediumList = performanceMediumList;
//	}

	public String getScoreId() {
		return scoreId;
	}

	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}

	public ArrayList<PerformanceMediumType> getPerformanceMediumList() {
		return performanceMediumList;
	}



	
}
