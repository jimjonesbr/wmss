package de.wwu.wmss.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import de.wwu.wmss.settings.SystemSettings;


public class ServletWebAdmin extends HttpServlet {

	private static Logger logger = Logger.getLogger("Servlet-Admin");
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException {

		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
				
		ServletContext context = httpRequest.getServletContext();
		String filename = httpRequest.getRequestURI().toString().replace("/"+SystemSettings.getService()+"/admin", "web/");
		File file = new File(filename);
		
		response.setContentType(context.getMimeType(filename));
		response.setContentLength((int)file.length());

		logger.info("MIME Type -> "+context.getMimeType(filename));
		logger.info("GET RequestURL -> " + httpRequest.getRequestURL());
		
		FileInputStream inputStream = new FileInputStream(file);
		OutputStream out = response.getOutputStream();
		

		byte[] buf = new byte[1024];
		int count = 0;
		while ((count = inputStream.read(buf)) >= 0) {
			out.write(buf, 0, count);
		}
		out.close();
		inputStream.close();	

	}



}

