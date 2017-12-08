package de.wwu.wmss.connectors;

import org.apache.log4j.Logger;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;

/**
 * 
 * @author Jim Jones
 * @version 1.0
 */

public class JenaConnector {

	static Logger  logger = Logger.getLogger("Jena-Connector");

	public JenaConnector() {
		super();
	}

	public static ResultSet executeQuery(String SPARQL, String endpoint){

		ResultSet results = null;

		Query query = QueryFactory.create(SPARQL);
		logger.info("SPARQL Query fired at the endpoint [" + endpoint + "]: \n\n" + SPARQL + "\n\n");
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

		results = qexec.execSelect();

		return results;

	}

}