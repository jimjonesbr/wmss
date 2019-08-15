package de.wwu.wmss.core;

import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import de.wwu.wmss.exceptions.InvalidWMSSRequestException;
import de.wwu.wmss.settings.SystemSettings;

public class WMSSImportRequest {

	private String format = SystemSettings.getDefaultRDFFormat();
	private String commitSize = SystemSettings.getDefaultCommitSize();
	private String source = "";
	private Date startDate = new Date();
	private String hostname = "";
	private String urlDataset = "";
	private String metadata = "";
	
	public WMSSImportRequest(HttpServletRequest httpRequest) throws InvalidWMSSRequestException{
		
		Enumeration<String> listParameters = httpRequest.getParameterNames();
	
		while(listParameters.hasMoreElements() ) {

			String parameter = (String) listParameters.nextElement();
			
			if (parameter.toLowerCase().equals("format")) {			
				
				if(httpRequest.getParameter(parameter).toLowerCase().equals("n-triples")) {
					this.format = "N-Triples";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("json-ld")) {
					this.format = "JSON-LD";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("rdf/xml")) {
					this.format = "RDF/XML";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("turtle")) {
					this.format = "Turtle";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("trig")) {
					this.format = "TriG";
				} else if(httpRequest.getParameter(parameter).toLowerCase().equals("musicxml")) {
					this.format = "musicxml";					
				} else {
					throw new InvalidWMSSRequestException(ErrorCodes.INVALID_RDFFORMAT_DESCRIPTION+" ["+this.format+"]", ErrorCodes.INVALID_RDFFORMAT_CODE , ErrorCodes.INVALID_RDFFORMAT_HINT);		
				}
								
			}
			
			if (parameter.toLowerCase().equals("commitsize")) {							
				this.commitSize = httpRequest.getParameter(parameter);				
			}

			if (parameter.toLowerCase().equals("source")) {							
				this.source = httpRequest.getParameter(parameter);				
			}
			
			if (parameter.toLowerCase().equals("url")) {							
				this.urlDataset = httpRequest.getParameter(parameter);				
			}	
			
			if (parameter.toLowerCase().equals("metadata")) {							
				this.metadata = httpRequest.getParameter(parameter);				
			}	
													
			this.hostname = httpRequest.getServerName();				
			
		}

		
	}

	public String getFormat() {
		return format;
	}

	public String getCommitSize() {
		return commitSize;
	}

	public String getSource() {
		return source;
	}

	public Date getStartDate() {
		return startDate;
	}

	public String getHostname() {
		return hostname;
	}

	public String getUrlDataset() {
		return urlDataset;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getMetadata() {
		return metadata;
	}
	
	
	
	
}
