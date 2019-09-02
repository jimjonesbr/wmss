package de.wwu.wmss.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import de.wwu.wmss.engine.DocumentBuilder;
import de.wwu.wmss.exceptions.InvalidClefException;
import de.wwu.wmss.exceptions.InvalidKeyException;
import de.wwu.wmss.exceptions.InvalidMelodyException;
import de.wwu.wmss.exceptions.InvalidTimeSignatureException;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;

public class WMSSRequest {
		
	private String requestType = "";
	private String requestMode = SystemSettings.getDefaultRequestMode();
	private String format = "";
	private String collection = "";
	private String personIdentifier = "";
	private String personName = "";
	private String personRole = "";		
	private String performanceMedium = "";
	private String performanceMediumType = "";
	private boolean solo = false;
	private String title = "";
	private String tonalityTonic = "";
	private String tonalityMode = "";
	private String tempoBeatUnit = "";
	private String tempoBeatsPerMinute = "";
	private String timeSignature = "";
	private String dateIssued = "";
	private String melody = "";
	private String melodyEncoding = "";
	private String identifier = "";
	private String protocolVersion = "";
	private String source = "";
	private String score = "";
	private String key = "";
	private String clef = "";
	private boolean ignoreChords = true;
	private boolean ensemble = false;
	private boolean ignoreOctave = true;
	private boolean ignorePitch = false;
	private boolean ignoreDuration  = false;
	private int pageSize = 0;
	private int totalSize = 0;
	private int offset = 0;
	private int logPreview = SystemSettings.getLogPreview();
	private String hostname = "";
	private ArrayList<Note> noteSequence;
	private ArrayList<String> dateIssuedArray = new ArrayList<String>(); 
	private File queryFile;
	
	private String responseContentType = "";
	private int responseStatus = 0;
	private String responseContent = "";
	private String responseHeaderName = "";
	private String responseHeaderValue= "";
	
	private static Logger logger = Logger.getLogger("QueryRequestParser");
	private ArrayList<Person> persons = new ArrayList<Person>();
	private ArrayList<PerformanceMedium> mediums = new ArrayList<PerformanceMedium>();
	private ArrayList<Collection> collections = new ArrayList<Collection>();
	private ArrayList<Key> keys = new ArrayList<Key>();
	private ArrayList<String> formats = new ArrayList<String>();
	private ArrayList<Time> times = new ArrayList<Time>();
	private ArrayList<Clef> clefs = new ArrayList<Clef>();
	private Melody melody2 = new Melody();
	
