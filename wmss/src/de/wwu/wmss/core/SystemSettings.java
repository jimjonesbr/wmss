package de.wwu.wmss.core;

import java.io.FileReader;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SystemSettings {

	private static int port;
	private static String service;
	private static int timeout;
	private static String admin;
	private static Logger logger = Logger.getLogger("System Settings");
	
	public static void loadSystemSettings(){

		JSONParser parser = new JSONParser();
				
		try {

			Object obj = parser.parse(new FileReader("config/settings.json"));

			JSONObject jsonObject = (JSONObject) obj;

			port = Integer.parseInt(jsonObject.get("port").toString());
			timeout = Integer.parseInt(jsonObject.get("timeout").toString());
			service = jsonObject.get("service").toString();
			admin = jsonObject.get("admin").toString();
			
			logger.info("Port: " + port);
			logger.info("Service Name: " + service);
			logger.info("Time-out: " + timeout + "ms");
			logger.info("System Administrator: " + admin);
			
		} catch (Exception e) {
			
			logger.error("Error initializing system variables.");
			e.printStackTrace();
			
		}

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
		return admin;
	}
	

}

