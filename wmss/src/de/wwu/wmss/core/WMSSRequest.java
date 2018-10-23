package de.wwu.wmss.core;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;

public class WMSSRequest {
		
	private String requestType = "";
	private String requestMode = SystemSettings.getDefaultRequestMode();
	private String format = "";
	private String collection = "";
	private String person = "";
	private String personRole = "";		
	private String performanceMedium = "";
	private String performanceMediumType = "";
	private boolean solo = false;
	private String tonalityTonic = "";
	private String tonalityMode = "";
	private String tempoBeatUnit = "";
	private String tempoBeatsPerMinute = "";
	private String creationDate = "";
	private String creationDateFrom = "";
	private String creationDateTo = "";
	private String melody = "";
	private String melodyEncoding = "";
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
	private ArrayList<Note> noteSequence;
		
	public WMSSRequest(HttpServletRequest httpRequest)  throws InvalidWMSSRequestException {
		
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
				
				this.person = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("personrole")) {
				
				this.personRole = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("performancemedium")) {
				
				this.performanceMedium = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("performancemediumtype")) {
				
				this.performanceMediumType = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("solo")) {
				
				this.solo = Boolean.parseBoolean(httpRequest.getParameter(parameter));

			} else if (parameter.toLowerCase().equals("tonalitytonic")) {
				
				this.tonalityTonic = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("tonalitymode")) {
				
				this.tonalityMode = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("tempobeatunit")) {
				
				this.tempoBeatUnit = httpRequest.getParameter(parameter).toLowerCase();
			
			} else if (parameter.toLowerCase().equals("tempobeatsperminute")) {	
			
				this.tempoBeatsPerMinute = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("creationdate")) {
				
				this.creationDate = httpRequest.getParameter(parameter).toLowerCase();

			} else if (parameter.toLowerCase().equals("creationdatefrom")) {
				
				this.creationDateFrom = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("creationdateto")) {
				
				this.creationDateTo = httpRequest.getParameter(parameter).toLowerCase();
				
			} else if (parameter.toLowerCase().equals("melody")) {
				
				this.melody = httpRequest.getParameter(parameter);
				
				try {
					this.noteSequence = Util.createNoteSequence(this.melody);
				} catch (InvalidMelodyException e) {
					e.printStackTrace();
					throw new InvalidWMSSRequestException(e.getMessage(),e.getCode(), e.getHint());
				}
								
			} else if (parameter.toLowerCase().equals("identifier")) {
				
				this.identifier = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("version")) {
				
				this.version = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("source")) {
				
				this.source = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("score")) {
				
				this.score = httpRequest.getParameter(parameter);
				
			} else if (parameter.toLowerCase().equals("ignorechords")) {
				
				this.ignoreChords = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ensemble")) {
				
				this.ensemble = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
			} else if (parameter.toLowerCase().equals("ignoreoctaves")) {
				
				this.ignoreOctaves = Boolean.parseBoolean(httpRequest.getParameter(parameter));
				
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
			}

		}
		
		if(pageSize==0) {
			this.pageSize = SystemSettings.getPageSize();
		}
		
		if(this.melodyEncoding.equals("") || this.melodyEncoding==null) {
			this.melodyEncoding = "pea"; //TODO create 'default melody format' at the settings file.
		}
		
		this.hostname = httpRequest.getServerName();
		
		
		
		/**
		 * Request validation
		 */
		
		if(this.requestType.equals("") || this.requestType==null) {			
			throw new InvalidWMSSRequestException(ErrorCodes.MISSING_REQUEST_DESCRIPTION,ErrorCodes.MISSING_REQUEST_CODE, ErrorCodes.INVALID_REQUEST_HINT);			
		} 
		if(!this.requestType.equals("listscores") && !this.requestType.equals("getscore") && !this.requestType.equals("checklog")
				&& !this.requestType.equals("describeservice")) {			
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_REQUEST_DESCRIPTION+" ["+this.requestType+"]",ErrorCodes.INVALID_REQUEST_CODE,ErrorCodes.INVALID_REQUEST_HINT);			
		} 
		if (!this.format.equals("mei") && !this.format.equals("musicxml") && !this.format.equals("") ){
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_DOCUMENT_FORMAT_DESCRIPTION+" ["+this.format+"]",ErrorCodes.INVALID_DOCUMENT_FORMAT_CODE,ErrorCodes.INVALID_DOCUMENT_FORMAT_HINT);
		}
		if (!this.tonalityMode.equals("minor") && !this.tonalityMode.equals("major") && !this.tonalityMode.equals("") ){
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_TONALITY_MODE_DESCRIPTION+" ["+this.tonalityMode+"]",ErrorCodes.INVALID_TONALITY_MODE_DESCRIPTION,ErrorCodes.INVALID_TONALITY_MODE_HINT);
		}
		if (!this.tonalityTonic.equals("a") && 
				!this.tonalityTonic.equals("aflat") && 
				!this.tonalityTonic.equals("asharp") && 
				!this.tonalityTonic.equals("b") &&
				!this.tonalityTonic.equals("bflat") &&
				!this.tonalityTonic.equals("bsharp") &&
				!this.tonalityTonic.equals("c") &&
				!this.tonalityTonic.equals("cflat") &&
				!this.tonalityTonic.equals("csharp") &&
				!this.tonalityTonic.equals("d") &&
				!this.tonalityTonic.equals("dflat") &&
				!this.tonalityTonic.equals("dsharp") &&
				!this.tonalityTonic.equals("e") &&
				!this.tonalityTonic.equals("eflat") &&
				!this.tonalityTonic.equals("esharp") &&
				!this.tonalityTonic.equals("f") &&
				!this.tonalityTonic.equals("fflat") &&
				!this.tonalityTonic.equals("fsharp") &&
				!this.tonalityTonic.equals("g") &&
				!this.tonalityTonic.equals("gflat") &&
				!this.tonalityTonic.equals("gsharp") &&
				!this.tonalityTonic.equals("")){
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_TONALITY_TONIC_DESCRIPTION+" ["+this.tonalityTonic+"]",ErrorCodes.INVALID_TONALITY_TONIC_CODE,ErrorCodes.INVALID_TONALITY_TONIC_HINT);
		}
		if(!this.melodyEncoding.equals("pea")) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_MELODY_ENCODING_DESCRIPTION+" ["+this.melodyEncoding+"]",ErrorCodes.INVALID_MELODY_ENCODING_CODE,ErrorCodes.INVALID_MELODY_ENCODING_HINT);
		}
		if(!isDatasourceValid(this)) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_DATASOURCE_DESCRIPTION+" ["+ this.source + "]",ErrorCodes.INVALID_DATASOURCE_CODE,ErrorCodes.INVALID_DATASOURCE_HINT);
		}
		if(this.requestType.equals("getscore") && this.identifier.equals("")) {
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_IDENTIFIER_DESCRIPTION,ErrorCodes.INVALID_IDENTIFIER_CODE,ErrorCodes.INVALID_IDENTIFIER_HINT);
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
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_TEMPO_BEAT_UNIT_DESCRIPTION+" [" + this.tempoBeatUnit + "]",ErrorCodes.INVALID_TEMPO_BEAT_UNIT_DESCRIPTION,ErrorCodes.INVALID_TEMPO_BEAT_UNIT_HINT);			
		}

		if(!this.tempoBeatsPerMinute.equals("")) {
			String[] beats = this.tempoBeatsPerMinute.replaceAll("[^\\d-]", "").split("-");
			if(beats.length>2) {
				throw new InvalidWMSSRequestException(ErrorCodes.INVALID_TEMPO_BPM_DESCRIPTION+" [" + this.tempoBeatsPerMinute + "]",ErrorCodes.INVALID_TEMPO_BPM_CODE,ErrorCodes.INVALID_TEMPO_BPM_HINT);
			}
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
		} else {
			
			result = true;
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

	public String getTempoBeatUnit() {
		return tempoBeatUnit;
	}
	
	public String getTempoBeatsPerMinute() {
		return tempoBeatsPerMinute;
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

	public String getMelodyFormat() {
		return melodyEncoding;
	}

	public String getRequestMode() {
		return requestMode;
	}

	public ArrayList<Note> getNoteSequence() {
		return noteSequence;
	}
	
	
}
