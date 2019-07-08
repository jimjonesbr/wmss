package de.wwu.wmss.core;

import java.util.ArrayList;

public class PerformanceMediumType {

	private String mediumTypeDescription;
	private String mediumTypeId;
	private ArrayList<PerformanceMedium> mediums;
	private String scoreId;
	private String movementId;
	
	
	public PerformanceMediumType() {
		super();
		this.mediums = new ArrayList<PerformanceMedium>();
	}


	public String getMediumTypeDescription() {
		return mediumTypeDescription;
	}


	public void setMediumTypeDescription(String mediumTypeDescription) {
		this.mediumTypeDescription = mediumTypeDescription;
	}


	public String getMediumTypeId() {
		return mediumTypeId;
	}


	public void setMediumTypeId(String mediumTypeId) {
		this.mediumTypeId = mediumTypeId;
	}


	public ArrayList<PerformanceMedium> getMediums() {
		return mediums;
	}


	public String getScoreId() {
		return scoreId;
	}


	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}


	public String getMovementId() {
		return movementId;
	}


	public void setMovementId(String movementId) {
		this.movementId = movementId;
	}

	

	
	
}