	public WMSSRequest(HttpServletRequest httpRequest)  throws InvalidWMSSRequestException {
		
		logger.info("---------------------------------------------------------------");
		logger.info("Request Method     : "+httpRequest.getMethod());
		logger.info("Request String     : " + httpRequest.getQueryString());
		logger.info("Request URL        : " + httpRequest.getRequestURL());
		logger.info("Request ContentType: "+httpRequest.getContentType());
		logger.info("---------------------------------------------------------------");
		
		if(httpRequest.getContentType()!=null) {

			if(httpRequest.getContentType().toLowerCase().contains("multipart/form-data")) {
			
				File uploadDiretory = new File(SystemSettings.getImportdDirectory());

				if (!uploadDiretory.exists()) {
					uploadDiretory.mkdirs();			
				}

				try {

					ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());				
					List<FileItem> multifiles = sf.parseRequest(httpRequest);
					String uuid = UUID.randomUUID().toString();

					for(FileItem item : multifiles) {

						if(item.getFieldName().equals("query")) {

							File file = new File(uploadDiretory.getAbsolutePath()+"/"+item.getName()+uuid);
							logger.debug("Query file: " + uploadDiretory.getAbsolutePath()+"/"+item.getName()+uuid);
							item.write(file);													
							this.parseQueryFile(FileUtils.readFileToString(file, StandardCharsets.UTF_8));

						}
					}					
					
				} catch (FileUploadException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
						
			if(httpRequest.getContentType().toLowerCase().contains("application/json")) {
				
				try {
					
					logger.info("Parsing query string (application/json)");
					this.parseQueryFile(IOUtils.toString(httpRequest.getReader()));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		Enumeration<String> listParameters = httpRequest.getParameterNames();
		
		while(listParameters.hasMoreElements() ) {

			String parameter = (String) listParameters.nextElement();

			if (parameter.toLowerCase().equals("request")) {
								
				this.requestType = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("requestmode")) {
				
				this.requestMode = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("format")) {
				
				this.format = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("collection")) {
				
				this.collection = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("person")) {
				
				this.personIdentifier = httpRequest.getParameter(parameter);

			} else if (parameter.toLowerCase().equals("personName")) {
				
				this.personName = httpRequest.getParameter(parameter);				
				
			} else if (parameter.toLowerCase().equals("personrole")) {
				
				this.personRole = Util.capitalizeFirstLetter(httpRequest.getParameter(parameter));

			} else if (parameter.toLowerCase().equals("performancemedium")) {
				
				this.performanceMedium = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("performancemediumtype")) {
				
				this.performanceMediumType = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("solo")) {
				
				this.solo = Boolean.parseBoolean(httpRequest.getParameter(parameter));

			} else if (parameter.toLowerCase().equals("tonalitytonic")) {
				
				this.tonalityTonic = httpRequest.getParameter(parameter);

			} else if (parameter.toLowerCase().equals("tonalitymode")) {
				
				this.tonalityMode = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("tempobeatunit")) {
				
				this.tempoBeatUnit = httpRequest.getParameter(parameter).toLowerCase();
			
			} else if (parameter.toLowerCase().equals("tempobeatsperminute")) {	
			
				this.tempoBeatsPerMinute = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("dateissued")) {
				
				this.dateIssued = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("melody")) {
				
				this.melody = httpRequest.getParameter(parameter);
				
				try {
					this.noteSequence = Util.createNoteSequence(this.melody);
				} catch (InvalidMelodyException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				} catch (InvalidKeyException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				} catch (InvalidTimeSignatureException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				} catch (InvalidClefException e) {
					e.printStackTrace();
					throw new InvalidClefException(e.getMessage(),e.getCode(), e.getHint());
				}
								
			} else if (parameter.toLowerCase().equals("identifier")) {
				
				this.identifier = httpRequest.getParameter(parameter);
			
			} else if (parameter.toLowerCase().equals("title")) {
				
				this.title = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("version")) {
				
				this.protocolVersion = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("source")) {
				
				this.source = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("score")) {
				
				this.score = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("ignorechords")) {
				
				this.ignoreChords = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ensemble")) {
				
				this.ensemble = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignoreoctave")) {
				
				this.ignoreOctave = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignorepitch")) {
				
				this.ignorePitch = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignoreduration")) {
				
				this.ignoreDuration = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("pagesize")) {
				
				this.pageSize = Integer.parseInt(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("offset")) {
				
				this.offset = Integer.parseInt(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("totalsize")) {
				
				this.totalSize = Integer.parseInt(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("melodyencoding")) {
				
				this.melodyEncoding =httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("logpreview")) {
				
				this.logPreview = Integer.parseInt(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("key")) {
	
				try {
					this.key = Util.formatPEAkey(httpRequest.getParameter(parameter));
				} catch (InvalidKeyException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				}
				
			} else if (parameter.toLowerCase().equals("time")) {
												
				try {
					this.timeSignature = Util.formatPEAtimeSignature(httpRequest.getParameter(parameter));
				} catch (InvalidTimeSignatureException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				}
				
			} else if (parameter.toLowerCase().equals("clef")) {
				
				try {
					this.clef = Util.formatPEAclef(httpRequest.getParameter(parameter).replace(" ", "+"));
				} catch (InvalidClefException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				}
				
			}  
 
		}
		
		if(pageSize==0) {
			this.pageSize = SystemSettings.getPageSize();
		}
		
		if(this.melodyEncoding.equals("") || this.melodyEncoding==null) {
			this.melodyEncoding = SystemSettings.getDefaultMelodyEncoding(); 
		}
		
		this.hostname = httpRequest.getServerName();
		
		
		
		
		if(this.personIdentifier != "" || this.personName != "" || this.personRole != "") {
			Person person = new Person();
			person.setName(this.personName);
			person.setIdentifier(personIdentifier);
			person.setRole(this.personRole);
			this.persons.add(person);
		}
		
		if(this.collection !="") {
			Collection collection = new Collection();
			collection.setIdentifier(this.collection);
		}
		
		
		if(this.format!="") {
			this.getFormats().add(this.format);
		}
		
			
		
		/**
		 * Request validation
		 */
		
		if(this.requestType.equals("") || this.requestType==null) {			
			throw new InvalidWMSSRequestException(ErrorCodes.MISSING_REQUEST_DESCRIPTION,ErrorCodes.MISSING_REQUEST_CODE, ErrorCodes.INVALID_REQUEST_HINT);			
		} 
		if(!this.requestType.equals("listscores") && !this.requestType.equals("getscore") && !this.requestType.equals("checklog")
				&& !this.requestType.equals("describeservice")&& !this.requestType.equals("deletescore") && !this.requestType.equals("insertscore")) {			
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_REQUEST_DESCRIPTION+" ["+this.requestType+"]",ErrorCodes.INVALID_REQUEST_CODE,ErrorCodes.INVALID_REQUEST_HINT);			
		} 
		if (!this.format.equals("mei") && !this.format.equals("musicxml") && !this.format.equals("") ){
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_DOCUMENT_FORMAT_DESCRIPTION+" ["+this.format+"]",ErrorCodes.INVALID_DOCUMENT_FORMAT_CODE,ErrorCodes.INVALID_DOCUMENT_FORMAT_HINT);
		}
		if(!this.melodyEncoding.equals("pea")) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_MELODY_ENCODING_DESCRIPTION+" ["+this.melodyEncoding+"]",ErrorCodes.INVALID_MELODY_ENCODING_CODE,ErrorCodes.INVALID_MELODY_ENCODING_HINT);
		}
		if(!isDatasourceValid(this) && !this.requestType.equals("describeservice") && !this.requestType.equals("checklog")) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_DATASOURCE_DESCRIPTION+" ["+ this.source + "]",ErrorCodes.INVALID_DATASOURCE_CODE,ErrorCodes.INVALID_DATASOURCE_HINT);
		}
		if((this.requestType.equals("getscore") || this.requestType.equals("deletescore") ) && this.identifier.equals("")) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_IDENTIFIER_DESCRIPTION +" for '"+this.requestType+"' request" ,ErrorCodes.INVALID_IDENTIFIER_CODE,ErrorCodes.INVALID_IDENTIFIER_HINT);
		}
		if(!this.requestMode.equals(SystemSettings.REQUEST_MODE_FULL)&&!this.requestMode.equals(SystemSettings.REQUEST_MODE_SIMPLIFIED)) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_REQUEST_MODE_DESCRIPTION+" [" + this.requestMode + "]",ErrorCodes.INVALID_REQUEST_MODE_CODE,ErrorCodes.INVALID_REQUEST_MODE_HINT);
		}
		if(!this.tempoBeatUnit.equals("maxima") && !this.tempoBeatUnit.equals("longa") &&
		   !this.tempoBeatUnit.equals("breve") && !this.tempoBeatUnit.equals("whole") &&
		   !this.tempoBeatUnit.equals("half") && !this.tempoBeatUnit.equals("quarter") &&
		   !this.tempoBeatUnit.equals("eigth") && !this.tempoBeatUnit.equals("16th") &&
		   !this.tempoBeatUnit.equals("32nd") && !this.tempoBeatUnit.equals("64th") &&
		   !this.tempoBeatUnit.equals("128th") && !this.tempoBeatUnit.equals("256th") &&
		   !this.tempoBeatUnit.equals("512th") && !this.tempoBeatUnit.equals("1024th") &&
		   !this.tempoBeatUnit.equals("")) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_TEMPO_BEAT_UNIT_DESCRIPTION+" [" + this.tempoBeatUnit + "]",ErrorCodes.INVALID_TEMPO_BEAT_UNIT_CODE,ErrorCodes.INVALID_TEMPO_BEAT_UNIT_HINT);			
		}

		if(!this.tempoBeatsPerMinute.equals("")) {
			String[] beats = this.tempoBeatsPerMinute.replaceAll("[^\\d-]", "").split("-");
			
			if(beats.length>2 || !this.tempoBeatsPerMinute.matches("^[0-9\\-]+")) {
				throw new InvalidWMSSRequestException(ErrorCodes.INVALID_TEMPO_BPM_DESCRIPTION+" [" + this.tempoBeatsPerMinute + "]",ErrorCodes.INVALID_TEMPO_BPM_CODE,ErrorCodes.INVALID_TEMPO_BPM_HINT);
			}
		}
		
		if(!this.dateIssued.equals("")) {	
			
			String[] dates = this.dateIssued.replaceAll("[^\\d-]", "").split("-");
			
			if(dates.length==2&& (Integer.parseInt(dates[0])>Integer.parseInt(dates[1]))) {
				throw new InvalidWMSSRequestException(ErrorCodes.INVALID_DATE_DESCRIPTION+" [" + this.dateIssued + "]",ErrorCodes.INVALID_DATE_CODE,ErrorCodes.INVALID_DATE_HINT);
			}
			
			for (int i = 0; i < dates.length; i++) {
				if((dates[i].length()!=4&&
				   dates[i].length()!=6&&
				   dates[i].length()!=8) || !dates[i].matches("^[0-9\\-]+") ) {
					throw new InvalidWMSSRequestException(ErrorCodes.INVALID_DATE_DESCRIPTION+" [" + this.dateIssued + "]",ErrorCodes.INVALID_DATE_CODE,ErrorCodes.INVALID_DATE_HINT);
				}			
				this.dateIssuedArray.add(dates[i]);				
			}
			
		}
		
		
		/**
		 * Sets response attributes based on the parsed and validated requests
		 */
		
		
		if (this.getRequestType().equals("checklog")) {

			this.responseContentType = "text/plain";
			this.responseStatus = HttpServletResponse.SC_OK;
			this.responseContent = Util.loadFileTail(new File("log/wmss.log"), this.getLogPreview());

		} else if (this.getRequestType().equals("describeservice")) {

			this.responseContentType = "text/javascript";
			this.responseStatus= HttpServletResponse.SC_OK;
			this.responseContent= DocumentBuilder.getServiceDescription();

		} else if (this.getRequestType().equals("getscore")) { 

			MusicScore score = DocumentBuilder.getScore(this);
			
			this.responseHeaderName = "Content-disposition";
			this.responseHeaderValue = "attachment; filename="+score.getTitle().replaceAll("[^a-zA-Z0-9\\.\\-]", "_")+".xml";
			this.responseContentType = "text/xml";
			this.responseStatus = HttpServletResponse.SC_OK;
			this.responseContent = score.getDocument();

		} else if (this.getRequestType().equals("listscores")) {

			this.responseContentType ="text/javascript";
			this.responseStatus= HttpServletResponse.SC_OK;
			this.responseContent= DocumentBuilder.getScoreList(this);

		} else if (this.getRequestType().equals("deletescore")) {

			this.responseContentType = "text/javascript";
			this.responseStatus = HttpServletResponse.SC_OK;
			this.responseContent = DocumentBuilder.deleteScore(this); 

		} 

	}
	
	private void parseQueryFile(String jsonRequest) {
		
		try {

			JSONParser parser = new JSONParser();						
			Object obj = parser.parse(jsonRequest);
			JSONObject queryObject = (JSONObject) obj;	

			this.requestType = queryObject.get("request").toString();
			
			if(queryObject.get("scoreIdentifier")!=null) {
				this.identifier = queryObject.get("scoreIdentifier").toString();
				logger.info("Score Identifier: " + queryObject.get("scoreIdentifier").toString());
			}
			
			if(queryObject.get("title")!=null) {
				this.title = queryObject.get("title").toString();
				logger.info("Title           : " + queryObject.get("title").toString());
			}
			
			if(queryObject.get("issued")!=null) {
				this.dateIssued = queryObject.get("issued").toString();
				logger.info("Date Issued     : " + queryObject.get("issued").toString());
			}
			
			if(queryObject.get("ignoreOctave")!=null) {
				this.ignoreOctave = Boolean.parseBoolean(queryObject.get("ignoreOctave").toString());
				logger.info("Ignore Octave     : " + queryObject.get("ignoreOctave").toString());
			}
			
			if(queryObject.get("ignoreDuration")!=null) {
				this.ignoreDuration = Boolean.parseBoolean(queryObject.get("ignoreDuration").toString());
				logger.info("Ignore Duration     : " + queryObject.get("ignoreDuration").toString());
			}
			
			if(queryObject.get("ignorePitch")!=null) {
				this.ignorePitch = Boolean.parseBoolean(queryObject.get("ignorePitch").toString());
				logger.info("Ignore Pitch     : " + queryObject.get("ignorePitch").toString());
			}
			
			if(queryObject.get("ignoreChords")!=null) {
				this.ignoreChords = Boolean.parseBoolean(queryObject.get("ignoreChords").toString());
				logger.info("Ignore Chords     : " + queryObject.get("ignoreChords").toString());
			}
			
			
			if(queryObject.get("melody")!=null) {
				
				JSONObject melodyObject = (JSONObject) queryObject.get("melody");				
				
				if(melodyObject.get("query")!=null) {
					try {
						this.melody = melodyObject.get("query").toString();
						this.noteSequence = Util.createNoteSequence(melodyObject.get("query").toString());
					} catch (InvalidMelodyException e) {
						e.printStackTrace();
						throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
					} catch (InvalidKeyException e) {
						e.printStackTrace();
						throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
					} catch (InvalidTimeSignatureException e) {
						e.printStackTrace();
						throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
					} catch (InvalidClefException e) {
						e.printStackTrace();
						throw new InvalidClefException(e.getMessage(),e.getCode(), e.getHint());
					}
				}
				if(melodyObject.get("format")!=null) {
					this.melodyEncoding = melodyObject.get("format").toString();
				}
				if(melodyObject.get("mediumCode")!=null) {
					this.performanceMedium = melodyObject.get("mediumCode").toString();
				}
				if(melodyObject.get("mediumType")!=null) {
					this.performanceMediumType = melodyObject.get("mediumType").toString();
				}
				if(melodyObject.get("clef")!=null) {
					this.clef = melodyObject.get("clef").toString();
				}
				if(melodyObject.get("time")!=null) {
					this.timeSignature = melodyObject.get("time").toString();
				}
				if(melodyObject.get("key")!=null) {
					
					try {
						this.key = Util.formatPEAkey(melodyObject.get("key").toString());
					} catch (InvalidKeyException e) {
						e.printStackTrace();
						throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
					}

				}

			}
			
			if(queryObject.get("collections")!=null) {
				
				JSONArray collectionObjects = (JSONArray) queryObject.get("collections");
				
				for (int j = 0; j < collectionObjects.size(); j++) {
					
					JSONObject collectionObject = (JSONObject) collectionObjects.get(j);
					Collection collection = new Collection();
					
					if(collectionObject.get("collectionIdentifier")!=null) {
						collection.setIdentifier(collectionObject.get("collectionIdentifier").toString());
						logger.info("Collection Identifier  : " + collectionObject.get("collectionIdentifier").toString());	
					}
					
					if(collectionObject.get("collectionName")!=null) {
						collection.setName(collectionObject.get("collectionName").toString());
						logger.info("Collection Name  : " + collectionObject.get("collectionName").toString());	
					}

					this.getCollections().add(collection);
				}
				
			}
			
			
			if(queryObject.get("formats")!=null) {
				
				JSONArray formatObjects = (JSONArray) queryObject.get("formats");
				
				for (int i = 0; i < formatObjects.size(); i++) {
				
					JSONObject formatObject = (JSONObject) formatObjects.get(i);
					
					logger.info("Format       : " + formatObject.get("format").toString());
					this.getFormats().add(formatObject.get("format").toString());
					
				}
				
			}
			
			JSONArray keyObjects = (JSONArray) queryObject.get("keys");
			
			for (int j = 0; j < keyObjects.size(); j++) {
				
				JSONObject keyObject = (JSONObject) keyObjects.get(j);
				Key key = new Key();

				if(keyObject.get("key")!=null) {
					logger.info("Key  : " + keyObject.get("key").toString());
					key.setKey(keyObject.get("key").toString());
				}
				
				if(keyObject.get("format")!=null) {
					logger.info("Key Format : " + keyObject.get("format").toString());
					key.setFormat(keyObject.get("format").toString());
				}

				this.getKeys().add(key);

			}
			
			JSONArray timeObjects = (JSONArray) queryObject.get("times");
			
			for (int j = 0; j < timeObjects.size(); j++) {
				
				JSONObject timeObject = (JSONObject) timeObjects.get(j);
				Time time = new Time();

				if(timeObject.get("time")!=null) {
					logger.info("Time  : " + timeObject.get("time").toString());
					time.setTime(timeObject.get("time").toString());
				}
				
				if(timeObject.get("format")!=null) {
					logger.info("Time Format : " + timeObject.get("format").toString());
					time.setFormat(timeObject.get("format").toString());
				}

				this.getTimes().add(time);

			}
			
			JSONArray clefObjects = (JSONArray) queryObject.get("clefs");
			
			for (int j = 0; j < clefObjects.size(); j++) {
				
				JSONObject clefObject = (JSONObject) clefObjects.get(j);
				Clef clef = new Clef();

				if(clefObject.get("clef")!=null) {
					logger.info("Clef  : " + clefObject.get("clef").toString());
					clef.setClef(clefObject.get("clef").toString());
				}
				
				if(clefObject.get("format")!=null) {
					logger.info("Clef Format : " + clefObject.get("format").toString());
					clef.setFormat(clefObject.get("format").toString());
				}

				this.getClefs().add(clef);

			}
			
			JSONArray mediumObjects = (JSONArray) queryObject.get("mediums");
			
			for (int j = 0; j < mediumObjects.size(); j++) {
				
				JSONObject mediumObject = (JSONObject) mediumObjects.get(j);
				PerformanceMedium medium = new PerformanceMedium();
				
				if(mediumObject.get("mediumType")!=null) {
					logger.info("Medium Type  : " + mediumObject.get("mediumType").toString());
					medium.setMediumTypeId(mediumObject.get("mediumType").toString());
				}

				if(mediumObject.get("mediumCode")!=null) {
					logger.info("Medium Code  : " + mediumObject.get("mediumCode").toString());
					medium.setMediumCode(mediumObject.get("mediumCode").toString());
				}

				this.mediums.add(medium);
				
			}
			
			
			JSONArray personObjects = (JSONArray) queryObject.get("persons");

			for (int j = 0; j < personObjects.size(); j++) {

				JSONObject personObject = (JSONObject) personObjects.get(j);
				Person person = new Person();
				
				if(personObject.get("personIdentifier")!=null) {
					logger.info("Person Identifier  : " + personObject.get("personIdentifier").toString());
					person.setIdentifier(personObject.get("personIdentifier").toString());
				}

				if(personObject.get("personName")!=null) {
					logger.info("Person Name   : " + personObject.get("personName").toString());
					person.setName(personObject.get("personName").toString());
				}
				
				if(personObject.get("personRole")!=null) {
					logger.info("Person Role  : " + personObject.get("personRole").toString());
					person.setRole(personObject.get("personRole").toString());
				}

				this.getPersons().add(person);

			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		
	}
		
	private boolean isDatasourceValid(WMSSRequest wmssRequest) {
		
		boolean result = false;
		
		
		if (!wmssRequest.getSource().equals("")){

			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

				if (result==false){

					if (wmssRequest.getSource().equals(SystemSettings.sourceList.get(i).getId().toString().toLowerCase())){

						result = true;						

					} 

				}
			}
		} 
		
		
		
		
		return result;
	}

	public String getRequestType() {
		return requestType;
	}

	public String getFormat() {
		return format;
	}

	public String getCollection() {
		return collection;
	}

	public String getPerson() {
		return personIdentifier;
	}

	public String getPersonRole() {
		String result = "";
		if(personRole.length()>0) {
			result = personRole.substring(0, 1).toUpperCase() + personRole.substring(1).toLowerCase() ;
		}
		return result;
		
	}

	public String getPerformanceMedium() {
		return performanceMedium;
	}

	public String getPerformanceMediumType() {
		return performanceMediumType;
	}

	public boolean isSolo() {
		return solo;
	}

	public String getTonalityTonic() {
		return tonalityTonic;
	}

	public String getTonalityMode() {
		return tonalityMode;
	}

	public String getTempoBeatUnit() {
		return tempoBeatUnit;
	}
	
	public String getTempoBeatsPerMinute() {
		return tempoBeatsPerMinute;
	}

	public String getDateIssued() {
		return dateIssued;
	}

	public String getMelody() {
		return melody;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public String getSource() {
		return source;
	}

	public String getScore() {
		return score;
	}

	public boolean isIgnoreChords() {
		return ignoreChords;
	}

	public boolean isEnsemble() {
		return ensemble;
	}

	public boolean isIgnoreOctaves() {
		return ignoreOctave;
	}

	public boolean isIgnorePitch() {
		return ignorePitch;
	}

	public boolean isIgnoreDuration() {
		return ignoreDuration;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public int getOffset() {
		return offset;
	}

	public String getHostname() {
		return hostname;
	}

	public void setIgnoreChords(boolean ignoreChords) {
		this.ignoreChords = ignoreChords;
	}

	public String getMelodyFormat() {
		return melodyEncoding;
	}

	public String getRequestMode() {
		return requestMode;
	}

	public ArrayList<Note> getNoteSequence() {
		return noteSequence;
	}

	public ArrayList<String> getDateIssuedArray() {
		return dateIssuedArray;
	}

	public int getLogPreview() {
		return logPreview;
	}

	public String getKey() {
		return key;
	}

	public String getTimeSignature() {
		return timeSignature;
	}

	public String getClef() {
		return clef;
	}

	public String getTitle() {
		return title;
	}

	public String getResponseContentType() {
		return responseContentType;
	}

	public int getResponseStatus() {
		return responseStatus;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public String getResponseHeaderName() {
		return responseHeaderName;
	}

	public String getResponseHeaderValue() {
		return responseHeaderValue;
	}

	public File getQueryFile() {
		return queryFile;
	}

	public ArrayList<Person> getPersons() {
		return persons;
	}

	public ArrayList<Collection> getCollections() {
		return collections;
	}

	public ArrayList<PerformanceMedium> getMediums() {
		return mediums;
	}

	public ArrayList<Key> getKeys() {
		return keys;
	}

	public ArrayList<String> getFormats() {
		return formats;
	}

	public ArrayList<Time> getTimes() {
		return times;
	}

	public ArrayList<Clef> getClefs() {
		return clefs;
	}

	public Melody getMelody2() {
		return melody2;
	}
	
	
}
