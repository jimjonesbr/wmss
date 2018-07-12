package de.wwu.wmss.connectors;

import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.settings.Util;
import java.util.Date;
import java.util.logging.LogManager;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Neo4jConnector {
	private static Driver driver;
	private static Logger logger = Logger.getLogger("Neo4j-Connector");

	public static StatementResult executeQuery(String cypher, DataSource ds){

		/**
		 * Disables logging mechanism of neo4j
		 */
		LogManager.getLogManager().reset();

		Date start = new Date();
		driver = GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword()));

		StatementResult result;

		try ( Session session = driver.session() )
		{
			result = session.run(cypher);
		}

		logger.info("Cypher query time ["+ds.getHost()+"]: " + Util.timeElapsed(start, new Date()));

		//driver.close();

		return result;
	}

}
