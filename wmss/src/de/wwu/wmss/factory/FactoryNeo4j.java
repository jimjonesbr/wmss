package de.wwu.wmss.factory;


import java.util.ArrayList;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import de.wwu.wmss.connectors.Neo4jConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.RequestParameter;

public class FactoryNeo4j {


	private static ArrayList<Note> createNoteSequence(String melody){

		String[] melodyRequest = melody.split(">");
		ArrayList<Note> result = new ArrayList<>();

		for (int i = 0; i < melodyRequest.length; i++) {

			String[] melodyElement = melodyRequest[i].split("-");

			Note note = new Note();

			if(melodyElement[0]!="*") {
				note.setPitch(melodyElement[0].substring(0, 1).toUpperCase());

			} else {
				note.setPitch(null);
			}
			
			
			if(melodyElement[1]=="*") {
				note.setDuration(null);
			} else if (melodyElement[1].equals("ow")) {
				note.setDuration("");
			} else if (melodyElement[1].equals("qw")) {
				note.setDuration("");
			} else if (melodyElement[1].equals("dw")) {
				note.setDuration("");
			} else if (melodyElement[1].equals("w")) {
				note.setDuration("Whole");
			} else if (melodyElement[1].equals("h")) {
				note.setDuration("Half");
			} else if (melodyElement[1].equals("4")) {
				note.setDuration("Quarter");
			} else if (melodyElement[1].equals("8")) {
				note.setDuration("Eighth");
			} else if (melodyElement[1].equals("16")) {
				note.setDuration("16th");
			} else if (melodyElement[1].equals("32")) {
				note.setDuration("32nd");
			} else if (melodyElement[1].equals("64")) {
				note.setDuration("64th");
			} else if (melodyElement[1].equals("128")) {
				note.setDuration("128th");
			} else if (melodyElement[1].equals("256")) {
				note.setDuration("256th");
			}
			
			if(melodyElement[2]!="*") {
				note.setOctave(melodyElement[2]);
			} else {
				note.setOctave(null);
			}
			
			//TODO: parse accidentals
			//TODO: parse chords
			note.setChord(false);
			note.setAccidental(null);
			
			result.add(note);
			
			
		}

		return result;

	}


	public static ArrayList<MusicScore> getScoreList(ArrayList<RequestParameter> parameters, DataSource dataSource){

		String melody = "";
		String match = "";
		String where = "";
		String ret = "";

		for (int i = 0; i < parameters.size(); i++) {

			if(parameters.get(i).getRequest().equals("melody")){

				melody = parameters.get(i).getValue();

			}	

		}

		if(!melody.equals("")) {

			//ArrayList<MelodyLocation> tmpMelodyLocationList = new ArrayList<MelodyLocation>();	
			ArrayList<Note> noteSequence = createNoteSequence(melody);

			match = "MATCH (creator:foaf__Person)<-[:dc__creator]-(scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(ns0:mso__NoteSet)\n" + 
					"MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail) \n" +
					"MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasNoteSet]->(ns0:mso__NoteSet) \n";

			for (int i = 0; i < noteSequence.size(); i++) {
				
				match = match + 
						"MATCH (ns"+i+":mso__NoteSet)-[:mso__hasNote]->(n"+i+")-[:chord__natural]->(val"+i+" {uri:'http://purl.org/ontology/chord/note/"+noteSequence.get(i).getPitch()+"'}) \n" + 
						"MATCH (ns"+i+":mso__NoteSet)-[:mso__hasDuration]->(:mso__"+ noteSequence.get(i).getDuration() +") \n";
				
				if(i <= noteSequence.size()-1 && i > 0) match = match + "MATCH (ns"+(i-1)+":mso__NoteSet)-[:mso__nextNoteSet]->(ns"+i+":mso__NoteSet)\n";
				
				if(noteSequence.get(i).getAccidental() == null) {
					where = where + "NOT EXISTS ((n"+i+")-[:chord__modifier]->()) ";
					
					if(i < noteSequence.size()-1) where = where + "AND \n";
				}
				 
			}
			
			 ret = "RETURN \n" + 
					"    scr.dc__title AS title,\n" + 
					"    scr.uri AS identifier,\n" + 
					"    thumbnail.uri AS thumbnail,\n" + 
					"    {persons: COLLECT(DISTINCT creator)} AS persons,\n" + 
					"	{score: scr.dc__title, \n" + 
					"     locations:\n" + 
					"      COLLECT(DISTINCT { \n" + 
					"       movementIdentifier: mov.uri,\n" + 
					"       movementName: mov.dc__title,\n" + 
					"       startingMeasure: measure.rdfs__ID, \n" + 
					"       staff: staff.rdfs__ID , \n" + 
					"       voice: voice.rdfs__ID, \n" + 
					"       instrumentName: part.dc__description \n" + 
					"      })    \n" + 
					"    } AS locations\n" + 
					"ORDER BY scr.dc__title "; 

		}



		String cypher = match + "\n WHERE \n" + where  + ret;

		System.out.println(cypher);

		StatementResult rs = Neo4jConnector.executeQuery(cypher, dataSource);

		while ( rs.hasNext() )
		{
			Record record = rs.next();
			System.out.println( record.get( "identifier" ).asString() + " > " + record.get( "title" ).asString());    

		}


		return null;
	}
}
