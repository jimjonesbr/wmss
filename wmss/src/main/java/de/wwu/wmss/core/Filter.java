package de.wwu.wmss.core;

public class Filter {

	private String filter;
	private boolean value;
	public Filter() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public boolean isEnabled() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	};

	
}