package de.wwu.wmss.web;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletFileAccess extends HttpServlet {

	/**
	 * @author Jim Jones
	 */
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse response) throws ServletException, IOException {
	
		String file = "";	
		String content = "";
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
		response.setCharacterEncoding("UTF-8");

		Enumeration<String> listParameters = httpRequest.getParameterNames();
		
		while(listParameters.hasMoreElements() ) {
			String parameter = (String) listParameters.nextElement();			
			if (parameter.toLowerCase().equals("get")) {
				file = httpRequest.getParameter(parameter);
				response.setHeader("Content-disposition", "attachment; filename="+file);
			}
		}
		
		if (file.equals("")) {
			content = "Invalid request.";		
		} else {		
			StringBuilder contentBuilder = new StringBuilder();
			try {
								
				BufferedReader in = new BufferedReader(new FileReader("upload/"+file));
			    String str;
			    while ((str = in.readLine()) != null) {
			        contentBuilder.append(str +System.getProperty("line.separator"));
			    }
			    in.close();
			} catch (IOException e) {
			}
			content = contentBuilder.toString();
		}
		
		response.setContentType("text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println(content);
  
	}

	
	
}

