package de.wwu.wmss.web;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import de.wwu.wmss.settings.SystemSettings;

public class Start implements Runnable {

	private static Logger logger = Logger.getLogger("WMSS-Servlet");
	public static int port = 0;
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		Start.port = port;
	}

	public static void main(String[] args) throws Exception  {

		startWMSS();

	}

	
	public static void startWMSS() {

		org.eclipse.jetty.util.log.Log.setLog(null);		

		
		
		SystemSettings.loadSystemSettings();
		SystemSettings.loadDataSources();

		ServletContextHandler contextAPI = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextAPI.setContextPath("/"+SystemSettings.getService());
		contextAPI.addServlet(new ServletHolder(new ServletWMSS()),"/*");
		contextAPI.addServlet(new ServletHolder(new ServletImport()),"/import");
		contextAPI.addServlet(new ServletHolder(new ServletWebAdmin()),"/admin/*");
		contextAPI.addServlet(new ServletHolder(new ServletFileAccess()),"/file");
		
		ServletContextHandler contextWebAdmin = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextWebAdmin.setContextPath("/admin");
		
		if(port!=0) {
			SystemSettings.setPort(port);
		}
		
		
		logger.info("\n\n" + SystemSettings.getTitle()+ "\n" +
				"Service Name: " + SystemSettings.getService() + "\n" +
				"WMSS Version: " + SystemSettings.getServiceVersion() + "\n" +
				"Port: " + SystemSettings.getPort() + "\n" +
				"Application Startup: " + SystemSettings.getStartup() + "\n" +
				"Default Melody Encoding: " + SystemSettings.getDefaultMelodyEncoding() + "\n" +
				"Time-out: " + SystemSettings.getTimeout()+ "ms" + "\n" +
				"Page Size: " + SystemSettings.getPageSize() + " records " + "\n" +
				"System Administrator: " + SystemSettings.getContact()  + "\n");
		
		Server server = new Server(SystemSettings.getPort());	
		server.setHandler(contextAPI);
		
		try {

			server.start();
			server.join();

		} catch (Exception e) {

			e.printStackTrace();
			logger.fatal(e.getMessage() +". Shutting down ...");
			System.exit(1);
		}



	}

	@Override
	public void run() {
		
		startWMSS();
		
	}

}