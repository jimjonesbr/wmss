package de.wwu.wmss.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ServletFiles extends HttpServlet {

	/**
	 * @author Jim Jones
	 */
	private static final long serialVersionUID = 1L;
	
//	protected void doPost(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException{
//
//	}
	
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException {
	
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

		
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader("web/index.html"));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		
		
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(content);

//        RequestDispatcher view = httpRequest.getRequestDispatcher("/web/index.html");
//        view.forward(httpRequest, response);    
	}

}

