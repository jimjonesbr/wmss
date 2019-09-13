package de.wwu.wmss.core;

import java.util.ArrayList;

public class Movement {

	private String movementIdentifier;
	private String movementLabel = "";	
	private String beatUnit = "";
	private int beatsPerMinute = 0;
	private int movementOrder = 0;
	private String action = "";
	private ArrayList<PerformanceMedium> mediums2 = new ArrayList<PerformanceMedium>();
	private ArrayList<PerformanceMediumType> mediumTypes = new ArrayList<PerformanceMediumType>();

	public Movement() {
		super();
	}

	public String getIdentifier() {
		return movementIdentifier;
	}

	public void setIdentifier(String identifier) {
		this.movementIdentifier = identifier;
	}

	public String getLabel() {
		return movementLabel;
	}

	public void setLabel(String movementName) {
		this.movementLabel = movementName;
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

	public int getOrder() {
		return movementOrder;
	}

	public void setOrder(int order) {
		this.movementOrder = order;
	}

	public ArrayList<PerformanceMedium> getMediums() {
		return mediums2;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}	
	
	public ArrayList<PerformanceMediumType> getMediumTypes() {
		return mediumTypes;
	}
}
