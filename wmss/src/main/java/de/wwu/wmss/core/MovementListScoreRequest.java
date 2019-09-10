package de.wwu.wmss.core;

import java.util.ArrayList;

public class MovementListScoreRequest extends Movement {

	private ArrayList<PerformanceMediumType> performanceMediumList;
	
	public MovementListScoreRequest() {
		super();
		this.performanceMediumList = new ArrayList<PerformanceMediumType>();
	}
	
	public ArrayList<PerformanceMediumType> getPerformanceMediumList() {
		return performanceMediumList;
	}


	
}
