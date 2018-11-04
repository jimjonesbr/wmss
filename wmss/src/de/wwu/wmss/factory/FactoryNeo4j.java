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
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.Format;
import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MelodyLocationGroup;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.PerformanceMediumType;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.PersonDescription;
import de.wwu.wmss.core.Provenance;
import de.wwu.wmss.core.WMSSRequest;
import de.wwu.wmss.core.Tonality;
import de.wwu.wmss.settings.Util;

public class FactoryNeo4j {

	private static Logger logger = Logger.getLogger("Neo4j-Factory");
			
	public static String getMusicXML(WMSSRequest request){
		
		String result = "";
		String cypher = "\n\nMATCH (score:mo__Score {uri:\""+request.getIdentifier()+"\"})\n";

		if(request.getFormat().equals("musicxml")||request.getFormat().equals("")) {
			cypher = cypher +"RETURN score.mso__asMusicXML AS xml\n"; 
		} else if (request.getFormat().equals("mei")) {
			cypher = cypher +"RETURN score.mso__asMEI AS xml\n";
		}

		logger.debug("getMusicXML:\n"+cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, Util.getDataSource(request));

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
		
		logger.debug("getPerformanceMedium:\n"+cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		
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
	
	public static int getScoresCount(DataSource ds) {
	
		String cypher = "MATCH (score:mo__Score) RETURN COUNT(score) AS total";
		
		logger.debug("getScoresCount:\n" + cypher);	
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		Record record = rs.next();
				
		return record.get("total").asInt();
		
	}
		
	public static ArrayList<PersonDescription> getRoles(DataSource ds){
	
		ArrayList<PersonDescription> result = new ArrayList<PersonDescription>();
		
		String cypher = "\n\nMATCH (role:prov__Role)<-[:gndo__professionOrOccupation]-(creator:foaf__Person)<-[:dc__creator]-(scr:mo__Score)\n" + 
						"RETURN DISTINCT creator.uri AS identifier, creator.foaf__name AS name, role.gndo__preferredNameForTheSubjectHeading AS role, COUNT(scr) AS total\n" + 
						"ORDER BY total DESC\n";

		logger.debug("getRoles:\n" + cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			PersonDescription person = new PersonDescription();
			
			person.setName(record.get("name").asString().trim());
			person.setRole(record.get("role").asString().trim());
			person.setUrl(record.get("identifier").asString().trim());
			person.setTotalRelatedScores(record.get("total").asInt());
			result.add(person);
		}
		
		return result;

	}
	
	public static ArrayList<Format> getFormats(DataSource ds){

		ArrayList<Format> result = new ArrayList<Format>();

		String cypher = "\n\nMATCH (scr:mo__Score)\n" + 
					    "RETURN DISTINCT CASE WHEN scr.mso__asMusicXML IS NULL THEN FALSE ELSE TRUE END AS musicxml\n";

		logger.debug("getFormats:\n" + cypher);

		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		Record record = rs.next();

		if(record.get("musicxml").asBoolean()) {
			Format musicxml = new Format();
			musicxml.setFormatId("musicxml");
			musicxml.setFormatDescription("MusicXML");
			result.add(musicxml);
		}

		cypher = "\n\nMATCH (scr:mo__Score)\n" + 
				 "RETURN DISTINCT CASE WHEN scr.mso__asMEI IS NULL THEN FALSE ELSE TRUE END AS mei\n";

		rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
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

		logger.debug("getTonalities:\n" + cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		
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
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);		
		
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

		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, dataSource);

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
			result.setGeneratedAtTime(provenanceJsonObject.get("generatedAtTime").toString().trim());
			result.setComments(provenanceJsonObject.get("comments").toString().trim());			

		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public static int getResultsetSize(WMSSRequest wmssRequest, DataSource dataSource) {
		
		String cypherQuery = createMelodyQuery(wmssRequest) + "\nRETURN COUNT(DISTINCT scr.uri) AS total";
		
		logger.debug("\n[count]:\n"+cypherQuery);
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypherQuery, dataSource);
		Record record = rs.next();
		
		return record.get("total").asInt();
		
	}
	
	public static String createMelodyQuery(WMSSRequest wmssRequest) {
	
		String match = "";
		String where = "";
						
		String personNode;
		
		if(!wmssRequest.getPerson().equals("")) {
			personNode = "(creator:foaf__Person {uri:\""+wmssRequest.getPerson()+"\"})";
		} else {
			personNode = "(creator:foaf__Person)";
		}
		
		String personRoleNode;

		if(!wmssRequest.getPersonRole().equals("")) {
			personRoleNode = "(role:prov__Role {gndo__preferredNameForTheSubjectHeading:\""+wmssRequest.getPersonRole()+"\"})";
		} else {
			personRoleNode = "(role:prov__Role)";
		}
		
		String scoreNode = "";
		
		if(!wmssRequest.getFormat().equals("")) {
			scoreNode = "(scr:mo__Score {format:\""+wmssRequest.getFormat()+"\"})";
		} else {
			scoreNode = "(scr:mo__Score)";
		}
		
		String instrumentNode = "";

		if(!wmssRequest.getPerformanceMedium().equals("")) {
			instrumentNode = "(part:mso__ScorePart {rdfs__label:\""+wmssRequest.getPerformanceMedium()+"\"})";
		} else {
			if(!wmssRequest.getPerformanceMediumType().equals("")) {
				instrumentNode = "(part:mso__ScorePart {typeLabel:\""+wmssRequest.getPerformanceMediumType()+"\"})";
			} else {
				instrumentNode = "(part:mso__ScorePart)";
			}
		}		
		
		
		if(!wmssRequest.getTempoBeatUnit().equals("")) {
			where = "AND mov.beatUnit='"+wmssRequest.getTempoBeatUnit()+"' \n"; 
		}
		
		if(!wmssRequest.getTempoBeatsPerMinute().equals("")) {
			String[] bpm = wmssRequest.getTempoBeatsPerMinute().split("-");
			
			if(bpm.length==1) {
				where = where + "AND mov.mso__hasBeatsPerMinute = " + bpm[0] +"\n";
			}
			
			if(bpm.length==2) {
				where = where + "AND mov.mso__hasBeatsPerMinute >= " + bpm[0] +"\n";
				where = where + "AND mov.mso__hasBeatsPerMinute <= " + bpm[1] +"\n";
			}
		}
		
		if(wmssRequest.getDateIssuedArray().size()!=0) {
			if(wmssRequest.getDateIssuedArray().size()==1) {
				where = where + "AND scr.issued = datetime('"+wmssRequest.getDateIssuedArray().get(0)+"') \n";
			}
			if(wmssRequest.getDateIssuedArray().size()==2) {
				where = where + "AND scr.issued >= datetime('"+wmssRequest.getDateIssuedArray().get(0)+"') \n";
				where = where + "AND scr.issued <= datetime('"+wmssRequest.getDateIssuedArray().get(1)+"') \n";
			}
		}
		
		match = "\nMATCH "+scoreNode+"-[:dc__creator]->"+personNode+"-[:gndo__professionOrOccupation]->"+personRoleNode+"\n";
		 
		if(!wmssRequest.getMelody().equals("")) {
	
			ArrayList<Note> noteSequence = wmssRequest.getNoteSequence();
			
			for (int j = 0; j < noteSequence.size(); j++) {
				/**
				 * Disables ignoreChord flag in case there are chords in the searched melody.
				 */
				if(noteSequence.get(j).isChord() && wmssRequest.isIgnoreChords()) {
					wmssRequest.setIgnoreChords(false);
					logger.warn("["+ErrorCodes.WARNING_CONFLICTING_CHORDS_PARAMETER_CODE + "] " + ErrorCodes.WARNING_CONFLICTING_CHORDS_PARAMETER_DESCRIPTION + " - " + ErrorCodes.WARNING_CONFLICTING_CHORDS_PARAMETER_HINT);
				}
			}
			
			int i = 0;
			int notesetCounter = 0;
			
			while(i<=noteSequence.size()-1) {

				
				String durationType = "mso__NoteSet";
				
				if(!wmssRequest.isIgnoreDuration()) {
					durationType = "d"+ noteSequence.get(i).getDuration();
				}	
				
				
				if(i==0) {

					match = match + "MATCH "+scoreNode+"-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->"+instrumentNode+"-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(ns0:"+durationType+")\n" + 
									"MATCH "+scoreNode+"-[:mo__movement]->(movements:mo__Movement) \n" + 
									"MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasNoteSet]->(ns0:"+durationType+") \n";
							
					if(!wmssRequest.isIgnorePitch()) {						
						match = match +	"MATCH (ns0:"+durationType+")-[:mso__hasNote]->(n0:"+noteSequence.get(i).getPitch()+noteSequence.get(i).getAccidental()+") \n";
					}
															
					if(!wmssRequest.isIgnoreOctaves()) {
						match = match +	"MATCH (n0:chord__Note {mso__hasOctave:"+noteSequence.get(i).getOctave()+"}) \n";
					}
					
				} else {
					
					if(!noteSequence.get(i).isChord()) {
						notesetCounter++;
						if(notesetCounter>0) {
							match = match + "MATCH (ns"+(notesetCounter-1)+":"+durationType+")-[:mso__nextNoteSet]->(ns"+notesetCounter+":"+durationType+") \n";
						}
						
					}
											
					if(!wmssRequest.isIgnorePitch()) {											
						match = match + "MATCH (ns"+notesetCounter+":"+durationType+")-[:mso__hasNote]->(n"+i+":"+noteSequence.get(i).getPitch()+noteSequence.get(i).getAccidental()+") \n";
					}										
					
					if(!wmssRequest.isIgnoreOctaves()) {	
						match = match +	"MATCH (n"+i+":chord__Note {mso__hasOctave:"+noteSequence.get(i).getOctave()+"}) \n";
					}										 
				}

				if(wmssRequest.isIgnoreChords()) {
					where = where  + "AND ns"+notesetCounter+".size = 1 \n";
				}
				
				i++;
			}
			

		} else {
			
			match = match + "\nMATCH (role:prov__Role)<-[:gndo__professionOrOccupation]-(creator:foaf__Person)<-[:dc__creator]-"+scoreNode+"-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->"+instrumentNode+"\n" + 
							"MATCH "+scoreNode+"-[:foaf__thumbnail]->(thumbnail) \n" +
							"MATCH "+scoreNode+"-[:mo__movement]->(movements:mo__Movement) \n";	
		}
		
		if(wmssRequest.isSolo()) {
			match = match + "\nMATCH (part:mso__ScorePart {mso__isSolo:\""+wmssRequest.isSolo()+"\"})";	
		}
		
		if(wmssRequest.isEnsemble()) {
			match = match + "\nMATCH (part:mso__ScorePart {mso__isEnsemble:\""+wmssRequest.isEnsemble()+"\"})";	
		}

		if(!wmssRequest.getCollection().equals("")) {
			where = where  + "AND scr.collectionUri = '"+wmssRequest.getCollection()+"' \n";
		}

		return match + "\nWHERE TRUE\n" + where;		
	}
		
	
	public static ArrayList<MusicScore> getScoreList(WMSSRequest request, DataSource dataSource){

		ArrayList<MusicScore> result = new ArrayList<MusicScore>();		
		String returnClause = "";
				
		String matchClause = createMelodyQuery(request);
		
		returnClause =   "\nRETURN \n" + 				
				"    scr.dc__title AS title,\n" + 
				"    scr.uri AS identifier,\n" +
				"    toString(scr.dcterms__issued) AS issued,\n" +
				"    scr.provGeneratedAtTime AS provGeneratedAtTime,\n " +
				"    scr.provComments AS provComments,\n " +
				"    scr.thumbnail AS thumbnail,\n " +
			    "	 scr.collectionUri AS collectionIdentifier, \n"+
			    "	 scr.collectionLabel AS collectionLabel, \n"+
				"    {movements: COLLECT(DISTINCT \n" + 
				"    	{movementIdentifier: movements.uri,\n" + 
				"        movementName: movements.dc__title ,\n" + 
				"        beatUnit: movements.beatUnit,\n" + 
				"        beatsPerMinute: movements.mso__hasBeatsPerMinute}\n" + 
				"    )} AS movements,\n" + 
				"    {persons: COLLECT(DISTINCT\n" + 
				"       {name: creator.foaf__name, \n" + 
				"     	 identifier: creator.uri, \n" +
				"	     role: role.gndo__preferredNameForTheSubjectHeading} \n" + 
				"    )} AS persons,\n" + 
				"    {persons: COLLECT(DISTINCT{name: scr.encoderName, identifier: scr.encoderUri, role: \"Encoder\"})} AS encoders, \n";
		
		if(!request.getMelody().equals("")) {
			
				returnClause = returnClause +"	 {locations: \n" +
				"    COLLECT(DISTINCT{ \n" + 
				"      movementIdentifier: mov.uri,\n" + 
				"      movementName: mov.dc__title,\n" + 
				"      startingMeasure: measure.rdfs__ID, \n" + 
				"      staff: staff.rdfs__ID , \n" + 
				"      voice: voice.rdfs__ID, \n" + 
				"      instrumentName: part.dc__description \n" + 
				"    })} AS locations, \n";				
		}		
				
		returnClause = returnClause +	"   CASE WHEN scr.mso__asMusicXML IS NULL THEN FALSE ELSE TRUE END AS musicxml,\n" + 
									    "   CASE WHEN scr.mso__asMEI IS NULL THEN FALSE ELSE TRUE END AS mei \n" + 
									    "ORDER BY scr.dc__title \n" +
									    "SKIP " + request.getOffset() + "\n" + 
									    "LIMIT " + request.getPageSize();
				
		String cypherQuery = matchClause + returnClause;

		logger.info("\n[main]:\n"+cypherQuery+"\n");
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypherQuery, dataSource);
				
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			MusicScore score = new MusicScore();
			score.setTitle(record.get("title").asString());
			score.setDateIssued(record.get("issued").asString());
			score.setScoreId(record.get("identifier").asString());
			score.setThumbnail(record.get("thumbnail").asString());					
			score.getCollection().setId(record.get("collectionIdentifier").asString());
			score.getCollection().setDescription(record.get("collectionLabel").asString());
			score.setOnlineResource(record.get("identifier").asString());
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			
			if(!request.getMelody().equals("")) {
				score.getMelodyLocation().addAll(getMelodyLocations(gson.toJson(record.get("locations").asMap()),request.getMelody()));
			}
			score.getPersons().addAll(getPersons(gson.toJson(record.get("persons").asMap())));
			score.getPersons().addAll(getPersons(gson.toJson(record.get("encoders").asMap())));			
			//score.setProvenance(getProvenance(gson.toJson(record.get("activity").asMap())));
			Provenance prov = new Provenance();
			prov.setGeneratedAtTime(record.get("provGeneratedAtTime").asString());
			prov.setComments(record.get("provComments").asString());
			
			score.setProvenance(prov);
						
			if(!request.getRequestMode().equals("simplified")) {
				score.getMovements().addAll(getMovements(gson.toJson(record.get("movements").asMap())));
				for (int i = 0; i < score.getMovements().size(); i++) {			
					score.getMovements().get(i).getPerformanceMediumList().add(getPerformanceMediums(score.getScoreId(),score.getMovements().get(i).getMovementId(),dataSource));
				}
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
				movement.setBeatsPerMinute(Integer.parseInt(movementsJsonObject.get("beatsPerMinute").toString()));
				movement.setBeatUnit(movementsJsonObject.get("beatUnit").toString());
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
