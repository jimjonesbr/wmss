package de.wwu.wmss.connector;

import org.apache.log4j.Logger;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.settings.Util;

import java.util.Date;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;

/**
 * @author Jim Jones
 * @version 1.0
 */

public class SPARQLConnector {

	static Logger  logger = Logger.getLogger("TripleStore-Connector");

	public SPARQLConnector() {
		super();
	}

	public static ResultSet executeQuery(String SPARQL, DataSource ds){

		ResultSet results = null;
		String endpoint = "";
		
		if(ds.getStorage().toLowerCase().equals("graphdb")) {
			
			endpoint = ds.getHost() + ":" + ds.getPort() + "/repositories/" + ds.getRepository();
			
		}
		
		Query query = QueryFactory.create(SPARQL);
		logger.info("SPARQL Query fired at the endpoint [" + endpoint + "]: \n\n" + SPARQL + "\n\n");
		Date start = new Date();

		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

		results = qexec.execSelect();
		
		logger.info("SPARQL Query time [" + endpoint + "]: " + Util.timeElapsed(start, new Date()));
		
		return results;

	}

}