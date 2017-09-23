package de.wwu.wmss.core;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
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
		
		serviceDescription.put("sources", SystemSettings.sourcesList.size());
		serviceDescription.put("type", "ServiceDescriptionReport");
		serviceDescription.put("service", SystemSettings.getService());
		serviceDescription.put("port", SystemSettings.getPort());
		serviceDescription.put("timeout", SystemSettings.getTimeout());
		serviceDescription.put("contact", SystemSettings.getAdmin());
		serviceDescription.put("title", SystemSettings.getTitle());
				
				
		JSONArray dsArray = new JSONArray();
		
		for (int i = 0; i < SystemSettings.sourcesList.size(); i++) {
			
			JSONObject ds = new JSONObject();
			
			ds.put("id", SystemSettings.sourcesList.get(i).getId());
			ds.put("active", SystemSettings.sourcesList.get(i).isActive());
			ds.put("type", SystemSettings.sourcesList.get(i).getType());
			ds.put("storage", SystemSettings.sourcesList.get(i).getStorage());
			ds.put("port", SystemSettings.sourcesList.get(i).getPort());
			ds.put("host", SystemSettings.sourcesList.get(i).getHost());
			ds.put("repository", SystemSettings.sourcesList.get(i).getRepository());
			ds.put("version", SystemSettings.sourcesList.get(i).getVersion());
			ds.put("user", SystemSettings.sourcesList.get(i).getUser());
			//ds.put("password", SystemSettings.sourcesList.get(i).getPassword());
			
			dsArray.add(ds);
		}
		
		
		serviceDescription.put("datasources", dsArray);
		
		return gson.toJson(serviceDescription);
		
	}
	
}
