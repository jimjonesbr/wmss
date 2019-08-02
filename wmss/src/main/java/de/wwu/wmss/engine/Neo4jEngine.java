package de.wwu.wmss.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.exceptions.TransientException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.wwu.wmss.connector.Neo4jConnector;
import de.wwu.wmss.core.Collection;
import de.wwu.wmss.core.DataSource;
import de.wwu.wmss.core.ErrorCodes;
import de.wwu.wmss.core.Format;
import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MelodyLocationGroup;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.DeletedRecord;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.PerformanceMediumType;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.PersonDescription;
import de.wwu.wmss.core.Provenance;
import de.wwu.wmss.core.WMSSRequest;
import de.wwu.wmss.exceptions.DatabaseImportException;
import de.wwu.wmss.core.Tonality;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;

public class Neo4jEngine {

	private static Logger logger = Logger.getLogger("Neo4j-Engine");

	
	public static ArrayList<MusicScore> scoreExists(String scoreIdentifier, WMSSImportRequest importRequest) {
		
		ArrayList<MusicScore> result = new ArrayList<MusicScore>();
		StatementResult rs = Neo4jConnector.getInstance().executeQuery("MATCH (score:Score {uri:\""+scoreIdentifier+"\"}) RETURN score.uri AS uri,score.title AS title", Util.getDataSource(importRequest.getSource()));

		while (rs.hasNext()){
			Record record = rs.next();
			MusicScore score = new MusicScore();
			score.setScoreId(record.get("uri").asString());
			score.setTitle(record.get("title").asString());
			result.add(score);
		}

		return result;
	}
	
	
	public static void prepareDatabase(WMSSImportRequest importRequest) {

    	InputStream is;
		try { 
			is = new FileInputStream("conf/neo4j/prepareDatabase.cql");

	    	@SuppressWarnings("resource")
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));        
	    	String line = buf.readLine();
	    	StringBuilder sb = new StringBuilder();
	    	        
	    	while(line != null){
	    	   sb.append(line).append("\n");
	    	   line = buf.readLine();
	    	}
	    		    	
	    	String arrayCypher[] = sb.toString().split(";");
	    	logger.info("Preparing Graph ... ");
	    	for (int i = 0; i < arrayCypher.length; i++) {
	    		if(!arrayCypher[i].trim().equals("")) {
	    			Neo4jConnector.getInstance().executeQuery(arrayCypher[i].replaceAll("\n", " "), Util.getDataSource(importRequest.getSource()));	
	    		}	    		
			}
	    	logger.info("Neo4j prepared for import statements.");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void formatGraph(WMSSImportRequest importRequest) {
				
    	InputStream is;
		try {
			is = new FileInputStream("conf/neo4j/formatGraph.cql");

	    	@SuppressWarnings("resource")
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));        
	    	String line = buf.readLine();
	    	StringBuilder sb = new StringBuilder();
	    	        
	    	while(line != null){
	    	   sb.append(line).append("\n");
	    	   line = buf.readLine();
	    	}
	    		    	
	    	String arrayCypher[] = sb.toString().split(";");
	    	logger.info("Formatting graph ... ");
	    	for (int i = 0; i < arrayCypher.length; i++) {
	    		if(!arrayCypher[i].trim().equals("")) {
	    			logger.debug(arrayCypher[i]);
	    			Neo4jConnector.getInstance().executeQuery(arrayCypher[i].replaceAll("\n", " "), Util.getDataSource(importRequest.getSource()));		    			
	    		}	    		
			}
	    	logger.info("Neo4j graph complete.");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int insertScore(File file, WMSSImportRequest importRequest) throws DatabaseImportException {
					
		int result = 0;
		
		try {
			
			String importRDF = "CALL semantics.importRDF(\"http://"+ importRequest.getHostname()+":"+
															  SystemSettings.getPort()+"/"+
															  SystemSettings.getService()+"/file?get="+ 
															  URLEncoder.encode(file.getName(), "UTF-8") + "\",\""+importRequest.getFormat()+"\",{shortenUrls: true, commitSize: "+importRequest.getCommitSize()+"});";
			logger.info(importRDF);
			
			StatementResult rs = Neo4jConnector.getInstance().executeQuery(importRDF, Util.getDataSource(importRequest.getSource()));

			while ( rs.hasNext() ){
				Record record = rs.next();
				result = record.get("triplesLoaded").asInt();
				logger.info(file.getName() +" [" + FileUtils.byteCountToDisplaySize(file.length()) +"] - RDF Triples Loaded: " + result);
			}
									
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
			logger.error("Internal error: " + e.getMessage());
			throw new DatabaseImportException(e.getMessage(),ErrorCodes.DATA_IMPORT_CODE, "");
			
		}
		
		return result;
	}
		
