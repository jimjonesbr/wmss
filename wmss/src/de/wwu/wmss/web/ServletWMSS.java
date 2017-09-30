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
		String group = "";
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
				req.setRequest("requestType");
				req.setValue(request.getParameter(parameter).toLowerCase());
			} else if (parameter.toLowerCase().equals("format")) {
				
				format=request.getParameter(parameter).toLowerCase();
				req.setRequest("format");
				req.setValue(request.getParameter(parameter).toLowerCase());
				
			} else if (parameter.toLowerCase().equals("group")) {
				
				group=request.getParameter(parameter).toLowerCase();
				req.setRequest("group");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("person")) {
				
				person=request.getParameter(parameter).toLowerCase();
				req.setRequest("person");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("personrole")) {
				
				personRole=request.getParameter(parameter).toLowerCase();
				req.setRequest("personRole");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("performancemedium")) {
				
				performanceMedium=request.getParameter(parameter).toLowerCase();
				req.setRequest("performanceMedium");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("performancemediumtype")) {
				
				performanceMediumType=request.getParameter(parameter).toLowerCase();
				req.setRequest("performanceMediumType");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("solo")) {
				
				solo=request.getParameter(parameter).toLowerCase();
				req.setRequest("solo");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("tonalitytonic")) {
				
				tonalityTonic=request.getParameter(parameter).toLowerCase();
				req.setRequest("tonalityTonic");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("tonalitymode")) {
				
				tonalityMode=request.getParameter(parameter).toLowerCase();
				req.setRequest("tonalityMode");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("tempo")) {
				
				tempo=request.getParameter(parameter).toLowerCase();
				req.setRequest("tempo");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("creationdate")) {
				
				creationDate=request.getParameter(parameter).toLowerCase();
				req.setRequest("creationDate");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("creationdatefrom")) {
				
				creationDateFrom=request.getParameter(parameter).toLowerCase();
				req.setRequest("creationDateFrom");
				req.setValue(request.getParameter(parameter).toLowerCase());

			} else if (parameter.toLowerCase().equals("creationdateto")) {
				
				creationDateTo=request.getParameter(parameter).toLowerCase();
				req.setRequest("creationDateTo");
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
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0001", "No request type provided"));

		} else if (!requestType.equals("listscores") 
				&& !requestType.equals("getscore")
				&& !requestType.equals("checklog")
				&& !requestType.equals("describeservice")){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0002", "Invalid request parameter [" + requestType + "]."));

		} else if (!format.equals("mei") && !format.equals("musicxml") && !format.equals("") ){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0003", "Invalid document format [" + format + "]."));

		} else if (!tonalityMode.equals("minor") && !tonalityMode.equals("major") && !tonalityMode.equals("") ){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0004", "Invalid mode [" + tonalityMode + "]."));

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
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0005", "Invalid tonic [" + tonalityTonic + "]."));

		} else if (!solo.equals("true") &&
				!solo.equals("false") &&
				!solo.equals(""))	{

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0006", "Invalid boolean value for 'solo' parameter [" + solo + "]."));

		} else if (!version.equals("") &&
				!SystemSettings.protocolVersions.contains(version.toString())){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0007", "Invalid protocol version [" + version+ "]."));

		} else if (!validSource) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0009", "Invalid data source [" + source + "]."));

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
				response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0008", "No identifier provided for GetScore request."));

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
			//TODO change value of active data source to boolean. Currently text
			//TODO create validation of filter capabilities based on the sources.json document
			//TODO create search for scores based on all search criteria

		}

	}
}

