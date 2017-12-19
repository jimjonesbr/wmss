package de.wwu.wmss.factory;

import java.util.ArrayList;

import org.apache.jena.query.ResultSet;

import de.wwu.wmss.connectors.SPARQLConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.PerformanceMediumType;

public class FactoryTripleStore {

	public static ArrayList<PerformanceMediumType> getPerformanceMediumList(DataSource dataSource){
		
		String prefixes = 				
				"PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
				"PREFIX chord: <http://purl.org/ontology/chord/>\n" + 
				"PREFIX note: <http://purl.org/ontology/chord/note/>\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
				"PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"; 
 
		String select = 
				"	?scoreNode ?scoreTitle ?creator ?creatorNode ?movemenTitle ?measure ?thumbnail ?partID ?partName ?voiceID ?staffID ?identifier\n" ; 
		
		String whereClause =
				"	?scoreNode foaf:thumbnail ?thumbnail.\n" + 
				"	?scoreNode dc:identifier ?identifier.\n" + 
				"	?scoreNode dc:title ?scoreTitle.\n" +				
				"	?scoreNode mo:movement ?movementNode.\n" + 
				"	?movementNode dc:title ?movemenTitle.\n" + 
				"	?scoreNode dc:creator ?creatorNode.\n" + 
				"	?creatorNode foaf:name ?creator.\n";
			
		String orderBy = 
				"ORDER BY\n" + 
				"?scoreTitle\n" + 
				"?movemenTitle\n" + 
				"?measure\n" + 
				"LIMIT 1000";
		
		
		ResultSet rs = SPARQLConnector.executeQuery(prefixes + 
												  "SELECT DISTINCT \n" + 
												  select + 
												  "WHERE {\n " + whereClause +" }\n" +
												  orderBy, dataSource);		
		
		return null;
	}
}
