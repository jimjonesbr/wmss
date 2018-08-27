package de.wwu.wmss.factory;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wwu.wmss.connectors.Neo4jConnector;
import de.wwu.wmss.core.Collection;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.Format;
import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MelodyLocationGroup;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.PerformanceMediumType;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.Provenance;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.core.Tonality;
import de.wwu.wmss.settings.SystemSettings;

public class FactoryNeo4j {

	private static Logger logger = Logger.getLogger("Neo4j-Factory");

	private static ArrayList<Note> createNoteSequence(String melody){

		String[] melodyRequest = melody.split(">");
		ArrayList<Note> result = new ArrayList<>();

		for (int i = 0; i < melodyRequest.length; i++) {

			String[] melodyElement = melodyRequest[i].split("-");

			Note note = new Note();

			if(melodyElement[0]!="*") {
				if(melodyElement[0].equals("r")) {
					note.setPitch("Rest");
				} else {
					note.setPitch(melodyElement[0].toLowerCase());
				}
			} else {
				note.setPitch(null);
			}
			
			
			if(melodyElement[0].length()>1) {
				
				if(melodyElement[0].substring(1, 2).toLowerCase().equals("s")) {					
					note.setAccidental("sharp");					
				}
				if(melodyElement[0].substring(1, 2).toLowerCase().equals("b")) {
					note.setAccidental("flat");
				}
			} else {
				note.setAccidental(null);
			}

			if(melodyElement[1]=="*") {
				note.setDuration(null);
			} else if (melodyElement[1].equals("ow")) {
				note.setDuration("80");
			} else if (melodyElement[1].equals("qw")) {
				note.setDuration("40");
			} else if (melodyElement[1].equals("dw")) {
				note.setDuration("20");
			} else if (melodyElement[1].equals("w")) {
				note.setDuration("0");
			} else if (melodyElement[1].equals("h")) {
				note.setDuration("2");
			} else if (melodyElement[1].equals("4")) {
				note.setDuration("4");
			} else if (melodyElement[1].equals("8")) {
				note.setDuration("8");
			} else if (melodyElement[1].equals("16")) {
				note.setDuration("16");
			} else if (melodyElement[1].equals("32")) {
				note.setDuration("32");
			} else if (melodyElement[1].equals("64")) {
				note.setDuration("64");
			} else if (melodyElement[1].equals("128")) {
				note.setDuration("128");
			} else if (melodyElement[1].equals("256")) {
				note.setDuration("256");
			} 

			if(melodyElement[2]!="*") {
				note.setOctave(melodyElement[2]);
			} else {
				note.setOctave(null);
			}

			//TODO: parse chords
			note.setChord(false);			
			result.add(note);


		}

		return result;

	}
		
	public static String getMusicXML(ArrayList<RequestParameter> parameters){
		
		String result = "";		
		String format = "";
		String source = "";
		String scoreIdentifier = "";
		
		DataSource dataSource = new DataSource();

		for (int i = 0; i < parameters.size(); i++) {


			if(parameters.get(i).getRequest().equals("identifier")){

				scoreIdentifier = parameters.get(i).getValue();

			}
			
			if(parameters.get(i).getRequest().equals("source")){

				source = parameters.get(i).getValue();

			}
			
			if(parameters.get(i).getRequest().equals("format")){
				
				format = parameters.get(i).getValue();

			}

		}


		for (int i = 0; i < SystemSettings.sourceList.size(); i++) {

			if(SystemSettings.sourceList.get(i).getId().equals(source)){

				dataSource = SystemSettings.sourceList.get(i); 

			}

		}

		String cypher = "\n\nMATCH (score:mo__Score {uri:\""+scoreIdentifier+"\"})\n";

		if(format.equals("musicxml")||format.equals("")) {
			cypher = cypher +"RETURN score.mso__asMusicXML AS xml\n"; 
		} else if (format.equals("mei")) {
			cypher = cypher +"RETURN score.mso__asMEI AS xml\n";
		}

		logger.info(cypher);
		
		StatementResult rs = Neo4jConnector.executeQuery(cypher, dataSource);

		while ( rs.hasNext() ){
			Record record = rs.next();

			result = record.get("xml").asString();
		}

		return result;
	}


