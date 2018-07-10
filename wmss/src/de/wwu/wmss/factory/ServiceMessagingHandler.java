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
import de.wwu.wmss.settings.Util;

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
			
			if(SystemSettings.sourceList.get(i).isActive()) {
			
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
				
				if(SystemSettings.sourceList.get(i).getStorage().equals("postgresql")) {
				
					ds.put("collections", FactoryPostgreSQL.getCollections(SystemSettings.sourceList.get(i)));
					ds.put("performanceMediums", FactoryPostgreSQL.getPerformanceMediumList(SystemSettings.sourceList.get(i)));
					ds.put("tempoMarkings", FactoryPostgreSQL.getTempoMarkings(SystemSettings.sourceList.get(i)));
					ds.put("formats", FactoryPostgreSQL.getFormats(SystemSettings.sourceList.get(i)));
					ds.put("tonalities", FactoryPostgreSQL.getTonalities(SystemSettings.sourceList.get(i)));
					ds.put("creationRange", FactoryPostgreSQL.getCreationInterval(SystemSettings.sourceList.get(i)));
					ds.put("roles", FactoryPostgreSQL.getRoles(SystemSettings.sourceList.get(i)));
				
				}
				
				if(SystemSettings.sourceList.get(i).getStorage().equals("neo4j")) {
					
					ds.put("collections", FactoryNeo4j.getCollections(SystemSettings.sourceList.get(i)));					
					ds.put("performanceMediums", FactoryNeo4j.getPerformanceMedium(SystemSettings.sourceList.get(i)));
					ds.put("tempoMarkings", null);
					ds.put("formats", FactoryNeo4j.getFormats(SystemSettings.sourceList.get(i)));
					ds.put("tonalities", FactoryNeo4j.getTonalities(SystemSettings.sourceList.get(i)));
					ds.put("creationRange", null);
					ds.put("roles", FactoryNeo4j.getRoles(SystemSettings.sourceList.get(i)));
					
				}
				
				dsArray.add(ds);
			}
			
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
						
						//System.out.println("SystemSettings.sourceList.get(i).getId() >"+SystemSettings.sourceList.get(i).getType());
						//if(SystemSettings.sourceList.get(i).getId().equals(anObject))
						
						if(SystemSettings.sourceList.get(i).getType().equals("database")) {
							
							if(SystemSettings.sourceList.get(i).getStorage().equals("postgresql")){
								
								listScores = FactoryPostgreSQL.getScoreList(parameterList, SystemSettings.sourceList.get(i));
							}
							
						}
						
						if(SystemSettings.sourceList.get(i).getType().equals("lpg")) {

							if(SystemSettings.sourceList.get(i).getStorage().equals("neo4j")){
								
								listScores = FactoryNeo4j.getScoreList(parameterList, SystemSettings.sourceList.get(i));
							}

						}
						
//						listScores = FactoryPostgreSQL.getScoreList(parameterList, SystemSettings.sourceList.get(i));

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

				
				//byte[] utf8JsonString = result.getBytes("ASCII");				
				//String str = new String(utf8JsonString, StandardCharsets.US_ASCII);
				//System.out.println(str);
				//responseToClient.write(utf8JsonString, 0, utf8JsonString.Length);


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

	public static String getScore(ArrayList<RequestParameter> parameters){

		String result = "";
		DataSource ds = Util.getDataSource(parameters);

		if(ds.getType().equals("triplestore")) {

			result = FactoryTripleStore.getScore(parameters);

		} else

			if(ds.getType().equals("postgresql")) {

				result = FactoryPostgreSQL.getScore(parameters);

			} else 

				if(ds.getType().equals("lpg")) {

					if(ds.getStorage().equals("neo4j")) {
						
						result = FactoryNeo4j.getMusicXML(parameters);
						
					}

				}



		return result;

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

			//System.out.println(prm.get(i).getRequest() + " > " + prm.get(i).getValue());
			
		}

		return result;

	}
	
}
