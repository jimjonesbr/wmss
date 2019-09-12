package de.wwu.wmss.core;

import java.util.ArrayList;

public class MelodyLocationGroup {
	
	private ArrayList<MelodyLocation> melodyLocation = new ArrayList<MelodyLocation>();
	private String movementIdentifier;
	private String movementLabel;
	private int movementOrder;
	private String scoreId;
	
	public MelodyLocationGroup() {
		super();
		this.melodyLocation = new ArrayList<MelodyLocation>();
	}
	public String getMovementId() {
		return movementIdentifier;
	}
	public void setMovementId(String movementId) {
		this.movementIdentifier = movementId;
	}
	public String getMovementName() {
		return movementLabel;
	}
	public void setMovementName(String movementName) {
		this.movementLabel = movementName;
	}
	public ArrayList<MelodyLocation> getMelodyLocation() {
		return melodyLocation;
	}
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}
	public int getOrder() {
		return movementOrder;
	}
	public void setOrder(int order) {
		this.movementOrder = order;
	}

}
