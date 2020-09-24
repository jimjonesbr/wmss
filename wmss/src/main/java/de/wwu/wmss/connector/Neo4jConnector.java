package de.wwu.wmss.connector;

import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
//import java.util.logging.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Neo4jConnector {
		
	//private static Logger logger = Logger.getLogger("Neo4j-Connector");
	private static Logger logger = LogManager.getLogger("Neo4j-Connector");

	private static Neo4jConnector instance;
	
	public static Neo4jConnector getInstance() {
		if (instance == null) {
			instance = new Neo4jConnector();
		}
		return instance;
	}	
	
	public StatementResult executeQuery(String cypher, DataSource ds){

		/**
		 * Disables logging mechanism of neo4j
		 */
		//TODO: LogManager.getLogManager().reset();

		Date start = new Date();
		
		if(ds.getNeo4jConnectionDriver() == null) {		
			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
				if(SystemSettings.sourceList.get(i).getId().equals(ds.getId())) {
					SystemSettings.sourceList.get(i).setNeo4jConnectionDriver(GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword())));
					ds.setNeo4jConnectionDriver(GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword())));					
					logger.info("Neo4j connection driver created.");
				}
			}
		}
		
		StatementResult result;

		try ( Session session = ds.getNeo4jConnectionDriver().session() )
		{
			result = session.run(cypher);
		}
		
		logger.debug(cypher);
		logger.debug("Cypher query time ["+ds.getHost()+"]: " + Util.timeElapsed(start, new Date()));

		ds.getNeo4jConnectionDriver().session().close();
		
		return result;
	}

}
