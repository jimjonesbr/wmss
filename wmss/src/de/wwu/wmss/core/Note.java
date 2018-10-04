package de.wwu.wmss.core;

public class Note {

	private String pitch;
	private String duration;
	private String octave;
	private String accidental;
	private boolean isChord;
	
	public Note() {
		super();
	}
	public String getPitch() {
		return pitch;
	}
	public void setPitch(String pitch) {
		this.pitch = pitch;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getOctave() {
		return octave;
	}
	public void setOctave(String octave) {
		this.octave = octave;
	}
	public boolean isChord() {
		return isChord;
	}
	public void setChord(boolean chord) {
		this.isChord = chord;
	}
	public String getAccidental() {
		return accidental;
	}
	public void setAccidental(String accidental) {
		this.accidental = accidental;
	}
	
}
