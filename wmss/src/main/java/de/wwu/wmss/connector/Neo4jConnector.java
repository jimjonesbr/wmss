package de.wwu.wmss.connector;

import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;
import java.util.Date;
import java.util.concurrent.CompletionStage;
import java.util.logging.LogManager;
import org.apache.log4j.Logger;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.Transaction;

public class Neo4jConnector {
		
	private static Logger logger = Logger.getLogger("Neo4j-Connector");
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
		LogManager.getLogManager().reset();

		Date start = new Date();
		
		if(ds.getNeo4jConnectionDriver() == null) {		
			for (int i = 0; i < SystemSettings.sourceList.size(); i++) {
				if(SystemSettings.sourceList.get(i).getId().equals(ds.getId())) {
					SystemSettings.sourceList.get(i).setNeo4jConnectionDriver(GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword())));
					ds.setNeo4jConnectionDriver(GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword())));					
					logger.debug("Neo4j connection driver created.");
				}
			}
		}
		
		//StatementResult result;
		
//		Driver driver = GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword()));
//		Session session = driver.session();
//		CompletionStage<StatementResultCursor> cursorStage = session.runAsync(cypher);
//		
//		cursorStage.thenCompose(StatementResultCursor::listAsync)
//        .whenComplete((records, error) -> {
//               
//        	if (records != null) {
//            	   System.out.println( records );
//            	   
//               }
//               else {
//            	   error.printStackTrace();
//               }
//               
//               session.closeAsync();
//        });
		

		
//		Driver driver = GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword()));
//		Session session = driver.session();
//		CompletionStage<StatementResultCursor> cursorStage = session.runAsync(cypher);
//			
//		StatementResult result = null;	
//		cursorStage.thenCompose(StatementResultCursor::listAsync)
//        .whenComplete((records, error) -> {
//               
//        	
//        	if (records != null) {
//            	   //System.out.println( records );
//            	for (int i = 0; i < records.size(); i++) {
//            		result.list().add(records.get(i));            		
//    			}
//            	   
//               }
//               else {
//            	   error.printStackTrace();
//               }
//               
//               session.closeAsync();
//        });
//
		
		Driver driver = GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword()));
		StatementResult result;

		try(Transaction tx = driver.session().beginTransaction())
		{
			result = tx.run(cypher);
			tx.success();
			tx.close();
		}	
		
		logger.debug(cypher);
		logger.debug("Cypher query time ["+ds.getHost()+"]: " + Util.timeElapsed(start, new Date()));

		//ds.getNeo4jConnectionDriver().session().close();
		driver.session().close();
		
		return result;
	}

}
