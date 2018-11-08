package de.wwu.wmss.factory;

import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.WMSSImportRecord;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.core.WMSSRequest;
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

		return StringEscapeUtils.unescapeJson(gson.toJson(exceptionReport));

	}
	
	@SuppressWarnings("unchecked")
	public static String getImportReport(ArrayList<WMSSImportRecord> fileList, WMSSImportRequest importRequest){

		JSONObject importReport = new JSONObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		importReport.put("type", "ImportReport");
		importReport.put("timeElapsed", Util.timeElapsed(importRequest.getStartDate(), new Date()));		
		importReport.put("files", fileList);

		return StringEscapeUtils.unescapeJson(gson.toJson(importReport));

	}
	
	@SuppressWarnings("unchecked")
	public static String deleteScore(WMSSRequest request) {
		
		JSONObject deletedScore = new JSONObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		DataSource ds = loadDataSource(request);
		
		deletedScore.put("type", "DeleteScoresReport");
		deletedScore.put("score", FactoryNeo4j.deleteScore(request, ds));

		return StringEscapeUtils.unescapeJson(gson.toJson(deletedScore));
	
	}

	
	private static DataSource loadDataSource(WMSSRequest request) {
	
		DataSource result = new DataSource();
		
		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
			if(request.getSource().equals(SystemSettings.sourceList.get(i).getId())) {
				result = SystemSettings.sourceList.get(i);
			}
		}
				
		return result;
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
		serviceDescription.put("pageSize", SystemSettings.getPageSize());
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
					
					ds.put("totalScores", FactoryNeo4j.getScoresCount(SystemSettings.sourceList.get(i)));
					ds.put("collections", FactoryNeo4j.getCollections(SystemSettings.sourceList.get(i)));					
					ds.put("performanceMediums", FactoryNeo4j.getPerformanceMedium(SystemSettings.sourceList.get(i)));
					ds.put("tempoMarkings", null);
					ds.put("formats", FactoryNeo4j.getFormats(SystemSettings.sourceList.get(i)));
					ds.put("tonalities", FactoryNeo4j.getTonalities(SystemSettings.sourceList.get(i)));
					ds.put("creationRange", null);					
					ds.put("persons", FactoryNeo4j.getRoles(SystemSettings.sourceList.get(i)));
					
				}
								
				dsArray.add(ds);
			}
			
		}

		
		serviceDescription.put("datasources", dsArray);

		return StringEscapeUtils.unescapeJson(gson.toJson(serviceDescription));

	}
	
	@SuppressWarnings("unchecked")
	public static String getScoreList(WMSSRequest request){

		ArrayList<MusicScore> listScores = new ArrayList<MusicScore>();
		JSONObject listScoresJSON = new JSONObject();
		String result = "";
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		try {

			JSONArray repoArray = new JSONArray();


			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

				if(request.getSource().equals(SystemSettings.sourceList.get(i).getId()) || request.getSource().equals("")){

					if(SystemSettings.sourceList.get(i).getType().equals("database")) {

						if(SystemSettings.sourceList.get(i).getStorage().equals("postgresql")){
							/**
							listScores = FactoryPostgreSQL.getScoreList(parameterList, SystemSettings.sourceList.get(i));							 
							 */
						}

					}

					if(SystemSettings.sourceList.get(i).getType().equals("lpg")) {

						if(SystemSettings.sourceList.get(i).getStorage().equals("neo4j")){

							listScores = FactoryNeo4j.getScoreList(request, SystemSettings.sourceList.get(i));

						}

					}

					JSONObject repo = new JSONObject();
					repo.put("identifier", SystemSettings.sourceList.get(i).getId());
					repo.put("host", SystemSettings.sourceList.get(i).getHost());
					repo.put("version", SystemSettings.sourceList.get(i).getVersion());
					repo.put("type", SystemSettings.sourceList.get(i).getType());
					repo.put("storage", SystemSettings.sourceList.get(i).getStorage());
									
					int totalSize;
					
					if(request.getOffset()==0) {
						totalSize = FactoryNeo4j.getResultsetSize(request, SystemSettings.sourceList.get(i));
						repo.put("requestSize",totalSize);	
					} else {
						totalSize = request.getTotalSize();
					}
					
					if(request.getOffset() + listScores.size() < totalSize ) {
						
						String nextPage = request.getHostname() + ":" +
										  SystemSettings.getPort() + "/"+
						                  SystemSettings.getService()+"?request=ListScores&source="+SystemSettings.sourceList.get(i).getId();						
						if(!request.getMelody().equals("")) {
							nextPage = nextPage + "&melody="+request.getMelody();
						}						
						if(!request.getFormat().equals("")) {
							nextPage = nextPage + "&format="+request.getFormat();
						}												
						if(!request.getCollection().equals("")) {
							nextPage = nextPage + "&collection="+request.getCollection();
						}												
						if(!request.getPerson().equals("")) {
							nextPage = nextPage + "&person="+request.getPerson();
						}												
						if(!request.getPersonRole().equals("")) {
							nextPage = nextPage + "&personRole="+request.getPersonRole();
						}												
						if(!request.getPerformanceMedium().equals("")) {
							nextPage = nextPage + "&performanceMedium="+request.getPerformanceMedium();
						}												
						if(!request.getPerformanceMediumType().equals("")) {
							nextPage = nextPage + "&performanceMediumType="+request.getPerformanceMediumType();
						}												
						if(request.isSolo()) {
							nextPage = nextPage + "&solo="+request.isSolo();
						}												
						if(!request.getTonalityTonic().equals("")) {
							nextPage = nextPage + "&tonalityTonic="+request.getTonalityTonic();
						}
						if(!request.getTonalityMode().equals("")) {
							nextPage = nextPage + "&tonalityMode="+request.getTonalityMode();
						}						
						if(!request.getTempoBeatUnit().equals("")) {
							nextPage = nextPage + "&tempo="+request.getTempoBeatUnit();
						}						
						if(!request.getDateIssued().equals("")) {
							nextPage = nextPage + "&creationDate="+request.getDateIssued();
						}					
						if(!request.getIdentifier().equals("")) {
							nextPage = nextPage + "&identifier="+request.getIdentifier();
						}						
						if(!request.getProtocolVersion().equals("")) {
							nextPage = nextPage + "&version="+request.getProtocolVersion();
						}						
						if(!request.isIgnoreChords()) {
							nextPage = nextPage + "&ignoreChords="+request.isIgnoreChords();
						}						
						if(request.isEnsemble()) {
							nextPage = nextPage + "&ensemble="+request.isEnsemble();
						}			
						if(request.isIgnoreOctaves()) {
							nextPage = nextPage + "&ignoreOctaves="+request.isIgnoreOctaves();
						}
						if(request.isIgnorePitch()) {
							nextPage = nextPage + "&ignorePitch="+request.isIgnorePitch();
						}
						if(request.isIgnoreDuration()) {
							nextPage = nextPage + "&ignoreDuration="+request.isIgnoreDuration();
						}
						if(!request.getRequestMode().equals("")) {
							nextPage = nextPage + "&requestMode="+request.getRequestMode();
						}
						nextPage = nextPage + "&offset=" + (request.getOffset() + listScores.size());
						nextPage = nextPage + "&pageSize=" + request.getPageSize();
						nextPage = nextPage + "&totalSize=" + totalSize;
						repo.put("nextPage", nextPage);	
					}
					
					repo.put("offset", request.getOffset());
					repo.put("pageSize", listScores.size());
					repo.put("totalSize", totalSize);					
					repo.put("scores", listScores);

					repoArray.add(repo);

				}
			}

			listScoresJSON.put("datasources", repoArray);
			listScoresJSON.put("type", "ScoreListReport");
			listScoresJSON.put("protocolVersion", "1.0");
			listScoresJSON.put("size", repoArray.size());

			result = StringEscapeUtils.unescapeJson(gson.toJson(listScoresJSON));

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unexpected error at the ListScores request.");
			logger.error(e.getMessage());
		}

		return result;
	}
			
	public static String getScore(WMSSRequest request){

		String result = "";
		DataSource ds = Util.getDataSource(request.getSource());

		if(ds.getType().equals("triplestore")) {

			//TODO tbw

		} else

			if(ds.getType().equals("postgresql")) {

				//TODO tbw

			} else 

				if(ds.getType().equals("lpg")) {

					if(ds.getStorage().equals("neo4j")) {
						
						result = FactoryNeo4j.getMusicXML(request);
						
					}

				}

		return result;

	}
		
}
