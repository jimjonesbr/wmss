package de.wwu.wmss.core;

public class Note {

	private String pitch;
	private String duration;
	private String octave;
	private String accidental;
	private boolean isChord;
	private boolean isGrace;
	private int measure;
	private String key ="";
	private String time = "";
	private String clef = "";
	private int dotted = 0;
	
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
	public int getMeasure() {
		return measure;
	}
	public void setMeasure(int measure) {
		this.measure = measure;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getClef() {
		return clef;
	}
	public void setClef(String clef) {
		this.clef = clef;
	}
	public int getDotted() {
		return dotted;
	}
	public void setDotted(int dotted) {
		this.dotted = dotted;
	}
	public boolean isGrace() {
		return isGrace;
	}
	public void setGrace(boolean isGrace) {
		this.isGrace = isGrace;
	}	
}
