package de.wwu.wmss.factory;

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
import de.wwu.wmss.core.DeletedRecord;
import de.wwu.wmss.core.Note;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.PerformanceMediumType;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.PersonDescription;
import de.wwu.wmss.core.Provenance;
import de.wwu.wmss.core.WMSSRequest;
import de.wwu.wmss.core.Tonality;
import de.wwu.wmss.core.WMSSImportRequest;
import de.wwu.wmss.settings.SystemSettings;
import de.wwu.wmss.settings.Util;

public class FactoryNeo4j {

	private static Logger logger = Logger.getLogger("Neo4j-Factory");

	
	public static void prepareDatabase(WMSSImportRequest importRequest) {

    	InputStream is;
		try {
			is = new FileInputStream("config/neo4j/prepareDatabase.cql");

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
			is = new FileInputStream("config/neo4j/formatGraph.cql");

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
	    			Neo4jConnector.getInstance().executeQuery(arrayCypher[i].replaceAll("\n", " "), Util.getDataSource(importRequest.getSource()));	
	    		}	    		
			}
	    	logger.info("Neo4j graph complete.");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**		
		String notes_octave_signature = "MATCH (note:chord__Note)-[:chord__natural]->(natural:chord__Natural) \n" + 
				"OPTIONAL MATCH (note:chord__Note)-[:chord__modifier]->(modifier)\n" + 
				"WITH note, CASE SUBSTRING(modifier.uri,31) \n" + 
				"        	WHEN 'flat' THEN 'b'\n" + 
				"        	WHEN 'doubleflat' THEN 'bb'\n" + 
				"        	WHEN 'sharp' THEN 'x'\n" + 
				"        	WHEN 'doublesharp' THEN 'xx'\n" + 
				"        	WHEN NULL THEN ''\n" + 
				"           END AS modifier,\n" + 
				"       SUBSTRING(natural.uri,36) AS natural           \n" + 
				"SET note.signature = modifier+natural+note.mso__hasOctave,\n" + 
				"    note.note = modifier+natural,\n" + 
				"    note.octave = note.mso__hasOctave\n" + 
				"WITH note\n" + 
				"CALL apoc.create.addLabels(id(note),[note.signature,note.note]) YIELD node\n" + 
				"RETURN COUNT(node) AS note_octave_signature";
		Neo4jConnector.getInstance().executeQuery(notes_octave_signature, Util.getDataSource(importRequest.getSource()));
		
		String noteset_signature_label = "MATCH (ns:mso__NoteSet)-[:mso__hasNote]->(n:chord__Note)\n" + 
				"CALL apoc.create.addLabels(id(ns),[n.signature]) YIELD node\n" + 
				"WITH node AS ns\n" + 
				"MATCH (ns)-[:mso__hasNote]->(n:chord__Note)\n" + 
				"CALL apoc.create.addLabels(id(ns),[n.note]) YIELD node\n" + 
				"RETURN COUNT(node) AS noteset_signature_label;";
		Neo4jConnector.getInstance().executeQuery(noteset_signature_label, Util.getDataSource(importRequest.getSource()));
		
		String noteset_size = "MATCH (noteset:mso__NoteSet)-[r:mso__hasNote]-()\n" + 
				"WITH noteset,COUNT(r) AS size\n" + 
				"SET noteset.size = size\n" + 
				"RETURN COUNT(noteset) AS noteset_size;";
		Neo4jConnector.getInstance().executeQuery(noteset_size, Util.getDataSource(importRequest.getSource()));
		
