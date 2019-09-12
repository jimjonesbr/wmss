package de.wwu.wmss.core;

import java.util.ArrayList;

public class PerformanceMediumType {

	private String mediumTypeLabel;
	private String mediumTypeIdentifier;
	private ArrayList<PerformanceMedium> mediums;
	private String scoreId;
	private String movementId;
	
	
	public PerformanceMediumType() {
		super();
		this.mediums = new ArrayList<PerformanceMedium>();
	}


	public String getMediumTypeDescription() {
		return mediumTypeLabel;
	}


	public void setMediumTypeDescription(String mediumTypeDescription) {
		this.mediumTypeLabel = mediumTypeDescription;
	}


	public String getMediumTypeId() {
		return mediumTypeIdentifier;
	}


	public void setMediumTypeId(String mediumTypeId) {
		this.mediumTypeIdentifier = mediumTypeId;
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
