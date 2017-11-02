package de.wwu.wmss.core;

public class MelodyLocation extends Movement {

	String startingMeasure;
	String voice;
	String staff;

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
	
	
}

