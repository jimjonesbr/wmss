package de.wwu.wmss.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class ServletWMSS extends HttpServlet {


	public ServletWMSS() {

		super();

	}

	private static final long serialVersionUID = 5582283184868743560L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Enumeration<String> listParameters = request.getParameterNames();

		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Methods","GET,POST");
		response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");		
		response.setStatus(HttpStatus.OK_200);

		
		response.getWriter().println("Web Music Score Service");

		System.out.println("Incoming request ... " + request.getParameter("service"));
		
		while (listParameters.hasMoreElements()) {
			
			String parameter = (String) listParameters.nextElement();
			System.out.println(parameter + " -> "+ request.getParameter(parameter)+"");

		}
	}



}

