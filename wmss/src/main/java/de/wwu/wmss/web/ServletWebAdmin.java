package de.wwu.wmss.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.wwu.wmss.settings.SystemSettings;


public class ServletWebAdmin extends HttpServlet {

	private static Logger logger = Logger.getLogger("ServletImport");
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException {
	
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

		logger.info("GET RequestURL -> " + httpRequest.getRequestURL());
		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    
			BufferedReader in = null;
			
			if(httpRequest.getRequestURI().toString().equals("/"+SystemSettings.getService()+"/admin")) {

				in = new BufferedReader(new FileReader("web/index.html"));

			} else {
				
				in = new BufferedReader(new FileReader(httpRequest.getRequestURI().toString().replace("/"+SystemSettings.getService()+"/admin", "web/")));
			}

		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }

		    in.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String content = contentBuilder.toString();
				
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(content);

	}

	
	
}

