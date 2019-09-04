package de.wwu.wmss.core;

import org.neo4j.driver.v1.Driver;

public class DataSource {

	private String id;
	private boolean active;
	private String type;
	private String storage;
	private int port;
	private String host;
	private String repository;
	private String version;
	private String user;
	private String password;
	private String info;
	private String edition;
	private Driver neo4jConnectionDriver;
	private FilterCapability filters;
	
	public DataSource() {

		super();
		this.neo4jConnectionDriver = null;
		this.filters = new FilterCapability();
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStorage() {
		return storage;
	}
	public void setStorage(String storage) {
		this.storage = storage;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public FilterCapability getFilters() {
		return filters;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Driver getNeo4jConnectionDriver() {
		return neo4jConnectionDriver;
	}

	public void setNeo4jConnectionDriver(Driver connectionDriver) {
		this.neo4jConnectionDriver = connectionDriver;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}
	
}
