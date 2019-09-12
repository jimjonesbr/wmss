package de.wwu.wmss.core;

public class Format {

	private String formatIdentifier;
	private String formatLabel;
	private String scoreId;
	
	public Format() {
		super();
	}
	public String getFormatId() {
		return formatIdentifier;
	}
	public void setFormatId(String formatId) {
		this.formatIdentifier = formatId;
	}
	public String getFormatDescription() {
		return formatLabel;
	}
	public void setFormatDescription(String formatDescription) {
		this.formatLabel = formatDescription;
	}
	public String getScoreId() {
		return scoreId;
	}
	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}

	
	
}
