package de.wwu.wmss.core;

public class PerformanceMedium {

	private String mediumTypeDescription;
	private String mediumTypeId;
	private String mediumDescription;
	private String mediumId;
	private String mediumCode;
	private String mediumScoreDescription;	
	private String movementId;
	private String scoreId;
	private boolean solo;
	private boolean ensemble;
	
	public PerformanceMedium() {
		super();
	}

	public String getTypeDescription() {
		return mediumTypeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.mediumTypeDescription = typeDescription;
	}

	public String getMediumTypeId() {
		return mediumTypeId;
	}

	public void setMediumTypeId(String typeClassification) {
		this.mediumTypeId = typeClassification;
	}

	public String getMediumDescription() {
		return mediumDescription;
	}

	public void setMediumDescription(String mediumDescription) {
		this.mediumDescription = mediumDescription;
	}

	public String getMediumId() {
		return mediumId;
	}

	public void setMediumId(String mediumClassification) {
		this.mediumId = mediumClassification;
	}

	public boolean isSolo() {
		return solo;
	}

	public void setSolo(boolean solo) {
		this.solo = solo;
	}

	public String getMovementId() {
		return movementId;
	}

	public void setMovementId(String movementId) {
		this.movementId = movementId;
	}

	public String getScoreId() {
		return scoreId;
	}

	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}

	public String getMediumScoreDescription() {
		return mediumScoreDescription;
	}

	public void setMediumScoreDescription(String mediumScoreDescription) {
		this.mediumScoreDescription = mediumScoreDescription;
	}

	public boolean isEnsemble() {
		return ensemble;
	}

	public void setEnsemble(boolean ensemble) {
		this.ensemble = ensemble;
	}

	public String getMediumCode() {
		return mediumCode;
	}

	public void setMediumCode(String mediumCode) {
		this.mediumCode = mediumCode;
	}

	
}

