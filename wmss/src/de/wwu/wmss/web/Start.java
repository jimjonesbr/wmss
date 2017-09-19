package de.wwu.wmss.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Start {

	public static void main(String[] args) throws Exception {

//		Server server = new Server(8282);
//		ServletContextHandler handler = new ServletContextHandler(server, "/wmss");
//		handler.addServlet(Servlet.class, "/");
//		server.start();
		

		Server server = new Server(8282);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/wmss");
		
    	server.setHandler(context);
		
		context.addServlet(new ServletHolder(new ServletWMSS()),"/*");
		
		
		//ContextHandler contextGUI = new ContextHandler("/admin");
	    //contextGUI.setHandler(new HandlerGUI("Bonjoir"));
	        
//		ContextHandlerCollection contexts = new ContextHandlerCollection();
//        contexts.setHandlers(new Handler[] { context });
        
        server.setHandler(context);

		server.start();
		server.join();
	}

}