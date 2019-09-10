package de.wwu.wmss.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.wwu.wmss.core.WMSSRequest;
import de.wwu.wmss.engine.DocumentBuilder;
import de.wwu.wmss.exceptions.InvalidWMSSRequestException;

public class ServletWMSS extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException{
		
		try {
									
			WMSSRequest wmssRequest = new WMSSRequest(httpRequest);

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
			response.setCharacterEncoding("UTF-8");

			if(!wmssRequest.getResponseHeaderName().equals("") && !wmssRequest.getResponseHeaderValue().equals("")) {
				response.setHeader(wmssRequest.getResponseHeaderName(), wmssRequest.getResponseHeaderValue());	
			}

			response.setContentType(wmssRequest.getResponseContentType());
			response.setStatus(wmssRequest.getResponseStatus());
			response.getWriter().println(wmssRequest.getResponseContent());

		} catch (InvalidWMSSRequestException e) {

			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();
		}
		
	}
	
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException
	{    	

		try {

			WMSSRequest wmssRequest = new WMSSRequest(httpRequest);

			response.addHeader("Access-Control-Allow-Origin","*");
			response.addHeader("Access-Control-Allow-Methods","GET,POST");
			response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
			response.setCharacterEncoding("UTF-8");

			if(!wmssRequest.getResponseHeaderName().equals("") && !wmssRequest.getResponseHeaderValue().equals("")) {
				response.setHeader(wmssRequest.getResponseHeaderName(), wmssRequest.getResponseHeaderValue());	
			}

			response.setContentType(wmssRequest.getResponseContentType());
			response.setStatus(wmssRequest.getResponseStatus());
			response.getWriter().println(wmssRequest.getResponseContent());

		} catch (InvalidWMSSRequestException e) {
			response.setContentType("text/javascript");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(DocumentBuilder.getServiceExceptionReport(e.getCode(), e.getMessage(), e.getHint()));
			e.printStackTrace();
		} 
		
	}

}