		String durations = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)\n" + 
				"REMOVE duration:Resource\n" + 
				"WITH noteset,LOWER(SUBSTRING(labels(duration)[0],5)) AS d\n" + 
				"CALL apoc.create.addLabels(id(noteset),['D_'+d]) YIELD node\n" + 
				"RETURN COUNT(node) AS durations;";
		Neo4jConnector.getInstance().executeQuery(durations, Util.getDataSource(importRequest.getSource()));
		
		String voice_staff_collection_role_dateissued = "MATCH (scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(noteset)\n" + 
				"OPTIONAL MATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)\n" + 
				"OPTIONAL MATCH (scr:mo__Score)-[:dc__creator]->(creator:foaf__Person)-[:gndo__professionOrOccupation]->(role:prov__Role)\n" + 
				"OPTIONAL MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail)\n" + 
				"SET scr.thumbnail = thumbnail.uri,\n" + 
				"    noteset.voice = voice.rdfs__ID, \n" + 
				"    noteset.staff = staff.rdfs__ID, \n" + 
				"    scr.collectionUri =  collection.uri, \n" + 
				"    scr.collectionLabel = collection.rdfs__label,\n" + 
				"    scr.issued = datetime(scr.dcterms__issued),\n" + 
				"    creator.roleUri = role.uri, \n" + 
				"    creator.roleName = role.gndo__preferredNameForTheSubjectHeading\n" + 
				"RETURN COUNT(noteset) AS voice_staff_collection_role_dateissued;";
		Neo4jConnector.getInstance().executeQuery(voice_staff_collection_role_dateissued, Util.getDataSource(importRequest.getSource()));
		
		String encoder = "MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person)\n" + 
				"WHERE scr.encoderUri IS NULL \n" + 
				"SET scr.encoderUri = encoder.uri,\n" + 
				"    scr.encoderName = encoder.foaf__name,\n" + 
				"    scr.provGeneratedAtTime = activity.prov__startedAtTime,\n" + 
				"    scr.provComments = activity.rdfs__comment\n" + 
				"RETURN COUNT(scr) AS Encoder;";
		Neo4jConnector.getInstance().executeQuery(encoder, Util.getDataSource(importRequest.getSource()));
		
		String dots = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute)\n" + 
				"REMOVE attribute:Resource\n" + 
				"WITH noteset,attribute\n" + 
				"CALL apoc.create.addLabels(id(noteset),[SUBSTRING(labels(attribute)[0],5)]) YIELD node\n" + 
				"RETURN COUNT(node) AS dots;\n";
		Neo4jConnector.getInstance().executeQuery(dots, Util.getDataSource(importRequest.getSource()));
		
		String measure_timesignature = "MATCH (measure:mso__Measure)-[r:mso__hasTime]->(time)\n" + 
				"WHERE measure.beats IS NULL\n" + 
				"SET measure.beats = time.mso__hasBeats, measure.beatType = time.mso__hasBeatType\n" + 
				"DELETE r,time\n" + 
				"RETURN COUNT(measure) AS MeasureTimeSignature;";
		Neo4jConnector.getInstance().executeQuery(measure_timesignature, Util.getDataSource(importRequest.getSource()));
		
		
		String measure_key = "MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)\n" + 
				"MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)\n" + 
				"WITH measure, tonicNode,modeNode, \n" + 
				"     LOWER(SUBSTRING(modeNode.uri,39)) AS mode, \n" + 
				"     LOWER(SUBSTRING(tonicNode.uri,36)) AS tonic\n" + 
				"WITH measure,tonic,mode,\n" + 
				"   CASE tonic+'-'+mode\n" + 
				"       WHEN 'c-major' THEN 'standard_key' \n" + 
				"       WHEN 'a-minor' THEN 'standard_key'        \n" + 
				"       WHEN 'g-major' THEN 'xF'\n" + 
				"       WHEN 'e-minor' THEN 'xF'       \n" + 
				"       WHEN 'd-major' THEN 'xFC'\n" + 
				"       WHEN 'b-minor' THEN 'xFC'       \n" + 
				"       WHEN 'a-major' THEN 'xFCG' \n" + 
				"       WHEN 'fsharp-minor' THEN 'xFCG'        \n" + 
				"       WHEN 'e-major' THEN 'xFCGD' \n" + 
				"       WHEN 'csharp-minor' THEN 'xFCGD'        \n" + 
				"       WHEN 'b-major' THEN 'xFCGDA' \n" + 
				"       WHEN 'gsharp-minor' THEN 'xFCGDA'        \n" + 
				"       WHEN 'fsharp-major' THEN 'xFCGDAE'\n" + 
				"       WHEN 'dsharp-minor' THEN 'xFCGDAE'       \n" + 
				"       WHEN 'csharp-major' THEN 'xFCGDAEB'\n" + 
				"       WHEN 'asharp-minor' THEN 'xFCGDAEB'              \n" + 
				"       WHEN 'f-major' THEN 'bB'\n" + 
				"       WHEN 'd-minor' THEN 'bB'       \n" + 
				"       WHEN 'bflat-major' THEN 'bBE'\n" + 
				"       WHEN 'g-minor' THEN 'bBE'       \n" + 
				"       WHEN 'eflat-major' THEN 'bBEA'\n" + 
				"       WHEN 'c-minor' THEN 'bBEA'       \n" + 
				"       WHEN 'aflat-major' THEN 'bBEAD'\n" + 
				"       WHEN 'f-minor' THEN 'bBEAD'              \n" + 
				"       WHEN 'dflat-major' THEN 'bBEADG'\n" + 
				"       WHEN 'bflat-minor' THEN 'bBEADG'       \n" + 
				"       WHEN 'gflat-major' THEN 'bBEADGC'\n" + 
				"       WHEN 'eflat-minor' THEN 'bBEADGC'       \n" + 
				"       WHEN 'cflat-major' THEN 'bBEADGCF'\n" + 
				"       WHEN 'aflat-minor' THEN 'bBEADGCF'       \n" + 
				"     END AS key\n" + 
				"CALL apoc.create.addLabels(id(measure),[key]) YIELD node\n" + 
				"RETURN COUNT(measure) AS MeasureKey;";
		Neo4jConnector.getInstance().executeQuery(measure_key, Util.getDataSource(importRequest.getSource()));
				
		String instrument = "MATCH (part:mso__ScorePart)\n" + 
				"CALL apoc.create.addLabels(id(part),[REPLACE(part.rdfs__label,'.','_')]) YIELD node\n" + 
				"WITH node AS part\n" + 
				"MATCH (part)-[:skos__broader]->(type)\n" + 
				"CALL apoc.create.addLabels(id(part),[type.skos__prefLabel]) YIELD node\n" + 
				"RETURN COUNT(node) AS instrument;";
		Neo4jConnector.getInstance().executeQuery(instrument, Util.getDataSource(importRequest.getSource()));
		
		String mediums = "MATCH (score:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:skos__broader]->(type) \n" + 
				"MERGE (movement)-[:hasMediumType]-(t:mediumType {mediumTypeId:type.uri, mediumTypeDescription: type.skos__prefLabel})\n" + 
				"MERGE (t)-[:hasMedium]->(i:Medium {mediumId: part.uri,mediumDescription: part.skos__prefLabel, mediumScoreDescription: part.dc__description, mediumCode: part.rdfs__label, ensemble: part.mso__isEnsemble, solo:part.mso__isSolo})\n" + 
				"RETURN COUNT(i) AS mediums;";
		Neo4jConnector.getInstance().executeQuery(mediums, Util.getDataSource(importRequest.getSource()));
		
		String movemements_beatunit = "\n" + 
				"MATCH (mov:mo__Movement)\n" + 
				"OPTIONAL MATCH (mov:mo__Movement)-[:mso__hasBeatUnit]-(unit)\n" + 
				"WITH mov,CASE unit WHEN NULL THEN \"unknown\" \n" + 
				"         ELSE LOWER(SUBSTRING(unit.uri,54))\n" + 
				"		 END AS b\n" + 
				"WHERE mov.beatUnit IS NULL\n" + 
				"SET mov.beatUnit = b\n" + 
				"RETURN COUNT(mov) AS movements_beatunit;";
		Neo4jConnector.getInstance().executeQuery(movemements_beatunit, Util.getDataSource(importRequest.getSource()));
		
		String noteset_clef_sign = "MATCH (noteset:mso__NoteSet)-[:mso__hasClef]->(clef)-[:mso__sign]->(sign)\n" + 
				"CALL apoc.create.addLabels(id(noteset),['c_'+SUBSTRING(sign.uri,36)+clef.mso__line]) YIELD node\n" + 
				"RETURN COUNT(clef) AS noteset_clef_sign;";
		Neo4jConnector.getInstance().executeQuery(noteset_clef_sign, Util.getDataSource(importRequest.getSource()));
		
		String format  = "MATCH (score:mo__Score)\n" + 
				"WITH  score, \n" + 
				"  CASE \n" + 
				"    WHEN NOT score.mso__asMusicXML = '' THEN 'musicxml'\n" + 
				"    WHEN NOT score.mso__asMEI = '' THEN 'mei'\n" + 
				"  END AS docFormat\n" + 
				"WHERE score.format IS NULL  \n" + 
				"SET score.format = docFormat\n" + 
				"RETURN COUNT(docFormat) AS format;";
		Neo4jConnector.getInstance().executeQuery(format, Util.getDataSource(importRequest.getSource()));
		
		// Renaming labels and properties to fit the neo4j naming conventions
						
		String nextNoteSet = "MATCH (n1)-[r:mso__nextNoteSet]->(n2) CREATE (n1)-[r2:NEXT]->(n2) DELETE r"; 
		Neo4jConnector.getInstance().executeQuery(nextNoteSet, Util.getDataSource(importRequest.getSource()));
		String nextMeasure = "MATCH (n1)-[r:mso__nextMeasure]->(n2) CREATE (n1)-[r2:NEXT]->(n2) DELETE r"; 
		Neo4jConnector.getInstance().executeQuery(nextMeasure, Util.getDataSource(importRequest.getSource()));		
		String hasMeasure = "MATCH (n1)-[r:mso__hasMeasure]->(n2) CREATE (n1)-[r2:MEASURE]->(n2) DELETE r;"; 
		Neo4jConnector.getInstance().executeQuery(hasMeasure, Util.getDataSource(importRequest.getSource()));
		String noteset  = "MATCH (n1)-[r:mso__hasNoteSet]->(n2) CREATE (n1)-[r2:NOTESET]->(n2) DELETE r;"; 
		Neo4jConnector.getInstance().executeQuery(noteset, Util.getDataSource(importRequest.getSource()));
		String measure = "MATCH (n:mso__Measure) SET n :Measure REMOVE n:mso__Measure;"; 
		Neo4jConnector.getInstance().executeQuery(measure, Util.getDataSource(importRequest.getSource()));	
		
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Movement(mso__hasBeatsPerMinute);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Movement(beatUnit);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(issued);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(encoderUri);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(collectionUri);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__NoteSet(size);", Util.getDataSource(importRequest.getSource()));		
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__NoteSet(clefShape,clefLine);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(format);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Measure(key);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Measure(beats);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Measure(beatType);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__NoteSet(clefSign);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__NoteSet(clefLine);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_longa(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_whole(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_half(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_quarter(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_eighth(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_16th(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_32nd(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_64th(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_128th(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D_256th(size);", Util.getDataSource(importRequest.getSource()));
		
		
		**/
		
	}
	
	public static int insertScore(File file, WMSSImportRequest importRequest) {
					
		int result = 0;
		
		try {
			/**			
			String createPredicates = 
					"CREATE (:NamespacePrefixDefinition {\n" + 
					"  `http://linkeddata.uni-muenster.de/ontology/musicscore#`: 'mso',\n" + 
					"  `http://purl.org/ontology/chord/`: 'chord',\n" + 
					"  `http://purl.org/dc/elements/1.1/`: 'dc',\n" + 
					"  `http://purl.org/dc/terms/`: 'dcterms',\n" + 
					"  `http://www.w3.org/2000/01/rdf-schema#`: 'rdfs',\n" + 
					"  `http://purl.org/ontology/chord/note/`: 'note',\n" + 
					"  `http://purl.org/ontology/tonality/`: 'ton',\n" + 
					"  `http://purl.org/ontology/tonality/mode/`: 'mode',\n" + 
					"  `http://purl.org/ontology/mo/`: 'mo',\n" + 
					"  `http://xmlns.com/foaf/0.1/`: 'foaf',\n" + 
					"  `http://www.w3.org/ns/prov#`: 'prov',\n" + 
					"  `http://d-nb.info/standards/elementset/gnd#`: 'gndo',\n" + 
					"  `http://www.w3.org/2004/02/skos/core#`: 'skos',\n" + 
					"  `http://purl.org/ontology/mo/mit#`: 'mit'\n" + 
					"});";
						
			//StatementResult rs = Neo4jConnector.getInstance().executeQuery(createPredicates, Util.getDataSource(importRequest.getSource()));
			
			String importRDF = "CALL semantics.importRDF(\""+file.toURI().toURL()+"\",\""+importRequest.getFormat()+"\",{shortenUrls: true, commitSize: "+importRequest.getCommitSize()+"});";	
			
			
			*/		
			
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
				
			//formatGraph(importRequest);
									
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
		
	public static String getMusicXML(WMSSRequest request){
		
		String result = "";
		String cypher = "\n\nMATCH (score:Score {uri:\""+request.getIdentifier()+"\"})\n";

		if(request.getFormat().equals("musicxml")||request.getFormat().equals("")) {
			cypher = cypher +"RETURN score.asMusicXML AS xml\n"; 
		} else if (request.getFormat().equals("mei")) {
			cypher = cypher +"RETURN score.asMEI AS xml\n";
		}

		logger.debug("getMusicXML:\n"+cypher);
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, Util.getDataSource(request.getSource()));

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
	
		String cypher = "MATCH (score:Score) RETURN COUNT(score) AS total";
		
		logger.debug("getScoresCount:\n" + cypher);	
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		Record record = rs.next();
				
		return record.get("total").asInt();
		
	}
		
	public static ArrayList<PersonDescription> getRoles(DataSource ds){
	
		ArrayList<PersonDescription> result = new ArrayList<PersonDescription>();
		
		String cypher = "\n\nMATCH (role:prov__Role)<-[:gndo__professionOrOccupation]-(creator:Person)<-[:CREATOR]-(scr:Score)\n" + 
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

		String cypher = "MATCH (scr:Score) RETURN DISTINCT CASE WHEN scr.asMusicXML IS NULL THEN FALSE ELSE TRUE END AS musicxml; \n";

		logger.info("getFormats:\n" + cypher);

		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypher, ds);
		Record record = rs.next();

		if(record.get("musicxml").asBoolean()) {
			Format musicxml = new Format();
			musicxml.setFormatId("musicxml");
			musicxml.setFormatDescription("MusicXML");
			result.add(musicxml);
		}

		cypher = "\n\nMATCH (scr:Score)\n" + 
				 "RETURN DISTINCT CASE WHEN scr.asMEI IS NULL THEN FALSE ELSE TRUE END AS mei\n";

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
		String cypher = "MATCH (measure:Measure) RETURN DISTINCT measure.tonic AS tonic, measure.mode AS mode";
		
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
		
		String cypher = "\n\nMATCH (score:Score)\n" + 
					    "RETURN DISTINCT score.collectionUri AS identifier, score.collectionLabel AS label \n";
		
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
				"MATCH (scr:Score)-[:MOVEMENT]->(mov:Movement)-[:hasMediumType]->(mediumType:mediumType)-[:hasMedium]->(medium:Medium)\n" + 
				"WHERE scr.uri=\""+ scoreURI +"\"\n" + 
				"WITH  \n" + 
				"  mov.uri AS movementIdentifier,\n" + 
				"  mov.title AS  movementName,\n" + 
				"  mov.beatUnit AS beatUnit,\n" + 
				"  mov.mso__hasBeatsPerMinute AS beatsPerMinute,    \n" + 
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
				"   mediumsList: COLLECT(mediumsListResultset)} AS movementsResultset \n";

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
				movement.setMovementName(movementsObject.get("movementName").toString());
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
					result.add(movement);
				}
				
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
		String match = "MATCH (scr:Score)-[:CREATOR]->(creator:Person)\n";
		
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
					//currentDurationType = "d"+ noteSequence.get(i).getDuration();					
					currentDurationType = "D_"+ noteSequence.get(i).getDuration();
					
					switch (noteSequence.get(i).getDuration()) {
					case "0":
						currentDurationType = "D_longa";
						break;
					case "1":
						currentDurationType = "D_whole";
						break;
					case "2":
						currentDurationType = "D_half";
						break;
					case "3":
						currentDurationType = "D_32nd";
						break;
					case "4":
						currentDurationType = "D_quarter";
						break;
					case "5":
						currentDurationType = "D_64th";
						break;
					case "6":
						currentDurationType = "D_16th";
						break;
					case "7":
						currentDurationType = "D_128th";
						break;
					case "8":
						currentDurationType = "D_eighth";
						break;
					case "9":
						currentDurationType = "D_breve";
						break;
					case "a":
						currentDurationType = "D_256th";
						break;
					default:
						break;
					}
					
				}	

				String currentPitchType = "NoteSet";
				//String currentPitchType = "chord__Note";
				
				if(!wmssRequest.isIgnorePitch()) {
					currentPitchType = noteSequence.get(i).getAccidental()+noteSequence.get(i).getPitch();
					if(!wmssRequest.isIgnoreOctaves()) {
						currentPitchType = currentPitchType + noteSequence.get(i).getOctave();
					}

				}

				if(!noteSequence.get(i).getTime().equals("")) {					
					String[] time = noteSequence.get(i).getTime().split("/");
					where = where + "AND measure"+noteSequence.get(i).getMeasure()+".beatType="+time[1]+"\n" 
								  + "AND measure"+noteSequence.get(i).getMeasure()+".beats="+time[0]+"\n";					
				}

				if(!noteSequence.get(i).getKey().equals("")) {
					//where = where + "AND measure"+noteSequence.get(i).getMeasure()+".key=\""+noteSequence.get(i).getKey()+"\"\n"; 
					match = match + "MATCH (measure1:"+noteSequence.get(i).getKey()+")\n";
				}

				if(!noteSequence.get(i).getClef().equals("")) {
					//where = where + "AND ns"+i+".clefSign=\""+String.valueOf(noteSequence.get(i).getClef().charAt(0))+"\"\n";
					//where = where + "AND ns"+i+".clefLine="+String.valueOf(noteSequence.get(i).getClef().charAt(2))+"\n";
					match = match + "MATCH (ns"+i+":c_"+String.valueOf(noteSequence.get(i).getClef().charAt(0))+String.valueOf(noteSequence.get(i).getClef().charAt(2))+")\n";
				}

				if(noteSequence.get(i).getDotted()!=0) {
					if(noteSequence.get(i).getDotted()==1) {
						//where = where + "AND ns"+i+":dot \n";
						match = match + "MATCH (ns"+i+":Dot) \n";
					} else if(noteSequence.get(i).getDotted()==2) {
						//where = where + "AND ns"+i+":doubledot \n";
						match = match + "MATCH (ns"+i+":DoubleDot) \n";
					} else if(noteSequence.get(i).getDotted()==3) {
						//where = where + "AND ns"+i+":tripledot \n";
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
					
					notesetCounter++;
					chordSize = 0;	
					
									
				} else {
					
					match = match + "MATCH (ns"+notesetCounter+":"+currentPitchType+")\n";
					chordSize++;
					firstElement = false;
				}
											
				//System.out.println("notesetCounter >" + notesetCounter+ " > " +currentPitchType + "> isChord? " +noteSequence.get(i).isChord());

				previousDurationType = currentDurationType;				
				i++;
			}


		} 
				
		/**
		 * Match clause
		 */
		
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
			//where = where + "AND part.rdfs__label=\""+wmssRequest.getPerformanceMedium()+"\"\n";
			match = match + "MATCH (part:"+wmssRequest.getPerformanceMedium().replace(".", "_")+")\n";
		} 

		if(!wmssRequest.getPerformanceMediumType().equals("")) {
			//where = where + "AND part.typeLabel=\""+wmssRequest.getPerformanceMediumType()+"\"\n";
			match = match + "MATCH (part:"+wmssRequest.getPerformanceMediumType().toLowerCase()+")\n";
		}
		
		
		/**
		 * Where clause
		 */
		
		
		if(!wmssRequest.getPerson().equals("")) {
			where = where + "AND creator.uri=\""+wmssRequest.getPerson()+"\"\n";
		}

		if(!wmssRequest.getPersonRole().equals("")) {
			where = where + "AND creator.roleName=\""+wmssRequest.getPersonRole()+"\"\n";
		}
				
		if(!wmssRequest.getFormat().equals("")) {
			where = where + "AND scr.format=\""+wmssRequest.getFormat()+"\"\n";
		} 
		
		if(!wmssRequest.getTempoBeatUnit().equals("")) {
			//where = where + "AND movements.beatUnit='"+wmssRequest.getTempoBeatUnit()+"' \n"; 
			match = match + "MATCH (scr:Score)-[:MOVEMENT]->(movements:Movement {beatUnit: '"+wmssRequest.getTempoBeatUnit()+"'})\n";
			
		}

		if(!wmssRequest.getClef().equals("")) {
			//where = where + "AND ns0.clefSign= \""+String.valueOf(wmssRequest.getClef().charAt(0))+"\"\n";
			//where = where + "AND ns0.clefLine= "+String.valueOf(wmssRequest.getClef().charAt(2))+"\n";
			match = match + "MATCH (ns0:c_"+String.valueOf(wmssRequest.getClef().charAt(0))+String.valueOf(wmssRequest.getClef().charAt(2))+")\n";
		}
		
		if(!wmssRequest.getTempoBeatsPerMinute().equals("")) {
			String[] bpm = wmssRequest.getTempoBeatsPerMinute().split("-");
			
			if(bpm.length==1) {
				where = where + "AND movements.mso__hasBeatsPerMinute = " + bpm[0] +"\n";
			}
			
			if(bpm.length==2) {
				where = where + "AND movements.mso__hasBeatsPerMinute >= " + bpm[0] +"\n";
				where = where + "AND movements.mso__hasBeatsPerMinute <= " + bpm[1] +"\n";
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
			
		if(!wmssRequest.getKey().equals("")) {
			//where = where + "AND measure1.key=\""+wmssRequest.getKey()+"\"\n";
			match = match + "MATCH (measure1:"+wmssRequest.getKey()+")";
		}

		if(!wmssRequest.getTimeSignature().equals("")) {
			String[] time = wmssRequest.getTimeSignature().split("/");
			where = where + "AND measure.beatType="+time[1]+"\n" 
					      + "AND measure.beats="+time[0]+"\n";					
		}
		
		if(wmssRequest.isSolo()) {
			where = where + "AND part.isSolo=\""+wmssRequest.isSolo()+"\"";
		}
		
		if(wmssRequest.isEnsemble()) {
			where = where + "AND part.isEnsemble=\""+wmssRequest.isEnsemble()+"\"";
		}

		if(!wmssRequest.getCollection().equals("")) {
			where = where  + "AND scr.collectionUri = '"+wmssRequest.getCollection()+"' \n";
		}
		
		return match + where;		
	}
			
	public static ArrayList<DeletedRecord> deleteScore(WMSSRequest request, DataSource dataSource) {

		ArrayList<DeletedRecord> result = new ArrayList<DeletedRecord>();
		String cypherQuery = "MATCH (score:Score)-[:MOVEMENT]->(movement)-[:PART]->(part)-[:mso__hasStaff]->(staff)-[:mso__hasVoice]->(voice)-[:NOTESET]->(noteset)-[:mso__hasNote]->(note)\n" + 
				"MATCH (score:Score)-[:MOVEMENT]->(movement)-[:PART]->(part)-[:MEASURE]->(measure)\n" + 
				"WHERE score.uri = '"+request.getIdentifier()+"'\n" + 
				"WITH note,noteset,voice,staff,measure,part,movement,score,score.dc__title AS title, score.uri AS identifier, score.collectionUri AS collection\n" + 
				"DETACH DELETE note,noteset,voice,staff,measure,part,movement,score\n" + 
				"RETURN DISTINCT title,identifier,collection";
		
		logger.info("[deleteScore]:\n\n"+cypherQuery+"\n");
		
		StatementResult rs = Neo4jConnector.getInstance().executeQuery(cypherQuery, dataSource);

		while ( rs.hasNext() ) {
			Record record = rs.next();
			DeletedRecord score = new DeletedRecord();
			score.setTitle(record.get("title").asString());
			score.setScoreIdentifier(record.get("identifier").asString());
			score.setCollection(record.get("collection").asString());
			result.add(score);
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
				"    toString(scr.dcterms__issued) AS issued,\n" +
				"    scr.provGeneratedAtTime AS provGeneratedAtTime,\n " +
				"    scr.provComments AS provComments,\n " +
				"    scr.thumbnail AS thumbnail,\n " +
			    "	 scr.collectionUri AS collectionIdentifier, \n"+
			    "	 scr.collectionLabel AS collectionLabel, \n"+
				"    {persons: COLLECT(DISTINCT\n" + 
				"       {name: creator.foaf__name, \n" + 
				"     	 identifier: creator.uri, \n" +
				"	     role: creator.roleName} \n" + 
				"    )} AS persons,\n" + 
				"    {persons: COLLECT(DISTINCT{name: scr.encoderName, identifier: scr.encoderUri, role: \"Encoder\"})} AS encoders, \n";
		
		if(!request.getMelody().equals("")) {
			
				returnClause = returnClause +"	 {locations: \n" +
				"    COLLECT(DISTINCT{ \n" + 
				"      movementIdentifier: mov.uri,\n" + 
				"      movementName: mov.title,\n" + 
				"      startingMeasure: measure1.rdfs__label, \n" + 
				"      staff: ns0.staff, \n" + 
				"      voice: ns0.voice, \n" + 
				"      instrumentName: part.dc__description \n" + 
				"    })} AS locations, \n";				
		}		
				
		returnClause = returnClause +	"   CASE WHEN scr.asMusicXML IS NULL THEN FALSE ELSE TRUE END AS musicxml,\n" + 
									    "   CASE WHEN scr.asMEI IS NULL THEN FALSE ELSE TRUE END AS mei \n" + 
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
			score.getPersons().addAll(getPersons(gson.toJson(record.get("encoders").asMap())));			
			Provenance prov = new Provenance();
			prov.setGeneratedAtTime(record.get("provGeneratedAtTime").asString());
			prov.setComments(record.get("provComments").asString());
			
			score.setProvenance(prov);
						
			//if(!request.getRequestMode().equals("simplified")) {
			score.getMovements().addAll(getMovementData(score.getScoreId(), dataSource));
			//}	

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
