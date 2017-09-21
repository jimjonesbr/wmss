package de.wwu.wmss.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletWMSS extends HttpServlet
{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{    	
    	Enumeration<String> listParameters = request.getParameterNames();
    	
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
    	
    	System.out.println(request.getQueryString());
    	
    	while(listParameters.hasMoreElements() ) {
    	       
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
    		} 
    	       
    	}
    	
    	
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
    	
    	response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>WMSS</h1>");
        
    }
}

