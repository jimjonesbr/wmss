package de.wwu.wmss.web;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.InvalidWMSSRequestException;
import de.wwu.wmss.core.WMSSRequest;
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

	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException
	{    	

		try {

			logger.info("GET Request String -> " + httpRequest.getQueryString());
			WMSSRequest wmssRequest = new WMSSRequest(httpRequest);

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");


			if (wmssRequest.getRequestType().equals("checklog")) {

				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(Util.loadFileTail(new File("logs/system.log"), SystemSettings.getLogPreview()));


			} else if (wmssRequest.getRequestType().equals("describeservice")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getServiceDescription());


			} else if (wmssRequest.getRequestType().equals("getscore")) { 

				response.setContentType("text/xml");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getScore(wmssRequest));

			} else if (wmssRequest.getRequestType().equals("listscores")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getScoreList(wmssRequest));

			} else if (wmssRequest.getRequestType().equals("deletescore")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.deleteScore(wmssRequest));

			} else if (wmssRequest.getRequestType().equals("insertscore")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport(ErrorCodes.NONSUPPORTED_REQUEST_DESCRIPTION +" [GET/"+wmssRequest.getRequestType()+"]", ErrorCodes.NONSUPPORTED_REQUEST_CODE, ErrorCodes.NONSUPPORTED_REQUEST_DATE_HINT));
			}

		} catch (InvalidWMSSRequestException e) {
			
			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(ServiceMessagingHandler.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();
			
		}

	}

}

