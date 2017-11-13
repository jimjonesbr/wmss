package de.wwu.wmss.core;

import java.util.ArrayList;

public class MelodyLocationGroup {
	
	ArrayList<MelodyLocation> melodyLocation = new ArrayList<MelodyLocation>();
	String movementId;
	String movementName;
	
	public MelodyLocationGroup() {
		super();
		this.melodyLocation = new ArrayList<MelodyLocation>();
	}

	public String getMovementId() {
		return movementId;
	}

	public void setMovementId(String movementId) {
		this.movementId = movementId;
	}

	public String getMovementName() {
		return movementName;
	}

	public void setMovementName(String movementName) {
		this.movementName = movementName;
	}

	public ArrayList<MelodyLocation> getMelodyLocation() {
		return melodyLocation;
	}
	
	

}
