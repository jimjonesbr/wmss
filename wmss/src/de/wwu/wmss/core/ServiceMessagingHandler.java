package de.wwu.wmss.core;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServiceMessagingHandler {

	private static Logger logger = Logger.getLogger("ServiceExceptionHandler");
	
	@SuppressWarnings("unchecked")
	public static String getServiceExceptionReport(String errorCode, String errorMessage){
		
		
		JSONObject exceptionReport = new JSONObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		exceptionReport.put("type", "ExceptionReport");
		exceptionReport.put("code", errorCode);
		exceptionReport.put("message", errorMessage);
		
        logger.error(errorCode + ": " + errorMessage);
        
        return gson.toJson(exceptionReport);

	}
	
	@SuppressWarnings("unchecked")
	public static String getServiceDescription(){
		
		JSONObject serviceDescription = new JSONObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		serviceDescription.put("type", "ServiceDescriptionReport");
		serviceDescription.put("service", SystemSettings.getService());
		serviceDescription.put("port", SystemSettings.getPort());
		serviceDescription.put("timeout", SystemSettings.getTimeout());
		serviceDescription.put("admin", SystemSettings.getAdmin());
		
		return gson.toJson(serviceDescription);
		
	}
	
}
