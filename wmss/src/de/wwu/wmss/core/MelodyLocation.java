package de.wwu.wmss.core;

public class MelodyLocation extends Movement {

	private String startingMeasure;
	private String voice;
	private String staff;
	private String instrumentName;
	private String melody;

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
	
	
}

