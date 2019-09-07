package de.wwu.wmss.engine;

import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.wwu.wmss.connector.Neo4jConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.WMSSImportRecord;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.core.WMSSRequest;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;

public class DocumentBuilder {

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
		importReport.put("size", fileList.size());
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
		deletedScore.put("score", Neo4jEngine.deleteScore(request, ds));

		return StringEscapeUtils.unescapeJson(gson.toJson(deletedScore));
	
	}
	
	public static String editScore(WMSSRequest request) {
		
		DataSource ds = loadDataSource(request);		
		Neo4jEngine.editScore(request, ds);		
		return StringEscapeUtils.unescapeJson(getScoreList(request));
		
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
		environment.put("heap_size", Util.readableFileSize(Runtime.getRuntime().totalMemory()));
		environment.put("heap_max_size", Util.readableFileSize(Runtime.getRuntime().maxMemory()));
		environment.put("heap_free_size", Util.readableFileSize(Runtime.getRuntime().freeMemory()));		
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
				
				ds.put("edition", SystemSettings.sourceList.get(i).getEdition());
				ds.put("user", SystemSettings.sourceList.get(i).getUser());
				ds.put("filterCapabilities", SystemSettings.sourceList.get(i).getFilters());
				
				if(SystemSettings.sourceList.get(i).getStorage().equals("postgresql")) {
				
					ds.put("collections", PostgreSQLEngine.getCollections(SystemSettings.sourceList.get(i)));
					ds.put("performanceMediums", PostgreSQLEngine.getPerformanceMediumList(SystemSettings.sourceList.get(i)));
					ds.put("tempoMarkings", PostgreSQLEngine.getTempoMarkings(SystemSettings.sourceList.get(i)));
					ds.put("formats", PostgreSQLEngine.getFormats(SystemSettings.sourceList.get(i)));
					ds.put("tonalities", PostgreSQLEngine.getTonalities(SystemSettings.sourceList.get(i)));
					ds.put("creationRange", PostgreSQLEngine.getCreationInterval(SystemSettings.sourceList.get(i)));
					ds.put("roles", PostgreSQLEngine.getRoles(SystemSettings.sourceList.get(i)));
				
				}
				
				if(SystemSettings.sourceList.get(i).getStorage().equals("neo4j")) {
					
					ds.put("totalScores", Neo4jEngine.getScoresCount(SystemSettings.sourceList.get(i)));
					ds.put("collections", Neo4jEngine.getCollections(SystemSettings.sourceList.get(i)));					
					ds.put("performanceMediums", Neo4jEngine.getPerformanceMedium(SystemSettings.sourceList.get(i)));
					ds.put("tempoMarkings", null);
					ds.put("formats", Neo4jEngine.getFormats(SystemSettings.sourceList.get(i)));
					ds.put("tonalities", Neo4jEngine.getTonalities(SystemSettings.sourceList.get(i)));
					ds.put("creationRange", null);					
					ds.put("persons", Neo4jEngine.getRoles(SystemSettings.sourceList.get(i)));
					
					
					StatementResult rs = Neo4jConnector.getInstance().executeQuery("CALL dbms.components() yield name, versions, edition unwind versions as version return name, version, edition;", SystemSettings.sourceList.get(i));

					while ( rs.hasNext() ){
						Record rec = rs.next();
						ds.put("version", rec.get("version").asString());
						ds.put("edition", rec.get("edition").asString());
					}
					
					
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

			JSONArray repositories = new JSONArray();
			DataSource ds = loadDataSource(request);

			if(ds.getType().equals("database")) {

				if(ds.getStorage().equals("postgresql")){
					/**
							listScores = FactoryPostgreSQL.getScoreList(parameterList, SystemSettings.sourceList.get(i));							 
					 */
				}

			}

			if(ds.getType().equals("lpg")) {

				if(ds.getStorage().equals("neo4j")){

					listScores = Neo4jEngine.getScoreList(request, ds);

				}

			}

			JSONObject repo = new JSONObject();
			repo.put("identifier", ds.getId());
			repo.put("host", ds.getHost());
			repo.put("version", ds.getVersion());
			repo.put("type", ds.getType());
			repo.put("storage", ds.getStorage());

			int totalSize;

			if(request.getOffset()==0 && listScores.size() == request.getPageSize()) {						
				totalSize = Neo4jEngine.getResultsetSize(request, ds);
				repo.put("requestSize",totalSize);	
			} else {

				if(request.getOffset()==0) {
					totalSize = listScores.size();
				} else {
					totalSize = request.getTotalSize();
				}

			}

			if(request.getOffset() + listScores.size() < totalSize ) {

				String nextPage = request.getHostname() + ":" +
						SystemSettings.getPort() + "/"+
						SystemSettings.getService()+"?request=ListScores&source="+ds;						
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
				if(!request.getScoreIdentifier().equals("")) {
					nextPage = nextPage + "&identifier="+request.getScoreIdentifier();
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

			repositories.add(repo);


			listScoresJSON.put("datasources", repositories);
			listScoresJSON.put("type", "ScoreListReport");
			listScoresJSON.put("protocolVersion", "1.0");
			listScoresJSON.put("size", repositories.size());

			result = StringEscapeUtils.unescapeJson(gson.toJson(listScoresJSON));

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unexpected error at the ListScores request.");
			logger.error(e.getMessage());
		}

		return result;
	}
			
	public static MusicScore getScore(WMSSRequest request){

		MusicScore score = new MusicScore();
		
		DataSource ds = Util.getDataSource(request.getSource());

		if(ds.getType().equals("triplestore")) {
			//TODO tbw
		} else

			if(ds.getType().equals("postgresql")) {
				//TODO tbw
			} else 

				if(ds.getType().equals("lpg")) {

					if(ds.getStorage().equals("neo4j")) {
						
						score = Neo4jEngine.getScore(request);
						
					}

				}

		return score;

	}
		
}