	public static MusicScore getScore(WMSSRequest request){
		
		MusicScore score = new MusicScore();
		String format = request.getFormat();
		
		if(format.equals("")) {
			format = SystemSettings.getDefaultScoreFormat();
		}
		
		String cypher = "\n\nMATCH (score:Score {uri:'"+request.getIdentifier()+"'})-[:DOCUMENT {type:'"+format+"'}]->(document:Document) "
				+ "RETURN score.title AS title, document.content AS xml";

		logger.debug("getMusicXML:\n"+cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, Util.getDataSource(request.getSource()));

		while ( rs.hasNext() ){
			Record record = rs.next();
			score.setDocument(record.get("xml").asString());
			score.setTitle(record.get("title").asString());			
		}

		return score;
	}

	public static ArrayList<PerformanceMediumType> getPerformanceMedium(DataSource ds){
		
		ArrayList<PerformanceMediumType> result = new ArrayList<PerformanceMediumType>();
		
		String cypher = "\n\nMATCH (type:mediumType)-[:MEDIUM]->(medium:Medium)\n" + 
				"RETURN DISTINCT \n" + 
				"     {performanceMediumList: {mediumTypeId: type.mediumTypeId,\n" + 
				"      mediumTypeDescription: type.mediumTypeDescription,\n" + 
				"      instruments: COLLECT(DISTINCT\n" + 
				"         {\n" + 
				"          mediumCode: medium.mediumCode,\n" + 
				"          mediumDescription: medium.mediumDescription\n" + 
				"         })}} AS performanceMediumList";
		
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
	
		String cypher = "MATCH (score:Score) RETURN COUNT(score) AS total";
		
		logger.debug("getScoresCount:\n" + cypher);	
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		Record record = rs.next();
				
		return record.get("total").asInt();
		
	}
		
	public static ArrayList<PersonDescription> getRoles(DataSource ds){
	
		ArrayList<PersonDescription> result = new ArrayList<PersonDescription>();
				
		String cypher = "\n\nMATCH (creator:Person)<-[rel_role:CREATOR]-(scr:Score)\n" + 
				"RETURN DISTINCT creator.uri AS identifier, creator.name AS name, rel_role.role AS role, COUNT(scr) AS total\n" + 
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
		String cypher = "MATCH (score:Score)-[rel_doc:DOCUMENT]->() RETURN DISTINCT rel_doc.type AS type, rel_doc.description AS description \n";
		logger.debug("getFormats:\n" + cypher);

		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		
		if(rs.hasNext()) {
			
			Record record = rs.next();
			Format musicxml = new Format();
			musicxml.setFormatId(record.get("type").asString().trim());
			musicxml.setFormatDescription(record.get("description").asString().trim());
			result.add(musicxml);
			
		}
			
		return result;
	}
		
	public static ArrayList<Tonality> getTonalities(DataSource ds){
		
		ArrayList<Tonality> result = new ArrayList<Tonality>();
		String cypher = "MATCH (measure:Measure) RETURN DISTINCT measure.tonic AS tonic, measure.mode AS mode, measure.key_code as code";
		
		logger.debug("getTonalities:\n" + cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		
		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Tonality tonality = new Tonality();
			
			tonality.setMode(record.get("mode").asString().trim());
			tonality.setTonic(record.get("tonic").asString().trim());
			tonality.setCode(record.get("code").asString().trim());
			result.add(tonality);
		}
		
		
		
		return result;
	}
	
