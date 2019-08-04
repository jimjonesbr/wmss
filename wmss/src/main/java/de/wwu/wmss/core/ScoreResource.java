package de.wwu.wmss.core;

public class ScoreResource {

	private String url;
	private String description;
	private String type;
	
	public ScoreResource() {
		super();

	}
	public String getId() {
		return url;
	}
	public void setId(String id) {
		this.url = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
