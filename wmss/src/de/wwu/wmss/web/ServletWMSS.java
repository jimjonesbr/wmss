package de.wwu.wmss.web;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.wwu.wmss.core.ServiceMessagingHandler;
import de.wwu.wmss.core.SystemSettings;

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

		System.out.println(request.getQueryString());

		while(listParameters.hasMoreElements() ) {

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

			String parameter = (String) listParameters.nextElement();
			System.out.println(parameter + " -> "+ request.getParameter(parameter)+"");

			if (parameter.toLowerCase().equals("request")) {
				requestType=request.getParameter(parameter);    			
			} else if (parameter.toLowerCase().equals("format")) {
				format=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("group")) {
				group=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("person")) {
				person=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("personrole")) {
				personRole=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("performancemedium")) {
				performanceMedium=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("performancemediumtype")) {
				performanceMediumType=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("solo")) {
				solo=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("tonalitytonic")) {
				tonalityTonic=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("tonalitymode")) {
				tonalityMode=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("tempo")) {
				tempo=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("creationdate")) {
				creationDate=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("creationdatefrom")) {
				creationDateFrom=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("creationdateto")) {
				creationDateTo=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("melody")) {
				melody=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("identifier")) {
				identifier=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("version")) {
				version=request.getParameter(parameter); 
			} else if (parameter.toLowerCase().equals("source")) {
				source=request.getParameter(parameter); 
			} 

		}

		boolean validSource = false;


		if (!source.equals("")){

			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

				System.out.println("DEBUG ->>" + SystemSettings.sourceList.get(i).getId().toString());
				if (validSource==false){

					if (source.toLowerCase().equals(SystemSettings.sourceList.get(i).getId().toString().toLowerCase())){

						validSource = true;
						System.out.println("found");

					} 

				}
			}
		}


		System.out.println("DEBUG ->> " + validSource);

		if(requestType.equals("")){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0001", "No request type provided"));

		} else if (!requestType.toLowerCase().equals("listscores") 
				&& !requestType.toLowerCase().equals("getscore")
				&& !requestType.toLowerCase().equals("describeservice")){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0002", "Invalid request parameter [" + requestType + "]."));

		} else if (!format.toLowerCase().equals("mei") && !format.toLowerCase().equals("musicxml") && !format.toLowerCase().equals("") ){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0003", "Invalid document format [" + format + "]."));

		} else if (!tonalityMode.toLowerCase().equals("minor") && !tonalityMode.toLowerCase().equals("major") && !tonalityMode.toLowerCase().equals("") ){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0004", "Invalid mode [" + tonalityMode + "]."));

		} else if (!tonalityTonic.toLowerCase().equals("a") && 
				!tonalityTonic.toLowerCase().equals("aflat") && 
				!tonalityTonic.toLowerCase().equals("asharp") && 
				!tonalityTonic.toLowerCase().equals("b") &&
				!tonalityTonic.toLowerCase().equals("bflat") &&
				!tonalityTonic.toLowerCase().equals("bsharp") &&
				!tonalityTonic.toLowerCase().equals("c") &&
				!tonalityTonic.toLowerCase().equals("cflat") &&
				!tonalityTonic.toLowerCase().equals("csharp") &&
				!tonalityTonic.toLowerCase().equals("d") &&
				!tonalityTonic.toLowerCase().equals("dflat") &&
				!tonalityTonic.toLowerCase().equals("dsharp") &&
				!tonalityTonic.toLowerCase().equals("e") &&
				!tonalityTonic.toLowerCase().equals("eflat") &&
				!tonalityTonic.toLowerCase().equals("esharp") &&
				!tonalityTonic.toLowerCase().equals("f") &&
				!tonalityTonic.toLowerCase().equals("fflat") &&
				!tonalityTonic.toLowerCase().equals("fsharp") &&
				!tonalityTonic.toLowerCase().equals("g") &&
				!tonalityTonic.toLowerCase().equals("gflat") &&
				!tonalityTonic.toLowerCase().equals("gsharp") &&
				!tonalityTonic.toLowerCase().equals("") 
				){


			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0005", "Invalid tonic [" + tonalityTonic + "]."));

		} else if (!solo.toLowerCase().equals("true") &&
				!solo.toLowerCase().equals("false") &&
				!solo.toLowerCase().equals(""))	{

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0006", "Invalid boolean value for 'solo' parameter [" + solo + "]."));

		} else if (!version.equals("") &&
				!SystemSettings.versions.contains(version.toString())){

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0007", "Invalid protocol version [" + version+ "]."));

		} else if (!validSource) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0009", "Invalid data source [" + source + "]."));



		} else if (requestType.toLowerCase().equals("describeservice")) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceDescription());


		} else if (requestType.toLowerCase().equals("getscore")) { 

			if (identifier.equals("")){

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport("E0008", "No identifier provided for GetScore request."));

			} else {


				//TODO create search for score based on identifier

			}


		} else if (requestType.toLowerCase().equals("listscores")) {

			//TODO create search for scores based on all search criteria

		}

	}
}

