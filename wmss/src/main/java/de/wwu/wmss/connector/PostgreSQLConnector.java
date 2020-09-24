package de.wwu.wmss.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.settings.Util;

public class PostgreSQLConnector {
	
	//private static Logger logger = Logger.getLogger("PostgreSQL-Connector");
	private static Logger logger = LogManager.getLogger("PostgreSQL-Connector");
	
	public static ResultSet executeQuery(String SQL, DataSource ds){
		
		ResultSet rs = null;
		
		try {
				
			logger.info("SQL Query fired ["+ds.getHost()+"]: \n\n"+ SQL +"\n");
			Date start = new Date();
			
			Connection con = DriverManager.getConnection("jdbc:postgresql://"+ds.getHost()+":"+ds.getPort()+"/"+ds.getRepository(), ds.getUser(), ds.getPassword());			
			Statement st = con.createStatement();
			rs = st.executeQuery(SQL);
			
			logger.info("SQL Query time ["+ds.getHost()+"]: " + Util.timeElapsed(start, new Date()));
			//rs.close();
			//st.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		
		return rs;
		
	}
}
