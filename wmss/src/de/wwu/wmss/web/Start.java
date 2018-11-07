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

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/"+SystemSettings.getService());
		server.setHandler(context);

		context.addServlet(new ServletHolder(new ServletWMSS()),"/*");
		context.addServlet(new ServletHolder(new ServletImport()),"/import");
		
		server.start();
		server.join();

	}

}