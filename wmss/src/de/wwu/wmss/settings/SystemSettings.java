package de.wwu.wmss.settings;

import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import de.wwu.wmss.core.DataSource;

public class SystemSettings {

	private static int port;
	private static String service;
	private static String title;
	private static int timeout;
	private static String contact;
	private static String defaultProtocol;
	private static String startup;
	private static String serviceVersion;
	private static int logPreview;
	private static Logger logger = Logger.getLogger("System Settings");
	public static ArrayList<DataSource> sourceList = new ArrayList<DataSource>();
	public static ArrayList<String> protocolVersions = new ArrayList<String>();

	public static void main(String[] args) {

		loadDataSources();

	}

	public static void loadSystemSettings(){

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader("config/settings.wmss"));

			JSONObject jsonObject = (JSONObject) obj;

			protocolVersions.add("1.0");
			protocolVersions.add("1.1");
			//TODO Create versioning system
			serviceVersion = "Dev-0.0.1";

			port = Integer.parseInt(jsonObject.get("port").toString());
			timeout = Integer.parseInt(jsonObject.get("timeout").toString());
			service = jsonObject.get("service").toString();
			contact = jsonObject.get("contact").toString();
			title = jsonObject.get("title").toString();
			defaultProtocol= jsonObject.get("defaultProtocol").toString();
			logPreview= Integer.parseInt(jsonObject.get("logpreview").toString());

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			startup = dateFormat.format(date); 

			logger.info("\n\n" + title + "\n" +
					"Service Name: " + service + "\n" +
					"Default Protocol: " + defaultProtocol + "\n" +
					"WMSS Version: " + serviceVersion + "\n" +
					"Port: " + port + "\n" +
					"Application Startup: " + startup + "\n" +

					"Time-out: " + timeout + "ms" + "\n" +
					"System Administrator: " + contact  + "\n");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static void loadDataSources(){

		JSONParser parser = new JSONParser();	

		try {

			Object obj = parser.parse(new FileReader("config/sources.wmss"));
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
				ds.setVersion(record.get("version").toString());
				ds.setUser(record.get("user").toString());
				ds.setPassword(record.get("password").toString());

				JSONObject filters = (JSONObject) record.get("filterCapabilities");
				
				ds.getFilters().setMelody((boolean)filters.get("melody"));
				ds.getFilters().setGroup((boolean)filters.get("group"));
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


}

