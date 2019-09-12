package de.wwu.wmss.core;

public class Resource {

	private String resourceURL = "";
	private String resourceLabel = "";
	private String action = "";
	private String resourceType = "";
	
	public Resource() {
		super();
	}
	public Resource(String url, String label) {
		super();
		this.resourceURL = url;
		this.resourceLabel = label;
	}
	public String getUrl() {
		return resourceURL;
	}
	public void setUrl(String url) {
		this.resourceURL = url;
	}
	public String getLabel() {
		return resourceLabel;
	}
	public void setLabel(String label) {
		this.resourceLabel = label;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getType() {
		return resourceType;
	}
	public void setType(String type) {
		this.resourceType = type;
	}
	
}