	public static ArrayList<PerformanceMediumType> getPerformanceMedium(DataSource ds){
		
		ArrayList<PerformanceMediumType> result = new ArrayList<PerformanceMediumType>();
		
		String cypher = "\n\nMATCH (instrument:mo__Instrument)-[:skos__broader]->(type)\n" + 
						"RETURN DISTINCT\n" + 
						"     {performanceMediumList: {mediumTypeId: type.uri,\n" + 
						"      mediumTypeDescription: type.skos__prefLabel,\n" + 
						"      instruments: COLLECT(DISTINCT\n" + 
						"         {\n" + 
						"          mediumCode: instrument.rdfs__label,\n" + 
						"          mediumDescription: instrument.skos__prefLabel\n" + 
						"         })}} AS performanceMediumList\n";
		logger.info(cypher);
		StatementResult rs = Neo4jConnector.executeQuery(cypher, ds);
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Gson gson = new GsonBuilder().create();					
			JSONParser parser = new JSONParser();

			try {

				Object objResultset = parser.parse(gson.toJson(record.get("performanceMediumList").asMap()));

				JSONObject jsonObject = (JSONObject) objResultset;
				JSONObject mediumList = (JSONObject) jsonObject.get("performanceMediumList");

				PerformanceMediumType type = new PerformanceMediumType();
				type.setMediumTypeId(mediumList.get("mediumTypeId").toString().trim());
				type.setMediumTypeDescription(mediumList.get("mediumTypeDescription").toString());
								
				JSONArray mediumListJsonArray = (JSONArray) mediumList.get("instruments");
				
				for (int j = 0; j < mediumListJsonArray.size(); j++) {

					PerformanceMedium medium = new PerformanceMedium();	

					JSONObject mediumJsonObject = (JSONObject) mediumListJsonArray.get(j);
					medium.setMediumDescription(mediumJsonObject.get("mediumDescription").toString().trim());
					medium.setMediumCode(mediumJsonObject.get("mediumCode").toString().trim());
														
					type.getMediums().add(medium);					
				
				}
				
				result.add(type);

			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}

		}
		
