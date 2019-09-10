package de.wwu.wmss.core;

import java.util.ArrayList;

public class MovementEditScoreRequest extends Movement{

	private ArrayList<PerformanceMedium> mediums = new ArrayList<PerformanceMedium>();
	private String action = "";
	
	public MovementEditScoreRequest() {
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public ArrayList<PerformanceMedium> getMediums() {
		return mediums;
	}

	
}
