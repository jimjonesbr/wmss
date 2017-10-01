package de.wwu.wmss.factory;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.settings.SystemSettings;

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
		serviceDescription.put("version", SystemSettings.getServiceVersion());
		serviceDescription.put("timeout", SystemSettings.getTimeout());
		serviceDescription.put("contact", SystemSettings.getAdmin());
		serviceDescription.put("title", SystemSettings.getTitle());
		serviceDescription.put("startup", SystemSettings.getStartup());
		serviceDescription.put("supportedProtocols", SystemSettings.getVersion());

		JSONObject environment = new JSONObject();


		environment.put("java", System.getProperty("java.version"));
		environment.put("os", System.getProperty("os.name").toString() + " " + 
				System.getProperty("os.version").toString() + " (" + 
				System.getProperty("os.arch").toString()+")");		
		serviceDescription.put("environment", environment);

		JSONArray dsArray = new JSONArray();

		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

			JSONObject ds = new JSONObject();

			ds.put("id", SystemSettings.sourceList.get(i).getId());
			ds.put("active", SystemSettings.sourceList.get(i).isActive());
			ds.put("type", SystemSettings.sourceList.get(i).getType());
			ds.put("storage", SystemSettings.sourceList.get(i).getStorage());
			ds.put("port", SystemSettings.sourceList.get(i).getPort());
			ds.put("host", SystemSettings.sourceList.get(i).getHost());
			ds.put("repository", SystemSettings.sourceList.get(i).getRepository());
			ds.put("version", SystemSettings.sourceList.get(i).getVersion());
			ds.put("user", SystemSettings.sourceList.get(i).getUser());
			ds.put("filterCapabilities", SystemSettings.sourceList.get(i).getFilters());
			dsArray.add(ds);
		}


		serviceDescription.put("datasources", dsArray);

		return gson.toJson(serviceDescription);

	}

	public static String getScoreList(ArrayList<RequestParameter> parameterList){

		ArrayList<MusicScore> listScores = new ArrayList<MusicScore>();
		JSONObject result = new JSONObject();	
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String source = "";

		for (int i = 0; i < parameterList.size(); i++) {

			if(parameterList.get(i).getRequest().equals("source")){

				source = parameterList.get(i).getValue();
			}

		}


		try {

			JSONArray repoArray = new JSONArray();

			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {


				if(source.equals(SystemSettings.sourceList.get(i).getId()) || source.equals("")){

					listScores = FactoryWMSS.getScoreList(parameterList, SystemSettings.sourceList.get(i));

					JSONObject repo = new JSONObject();
					repo.put("identifier", SystemSettings.sourceList.get(i).getId());
					repo.put("host", SystemSettings.sourceList.get(i).getHost());
					repo.put("version", SystemSettings.sourceList.get(i).getVersion());
					repo.put("type", SystemSettings.sourceList.get(i).getType());
					repo.put("storage", SystemSettings.sourceList.get(i).getStorage());
					repo.put("size", listScores.size());
					repo.put("scores", listScores);

					repoArray.add(repo);

				}


			}

			result.put("datasources", repoArray);
			result.put("type", "ScoreListReport");
			result.put("size", repoArray.size());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return gson.toJson(result);

	}

	public static String getScore(ArrayList<RequestParameter> parameterList){
		
		return FactoryWMSS.getScore(parameterList);

	}


}