		return result;
	}
	
	public static ArrayList<Person> getRoles(DataSource ds){
	
		ArrayList<Person> result = new ArrayList<Person>();
		
		String cypher = "\n\nMATCH (role:prov__Role)<-[:gndo__professionOrOccupation]-(creator:foaf__Person)<-[:dc__creator]-(scr:mo__Score)\n" + 
						"RETURN DISTINCT creator.uri AS identifier, creator.foaf__name AS name, role.gndo__preferredNameForTheSubjectHeading AS role\n";

		logger.info(cypher);		
		StatementResult rs = Neo4jConnector.executeQuery(cypher, ds);
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Person person = new Person();
			
			person.setName(record.get("name").asString().trim());
			person.setRole(record.get("role").asString().trim());
			person.setUrl(record.get("identifier").asString().trim());
			
			result.add(person);
		}
		
		return result;

	}
	
	public static ArrayList<Format> getFormats(DataSource ds){

		ArrayList<Format> result = new ArrayList<Format>();

		String cypher = "\n\nMATCH (scr:mo__Score)\n" + 
					    "RETURN DISTINCT CASE WHEN scr.mso__asMusicXML IS NULL THEN FALSE ELSE TRUE END AS musicxml\n";

		logger.info("getFormats:" + cypher);

		StatementResult rs = Neo4jConnector.executeQuery(cypher, ds);
		Record record = rs.next();

		if(record.get("musicxml").asBoolean()) {
			Format musicxml = new Format();
			musicxml.setFormatId("musicxml");
			musicxml.setFormatDescription("MusicXML");
			result.add(musicxml);
		}

		cypher = "\n\nMATCH (scr:mo__Score)\n" + 
				 "RETURN DISTINCT CASE WHEN scr.mso__asMEI IS NULL THEN FALSE ELSE TRUE END AS mei\n";

		logger.info(cypher);

		rs = Neo4jConnector.executeQuery(cypher, ds);
		record = rs.next();

		if(record.get("mei").asBoolean()) {
			Format musicxml = new Format();
			musicxml.setFormatId("mei");
			musicxml.setFormatDescription("MEI");
			result.add(musicxml);
		}

		return result;
	}
	
	
	public static ArrayList<Tonality> getTonalities(DataSource ds){
		
		ArrayList<Tonality> result = new ArrayList<Tonality>();
		
		String cypher = "\n\nMATCH (mode)<-[:ton__mode]-(key:ton__Key)-[:ton__tonic]->(tonic)\n" + 
					    "RETURN DISTINCT LOWER(SUBSTRING(tonic.uri,36)) AS tonic, LOWER(SUBSTRING(mode.uri,39)) AS mode\n";

		StatementResult rs = Neo4jConnector.executeQuery(cypher, ds);
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Tonality tonality = new Tonality();
			
			tonality.setMode(record.get("mode").asString().trim());
			tonality.setTonic(record.get("tonic").asString().trim());
			
			result.add(tonality);
		}
		
		
		
		return result;
	}
	
	public static ArrayList<Collection> getCollections(DataSource ds){
		
		ArrayList<Collection> result = new ArrayList<Collection>();
		
		String cypher = "\n\nMATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)\n" + 
					    "RETURN DISTINCT collection.uri AS identifier, collection.rdfs__label AS label \n";
		
		StatementResult rs = Neo4jConnector.executeQuery(cypher, ds);		
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Collection collection = new Collection();
			
			collection.setId(record.get("identifier").asString().trim());
			collection.setDescription(record.get("label").asString().trim());
			
			result.add(collection);
		}
		
		return result;
		
	}
	
	public static PerformanceMediumType getPerformanceMediums(String scoreURI, String movementURI, DataSource dataSource){

		PerformanceMediumType result = new PerformanceMediumType();

		String cypher = "\n\nMATCH (:mo__Score {uri:\""+scoreURI+"\"})-[:mo__movement]->(:mo__Movement {uri:\""+movementURI+"\"})-[:mso__hasScorePart]->(instrument:mo__Instrument) \n" + 
						"OPTIONAL MATCH (instrument:mo__Instrument)-[:skos__broader]->(type) \n" +				
						"RETURN\n" + 
						"  {mediumsList :\n" + 
						"     {type: type.skos__prefLabel, \n" + 
						"      typeIdentifier: type.uri,\n" + 
						"      performanceMediums: COLLECT(DISTINCT {\n" + 
						"	   mediumCode: instrument.rdfs__label,\n"+
						"      mediumIdentifier: instrument.uri,\n" + 
						"      mediumName: instrument.skos__prefLabel,\n" +
						"	   mediumLabel: instrument.dc__description,\n" +
						"	   solo: instrument.mso__isSolo,\n" +
						"	   ensemble: instrument.mso__isEnsemble}\n" + 
						"      )}} AS mediumsListResultset \n";

		logger.info("[getPerformanceMediums(String movementURI, DataSource dataSource)]: \n"+cypher);

		StatementResult rs = Neo4jConnector.executeQuery(cypher, dataSource);

		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Gson gson = new GsonBuilder().create();					
			JSONParser parser = new JSONParser();

			try {

				Object objResultset = parser.parse(gson.toJson(record.get("mediumsListResultset").asMap()));

				JSONObject jsonObject = (JSONObject) objResultset;
				JSONObject mediumList = (JSONObject) jsonObject.get("mediumsList");			

				if(mediumList.get("type") == null) {
					result.setMediumTypeDescription("Unknown");
				} else {
					result.setMediumTypeDescription(mediumList.get("type").toString());	
				}

				if(mediumList.get("type") == null) {
					result.setMediumTypeId("Unknown");
				} else {
					result.setMediumTypeId(mediumList.get("typeIdentifier").toString());
				}		

				JSONArray mediumListJsonArray = (JSONArray) mediumList.get("performanceMediums");

				for (int j = 0; j < mediumListJsonArray.size(); j++) {

					PerformanceMedium medium = new PerformanceMedium();	

					JSONObject mediumJsonObject = (JSONObject) mediumListJsonArray.get(j);
					//medium.setMediumScoreDescription(mediumJsonObject.get("dc__description").toString());
					medium.setMediumDescription(mediumJsonObject.get("mediumName").toString().trim());
					medium.setMediumId(mediumJsonObject.get("mediumIdentifier").toString().trim());
					medium.setMediumScoreDescription(mediumJsonObject.get("mediumLabel").toString().trim());
					medium.setMediumCode(mediumJsonObject.get("mediumCode").toString().trim());
					
					
					if(mediumJsonObject.get("solo")!=null) {
						medium.setSolo(Boolean.parseBoolean(mediumJsonObject.get("solo").toString().trim()));
					}
					
					if(mediumJsonObject.get("ensemble")!=null) {
						medium.setEnsemble(Boolean.parseBoolean(mediumJsonObject.get("ensemble").toString().trim()));
					}

					
					result.getMediums().add(medium);
					
				}

			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}

		}

		return result;

	}
	
	public static Provenance getProvenance(String json) {

		JSONParser parser = new JSONParser();
		Provenance result = new Provenance();

		try {

			Object obj = parser.parse(json);						
			JSONObject provenanceJsonObject = (JSONObject) obj;
			result.setGeneratedAtTime(provenanceJsonObject.get("prov__startedAtTime").toString().trim());
			result.setComments(provenanceJsonObject.get("rdfs__comment").toString().trim());			

		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static ArrayList<MusicScore> getScoreList(ArrayList<RequestParameter> parameters, DataSource dataSource){

		ArrayList<MusicScore> result = new ArrayList<MusicScore>();		
		String melody = "";
		String match = "";
		String where = "";
		String ret = "";
		String collection = "";
		String person = "";
		String personRole = "";
		String docFormat = "";
		String instrument = "";
		String instrumentType = "";
		String solo = "";
		String ensemble = "";
		
		boolean ignoreChords = true;
		
		for (int i = 0; i < parameters.size(); i++) {
			if(parameters.get(i).getRequest().equals("melody")){
				melody = parameters.get(i).getValue();
			}
			if(parameters.get(i).getRequest().equals("ignorechords")){
				ignoreChords = Boolean.valueOf(parameters.get(i).getValue());
			}
			if(parameters.get(i).getRequest().equals("collection")){
				collection = parameters.get(i).getValue();
			}
			if(parameters.get(i).getRequest().equals("person")){
				person = parameters.get(i).getValue().toString();
			}
			if(parameters.get(i).getRequest().equals("personRole")){
				personRole = parameters.get(i).getValue().toString();
			}
			if(parameters.get(i).getRequest().equals("format")){
				docFormat = parameters.get(i).getValue().toString();
			}
			if(parameters.get(i).getRequest().equals("performancemedium")){
				instrument = parameters.get(i).getValue().toString();				
			}
			if(parameters.get(i).getRequest().equals("performancemediumtype")){
				instrumentType = parameters.get(i).getValue().toString();				
			} 
			if(parameters.get(i).getRequest().equals("solo")){
				solo = parameters.get(i).getValue().toString();				
			} 
			if(parameters.get(i).getRequest().equals("ensemble")){
				ensemble = parameters.get(i).getValue().toString();				
			} 
		}
		
		
		String personNode;
		
		if(!person.equals("")) {
			personNode = "(creator:foaf__Person {uri:\""+person+"\"})";
		} else {
			personNode = "(creator:foaf__Person)";
		}
		
		String personRoleNode;

		if(!personRole.equals("")) {
			personRoleNode = "(role:prov__Role {gndo__preferredNameForTheSubjectHeading:\""+personRole+"\"})";
		} else {
			personRoleNode = "(role:prov__Role)";
		}
		
		String scoreNode = "";
		
		if(!docFormat.equals("")) {
			scoreNode = "(scr:mo__Score {format:\""+docFormat+"\"})";
		} else {
			scoreNode = "(scr:mo__Score)";
		}
		
		String instrumentNode = "";

		if(!instrument.equals("")) {
			instrumentNode = "(part:mso__ScorePart {rdfs__label:\""+instrument+"\"})";
		} else {
			if(!instrumentType.equals("")) {
				instrumentNode = "(part:mso__ScorePart {typeLabel:\""+instrumentType+"\"})";
			} else {
				instrumentNode = "(part:mso__ScorePart)";
			}
		}
		
		
		match = "\nMATCH "+scoreNode+"-[:dc__creator]->"+personNode+"-[:gndo__professionOrOccupation]->"+personRoleNode+"\n";
		 
		if(!melody.equals("")) {
	
			ArrayList<Note> noteSequence = createNoteSequence(melody);

			match = match + "MATCH "+scoreNode+"-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->"+instrumentNode+"-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(ns0:mso__NoteSet)\n" + 
							"MATCH "+scoreNode+"-[:foaf__thumbnail]->(thumbnail) \n" +
							"MATCH "+scoreNode+"-[:mo__movement]->(movements:mo__Movement) \n"+
							"MATCH "+instrumentNode+"-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasNoteSet]->(ns0:mso__NoteSet) \n"; 
					
			for (int i = 0; i < noteSequence.size(); i++) {

				if(i==0) {
					
					if(!noteSequence.get(i).getPitch().equals("*")) {
						match = match +	"MATCH (ns0:mso__NoteSet)-[:mso__hasNote]->(n0:chord__Note {note:'"+noteSequence.get(i).getPitch()+"'}) \n";
					}
					if(!noteSequence.get(i).getDuration().equals("*")) {
						where = where +	"AND ns0.duration = "+noteSequence.get(i).getDuration()+" \n";
					}					
					if(!noteSequence.get(i).getOctave().equals("*")) {
						match = match +	"MATCH (n0:chord__Note {mso__hasOctave:"+noteSequence.get(i).getOctave()+"}) \n";
					}
					
				} else {
					
					if(!noteSequence.get(i).getPitch().equals("*")) {						
						match = match + "MATCH (ns"+i+":mso__NoteSet)-[:mso__hasNote]->(n"+i+":chord__Note {note:'"+noteSequence.get(i).getPitch()+"'}) \n";
					}
					
					if(!noteSequence.get(i).getDuration().equals("*")) {				
						where = where +	"AND ns"+i+".duration = "+noteSequence.get(i).getDuration()+" \n";
					}
					if(!noteSequence.get(i).getOctave().equals("*")) {
						match = match +	"MATCH (n"+i+":chord__Note {mso__hasOctave:"+noteSequence.get(i).getOctave()+"}) \n";
					}
					 
				}
								
				if(i <= noteSequence.size()-1 && i > 0) {
					match = match + "MATCH (ns"+(i-1)+":mso__NoteSet)-[:mso__nextNoteSet]->(ns"+i+":mso__NoteSet) \n";
				}
				
				if(ignoreChords) {
					where = where  + "AND ns"+i+".size = 1 \n";
				}

			}
			

		} else {
			
			match = match + "\nMATCH (role:prov__Role)<-[:gndo__professionOrOccupation]-(creator:foaf__Person)<-[:dc__creator]-"+scoreNode+"-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->"+instrumentNode+"\n" + 
							"MATCH "+scoreNode+"-[:foaf__thumbnail]->(thumbnail) \n" +
							"MATCH "+scoreNode+"-[:mo__movement]->(movements:mo__Movement) \n";	
		}
		
		if(!solo.equals("")) {
			match = match + "\nMATCH (part:mso__ScorePart {mso__isSolo:\""+solo+"\"})";	
		}
		
		if(!ensemble.equals("")) {
			match = match + "\nMATCH (part:mso__ScorePart {mso__isEnsemble:\""+ensemble+"\"})";	
		}
		
		if(collection.equals("")) {
			match = match + "MATCH (collection:prov__Collection)-[:prov__hadMember]->"+scoreNode+"\n";
		} else {
			match = match + "MATCH (collection:prov__Collection {uri:\""+collection+"\"})-[:prov__hadMember]->"+scoreNode+"\n";
		}
		ret =   "\nRETURN \n" + 				
				"    scr.dc__title AS title,\n" + 
				"    scr.uri AS identifier,\n" +
				"    activity,\n" + 
				"    thumbnail.uri AS thumbnail,\n " +
				"	 collection.uri AS collectionIdentifier, \n"+
				"	 collection.rdfs__label AS collectionLabel, \n"+
				"    {movements: COLLECT(DISTINCT \n" + 
				"    	{movementIdentifier: movements.uri,\n" + 
				"        movementName: movements.dc__title }\n" + 
				"    )} AS movements,\n" + 
				"    {persons: COLLECT(DISTINCT\n" + 
				"       {name: creator.foaf__name, \n" + 
				"     	 identifier: creator.uri, \n" +
				"	     role: role.gndo__preferredNameForTheSubjectHeading} \n" + 
				"    )} AS persons,\n" + 
				"    {persons: \n" +
				"		COLLECT(DISTINCT {name: encoder.foaf__name, identifier: encoder.uri, role: \"Encoder\"})} AS encoders, \n";
		
		if(!melody.equals("")) {
			
				ret = ret +"	 {locations: \n" +
				"    COLLECT(DISTINCT{ \n" + 
				"	   	  movementIdentifier: mov.uri,\n" + 
				"		  movementName: mov.dc__title,\n" + 
				"      startingMeasure: measure.rdfs__ID, \n" + 
				"      staff: staff.rdfs__ID , \n" + 
				"      voice: voice.rdfs__ID, \n" + 
				"      instrumentName: part.dc__description \n" + 
				"      })} AS locations, \n";
				
		}		
				
		ret = ret +	"   CASE WHEN scr.mso__asMusicXML IS NULL THEN FALSE ELSE TRUE END AS musicxml,\n" + 
					"   CASE WHEN scr.mso__asMEI IS NULL THEN FALSE ELSE TRUE END AS mei \n" + 
					"ORDER BY scr.dc__title "; 

		String optionalMatch = "MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person) \n";
		String cypher = match + optionalMatch + "WHERE TRUE\n" + where  + ret;

		logger.info("\n[main]:\n"+cypher+"\n");

		StatementResult rs = Neo4jConnector.executeQuery(cypher, dataSource);
				
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			MusicScore score = new MusicScore();
			score.setTitle(record.get("title").asString());
			score.setScoreId(record.get("identifier").asString());
			score.setThumbnail(record.get("thumbnail").asString());					
			score.getCollection().setId(record.get("collectionIdentifier").asString());
			score.getCollection().setDescription(record.get("collectionLabel").asString());
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			if(!melody.equals("")) {
				score.getMelodyLocation().addAll(getMelodyLocations(gson.toJson(record.get("locations").asMap()),melody));
			}
			score.getPersons().addAll(getPersons(gson.toJson(record.get("persons").asMap())));
			score.getPersons().addAll(getPersons(gson.toJson(record.get("encoders").asMap())));
			score.getMovements().addAll(getMovements(gson.toJson(record.get("movements").asMap())));
			score.setProvenance(getProvenance(gson.toJson(record.get("activity").asMap())));
			
			for (int i = 0; i < score.getMovements().size(); i++) {
			
				score.getMovements().get(i).getPerformanceMediumList().add(getPerformanceMediums(score.getScoreId(),score.getMovements().get(i).getMovementId(),dataSource));

			}
			
			if(record.get("musicxml").asBoolean()) {
				Format format = new Format();
				format.setFormatId("musicxml");
				format.setFormatDescription("MusicXML 3.0"); //TODO: create triples for describing MusicXML version
				score.getFormats().add(format);	
			} else if(record.get("mei").asBoolean()) {
				Format format = new Format();
				format.setFormatId("mei");
				format.setFormatDescription("Music Encoding Initiative 3.0"); //TODO: create triples for describing MusicXML version
				score.getFormats().add(format);	
				
			}
			
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
	
	private static ArrayList<MelodyLocationGroup> getMelodyLocations (String json, String melodyQuery){

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
					location.setStartingMeasure(locationJsonObject.get("startingMeasure").toString());
					location.setMelody(melodyQuery);
					
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
						loc.setMovementName(locationJsonObject.get("movementName").toString().trim());
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
