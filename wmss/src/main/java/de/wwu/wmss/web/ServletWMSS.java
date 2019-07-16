package de.wwu.wmss.web;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.WMSSRequest;
import de.wwu.wmss.engine.DocumentBuilder;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;
import de.wwu.wmss.settings.Util;

public class ServletWMSS extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger("ServletWMSS");

	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException
	{    	

		try {

			logger.info("GET Request String -> " + httpRequest.getQueryString());
			logger.info("GET RequestURL -> " + httpRequest.getRequestURL());
			
			WMSSRequest wmssRequest = new WMSSRequest(httpRequest);

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
			response.setCharacterEncoding("UTF-8");

			if (wmssRequest.getRequestType().equals("checklog")) {

				response.setContentType("text/plain");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(Util.loadFileTail(new File("logs/system.log"), wmssRequest.getLogPreview()));

			} else if (wmssRequest.getRequestType().equals("describeservice")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(DocumentBuilder.getServiceDescription());

			} else if (wmssRequest.getRequestType().equals("getscore")) { 

				response.setHeader("Content-disposition", "attachment; filename=score.xml");
				response.setContentType("text/xml");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(DocumentBuilder.getScore(wmssRequest));

			} else if (wmssRequest.getRequestType().equals("listscores")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(DocumentBuilder.getScoreList(wmssRequest));

			} else if (wmssRequest.getRequestType().equals("deletescore")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(DocumentBuilder.deleteScore(wmssRequest)); 

			} else if (wmssRequest.getRequestType().equals("insertscore")) {

				response.setContentType("text/javascript");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println(DocumentBuilder.getServiceExceptionReport(ErrorCodes.NONSUPPORTED_REQUEST_DESCRIPTION +" [GET/"+wmssRequest.getRequestType()+"]", ErrorCodes.NONSUPPORTED_REQUEST_CODE, ErrorCodes.NONSUPPORTED_REQUEST_DATE_HINT));
			}

		} catch (InvalidWMSSRequestException e) {
			
			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();
		}

	}

}

