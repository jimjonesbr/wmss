package de.wwu.wmss.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import de.wwu.wmss.settings.SystemSettings;

public class Start {

	public static void main(String[] args) throws Exception {
		
		org.eclipse.jetty.util.log.Log.setLog(null);		
		
		SystemSettings.loadSystemSettings();
		SystemSettings.loadDataSources();

		Server server = new Server(SystemSettings.getPort());

		ServletContextHandler contextAPI = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextAPI.setContextPath("/"+SystemSettings.getService());
		
		server.setHandler(contextAPI);
		
		contextAPI.addServlet(new ServletHolder(new ServletWMSS()),"/*");
		contextAPI.addServlet(new ServletHolder(new ServletImport()),"/import");
		
		ServletContextHandler contextWebApp = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextWebApp.setContextPath("/admin");
		contextWebApp.addServlet(new ServletHolder(new ServletFiles()),"/");
		
		server.setHandler(contextWebApp);
					
		server.start();
		server.join();

	}

}