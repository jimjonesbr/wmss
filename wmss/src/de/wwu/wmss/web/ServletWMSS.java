package de.wwu.wmss.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.factory.ServiceMessagingHandler;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;

public class ServletWMSS extends HttpServlet
{

	/**
	 * @author Jim Jones
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("ServletWMSS");

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{    	
		Enumeration<String> listParameters = request.getParameterNames();
		ArrayList<RequestParameter> parametersList = new ArrayList<RequestParameter>(); 
		
		logger.info("Query String -> "+request.getQueryString());
		
		String identifier = "";
		String version = "";
		String format = "";
		String requestType = "";
		String collection = "";
		String person = "";
		String personRole = "";
		String performanceMedium = "";
		String performanceMediumType = "";
		String solo = "";
		String tonalityTonic = "";
		String tonalityMode = "";
		String tempo = "";
		String creationDate = "";
		String creationDateFrom = "";
		String creationDateTo = "";
		String melody = "";
		String source = "";
		String score = "";

		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

		while(listParameters.hasMoreElements() ) {

			String parameter = (String) listParameters.nextElement();
			RequestParameter req = new RequestParameter();
		
			if (parameter.toLowerCase().equals("request")) {
				requestType=request.getParameter(parameter).toLowerCase();
				req.setRequest("requesttype");
				req.setValue(request.getParameter(parameter).toLowerCase());
			} else if (parameter.toLowerCase().equals("format")) {
				
				format=request.getParameter(parameter).toLowerCase();
				req.setRequest("format");
				req.setValue(request.getParameter(parameter).toLowerCase());
				
			} else if (parameter.toLowerCase().equals("collection")) {
				
				collection=request.getParameter(parameter).toLowerCase();
				req.setRequest("collection");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("person")) {
				
				person=request.getParameter(parameter).toLowerCase();
				req.setRequest("person");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("personrole")) {
				
				personRole=request.getParameter(parameter).toLowerCase();
				req.setRequest("personrole");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("performancemedium")) {
				
				performanceMedium=request.getParameter(parameter).toLowerCase();
				req.setRequest("performancemedium");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("performancemediumtype")) {
				
				performanceMediumType=request.getParameter(parameter).toLowerCase();
				req.setRequest("performancemediumtype");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("solo")) {
				
				solo=request.getParameter(parameter).toLowerCase();
				req.setRequest("solo");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("tonalitytonic")) {
				
				tonalityTonic=request.getParameter(parameter).toLowerCase();
				req.setRequest("tonalitytonic");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("tonalitymode")) {
				
				tonalityMode=request.getParameter(parameter).toLowerCase();
				req.setRequest("tonalitymode");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("tempo")) {
				
				tempo=request.getParameter(parameter).toLowerCase();
				req.setRequest("tempo");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("creationdate")) {
				
				creationDate=request.getParameter(parameter).toLowerCase();
				req.setRequest("creationdate");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("creationdatefrom")) {
				
				creationDateFrom=request.getParameter(parameter).toLowerCase();
				req.setRequest("creationdatefrom");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("creationdateto")) {
				
				creationDateTo=request.getParameter(parameter).toLowerCase();
				req.setRequest("creationdateto");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("melody")) {
				
				melody=request.getParameter(parameter).toLowerCase();
				req.setRequest("melody");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("identifier")) {
				
				identifier=request.getParameter(parameter).toLowerCase();
				req.setRequest("identifier");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("version")) {
				
				version=request.getParameter(parameter).toLowerCase();
				req.setRequest("vesion");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("source")) {
				
				source=request.getParameter(parameter).toLowerCase();
				req.setRequest("source");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("score")) {
				
				score=request.getParameter(parameter).toLowerCase();
				req.setRequest("score");
				req.setValue(request.getParameter(parameter).toLowerCase());

			}

			parametersList.add(req);
		}

		boolean validSource = false;

		if (!source.equals("")){

			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

				if (validSource==false){

					if (source.equals(SystemSettings.sourceList.get(i).getId().toString().toLowerCase())){

						validSource = true;						

					} 

				}
			}
		} else {
			
			validSource = true;
		}

		
		
		
		
		
		
		if(requestType.equals("")){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0001", "No request type provided","Provide one of the following request types: ListScores, GetScores, Checklog, DescribeService."));

		} else if (!requestType.equals("listscores") 
				&& !requestType.equals("getscore")
				&& !requestType.equals("checklog")
				&& !requestType.equals("describeservice")){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0002", "Invalid request parameter [" + requestType + "].","Provide one of the following request types: ListScores, GetScores, Checklog, DescribeService."));

		} else if (!format.equals("mei") && !format.equals("musicxml") && !format.equals("") ){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0003", "Invalid document format [" + format + "].",""));

		} else if (!tonalityMode.equals("minor") && !tonalityMode.equals("major") && !tonalityMode.equals("") ){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0004", "Invalid mode [" + tonalityMode + "].","Provide one of the tonality modes: 'minor' or 'major'"));

		} else if (!tonalityTonic.equals("a") && 
					!tonalityTonic.equals("aflat") && 
					!tonalityTonic.equals("asharp") && 
					!tonalityTonic.equals("b") &&
					!tonalityTonic.equals("bflat") &&
					!tonalityTonic.equals("bsharp") &&
					!tonalityTonic.equals("c") &&
					!tonalityTonic.equals("cflat") &&
					!tonalityTonic.equals("csharp") &&
					!tonalityTonic.equals("d") &&
					!tonalityTonic.equals("dflat") &&
					!tonalityTonic.equals("dsharp") &&
					!tonalityTonic.equals("e") &&
					!tonalityTonic.equals("eflat") &&
					!tonalityTonic.equals("esharp") &&
					!tonalityTonic.equals("f") &&
					!tonalityTonic.equals("fflat") &&
					!tonalityTonic.equals("fsharp") &&
					!tonalityTonic.equals("g") &&
					!tonalityTonic.equals("gflat") &&
					!tonalityTonic.equals("gsharp") &&
					!tonalityTonic.equals("") 
				){


			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0005", "Invalid tonic '" + tonalityTonic + "'.","[TBW]"));

		} else if (!solo.equals("true") &&
				!solo.equals("false") &&
				!solo.equals(""))	{

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0006", "Invalid boolean value for 'solo' parameter '" + solo + "'.","The 'solo' filter required a boolean value ('true' or 'false')."));

		} else if (!version.equals("") &&
				!SystemSettings.protocolVersions.contains(version.toString())){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0007", "Invalid protocol version '" + version+ "'.","Check the 'Service Description Report' for more information on the supported WMSS protocols."));

		} else if (!validSource) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0009", "Invalid data source '" + source + "'.","The provided data source cannot be found. Check the 'Service Description Report' for more information on the available data sources."));

		} else if (!melody.equals("") && !isMelodyRequestValid(melody)) {
			
			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0012", "Invalid melody '" + melody +"'","The provided melody is not valid. For more information go to: https://github.com/jimjonesbr/wmss/blob/master/README.md#melody"));

		} else if (!collection.equals("") && !isCollectionValid(collection)) {
			
			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0013", "Invalid collection string '" + collection +"'","The provided collection string is not valid. Provided either a single numeric identifier '1' or multiple separated by comma '1,2,3'."));

			
		} else if (requestType.equals("checklog")) {
			
			response.setContentType("text/plain");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(Util.loadFileTail(new File("logs/system.log"), SystemSettings.getLogPreview()));
			

		} else if (requestType.equals("describeservice")) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceDescription());


		} else if (requestType.equals("getscore")) { 

			if (identifier.equals("")){

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0008", "No identifier provided for GetScore request.","The GetScore request expects a valid score identifier."));

			} else {
						
				response.setContentType("text/xml");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getScore(parametersList));
				
			}


		} else if (requestType.equals("listscores")) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getScoreList(parametersList));

			//TODO create an error message for invalid parameters. Currently they are being only ignored.
			//TODO create validation of filter capabilities based on the sources.json document
			//TODO create search for scores based on all search criteria

		}

	}

	private static boolean isMelodyRequestValid(String melody) {

		String[] melodyElements = melody.split("/");

		boolean validNotes = false;
		boolean validDurations = false;
		boolean validOctaves = false;
		boolean result = true;

		for (int i = 0; i < melodyElements.length; i++) {

			String[] element = melodyElements[i].split("-");

			if(element.length < 3) {

				result = false;
				
			} else {
				
				validNotes = element[0].matches("0|a|b|c|d|e|f|g|" 				// natural
											  + "ahb|bhb|chb|dhb|ehb|fhb|ghb|"	// half flat
											  + "ab|bb|cb|db|eb|fb|gb|"			// flat
											  + "abh|bbh|cbh|dbh|ebh|fbh|gbh|"	// flat and a half										  
											  + "abb|bbb|cbb|dbb|ebb|fbb|gbb|"	// double flat
											  + "ahs|bhs|chs|dhs|ehs|fhs|ghs|"	// half sharp
											  + "as|bs|cs|ds|es|fs|gs|"			// sharp					  
											  + "ash|bsh|csh|dsh|esh|fsh|gsh|"	// sharp and a half							  
											  + "ass|bss|css|dss|ess|fss|gss");	// double sharp
				
				validDurations = element[1].matches("0|ow|qw|dw|w|h|4|8|16|32|64|128|256");	
				validOctaves = element[2].matches("0|1|2|3|4|5|6|7|8|9");
				
			}
			
			if(validNotes && validDurations && validOctaves) {

				if(element[0].equals("0") && element[1].equals("0")) {

					result = false;
				}

			} else {

				result = false;
			}

		}

		return result;

	}
	
	private static boolean isCollectionValid(String collectionString) {

		boolean result = true;

		result = collectionString.matches("^[0-9,]+$");

		int comma = 0;
		int digits = 0;

		if (result) {
			
			for( int i=0; i<collectionString.length(); i++ ) {
				
				if( collectionString.charAt(i) == ',' ) {
					comma++;
				} 

				if (Character.isDigit(collectionString.charAt(i))) {
					digits++;
				}

			}

			if(comma >= digits) result = false;
			
		}


		return result;
	}

}

