package de.wwu.wmss.core;

public class RequestParameter {

	private String request = "";
	private String value ="";
	
	public RequestParameter() {
		super();
	}
	
	public RequestParameter(String request, String value) {
		super();
		this.request = request;
		this.value = value;
	}

	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
