package de.wwu.wmss.connectors;

import de.wwu.wmss.core.DataSource;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

public class Neo4jConnector {
	private static Driver driver;

	public static StatementResult executeQuery(String cypher, DataSource ds){

		driver = GraphDatabase.driver(ds.getHost(),AuthTokens.basic(ds.getUser(),ds.getPassword()));

		StatementResult result;
		
		try ( Session session = driver.session() )
		{

			result = session.run(cypher);

		}

		driver.close();
		
		return result;
	}

}
