package de.wwu.wmss.core;

import java.io.FileReader;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SystemSettings {

	private static int port;
	private static String service;
	private static String title;
	private static int timeout;
	private static String contact;

	private static Logger logger = Logger.getLogger("System Settings");
	public static ArrayList<DataSource> sourcesList = new ArrayList<DataSource>();

	public static void main(String[] args) {

		loadDataSources();

	}

	public static void loadSystemSettings(){

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader("config/settings.json"));

			JSONObject jsonObject = (JSONObject) obj;

			port = Integer.parseInt(jsonObject.get("port").toString());
			timeout = Integer.parseInt(jsonObject.get("timeout").toString());
			service = jsonObject.get("service").toString();
			contact = jsonObject.get("contact").toString();
			title = jsonObject.get("title").toString();

			logger.info("Title: " + title);
			logger.info("Port: " + port);
			logger.info("Service Name: " + service);
			logger.info("Time-out: " + timeout + "ms");
			logger.info("System Administrator: " + contact);


		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void loadDataSources(){

		JSONParser parser = new JSONParser();	

		try {

			Object obj = parser.parse(new FileReader("config/sources.json"));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray sourcesArray = (JSONArray) jsonObject.get("datasource");

			for (int i = 0; i < sourcesArray.size(); i++) {

				DataSource ds = new DataSource();
				JSONObject record = (JSONObject) sourcesArray.get(i);

				ds.setId(record.get("id").toString());

				if (record.get("active").toString().toLowerCase().equals("true")){
					ds.setActive(true);
				} else {
					ds.setActive(false);
				}

				ds.setType(record.get("type").toString());
				ds.setStorage(record.get("storage").toString());
				ds.setPort(Integer.parseInt(record.get("port").toString()));
				ds.setHost(record.get("host").toString());
				ds.setRepository(record.get("repository").toString());
				ds.setVersion(record.get("version").toString());
				ds.setUser(record.get("user").toString());
				ds.setPassword(record.get("password").toString());

				sourcesList.add(ds);            	
			}


		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	public SystemSettings() {
		super();

	}

	public static int getPort() {
		return port;
	}

	public static String getService() {
		return service;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static String getAdmin() {
		return contact;
	}

	public static String getTitle() {
		return title;
	}

	public static String getContact() {
		return contact;
	}


}

