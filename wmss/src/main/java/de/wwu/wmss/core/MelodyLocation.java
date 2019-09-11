package de.wwu.wmss.core;

public class MelodyLocation {

	private String startingMeasure;
	private String voice;
	private String staff;
	private String instrumentName;
	private String melody;
	private String movementIdentifier;
	private String movementName;	
	private String movementId;
	private String scoreId;
	private String order;
	
	public MelodyLocation() {
		super();
	}

	public String getStartingMeasure() {
		return startingMeasure;
	}

	public void setStartingMeasure(String startingMeasure) {
		this.startingMeasure = startingMeasure;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getMelody() {
		return melody;
	}

	public void setMelody(String query) {
		this.melody = query;
	}

	public String getMovementIdentifier() {
		return movementIdentifier;
	}

	public void setMovementIdentifier(String movementIdentifier) {
		this.movementIdentifier = movementIdentifier;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
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

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	
	
	
}

