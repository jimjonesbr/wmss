package de.wwu.wmss.core;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import de.wwu.wmss.settings.SystemSettings;

public class WMSSRequest {
		
	private String requestType = "";
	private String format = "";
	private String collection = "";
	private String person = "";
	private String personRole = "";		
	private String performanceMedium = "";
	private String performanceMediumType = "";
	private boolean solo = false;
	private String tonalityTonic = "";
	private String tonalityMode = "";
	private String tempo = "";
	private String creationDate = "";
	private String creationDateFrom = "";
	private String creationDateTo = "";
	private String melody = "";
	private String identifier = "";
	private String version = "";
	private String source = "";
	private String score = "";
	private boolean ignoreChords = true;
	private boolean ensemble = false;
	private boolean ignoreOctaves = true;
	private boolean ignorePitch = false;
	private boolean ignoreDuration  = false;
	private int pageSize = 0;
	private int totalSize = 0;
	private int offset = 0;
	private String hostname = "";
		
	
	public boolean isValid() {
	
		return true;
		
	}
	
	
	public WMSSRequest(HttpServletRequest request)  throws InvalidWMSSRequestException {
		
		Enumeration<String> listParameters = request.getParameterNames();
		
		while(listParameters.hasMoreElements() ) {

			String parameter = (String) listParameters.nextElement();

			if (parameter.toLowerCase().equals("request")) {
								
				this.requestType = request.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("format")) {
				
				this.format = request.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("collection")) {
				
				this.collection = request.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("person")) {
				
				this.person = request.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("personrole")) {
				
				this.personRole = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("performancemedium")) {
				
				this.performanceMedium = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("performancemediumtype")) {
				
				this.performanceMediumType = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("solo")) {
				
				this.solo = Boolean.parseBoolean(request.getParameter(parameter));

			} else if (parameter.toLowerCase().equals("tonalitytonic")) {
				
				this.tonalityTonic = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("tonalitymode")) {
				
				this.tonalityMode = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("tempo")) {
				
				this.tempo = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("creationdate")) {
				
				this.creationDate = request.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("creationdatefrom")) {
				
				this.creationDateFrom = request.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("creationdateto")) {
				
				this.creationDateTo = request.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("melody")) {
				
				this.melody = request.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("identifier")) {
				
				this.identifier = request.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("version")) {
				
				this.version = request.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("source")) {
				
				this.source = request.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("score")) {
				
				this.score = request.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("ignorechords")) {
				
				this.ignoreChords = Boolean.parseBoolean(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ensemble")) {
				
				this.ensemble = Boolean.parseBoolean(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignoreoctaves")) {
				
				this.ignoreOctaves = Boolean.parseBoolean(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignorepitch")) {
				
				this.ignorePitch = Boolean.parseBoolean(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignoreduration")) {
				
				this.ignoreDuration = Boolean.parseBoolean(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("pagesize")) {
				
				this.pageSize = Integer.parseInt(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("offset")) {
				
				this.offset = Integer.parseInt(request.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("totalsize")) {
				
				this.totalSize = Integer.parseInt(request.getParameter(parameter));
				
			} 			

		}
		
		if(pageSize==0) {
			this.pageSize = SystemSettings.getPageSize();
		}
		
		this.hostname = request.getServerName();
		
		
		if(this.requestType.equals("")||this.requestType==null) {
			
			throw new InvalidWMSSRequestException("No request type provided. Provide one of the following request types: ListScores, GetScores, Checklog, DescribeService.");
			
		}
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
		return person;
	}



	public String getPersonRole() {
		return personRole;
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



	public String getTempo() {
		return tempo;
	}



	public String getCreationDate() {
		return creationDate;
	}



	public String getCreationDateFrom() {
		return creationDateFrom;
	}



	public String getCreationDateTo() {
		return creationDateTo;
	}



	public String getMelody() {
		return melody;
	}



	public String getIdentifier() {
		return identifier;
	}



	public String getVersion() {
		return version;
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
		return ignoreOctaves;
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

	
}
