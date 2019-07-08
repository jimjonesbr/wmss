package de.wwu.wmss.core;

import java.util.ArrayList;

public class Movement {

	private String movementIdentifier;
	private String movementName;	
	private String beatUnit;
	private int beatsPerMinute;
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

	public String getBeatUnit() {
		return beatUnit;
	}

	public void setBeatUnit(String beatunit) {
		this.beatUnit = beatunit;
	}
	
	public int getBeatsPerMinute() {
		return beatsPerMinute;
	}

	public void setBeatsPerMinute(int beatsPerMinute) {
		this.beatsPerMinute = beatsPerMinute;
	}

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
