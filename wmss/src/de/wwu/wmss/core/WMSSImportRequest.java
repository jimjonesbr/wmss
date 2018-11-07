package de.wwu.wmss.core;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class WMSSImportRequest {

	private String format = "n-triples"; 
	
	public WMSSImportRequest(HttpServletRequest httpRequest) throws InvalidWMSSRequestException{

		Enumeration<String> listParameters = httpRequest.getParameterNames();
		
		while(listParameters.hasMoreElements() ) {

			String parameter = (String) listParameters.nextElement();
			
			if (parameter.toLowerCase().equals("format")) {
				
				this.format = httpRequest.getParameter(parameter).toLowerCase();
				
			}

		}
		
		
		if(!this.format.equals("n-triples") && !this.format.equals("json-ld") && !this.format.equals("turtle") && !this.format.equals("rdf/xml")) {
			
			throw new InvalidWMSSRequestException(ErrorCodes.INVALID_RDFFORMAT_DESCRIPTION+" ["+this.format+"]", ErrorCodes.INVALID_RDFFORMAT_CODE , ErrorCodes.INVALID_RDFFORMAT_HINT);
			
		}
		
	}
	
}
