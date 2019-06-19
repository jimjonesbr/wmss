package de.wwu.wmss.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class IStG {


	public static void main(String[] args) {

		String SPARQL = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" + 
				"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" + 
				"PREFIX schema: <http://schema.org/>\n" + 
				"PREFIX cc: <http://creativecommons.org/ns#>\n" + 
				"PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" + 
				"PREFIX prov: <http://www.w3.org/ns/prov#>\n" + 
				"PREFIX wikibase: <http://wikiba.se/ontology#>\n" + 
				"PREFIX wdata: <http://www.wikidata.org/wiki/Special:EntityData/>\n" + 
				"PREFIX wd: <http://www.wikidata.org/entity/>\n" + 
				"PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" + 
				"PREFIX wdtn: <http://www.wikidata.org/prop/direct-normalized/>\n" + 
				"\n" + 
				"# Get cities by label\n" + 
				"SELECT DISTINCT ?city ?cityLabel\n" + 
				"WHERE\n" + 
				"{\n" + 
				"	BIND(wd:Q515 AS ?c)\n" + 
				"	?city wdt:P31/wdt:P279* ?c .  # find instances of subclasses of city\n" + 
				"	?city rdfs:label ?cityLabel .\n" + 
				"	FILTER(LANG(?cityLabel) = \"de\").\n" + 
				"}\n";

		Query query = QueryFactory.create(SPARQL);

		//System.out.println(query);		

		QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query);

		ResultSet results = qexec.execSelect();	
				
		StringBuilder sb = new StringBuilder();
		
		for ( ; results.hasNext() ; ) {
			
			QuerySolution soln = results.nextSolution() ;		
			sb.append(soln.get("?city").toString() + ";" + soln.getLiteral("?cityLabel").getLexicalForm()+"\n");			

		}

		

		System.out.println(sb.toString());
		
		try (PrintStream out = new PrintStream(new FileOutputStream("/home/jones/Schreibtisch/filename.txt"))) {
		    out.print(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		qexec.close();

		/**
		 * Command to import CSV output into PostgreSQL:
		 * 
		 * cat /home/jones/Schreibtisch/filename.txt | psql "host=udc2pgsql4.uni-muenster.de port=54331 dbname=istg user=jones sslmode=verify-full sslkey=/home/jones/.postgresql/postgresql.key sslcert=/home/jones/.postgresql/postgresql.crt sslrootcert=/home/jones/.postgresql/root.crt" -c "TRUNCATE TABLE places; COPY places(uri,label) FROM STDIN WITH CSV HEADER DELIMITER ';'; UPDATE places SET source = 'wikidata' WHERE source IS NULL;" 
		 */
		




	}

}
