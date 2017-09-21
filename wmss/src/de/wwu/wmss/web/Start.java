package de.wwu.wmss.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Start {

	public static void main(String[] args) throws Exception {

        Server server = new Server(8295);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/wmss");
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(new ServletWMSS()),"/*");
 
        server.start();
        server.join();
        
	}

}