package de.wwu.wmss.core;

public class PerformanceMedium {

	private String mediumTypeLabel="";
	private String mediumTypeIdentifier="";
	private String mediumLabel="";
	private String mediumIdentifier="";
	private String mediumCode="";
	private String mediumScoreLabel="";	
	private String movementId;
	private String scoreId;
	private boolean solo;
	private boolean ensemble;
	private String action;
	
	public PerformanceMedium() {
		super();
	}

	public String getTypeLabel() {
		return mediumTypeLabel;
	}

	public void setTypeLabel(String typeDescription) {
		this.mediumTypeLabel = typeDescription;
	}

	public String getTypeIdentifier() {
		return mediumTypeIdentifier;
	}

	public void setTypeIdentifier(String typeClassification) {
		this.mediumTypeIdentifier = typeClassification;
	}

	public String getLabel() {
		return mediumLabel;
	}

	public void setLabel(String mediumDescription) {
		this.mediumLabel = mediumDescription;
	}

	public String getIdentifier() {
		return mediumIdentifier;
	}

	public void setIdentifier(String mediumClassification) {
		this.mediumIdentifier = mediumClassification;
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

	public String getScoreLabel() {
		return mediumScoreLabel;
	}

	public void setScoreLabel(String mediumScoreDescription) {
		this.mediumScoreLabel = mediumScoreDescription;
	}

	public boolean isEnsemble() {
		return ensemble;
	}

	public void setEnsemble(boolean ensemble) {
		this.ensemble = ensemble;
	}

	public String getCode() {
		return mediumCode;
	}

	public void setCode(String mediumCode) {
		this.mediumCode = mediumCode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}

