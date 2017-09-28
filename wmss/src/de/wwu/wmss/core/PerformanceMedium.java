package de.wwu.wmss.core;

public class PerformanceMedium {

	private String typeDescription;
	private String typeClassification;
	private String mediumDescription;
	private String mediumClassification;
	private String mediumScoreDescription;	
	private int movementId;
	private int scoreId;
	
	
	private boolean solo;
	
	public PerformanceMedium() {
		super();
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	public String getTypeClassification() {
		return typeClassification;
	}

	public void setTypeClassification(String typeClassification) {
		this.typeClassification = typeClassification;
	}

	public String getMediumDescription() {
		return mediumDescription;
	}

	public void setMediumDescription(String mediumDescription) {
		this.mediumDescription = mediumDescription;
	}

	public String getMediumClassification() {
		return mediumClassification;
	}

	public void setMediumClassification(String mediumClassification) {
		this.mediumClassification = mediumClassification;
	}

	public boolean isSolo() {
		return solo;
	}

	public void setSolo(boolean solo) {
		this.solo = solo;
	}

	public int getMovementId() {
		return movementId;
	}

	public void setMovementId(int movementId) {
		this.movementId = movementId;
	}

	public int getScoreId() {
		return scoreId;
	}

	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	public String getMediumScoreDescription() {
		return mediumScoreDescription;
	}

	public void setMediumScoreDescription(String mediumScoreDescription) {
		this.mediumScoreDescription = mediumScoreDescription;
	}

	
	
}
