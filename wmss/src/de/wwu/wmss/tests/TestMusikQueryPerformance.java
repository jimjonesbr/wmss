package de.wwu.wmss.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import de.wwu.wmss.settings.Util;

public class TestMusikQueryPerformance {


	public static void main(String[] args) {


		StringBuilder sb = new StringBuilder();

		try (PrintWriter writer = new PrintWriter(new File("/home/jones/test-performance.csv"))) {

			String SPARQL = "PREFIX mso: <http://linkeddata.uni-muenster.de/ontology/musicscore#>\n" + 
					"PREFIX chord: <http://purl.org/ontology/chord/>\n" + 
					"PREFIX note: <http://purl.org/ontology/chord/note/>\n" + 
					"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" + 
					"PREFIX rdfs: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
					"PREFIX mo: <http://purl.org/ontology/mo/>\n" + 
					"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
					"SELECT\n" + 
					"DISTINCT\n" + 
					"?scoreNode ?scoreTitle\n" +  
					"WHERE {\n" + 
					"?scoreNode foaf:thumbnail ?thumbnail.\n" + 
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
					"?duration0 a mso:Quarter.\n" + 
					"?noteset0 mso:nextNoteSet ?noteset1.\n" + 
					"\n" + 
					"?noteset1 mso:hasNote ?note1.\n" + 
					"?voice mso:hasNoteSet ?noteset1.\n" + 
					"?note1 chord:natural note:D.\n" + 
					"?noteset1 mso:hasDuration ?duration1.\n" + 
					"?duration1 a mso:Quarter.\n" + 
					"?noteset1 mso:nextNoteSet ?noteset2.\n" + 
					"\n" + 
					"?noteset2 mso:hasNote ?note2.\n" + 
					"?voice mso:hasNoteSet ?noteset2.\n" + 
					"?note2 chord:natural note:E.\n" + 
					"?noteset2 mso:hasDuration ?duration2.\n" + 
					"?duration2 a mso:Quarter.\n" + 
					"?noteset2 mso:nextNoteSet ?noteset3.\n" + 
					"\n" + 
					"?noteset3 mso:hasNote ?note3.\n" + 
					"?voice mso:hasNoteSet ?noteset3.\n" + 
					"?note3 chord:natural note:F.\n" + 
					"?noteset3 mso:hasDuration ?duration3.\n" + 
					"?duration3 a mso:Quarter.\n" + 
					"?noteset3 mso:nextNoteSet ?noteset4.\n" + 
					"    \n" + 
					"?noteset4 mso:hasNote ?note4.\n" + 
					"?voice mso:hasNoteSet ?noteset4.\n" + 
					"?note4 chord:natural note:G.\n" + 
					"?noteset4 mso:hasDuration ?duration4.\n" + 
					"?duration4 a mso:Quarter.\n" + 
					"#?noteset4 mso:nextNoteSet ?noteset4.\n" + 
					"    \n" + 
					"FILTER ( NOT EXISTS {?note0 chord:modifier ?modifier} )\n" + 
					"FILTER ( NOT EXISTS {?note1 chord:modifier ?modifier} )\n" + 
					"FILTER ( NOT EXISTS {?note2 chord:modifier ?modifier} )    \n" + 
					"FILTER ( NOT EXISTS {?note3 chord:modifier ?modifier} )  \n" + 
					"FILTER ( NOT EXISTS {?note4 chord:modifier ?modifier} )\n" + 
					"    \n" + 
					"}\n";

			System.out.println(SPARQL);

			System.out.println("\n########################  GraphDB  #################################\n");

			for (int i = 0; i < 100; i++) {

				Date startSPARQL = new Date();

				Query query = QueryFactory.create(SPARQL);				

				QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:7200/repositories/wwu", query);

				ResultSet results = qexec.execSelect();	
				//			for ( ; results.hasNext() ; ) {
				//
				//				QuerySolution soln = results.nextSolution() ;			      
				//				//url = soln.get("?scoreTitle").toString();
				//				//System.out.println(url);
				//			}

				qexec.close();

				System.out.println(i+" - "+Util.timeElapsed(startSPARQL, new Date()));
				sb.append(Util.timeElapsed(startSPARQL, new Date())+",graphdb\n");

			}

			System.out.println("\n########################  Neo4j  #################################\n");

			for (int i = 0; i < 100; i++) {

				Date startCYPHER = new Date();
				Driver drv = GraphDatabase.driver("bolt://localhost",AuthTokens.basic("neo4j","123456"));

				try ( Session session = drv.session() )
				{
					StatementResult result = session.run("MATCH (scr:mo__Score)-[:dc__creator]->(creator:foaf__Person)\n" + 
							"MATCH (measure1:mso__Measure)-[:mso__hasNoteSet]->(ns0:d4)\n" + 
							"MATCH (ns0:d4)-[:mso__hasNote]->(n0:C) \n" + 
							"MATCH (ns1:d4)-[:mso__hasNote]->(n1:D) \n" + 
							"MATCH (ns0:d4)-[:mso__nextNoteSet]->(ns1:d4) \n" + 
							"MATCH (ns2:d4)-[:mso__hasNote]->(n2:E) \n" + 
							"MATCH (ns1:d4)-[:mso__nextNoteSet]->(ns2:d4) \n" + 
							"MATCH (ns3:d4)-[:mso__hasNote]->(n3:F) \n" + 
							"MATCH (ns2:d4)-[:mso__nextNoteSet]->(ns3:d4) \n" + 
							"MATCH (ns4:d4)-[:mso__hasNote]->(n4:G) \n" + 
							"MATCH (ns3:d4)-[:mso__nextNoteSet]->(ns4:d4) \n" + 
							"MATCH (scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasMeasure]->(measure1:mso__Measure)\n" + 
							"\n" + 
							"WHERE TRUE \n" + 
							"AND ns0.size = 1 \n" + 
							"AND ns1.size = 1 \n" + 
							"AND ns2.size = 1 \n" + 
							"AND ns3.size = 1 \n" + 
							"AND ns4.size = 1 \n" + 
							"\n" + 
							"RETURN COUNT(scr)\n" + 
							"");
				}

				drv.session().close();

				sb.append(Util.timeElapsed(startCYPHER, new Date())+",neo4j\n");

				System.out.println(i+" - "+Util.timeElapsed(startCYPHER, new Date()));

			}

			writer.write(sb.toString());


		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}

		System.exit(0);
	}

}
