package de.wwu.wmss.factory;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wwu.wmss.connectors.Neo4jConnector;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MelodyLocationGroup;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.Person;
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

		ArrayList<MusicScore> result = new ArrayList<MusicScore>();		
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
	
			ArrayList<Note> noteSequence = createNoteSequence(melody);

			match = "\nMATCH (role)<-[:gndo__professionOrOccupation]-(creator:foaf__Person)<-[:dc__creator]-(scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(ns0:mso__NoteSet)\n" + 
					"MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail) \n" +
					"MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasNoteSet]->(ns0:mso__NoteSet) \n" +
					"WITH creator,role,scr,part,mov,measure,staff,voice,ns0,thumbnail \n" + 
					"MATCH (scr:mo__Score)-[:mo__movement]->(movements:mo__Movement) \n" +
					//"MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person) \n" + 
					"\n";

			for (int i = 0; i < noteSequence.size(); i++) {

				if(i==0) {
					match = match +
							"MATCH (ns0:mso__NoteSet)-[:mso__hasNote]->(n0)-[:chord__natural]->(val0 {uri:'http://purl.org/ontology/chord/note/"+noteSequence.get(i).getPitch()+"'}) \n" + 
							"MATCH (ns0:mso__NoteSet)-[:mso__hasDuration]->(:mso__"+ noteSequence.get(i).getDuration() +") \n";
					
				} else {
				
					match = match + 						
						"MATCH (ns"+i+":mso__NoteSet)-[:mso__hasNote]->(n"+i+":chord__Note)-[:chord__natural]->(val"+i+" {uri:'http://purl.org/ontology/chord/note/"+noteSequence.get(i).getPitch()+"'}) \n" + 
						"MATCH (ns"+i+":mso__NoteSet)-[:mso__hasDuration]->(:mso__"+ noteSequence.get(i).getDuration() +") \n";
				}
				
				if(i <= noteSequence.size()-1 && i > 0) match = match + "MATCH (ns"+(i-1)+":mso__NoteSet)-[:mso__nextNoteSet]->(ns"+i+":mso__NoteSet) \n";

				if(noteSequence.get(i).getAccidental() == null) {
					where = where + "NOT EXISTS ((n"+i+")-[:chord__modifier]->()) ";

					if(i < noteSequence.size()-1) where = where + "AND \n";
				}

			}

			ret = "\nRETURN \n" + 
					"    scr.dc__title AS title,\n" + 
					"    scr.uri AS identifier,\n" + 
					"    thumbnail.uri AS thumbnail,\n " +
					"    {movements: COLLECT(DISTINCT \n" + 
					"    	{movementIdentifier: movements.uri,\n" + 
					"        movementName: movements.dc__title }\n" + 
					"    )} AS movements,\n" + 
					"    {persons: COLLECT(DISTINCT\n" + 
					"       {name: creator.foaf__name, \n" + 
					"     	 identifier: creator.uri, \n" +
					"	     role: role.gndo__preferredNameForTheSubjectHeading} \n" + 
					"    )} AS persons,\n" + 
					"    {encoders: \n" +
					"		COLLECT(DISTINCT {name: encoder.foaf__name})} AS encoders, \n" +
					"	 {locations: \n" +
					"    COLLECT(DISTINCT{ \n" + 
					"	   	  movementIdentifier: movements.uri,\n" + 
					"		  movementName: movements.dc__title,\n" + 
					"      startingMeasure: measure.rdfs__ID, \n" + 
					"      staff: staff.rdfs__ID , \n" + 
					"      voice: voice.rdfs__ID, \n" + 
					"      instrumentName: part.dc__description \n" + 
					"      })    \n" + 
					"    } AS locations\n" + 
					"ORDER BY scr.dc__title"; 

		}

		String optionalMatch = "MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person) \n";
		
		String cypher = match + optionalMatch + "WHERE \n" + where  + ret;

		System.out.println(cypher);

		StatementResult rs = Neo4jConnector.executeQuery(cypher, dataSource);
		
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			MusicScore score = new MusicScore();
			score.setTitle(record.get("title").asString());
			score.setScoreId(record.get("identifier").asString());
			score.setThumbnail(record.get("thumbnail").asString());
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			System.out.println(record.get("title").asString());		
			score.getMelodyLocation2().addAll(getMelodyLocations(gson.toJson(record.get("locations").asMap())));
			score.getPersons().addAll(getPersons(gson.toJson(record.get("persons").asMap())));
			score.getMovements().addAll(getMovements(gson.toJson(record.get("movements").asMap())));
			
			result.add(score);
		}

		return result;
	}
	
	private static ArrayList<Movement> getMovements(String json){
		
		JSONParser parser = new JSONParser();
		ArrayList<Movement> result = new ArrayList<Movement>();
		
		try {
		
			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray movementsJsonArray = (JSONArray) jsonObject.get("movements");
			
			for (int i = 0; i < movementsJsonArray.size(); i++) {
				
				Movement movement = new Movement();
				JSONObject movementsJsonObject = (JSONObject) movementsJsonArray.get(i);
				movement.setMovementIdentifier(movementsJsonObject.get("movementIdentifier").toString());
				movement.setMovementName(movementsJsonObject.get("movementName").toString());
				
				result.add(movement);
			}
		
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return result;

	}
	
	private static ArrayList<Person> getPersons(String json){
		
		JSONParser parser = new JSONParser();
		ArrayList<Person> result = new ArrayList<Person>();
		
		try {
		
			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray personsJsonArray = (JSONArray) jsonObject.get("persons");

			for (int i = 0; i < personsJsonArray.size(); i++) {
				
				Person person = new Person();
				JSONObject personsJsonObject = (JSONObject) personsJsonArray.get(i);
				person.setName(personsJsonObject.get("name").toString());
				person.setRole(personsJsonObject.get("role").toString());
				person.setUrl(personsJsonObject.get("identifier").toString()); 
				result.add(person);
			}
		
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static ArrayList<MelodyLocationGroup> getMelodyLocations (String json){

		JSONParser parser = new JSONParser();
		ArrayList<MelodyLocationGroup> result = new ArrayList<MelodyLocationGroup>();

		try {

			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray melodyJsonArray = (JSONArray) jsonObject.get("locations");

			if(melodyJsonArray!=null) {

				for (int i = 0; i < melodyJsonArray.size(); i++) {

					JSONObject locationJsonObject = (JSONObject) melodyJsonArray.get(i);	    			
					MelodyLocation location = new MelodyLocation();
					location.setInstrumentName(locationJsonObject.get("instrumentName").toString());
					location.setVoice(locationJsonObject.get("voice").toString());
					location.setStaff(locationJsonObject.get("staff").toString());
					location.setMovementName(locationJsonObject.get("movementName").toString());
					location.setMovementIdentifier(locationJsonObject.get("movementIdentifier").toString());
					location.setStartingMeasure(locationJsonObject.get("startingMeasure").toString());

					boolean movementAdded = false;

					for (int j = 0; j < result.size(); j++) {
						if(result.get(j).getMovementId().equals(locationJsonObject.get("movementIdentifier").toString())) {
							movementAdded=true;
							result.get(j).getMelodyLocation().add(location);
						}
					}

					if(!movementAdded) {    					
						MelodyLocationGroup loc = new MelodyLocationGroup();    					
						loc.setMovementId(locationJsonObject.get("movementIdentifier").toString());
						loc.setMovementName(locationJsonObject.get("movementName").toString());
						loc.getMelodyLocation().add(location);
						result.add(loc);
					}    					    			
				}

			}

		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
}