	public static ArrayList<Collection> getCollections(DataSource ds){
		
		ArrayList<Collection> result = new ArrayList<Collection>();
		
		String cypher = "\n\nMATCH (score:Score)-[:COLLECTION]->(collection:Collection)\n" + 
				"RETURN DISTINCT collection.uri AS identifier, collection.label AS label\n";
		
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
	
	public static ArrayList<Movement> getMovementData(String scoreURI, DataSource dataSource){

		ArrayList<Movement> result = new ArrayList<Movement>();
		
		String cypher = 
				"MATCH (scr:Score)-[:MOVEMENT]->(mov:Movement)-[:MEDIUMTYPE]->(mediumType:mediumType)-[:MEDIUM]->(medium:Medium)\n" + 
				"WHERE scr.uri=\""+ scoreURI +"\"\n" + 
				"WITH  \n" + 
				"  mov.uri AS movementIdentifier,\n" + 
				"  mov.title AS  movementName,\n" + 
				"  mov.beatUnit AS beatUnit,\n" + 
				"  mov.order AS movOrder,\n" +				
				"  mov.beatsPerMinute AS beatsPerMinute,    \n" + 
				"    {type: mediumType.mediumTypeDescription,\n" + 
				"     typeIdentifier: mediumType.mediumTypeId,\n" + 
				"     performanceMediums: COLLECT(DISTINCT{\n" + 
				"       mediumCode: medium.mediumCode,\n" + 
				"       mediumIdentifier: medium.mediumId,\n" + 
				"       mediumName: medium.mediumDescription,\n" + 
				"       mediumLabel: medium.mediumScoreDescription,\n" + 
				"       solo: medium.solo,\n" + 
				"       ensemble: medium.ensemble})} AS mediumsListResultset\n" + 
				"RETURN \n" + 
				"  {movementIdentifier: movementIdentifier,\n" + 
				"   movementName: movementName, \n" + 
				"   beatUnit: beatUnit,\n" + 
				"   beatsPerMinute: beatsPerMinute,\n" + 
				"   mediumsList: COLLECT(mediumsListResultset)} AS movementsResultset,movOrder \n"+
				"ORDER BY movOrder \n";

		logger.debug("getMovementData:\n"+cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, dataSource);

		while ( rs.hasNext() )
		{
			Record record = rs.next();
			Gson gson = new GsonBuilder().create();					
			JSONParser parser = new JSONParser();

			try {
				
				Movement movement = new Movement();
				
				JSONObject movementsObject = (JSONObject) parser.parse(gson.toJson(record.get("movementsResultset").asMap()));
				movement.setMovementIdentifier(movementsObject.get("movementIdentifier").toString().trim());
				if(movementsObject.get("movementName")!=null) {
					movement.setMovementName(movementsObject.get("movementName").toString());
				} else {
					movement.setMovementName("");
				}
				movement.setBeatUnit(movementsObject.get("beatUnit").toString());
				movement.setBeatsPerMinute(Integer.parseInt(movementsObject.get("beatsPerMinute").toString()));
				
				JSONArray mediumTypeArray = (JSONArray) movementsObject.get("mediumsList");
				
				for (int i = 0; i < mediumTypeArray.size(); i++) {
					
					JSONObject mediumTypeObject = (JSONObject) mediumTypeArray.get(i);

					PerformanceMediumType mediumType = new PerformanceMediumType();	
					mediumType.setMediumTypeId(mediumTypeObject.get("typeIdentifier").toString().trim());
					mediumType.setMediumTypeDescription(mediumTypeObject.get("type").toString().trim());
					
					JSONArray mediumsArray = (JSONArray) mediumTypeObject.get("performanceMediums");
				
					for (int j = 0; j < mediumsArray.size(); j++) {
						
						PerformanceMedium medium = new PerformanceMedium();	

						JSONObject mediumJsonObject = (JSONObject) mediumsArray.get(j);
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
						
						mediumType.getMediums().add(medium);
					}
					
					movement.getPerformanceMediumList().add(mediumType);
					
				}
				
				result.add(movement);
				
			}  catch (org.json.simple.parser.ParseException e) {
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
	
		
		String where = "\nWHERE TRUE \n";														
		String match = "MATCH (scr:Score)-[:MOVEMENT]->(movements:Movement)\n" +
					   "MATCH (scr:Score)-[rel_doc:DOCUMENT ]->(document:Document)\n";
		
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
			int currentMeasure = 0;
			int chordSize = 0;
			String previousDurationType = "";
			String measureNode ="";
			boolean firstElement = true;
			
			while(i<=noteSequence.size()-1) {

				String currentDurationType = "NoteSet";								
				if(!wmssRequest.isIgnoreDuration()) {
					
					currentDurationType = "Duration_"+ noteSequence.get(i).getDuration();
					
					switch (noteSequence.get(i).getDuration()) {
					case "0":
						currentDurationType = "Duration_longa";
						break;
					case "1":
						currentDurationType = "Duration_whole";
						break;
					case "2":
						currentDurationType = "Duration_half";
						break;
					case "3":
						currentDurationType = "Duration_32nd";
						break;
					case "4":
						currentDurationType = "Duration_quarter";
						break;
					case "5":
						currentDurationType = "Duration_64th";
						break;
					case "6":
						currentDurationType = "Duration_16th";
						break;
					case "7":
						currentDurationType = "Duration_128th";
						break;
					case "8":
						currentDurationType = "Duration_eighth";
						break;
					case "9":
						currentDurationType = "Duration_breve";
						break;
					case "a":
						currentDurationType = "Duration_256th";
						break;
					default:
						break;
					}
					
				}	

				String currentPitchType = "NoteSet";
				
				logger.debug("Index: "+ i+ " Pitch:" + noteSequence.get(i).getPitch() + " Octave: " + noteSequence.get(i).getOctave() + " Duration: " + noteSequence.get(i).getDuration() );
				
				if(!wmssRequest.isIgnorePitch()) {

					currentPitchType = noteSequence.get(i).getAccidental()+noteSequence.get(i).getPitch();
					
					if(!wmssRequest.isIgnoreOctaves()) {
						
						if(!noteSequence.get(i).getPitch().equals("-")) {
							
							currentPitchType = currentPitchType + noteSequence.get(i).getOctave();
							
							
						}
						
					}
					
					if(noteSequence.get(i).getPitch().equals("-")) {
						
						currentPitchType = "Rest";
						
					}

				}

				if(!noteSequence.get(i).getTime().equals("")) {					
					
					String[] time = noteSequence.get(i).getTime().split("/");
					where = where + "AND measure"+noteSequence.get(i).getMeasure()+".beatType="+time[1]+"\n" 
								  + "AND measure"+noteSequence.get(i).getMeasure()+".beats="+time[0]+"\n";
					
				}

				if(!noteSequence.get(i).getKey().equals("")) {

					match = match + "MATCH (measure1:"+noteSequence.get(i).getKey()+")\n";
					
				}

				if(!noteSequence.get(i).getClef().equals("")) {

					match = match + "MATCH (ns"+i+":Clef_"+String.valueOf(noteSequence.get(i).getClef().charAt(0))+String.valueOf(noteSequence.get(i).getClef().charAt(2))+")\n";
					
				}

				if(noteSequence.get(i).getDotted()!=0) {
					if(noteSequence.get(i).getDotted()==1) {					
						match = match + "MATCH (ns"+i+":Dot) \n";
					} else if(noteSequence.get(i).getDotted()==2) {					
						match = match + "MATCH (ns"+i+":DoubleDot) \n";
					} else if(noteSequence.get(i).getDotted()==3) {					
						match = match + "MATCH (ns"+i+":TripleDot) \n";
					}
				}
				
				if(currentMeasure != noteSequence.get(i).getMeasure()) {
					measureNode = "(measure"+noteSequence.get(i).getMeasure()+":Measure)";
					if(noteSequence.get(i).getMeasure()>1) {
						match = match + "MATCH (measure"+currentMeasure+":Measure)-[:NEXT]->(measure"+noteSequence.get(i).getMeasure()+":Measure)-[:NOTESET]->(ns"+(notesetCounter)+":"+currentDurationType+")\n";						
					}
					currentMeasure = noteSequence.get(i).getMeasure();
				}

											
				if(i==0) {
					match = match + "MATCH "+measureNode+"-[:NOTESET]->(ns"+i+":"+currentDurationType+":"+currentPitchType+")\n";
					previousDurationType = currentDurationType;
				}
				
				if(!noteSequence.get(i).isChord()) {
							
					
					if(chordSize>0) {
						where = where  + "AND ns"+notesetCounter+".size = "+chordSize+" \n";
					}
					
					if( (notesetCounter>0) ) {
						match = match + "MATCH (ns"+(notesetCounter-1)+":"+previousDurationType+")-[:NEXT]->(ns"+notesetCounter+":"+currentDurationType+":"+currentPitchType+") \n";						
					} else if (!firstElement) {
						notesetCounter++;
						match = match + "MATCH (ns"+(notesetCounter-1)+":"+previousDurationType+")-[:NEXT]->(ns"+notesetCounter+":"+currentDurationType+":"+currentPitchType+") \n";
					}
					
					if(wmssRequest.isIgnoreChords()) {
						
						where = where  + "AND ns"+notesetCounter+".size = 1 \n";
						
					}
					
					if(noteSequence.get(i).isGrace()) {
						
						match = match + "MATCH (ns"+notesetCounter+":GraceNote)\n";
						
					}
					
					notesetCounter++;
					chordSize = 0;	
														
				} else {
					
					match = match + "MATCH (ns"+notesetCounter+":"+currentPitchType+")\n";
					chordSize++;
					firstElement = false;
				}

				previousDurationType = currentDurationType;				
				i++;
			}


		}
		
				
		if(!wmssRequest.getTitle().equals("")){
		
			where = where + "AND scr.title =~ '(?i).*"+wmssRequest.getTitle()+".*'\n";
			
		}
		
		if(!wmssRequest.getPerformanceMedium().equals("") || 
		   !wmssRequest.getPerformanceMediumType().equals("") &&
		    wmssRequest.getMelody().equals("")) {
			match = match + "MATCH (movements:Movement)-[:PART]->(part:Part) \n";
		}
		
		if(!wmssRequest.getMelody().equals("") || !wmssRequest.getKey().equals("")) {
			if(wmssRequest.getClef().equals("")) {
				match = match + "MATCH (scr:Score)-[:MOVEMENT]->(mov:Movement)-[:PART]->(part:Part)-[:MEASURE]->(measure1:Measure)\n";
			}			
		}
		
		if(!wmssRequest.getClef().equals("")) {
			match = match + "MATCH (scr:Score)-[:MOVEMENT]->(mov:Movement)-[:PART]->(part:Part)-[:MEASURE]->(measure1:Measure)-[:NOTESET]->(ns0)\n";
		}
		
		
		if(!wmssRequest.getPerformanceMedium().equals("")) {

			match = match + "MATCH (part:"+wmssRequest.getPerformanceMedium().replace(".", "_")+")\n";
			
		} 

		if(!wmssRequest.getPerformanceMediumType().equals("")) {
			
			match = match + "MATCH (part:"+wmssRequest.getPerformanceMediumType().toLowerCase()+")\n";
			
		}
		
		
		/**
		 * Where clause
		 */
		
		
		if(!wmssRequest.getPerson().equals("")) {
			
			where = where + "AND creator2.uri=\""+wmssRequest.getPerson()+"\"\n";
			
			match = match + "MATCH (scr:Score)-[:CREATOR]->(creator2:Person)\n";
		} 

		if(!wmssRequest.getPersonRole().equals("")) {

			match = match + "MATCH (scr:Score)-[rel_role:CREATOR {role:'"+wmssRequest.getPersonRole()+"' }]->(creator:Person)\n";

		} else {
			
			match = match + "MATCH (scr:Score)-[rel_role:CREATOR]->(creator:Person)\n";
		}
				
		if(!wmssRequest.getFormat().equals("")) {
			
			where = where + "AND rel_doc.type=\""+wmssRequest.getFormat()+"\"\n";
		} 
		
		if(!wmssRequest.getTempoBeatUnit().equals("")) {

			match = match + "MATCH (scr:Score)-[:MOVEMENT]->(movements:Movement {beatUnit: '"+wmssRequest.getTempoBeatUnit()+"'})\n";
			
		}

		if(!wmssRequest.getClef().equals("")) {

			match = match + "MATCH (ns0:Clef_"+String.valueOf(wmssRequest.getClef().charAt(0))+String.valueOf(wmssRequest.getClef().charAt(2))+")\n";
			
		}
		
		if(!wmssRequest.getTempoBeatsPerMinute().equals("")) {
			String[] bpm = wmssRequest.getTempoBeatsPerMinute().split("-");
			
			if(bpm.length==1) {
				where = where + "AND movements.beatsPerMinute = " + bpm[0] +"\n";
			}
			
			if(bpm.length==2) {
				where = where + "AND movements.beatsPerMinute >= " + bpm[0] +"\n";
				where = where + "AND movements.beatsPerMinute <= " + bpm[1] +"\n";
			}
		}
		
		if(wmssRequest.getDateIssuedArray().size()!=0) {
			if(wmssRequest.getDateIssuedArray().size()==1) {
				where = where + "AND date(scr.issued) = date('"+wmssRequest.getDateIssuedArray().get(0)+"') \n";
			}
			if(wmssRequest.getDateIssuedArray().size()==2) {
				where = where + "AND date(scr.issued) >= date('"+wmssRequest.getDateIssuedArray().get(0)+"') \n";
				where = where + "AND date(scr.issued) <= date('"+wmssRequest.getDateIssuedArray().get(1)+"') \n";
			}
		}
			
		if(!wmssRequest.getKey().equals("")) {
			match = match + "MATCH (measure1:"+wmssRequest.getKey()+")";
		}

		if(!wmssRequest.getTimeSignature().equals("")) {
			
			String[] time = wmssRequest.getTimeSignature().split("/");
			where = where + "AND measure.beatType="+time[1]+"\n" 
					      + "AND measure.beats="+time[0]+"\n";
			match = match + "MATCH (movements:Movement)-[:PART]->(part:Part)-[:MEASURE]->(measure:Measure) \n";
		}
		
		if(wmssRequest.isSolo()) {
			where = where + "AND part.solo=\""+wmssRequest.isSolo()+"\"";
		}
		
		if(wmssRequest.isEnsemble()) {
			where = where + "AND part.ensemble=\""+wmssRequest.isEnsemble()+"\"";
		}

		if(!wmssRequest.getCollection().equals("")) {
			where = where  + "AND scr.collectionUri = '"+wmssRequest.getCollection()+"' \n";
		}
		
		return match + where;		
	}
			
	public static ArrayList<DeletedRecord> deleteScore(WMSSRequest request, DataSource dataSource) {

		
		ArrayList<DeletedRecord> result = new ArrayList<DeletedRecord>();
		
	  	InputStream is;
			try {
				is = new FileInputStream("conf/neo4j/deleteScore.cql");

		    	@SuppressWarnings("resource")
				BufferedReader buf = new BufferedReader(new InputStreamReader(is));        
		    	String line = buf.readLine();
		    	StringBuilder sb = new StringBuilder();
		    	        
		    	while(line != null){
		    	   sb.append(line).append("\n");
		    	   line = buf.readLine();
		    	}
		    		    	
		    	String arrayCypher[] = sb.toString().split(";");
		    	logger.info("Deleting music score ... ");
		    	for (int i = 0; i < arrayCypher.length; i++) {
		    		if(!arrayCypher[i].trim().equals("")) {
		    			logger.debug(arrayCypher[i]);
		    			
		    			try {
		    				StatementResult rs = Neo4jConnector.getInstance().executeQuery(arrayCypher[i].replaceAll("\n", " ").replace("SCORE_URI_PLACEHOLDER", request.getIdentifier()), dataSource);
						
		    			
			    			while ( rs.hasNext() ) {
			    				
			    				Record record = rs.next();
	
			    				if(record.containsKey("uri")) {
			    					DeletedRecord score = new DeletedRecord();
				    				score.setTitle(record.get("title").asString());
				    				score.setScoreIdentifier(record.get("uri").asString());
				    				score.setCollection(record.get("collection").asString());
				    				result.add(score);
				    				
				    				logger.info("Music score deleted: " + record.get("title").asString() + " ("+record.get("uri").asString()+")");
			    				}
			    				
			    			}
			    			
		    			} catch (TransientException e) {

							logger.error("TransientException: " + e.getMessage());
						}
		    				
		    		}	    		
				}
		    	
		    	
		    	

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		return result;
		
	}

	
	public static ArrayList<MusicScore> getScoreList(WMSSRequest request, DataSource dataSource){

		ArrayList<MusicScore> result = new ArrayList<MusicScore>();		
		String returnClause = "";
				
		String matchClause = createMelodyQuery(request);
		
		returnClause =   "\nRETURN \n" + 				
				"    scr.title AS title,\n" + 
				"    scr.uri AS identifier,\n" +
				"    toString(scr.issued) AS issued,\n" +
				"    scr.generatedAtTime AS generatedAtTime,\n " +
				"    scr.comments AS comments,\n " +
				"    scr.thumbnail AS thumbnail,\n " +
			    "	 scr.collectionUri AS collectionIdentifier, \n"+
			    "	 scr.collectionLabel AS collectionLabel, \n"+
				"    {persons: COLLECT(DISTINCT\n" + 
				"       {name: creator.name, \n" + 
				"     	 identifier: creator.uri, \n" +
				"	     role: rel_role.role} \n" + 
				"    )} AS persons,\n";
		
		if(!request.getMelody().equals("")) {
			
				returnClause = returnClause +"	 {locations: \n" +
				"    COLLECT(DISTINCT{ \n" + 
				"      movementIdentifier: mov.uri,\n" + 
				"      movementName: mov.title,\n" + 
				"      startingMeasure: measure1.order, \n" + 
				"      staff: ns0.staff, \n" + 
				"      voice: ns0.voice, \n" + 
				"      instrumentName: part.description \n" + 
				"    })} AS locations, \n";				
		}		
				
		returnClause = returnClause +	"    {formats: COLLECT (DISTINCT {format: rel_doc.type, description: rel_doc.description })} AS formats " +
									    "ORDER BY scr.title \n" +
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
			Provenance prov = new Provenance();
			prov.setGeneratedAtTime(record.get("generatedAtTime").asString());
			prov.setComments(record.get("comments").asString());
			
			score.setProvenance(prov);
			score.getMovements().addAll(getMovementData(score.getScoreId(), dataSource));
	
			
			JSONParser parser = new JSONParser();
			try {

				JSONObject formatsObject = (JSONObject) parser.parse(gson.toJson(record.get("formats").asMap()));
			
				JSONArray formatArray = (JSONArray) formatsObject.get("formats");

				for (int i = 0; i < formatArray.size(); i++) {
					
					JSONObject formatJsonObject = (JSONObject) formatArray.get(i);
					Format format = new Format();
					format.setFormatId(formatJsonObject.get("format").toString());
					format.setFormatDescription(formatJsonObject.get("description").toString());
					score.getFormats().add(format);					
				}

			} catch (ParseException e) {

				e.printStackTrace();
			}
		
			result.add(score);
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
					if(locationJsonObject.get("instrumentName")!=null) {
						location.setInstrumentName(locationJsonObject.get("instrumentName").toString());	
					} else {
						location.setInstrumentName("(no title)");
					}
						
					
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
						if(locationJsonObject.get("movementName")!=null) {
							loc.setMovementName(locationJsonObject.get("movementName").toString().trim());	
						} else {
							loc.setMovementName("(no title)");
						}
						
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
