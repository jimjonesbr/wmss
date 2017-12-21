package de.wwu.wmss.tests;

import java.util.Date;
import org.apache.jena.query.ResultSet;
import org.apache.log4j.Logger;

import de.wwu.wmss.connectors.SPARQLConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.settings.Util;

public class TestSPARQL {
	
	static Logger  logger = Logger.getLogger("Jena-Connector");
	
	public static void main(String[] args) {
		
		
		String query = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
						"PREFIX chord: <http://purl.org/ontology/chord/>\n" + 
						"PREFIX note: <http://purl.org/ontology/chord/note/>\n" + 
						"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
						"PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
						"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
						"SELECT\n" + 
						"DISTINCT\n" + 
						"?scoreNode\n" + 
						"?scoreTitle\n" + 
						"?creator\n" + 
						"?creatorNode\n" + 
						"?movemenTitle\n" + 
						"?measure\n" + 
						"?thumbnail\n" + 
						"?partID\n" + 
						"?partName\n" + 
						"?voiceID\n" + 
						"?staffID\n" + 
						"?identifier\n" + 
						"WHERE {\n" + 
						"?scoreNode foaf:thumbnail ?thumbnail.\n" + 
						"?scoreNode dc:identifier ?identifier.\n" + 
						"?scoreNode dc:title ?scoreTitle.\n" + 
						"?scoreNode mo:movement ?movementNode.\n" + 
						"?movementNode dc:title ?movemenTitle.\n" + 
						"?scoreNode dc:creator ?creatorNode.\n" + 
						"?creatorNode foaf:name ?creator.\n" + 
						"?movementNode mso:hasScorePart ?part.\n" + 
						"?movementNode dc:title ?movement.\n" + 
						"?part mso:hasMeasure ?measureNode.\n" + 
						"?part rdfs:ID ?partID.\n" + 
						"?part dc:description ?partName.\n" + 
						"?part mso:hasStaff ?staff.\n" + 
						"?measureNode rdfs:ID ?measure.\n" + 
						"?voice a mso:Voice.\n" + 
						"?voice rdfs:ID ?voiceID.\n" + 
						"?measureNode mso:hasNoteSet ?noteset0.\n" + 
						"?staff mso:hasVoice ?voice.\n" + 
						"?staff rdfs:ID ?staffID.\n" + 
						"?noteset0 mso:hasNote ?note0.\n" + 
						"?voice mso:hasNoteSet ?noteset0.\n" + 
						"?note0 chord:natural note:C.\n" + 
						"?noteset0 mso:hasDuration ?duration0.\n" + 
						"?duration0 a mso:Whole.\n" + 
						"?noteset0 mso:nextNoteSet ?noteset1.\n" + 
						"?noteset1 mso:hasNote ?note1.\n" + 
						"?voice mso:hasNoteSet ?noteset1.\n" + 
						"?note1 chord:natural note:C.\n" + 
						"?noteset1 mso:hasDuration ?duration1.\n" + 
						"?duration1 a mso:Whole.\n" + 
						"FILTER ( NOT EXISTS {?note0 chord:modifier ?modifier} )\n" + 
						"FILTER ( NOT EXISTS {?note1 chord:modifier ?modifier} )\n" + 
						"}\n" + 
						"ORDER BY\n" + 
						"?scoreTitle\n" + 
						"?movemenTitle\n" + 
						"?measure\n" + 
						"LIMIT 1000";
		
		
		//query = "select * where {?s ?p ?o} limit 100";
		
		Date start = new Date();
		//String endpoint = "http://linkeddata.uni-muenster.de:7200/repositories/wwu";
		//String endpoint = "http://localhost:8989/solr/store/sparql";
		String endpoint = "http://localhost:7200/repositories/wwu";
		System.out.println("Query running ... ");
		
		DataSource ds = new DataSource();
		ds.setHost("http://localhost");
		ds.setPort(7200);
		ds.setRepository("wwu");
		ds.setStorage("graphdb");
		
		ResultSet rs = SPARQLConnector.executeQuery(query,ds);		
		
		logger.info("SQL Query time: " + Util.timeElapsed(start, new Date()));
		
//		while (rs.hasNext()) {
//			
//			QuerySolution soln = rs.nextSolution();	
//			
//			System.out.println(soln.getResource("?s").toString());
//			
//		}
		
	}

}
