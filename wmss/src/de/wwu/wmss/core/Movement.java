package de.wwu.wmss.core;

import java.util.ArrayList;

public class Movement {

	private int identifier;
	private String title;	
	private String tempo;
	
	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}


	private ArrayList<PerformanceMedium> performanceMediumList;
	
	public Movement() {
		super();
		this.performanceMediumList = new ArrayList<PerformanceMedium>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public ArrayList<PerformanceMedium> getPerformanceMediumList() {
		return performanceMediumList;
	}

	public void setPerformanceMediumList(ArrayList<PerformanceMedium> performanceMediumList) {
		this.performanceMediumList = performanceMediumList;
	}



	
}
