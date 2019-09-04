package de.wwu.wmss.settings;

import java.io.FileReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import de.wwu.music2rdf.converter.MusicXML2RDF;
import de.wwu.wmss.core.DataSource;


public class SystemSettings {
	private static Logger logger = Logger.getLogger("System-Settings");
	private static int port;
	private static String service;
	private static String defaultMelodyEncoding;
	private static String title;
	private static String importdDirectory = "";
	private static int timeout;
	private static String contact;
	private static String defaultProtocol = "";
	private static String startup;
	private static String serviceVersion = "";
	private static int logPreview;
	private static int pageSize;
	private static String defaultRequestMode;
	private static String defaultCommitSize;
	private static String defaultRDFFormat;
	private static String defaultScoreFormat = "";
	public static ArrayList<DataSource> sourceList = new ArrayList<DataSource>();
	public static ArrayList<String> protocolVersions = new ArrayList<String>();
	public static String REQUEST_MODE_FULL = "full";
	public static String REQUEST_MODE_SIMPLIFIED = "simplified";
	public static MusicXML2RDF music2rdf = new MusicXML2RDF();
	
	public static void main(String[] args) {

		loadDataSources();

	}

	public static void loadSystemSettings(){

		JSONParser parser = new JSONParser();

		try {

			protocolVersions.add("1.0");
			Object obj = parser.parse(new FileReader("conf/settings.json"));

			JSONObject jsonObject = (JSONObject) obj;
			SystemSettings settings = new SystemSettings();
			
			try (InputStream is = settings.getClass().getClassLoader().getResourceAsStream("pom.xml")) {
	            
				MavenXpp3Reader reader = new MavenXpp3Reader();
	            org.apache.maven.model.Model model = reader.read(is);
	            serviceVersion = model.getVersion();			
	            
	        } catch (XmlPullParserException e) {
	        	logger.error("Problem reading pom.xml.");
			}
			
			port = Integer.parseInt(jsonObject.get("port").toString());
			pageSize = Integer.parseInt(jsonObject.get("pageSize").toString());
			timeout = Integer.parseInt(jsonObject.get("timeout").toString());
			service = jsonObject.get("service").toString();
			contact = jsonObject.get("contact").toString();
			defaultMelodyEncoding = jsonObject.get("defaultMelodyEncoding").toString();
			defaultScoreFormat = jsonObject.get("defaultScoreFormat").toString();
			title = jsonObject.get("title").toString();
			defaultRequestMode = jsonObject.get("defaultRequestMode").toString();
			defaultProtocol= jsonObject.get("defaultProtocol").toString();
			defaultRDFFormat= jsonObject.get("defaultRDFFormat").toString();
			defaultCommitSize= jsonObject.get("defaultCommitSize").toString();
			logPreview = Integer.parseInt(jsonObject.get("logpreview").toString());
			importdDirectory= jsonObject.get("importDicretory").toString();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			startup = dateFormat.format(date); 

		} catch (Exception e) {

			logger.fatal("Configuration file not found!");
			
		}

	}

	public static void loadDataSources(){

		JSONParser parser = new JSONParser();	

		try {

			Object obj = parser.parse(new FileReader("conf/sources.json"));
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
				ds.setInfo(record.get("info").toString());
				ds.setStorage(record.get("storage").toString());
				ds.setPort(Integer.parseInt(record.get("port").toString()));
				ds.setHost(record.get("host").toString());
				ds.setRepository(record.get("repository").toString());
				ds.setUser(record.get("user").toString());
				ds.setPassword(record.get("password").toString());
				
				JSONObject filters = (JSONObject) record.get("filterCapabilities");
				
				ds.getFilters().setMelody((boolean)filters.get("melody"));
				ds.getFilters().setCollection((boolean)filters.get("collection"));
				ds.getFilters().setPersonRole((boolean)filters.get("personRole"));
				ds.getFilters().setPerformanceMedium((boolean)filters.get("performanceMedium"));
				ds.getFilters().setPerformanceMediumType((boolean)filters.get("performanceMediumType"));
				ds.getFilters().setSolo((boolean)filters.get("solo"));
				ds.getFilters().setTonalityTonic((boolean)filters.get("tonalityTonic"));
				ds.getFilters().setTonalityMode((boolean)filters.get("tonalityMode"));
				ds.getFilters().setTempo((boolean)filters.get("tempo"));
				ds.getFilters().setCreationDateFrom((boolean)filters.get("creationDateFrom"));
				ds.getFilters().setCreationDateTo((boolean)filters.get("creationDateTo"));
				ds.getFilters().setSource((boolean)filters.get("source"));
				ds.getFilters().setIdentifier((boolean)filters.get("identifier"));
				ds.getFilters().setFormat((boolean)filters.get("format"));
			
				
				sourceList.add(ds);            	
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
	
	public static void setPort(int port) {
		SystemSettings.port = port;
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

	public static String getStartup() {
		return startup;
	}

	public static ArrayList<String> getVersion() {
		return protocolVersions;
	}

	public static String getServiceVersion() {
		return serviceVersion;
	}

	public static int getLogPreview() {
		return logPreview;
	}

	public static int getPageSize() {
		return pageSize;
	}

	public static String getDefaultRequestMode() {
		return defaultRequestMode;
	}

	public static String getDefaultMelodyEncoding() {
		return defaultMelodyEncoding;
	}

	public static String getDefaultCommitSize() {
		return defaultCommitSize;
	}

	public static String getDefaultRDFFormat() {
		return defaultRDFFormat;
	}

	public static String getDefaultProtocol() {
		return defaultProtocol;
	}

	public static String getImportdDirectory() {
		return importdDirectory;
	}

	public static String getDefaultScoreFormat() {
		return defaultScoreFormat;
	}
	
		
}