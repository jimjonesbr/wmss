package de.wwu.wmss.core;

public class Resource {

	private String url = "";
	private String label = "";
	private String action = "";
	
	public Resource() {
		super();
	}
	public Resource(String url, String label) {
		super();
		this.url = url;
		this.label = label;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
