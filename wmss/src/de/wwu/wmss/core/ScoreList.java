package de.wwu.wmss.core;

import java.util.ArrayList;

public class ScoreList {

	private String sourceId;
	private String sourceDescription;
	private ArrayList<ArrayList<MusicScore>> scoreLists;
	
	public ScoreList() {
		super();
		scoreLists = new ArrayList<ArrayList<MusicScore>>(); 

	}

	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceDescription() {
		return sourceDescription;
	}
	public void setSourceDescription(String sourceDescription) {
		this.sourceDescription = sourceDescription;
	}
	public ArrayList<ArrayList<MusicScore>> getScoreLists() {
		return scoreLists;
	}
	
	
}
