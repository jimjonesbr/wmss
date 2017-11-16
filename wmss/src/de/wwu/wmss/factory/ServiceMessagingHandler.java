package de.wwu.wmss.factory;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.Filter;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.settings.SystemSettings;

public class ServiceMessagingHandler {

	private static Logger logger = Logger.getLogger("ServiceExceptionHandler");

	@SuppressWarnings("unchecked")
	public static String getServiceExceptionReport(String errorCode, String errorMessage, String hint){


		JSONObject exceptionReport = new JSONObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		exceptionReport.put("type", "ExceptionReport");
		exceptionReport.put("code", errorCode);
		exceptionReport.put("message", errorMessage);
		exceptionReport.put("hint", hint);

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
		serviceDescription.put("appVersion", SystemSettings.getServiceVersion());
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
			ds.put("info", SystemSettings.sourceList.get(i).getInfo());
			ds.put("active", SystemSettings.sourceList.get(i).isActive());
			ds.put("type", SystemSettings.sourceList.get(i).getType());
			ds.put("storage", SystemSettings.sourceList.get(i).getStorage());
			ds.put("port", SystemSettings.sourceList.get(i).getPort());
			ds.put("host", SystemSettings.sourceList.get(i).getHost());
			ds.put("repository", SystemSettings.sourceList.get(i).getRepository());
			ds.put("version", SystemSettings.sourceList.get(i).getVersion());
			ds.put("user", SystemSettings.sourceList.get(i).getUser());
			ds.put("filterCapabilities", SystemSettings.sourceList.get(i).getFilters());
			ds.put("collections", FactoryWMSS.getCollections(SystemSettings.sourceList.get(i)));
			ds.put("performanceMediums", FactoryWMSS.getPerformanceMediumList(SystemSettings.sourceList.get(i)));
			ds.put("tempoMarkings", FactoryWMSS.getTempoMarkings(SystemSettings.sourceList.get(i)));
			ds.put("formats", FactoryWMSS.getFormats(SystemSettings.sourceList.get(i)));
			ds.put("tonalities", FactoryWMSS.getTonalities(SystemSettings.sourceList.get(i)));
			ds.put("creationRange", FactoryWMSS.getCreationInterval(SystemSettings.sourceList.get(i)));
			ds.put("roles", FactoryWMSS.getRoles(SystemSettings.sourceList.get(i)));
			dsArray.add(ds);
		}


		serviceDescription.put("datasources", dsArray);

		return gson.toJson(serviceDescription);

	}

	@SuppressWarnings("unchecked")
	public static String getScoreList(ArrayList<RequestParameter> parameterList){

		ArrayList<MusicScore> listScores = new ArrayList<MusicScore>();
		JSONObject listScoresJSON = new JSONObject();
		String result = "";
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String source = "";
		Filter fil = new Filter();
		boolean isGlobalRequest = true;

		for (int k = 0; k < parameterList.size(); k++) {

			if(parameterList.get(k).getRequest().equals("source")){

				isGlobalRequest = false;
				
				if(parameterList.get(k).getValue().equals("")){

					result = getServiceExceptionReport("E0009", "Invalid data source (empty)","Either provide a valid data source or remove the 'source' parameter to list scores from all active data sources.");

				} else {

					source = parameterList.get(k).getValue();

				}

			}

		}

		

		if(!source.equals("")){

			for (int j = 0; j < SystemSettings.sourceList.size(); j++) {

				if(source.equals(SystemSettings.sourceList.get(j).getId())){

					fil = filtersSupported(SystemSettings.sourceList.get(j), parameterList);

				}


			}

		} 
		

		if(fil.isEnabled() || isGlobalRequest){

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


				listScoresJSON.put("datasources", repoArray);
				listScoresJSON.put("type", "ScoreListReport");
				listScoresJSON.put("protocolVersion", "1.0");
				listScoresJSON.put("size", repoArray.size());

				result = gson.toJson(listScoresJSON);


			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Unexpected error at the ListScores request.");
				logger.error(e.getMessage());
			}
			
		} else {
			
			result = getServiceExceptionReport("E0011", "Unsupported filter [" + fil.getFilter() + "]","Check the 'Service Description Report' for more information on which filters are enabled for the data source '"+source+"'.");
		}

		return result;

	}

	public static String getScore(ArrayList<RequestParameter> parameterList){

		return FactoryWMSS.getScore(parameterList);

	}

	private static Filter filtersSupported(DataSource ds, ArrayList<RequestParameter> prm){

		Filter result = new Filter();
		result.setValue(true);

		for (int i = 0; i < prm.size(); i++) {

			if(prm.get(i).getRequest().equals("melody") && !ds.getFilters().isMelodyEnabled()) {
				
				result.setFilter("melody");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("group") && !ds.getFilters().isGroupEnabled() )	{
			
				result.setFilter("group");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("personrole") && !ds.getFilters().isPersonRoleEnabled() )	{
				
				result.setFilter("personRole");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("performancemedium") && !ds.getFilters().isPerformanceMediumEnabled() )	{
				
				result.setFilter("performanceMedium");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("performancemediumtype") && !ds.getFilters().isPerformanceMediumTypeEnabled() )	{
			
				result.setFilter("performanceMediumType");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("solo") && !ds.getFilters().isSoloEnabled())	{
				
				result.setFilter("solo");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("tonalitytonic") && !ds.getFilters().isTonalityTonicEnabled() )	{
				
				result.setFilter("tonalityTonic");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("tonalitymode") && !ds.getFilters().isTonalityModeEnabled() )	{
			
				result.setFilter("tonalityMode");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("tempo") && !ds.getFilters().isTempoEnabled() )	{
				
				result.setFilter("tempo");
				result.setValue(false);
				
			}
						
			if(prm.get(i).getRequest().equals("creationdatefrom") && !ds.getFilters().isCreationDateFromEnabled() )	{
			
				result.setFilter("creationDateFrom");
				result.setValue(false);
			}
			
			if(prm.get(i).getRequest().equals("creationdateto") && !ds.getFilters().isCreationDateToEnabled() )	{
				
				result.setFilter("creationDateTo");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("source") && !ds.getFilters().isSourceEnabled() )	{
				
				result.setFilter("source");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("identifier") && !ds.getFilters().isIdentifierEnabled() )	{
				
				result.setFilter("identifier");
				result.setValue(false);
				
			}
			
			if(prm.get(i).getRequest().equals("format") && !ds.getFilters().isFormatEnabled() )	{
				
				result.setFilter("format");
				result.setValue(false);
				
			}

			System.out.println(prm.get(i).getRequest() + " > " + prm.get(i).getValue());
			
		}

		return result;

	}
	
}
