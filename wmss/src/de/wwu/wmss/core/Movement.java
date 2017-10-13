package de.wwu.wmss.core;

import java.util.ArrayList;

public class Movement {

	private String movementIdentifier;
	private String title;	
	private String tempo;
	private String scoreId;
	//private ArrayList<PerformanceMedium> performanceMediumList;
	private ArrayList<PerformanceMediumType> performanceMediumList;
	
	public Movement() {
		super();
		//this.performanceMediumList = new ArrayList<PerformanceMedium>();
		this.performanceMediumList = new ArrayList<PerformanceMediumType>();
	}
	
	public String getMovementId() {
		return movementIdentifier;
	}

	public void setMovementIdentifier(String identifier) {
		this.movementIdentifier = identifier;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
