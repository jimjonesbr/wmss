package de.wwu.wmss.factory;

import java.io.File;
import java.io.IOException;
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
import de.wwu.wmss.settings.Util;

public class FactoryNeo4j {

	private static Logger logger = Logger.getLogger("Neo4j-Factory");
			
	public static void formatGraph(WMSSImportRequest importRequest) {
		
		String beatUnitStatement = 
				"MATCH (mov:mo__Movement)\n" + 
				"OPTIONAL MATCH (mov:mo__Movement)-[:mso__hasBeatUnit]-(unit)\n" + 
				"WITH mov,CASE unit \n" + 
				"           WHEN NULL THEN \"unknown\"\n" + 
				"           ELSE LOWER(SUBSTRING(unit.uri,54)) \n" + 
				"        END AS b\n" + 
				"WHERE mov.beatUnit IS NULL\n" + 
				"SET mov.beatUnit = b\n" + 
				"RETURN COUNT(mov) AS Movements;";
		
		Neo4jConnector.getInstance().executeQuery(beatUnitStatement, Util.getDataSource(importRequest.getSource()));
		
		String bpmStatement = 
				"MATCH (mov:mo__Movement) \n" + 
				"WITH mov, CASE mov.mso__hasBeatsPerMinute \n" + 
				"            WHEN NULL THEN 0 \n" + 
				"            ELSE toInt(mov.mso__hasBeatsPerMinute) \n" + 
				"          END AS bpm\n" + 
				"WHERE mov.mso__hasBeatsPerMinute IS NULL  \n" + 
				"SET mov.mso__hasBeatsPerMinute = bpm  \n" + 
				"RETURN COUNT(mov) AS BPM;";
		
		Neo4jConnector.getInstance().executeQuery(bpmStatement, Util.getDataSource(importRequest.getSource()));
		
		String dateIssuedStatement = 
				"MATCH (scr:mo__Score)\n" + 
				"WHERE scr.issued IS NULL \n" + 
				"SET scr.issued = datetime(scr.dcterms__issued)\n" + 
				"RETURN COUNT(scr.dcterms__issued) AS IssuedDates;";
		
		Neo4jConnector.getInstance().executeQuery(dateIssuedStatement, Util.getDataSource(importRequest.getSource()));
		
		String encoderStatement =
				"MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person) \n" + 
				"WHERE scr.encoderUri IS NULL\n" + 
				"SET scr.encoderUri = encoder.uri,\n" + 
				"    scr.encoderName = encoder.foaf__name,\n" + 
				"    scr.provGeneratedAtTime = activity.prov__startedAtTime,\n" + 
				"    scr.provComments = activity.rdfs__comment\n" + 
				"RETURN COUNT(scr) AS Encoder;";
		
		Neo4jConnector.getInstance().executeQuery(encoderStatement, Util.getDataSource(importRequest.getSource()));
				
		String thumbnailStatement = 
				"MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail) \n" + 
				"WHERE scr.thumbnail IS NULL\n" + 
				"SET scr.thumbnail = thumbnail.uri\n" + 
				"RETURN COUNT(scr) AS Thumbnails;";
		
		Neo4jConnector.getInstance().executeQuery(thumbnailStatement, Util.getDataSource(importRequest.getSource()));
		
		String notesetSizeStatement = 
				"MATCH (noteset:mso__NoteSet)-[r:mso__hasNote]-()\n" + 
				"WITH noteset,COUNT(r) AS size,\n" + 
				"  CASE \n" + 
				"  WHEN COUNT(r)>1 THEN TRUE \n" + 
				"  WHEN COUNT(r)=1 THEN FALSE END AS chord\n" + 
				"WHERE noteset.size IS NULL  \n" + 
				"SET noteset.size = size\n" + 
				"RETURN COUNT(noteset) AS NoteSets;";
		
		Neo4jConnector.getInstance().executeQuery(notesetSizeStatement, Util.getDataSource(importRequest.getSource()));
		
		String formatStatement =
				"MATCH (score:mo__Score)\n" + 
				"WITH  score, \n" + 
				"  CASE \n" + 
				"    WHEN NOT score.mso__asMusicXML = '' THEN 'musicxml'\n" + 
				"    WHEN NOT score.mso__asMEI = '' THEN 'mei'\n" + 
				"  END AS docFormat\n" + 
				"WHERE score.format IS NULL  \n" + 
				"SET score.format = docFormat\n" + 
				"RETURN COUNT(docFormat) AS Format;";
		
		Neo4jConnector.getInstance().executeQuery(formatStatement, Util.getDataSource(importRequest.getSource()));
		
		String instrumentStatement =
				"MATCH (:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(instrument:mo__Instrument) \n" + 
				"OPTIONAL MATCH (instrument:mo__Instrument)-[:skos__broader]->(type) \n" +  
				"WHERE instrument.typeUri IS NULL \n" + 
				"SET instrument.typeUri = type.uri,\n" + 
				"    instrument.typeLabel = type.skos__prefLabel\n" + 
				"RETURN instrument;";
				
		Neo4jConnector.getInstance().executeQuery(instrumentStatement, Util.getDataSource(importRequest.getSource()));
		
		String accidentalStatements = 
				"MATCH (note:chord__Note)-[:chord__natural]->(natural:chord__Natural) \n" + 
				"OPTIONAL MATCH (note:chord__Note)-[:chord__modifier]->(modifier)\n" + 
				"WITH note, CASE SUBSTRING(modifier.uri,31) \n" + 
				"        	WHEN 'flat' THEN 'b'\n" + 
				"        	WHEN 'doubleflat' THEN 'bb'\n" + 
				"        	WHEN 'sharp' THEN 'x'\n" + 
				"        	WHEN 'doublesharp' THEN 'xx'\n" + 
				"        	WHEN NULL THEN ''\n" + 
				"           END AS modifier,\n" + 
				"       SUBSTRING(natural.uri,36) AS natural \n" + 
				"WHERE NOT EXISTS (note.note)\n" + 
				"SET note.note = natural, note.accidental = modifier \n" + 
				"RETURN COUNT(note) AS Note_Accidentals;";		
		Neo4jConnector.getInstance().executeQuery(accidentalStatements, Util.getDataSource(importRequest.getSource()));
		
		String restStatement = 
				"MATCH (note:chord__Note)\n" + 
				"WHERE note.note IS NULL  \n" + 
				"SET note.note = '-', note.accidental = ''\n" + 
				"RETURN COUNT(note) AS Rests;";		
		Neo4jConnector.getInstance().executeQuery(restStatement, Util.getDataSource(importRequest.getSource()));
		
		String noteA = "MATCH (note:chord__Note {note:'A', accidental: ''}) WHERE NOT 'A' IN labels(note) SET note :A RETURN COUNT(note) AS A;";
		Neo4jConnector.getInstance().executeQuery(noteA, Util.getDataSource(importRequest.getSource()));
		String noteAx = "MATCH (note:chord__Note {note:'A', accidental: 'x'}) WHERE NOT 'Ax' IN labels(note) SET note :Ax RETURN COUNT(note) AS Ax;";
		Neo4jConnector.getInstance().executeQuery(noteAx, Util.getDataSource(importRequest.getSource()));
		String noteAxx = "MATCH (note:chord__Note {note:'A', accidental: 'xx'}) WHERE NOT 'Axx' IN labels(note) SET note :Axx RETURN COUNT(note) AS Axx;";
		Neo4jConnector.getInstance().executeQuery(noteAxx, Util.getDataSource(importRequest.getSource()));
		String noteAb = "MATCH (note:chord__Note {note:'A', accidental: 'b'}) WHERE NOT 'Ab' IN labels(note) SET note :Ab RETURN COUNT(note) AS Ab;";
		Neo4jConnector.getInstance().executeQuery(noteAb, Util.getDataSource(importRequest.getSource()));
		String noteAbb = "MATCH (note:chord__Note {note:'A', accidental: 'bb'}) WHERE NOT 'Abb' IN labels(note) SET note :Abb RETURN COUNT(note) AS Abb;";
		Neo4jConnector.getInstance().executeQuery(noteAbb, Util.getDataSource(importRequest.getSource()));

		String noteB = "MATCH (note:chord__Note {note:'B', accidental: ''}) WHERE NOT 'B' IN labels(note) SET note :B RETURN COUNT(note) AS B;";
		Neo4jConnector.getInstance().executeQuery(noteB, Util.getDataSource(importRequest.getSource()));
		String noteBx = "MATCH (note:chord__Note {note:'B', accidental: 'x'}) WHERE NOT 'Bx' IN labels(note) SET note :Bx RETURN COUNT(note) AS Bx;";
		Neo4jConnector.getInstance().executeQuery(noteBx, Util.getDataSource(importRequest.getSource()));
		String noteBxx = "MATCH (note:chord__Note {note:'B', accidental: 'xx'}) WHERE NOT 'Bxx' IN labels(note) SET note :Bxx RETURN COUNT(note) AS Bxx;";
		Neo4jConnector.getInstance().executeQuery(noteBxx, Util.getDataSource(importRequest.getSource()));
		String noteBb = "MATCH (note:chord__Note {note:'B', accidental: 'b'}) WHERE NOT 'Bb' IN labels(note) SET note :Bb RETURN COUNT(note) AS Bb;";
		Neo4jConnector.getInstance().executeQuery(noteBb, Util.getDataSource(importRequest.getSource()));
		String noteBbb = "MATCH (note:chord__Note {note:'B', accidental: 'bb'}) WHERE NOT 'Bbb' IN labels(note) SET note :Bbb RETURN COUNT(note) AS Bbb;";
		Neo4jConnector.getInstance().executeQuery(noteBbb, Util.getDataSource(importRequest.getSource()));
		
		String noteC = "MATCH (note:chord__Note {note:'C', accidental: ''}) WHERE NOT 'C' IN labels(note) SET note :C RETURN COUNT(note) AS C;";
		Neo4jConnector.getInstance().executeQuery(noteC, Util.getDataSource(importRequest.getSource()));
		String noteCx = "MATCH (note:chord__Note {note:'C', accidental: 'x'}) WHERE NOT 'Cx' IN labels(note) SET note :Cx RETURN COUNT(note) AS Cx;";
		Neo4jConnector.getInstance().executeQuery(noteCx, Util.getDataSource(importRequest.getSource()));
		String noteCxx = "MATCH (note:chord__Note {note:'C', accidental: 'xx'}) WHERE NOT 'Cxx' IN labels(note) SET note :Cxx RETURN COUNT(note) AS Cxx;";
		Neo4jConnector.getInstance().executeQuery(noteCxx, Util.getDataSource(importRequest.getSource()));
		String noteCb = "MATCH (note:chord__Note {note:'C', accidental: 'b'}) WHERE NOT 'Cb' IN labels(note) SET note :Cb RETURN COUNT(note) AS Cb;";
		Neo4jConnector.getInstance().executeQuery(noteCb, Util.getDataSource(importRequest.getSource()));
		String noteCbb = "MATCH (note:chord__Note {note:'C', accidental: 'bb'}) WHERE NOT 'Cbb' IN labels(note) SET note :Cbb RETURN COUNT(note) AS Cbb;";
		Neo4jConnector.getInstance().executeQuery(noteCbb, Util.getDataSource(importRequest.getSource()));
		
		String noteD = "MATCH (note:chord__Note {note:'D', accidental: ''}) WHERE NOT 'D' IN labels(note) SET note :D RETURN COUNT(note) AS D;";
		Neo4jConnector.getInstance().executeQuery(noteD, Util.getDataSource(importRequest.getSource()));
		String noteDx = "MATCH (note:chord__Note {note:'D', accidental: 'x'}) WHERE NOT 'Dx' IN labels(note) SET note :Dx RETURN COUNT(note) AS Dx;";
		Neo4jConnector.getInstance().executeQuery(noteDx, Util.getDataSource(importRequest.getSource()));
		String noteDxx = "MATCH (note:chord__Note {note:'D', accidental: 'xx'}) WHERE NOT 'Dxx' IN labels(note) SET note :Dxx RETURN COUNT(note) AS Dxx;";
		Neo4jConnector.getInstance().executeQuery(noteDxx, Util.getDataSource(importRequest.getSource()));
		String noteDb = "MATCH (note:chord__Note {note:'D', accidental: 'b'}) WHERE NOT 'Db' IN labels(note) SET note :Db RETURN COUNT(note) AS Db;";
		Neo4jConnector.getInstance().executeQuery(noteDb, Util.getDataSource(importRequest.getSource()));
		String noteDbb = "MATCH (note:chord__Note {note:'D', accidental: 'bb'}) WHERE NOT 'Dbb' IN labels(note) SET note :Dbb RETURN COUNT(note) AS Dbb;";
		Neo4jConnector.getInstance().executeQuery(noteDbb, Util.getDataSource(importRequest.getSource()));
	
		String noteE = "MATCH (note:chord__Note {note:'E', accidental: ''}) WHERE NOT 'E' IN labels(note) SET note :E RETURN COUNT(note) AS E;";
		Neo4jConnector.getInstance().executeQuery(noteE, Util.getDataSource(importRequest.getSource()));
		String noteEx = "MATCH (note:chord__Note {note:'E', accidental: 'x'}) WHERE NOT 'Ex' IN labels(note) SET note :Ex RETURN COUNT(note) AS Ex;";
		Neo4jConnector.getInstance().executeQuery(noteEx, Util.getDataSource(importRequest.getSource()));
		String noteExx = "MATCH (note:chord__Note {note:'E', accidental: 'xx'}) WHERE NOT 'Exx' IN labels(note) SET note :Exx RETURN COUNT(note) AS Exx;";
		Neo4jConnector.getInstance().executeQuery(noteExx, Util.getDataSource(importRequest.getSource()));
		String noteEb = "MATCH (note:chord__Note {note:'E', accidental: 'b'}) WHERE NOT 'Eb' IN labels(note) SET note :Eb RETURN COUNT(note) AS Eb;";
		Neo4jConnector.getInstance().executeQuery(noteEb, Util.getDataSource(importRequest.getSource()));
		String noteEbb = "MATCH (note:chord__Note {note:'E', accidental: 'bb'}) WHERE NOT 'Ebb' IN labels(note) SET note :Ebb RETURN COUNT(note) AS Ebb;";
		Neo4jConnector.getInstance().executeQuery(noteEbb, Util.getDataSource(importRequest.getSource()));
		
		String noteF = "MATCH (note:chord__Note {note:'F', accidental: ''}) WHERE NOT 'F' IN labels(note) SET note :F RETURN COUNT(note) AS F;";
		Neo4jConnector.getInstance().executeQuery(noteF, Util.getDataSource(importRequest.getSource()));
		String noteFx = "MATCH (note:chord__Note {note:'F', accidental: 'x'}) WHERE NOT 'Fx' IN labels(note) SET note :Fx RETURN COUNT(note) AS Fx;";
		Neo4jConnector.getInstance().executeQuery(noteFx, Util.getDataSource(importRequest.getSource()));
		String noteFxx = "MATCH (note:chord__Note {note:'F', accidental: 'xx'}) WHERE NOT 'Fxx' IN labels(note) SET note :Fxx RETURN COUNT(note) AS Fxx;";
		Neo4jConnector.getInstance().executeQuery(noteFxx, Util.getDataSource(importRequest.getSource()));
		String noteFb = "MATCH (note:chord__Note {note:'F', accidental: 'b'}) WHERE NOT 'Fb' IN labels(note) SET note :Fb RETURN COUNT(note) AS Fb;";
		Neo4jConnector.getInstance().executeQuery(noteFb, Util.getDataSource(importRequest.getSource()));
		String noteFbb = "MATCH (note:chord__Note {note:'F', accidental: 'bb'}) WHERE NOT 'Fbb' IN labels(note) SET note :Fbb RETURN COUNT(note) AS Fbb;";
		Neo4jConnector.getInstance().executeQuery(noteFbb, Util.getDataSource(importRequest.getSource()));
		
		String noteG = "MATCH (note:chord__Note {note:'G', accidental: ''}) WHERE NOT 'G' IN labels(note) SET note :G RETURN COUNT(note) AS G;";
		Neo4jConnector.getInstance().executeQuery(noteG, Util.getDataSource(importRequest.getSource()));
		String noteGx = "MATCH (note:chord__Note {note:'G', accidental: 'x'}) WHERE NOT 'Gx' IN labels(note) SET note :Gx RETURN COUNT(note) AS Gx;";
		Neo4jConnector.getInstance().executeQuery(noteGx, Util.getDataSource(importRequest.getSource()));
		String noteGxx = "MATCH (note:chord__Note {note:'G', accidental: 'xx'}) WHERE NOT 'Gxx' IN labels(note) SET note :Gxx RETURN COUNT(note) AS Gxx;";
		Neo4jConnector.getInstance().executeQuery(noteGxx, Util.getDataSource(importRequest.getSource()));
		String noteGb = "MATCH (note:chord__Note {note:'G', accidental: 'b'}) WHERE NOT 'Gb' IN labels(note) SET note :Gb RETURN COUNT(note) AS Gb;";
		Neo4jConnector.getInstance().executeQuery(noteGb, Util.getDataSource(importRequest.getSource()));
		String noteGbb = "MATCH (note:chord__Note {note:'G', accidental: 'bb'}) WHERE NOT 'Gbb' IN labels(note) SET note :Gbb RETURN COUNT(note) AS Gbb;";
		Neo4jConnector.getInstance().executeQuery(noteGbb, Util.getDataSource(importRequest.getSource()));
		
		String longa =	"MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__Longa) WHERE NOT 'd0' IN labels(noteset) SET noteset :d0 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(longa, Util.getDataSource(importRequest.getSource()));

		String breve = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__Breve) WHERE NOT 'd9' IN labels(noteset) SET noteset :d9 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(breve, Util.getDataSource(importRequest.getSource()));
		
		String whole = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__Whole) WHERE NOT 'd1' IN labels(noteset) SET noteset :d1 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(whole, Util.getDataSource(importRequest.getSource()));

		String half = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__Half) WHERE NOT 'd2' IN labels(noteset) SET noteset :d2 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(half, Util.getDataSource(importRequest.getSource()));
		
		String quarter = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__Quarter) WHERE NOT 'd4' IN labels(noteset) SET noteset :d4 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(quarter, Util.getDataSource(importRequest.getSource()));
		
		String eighth =	"MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__Eighth) WHERE NOT 'd8' IN labels(noteset) SET noteset :d8 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(eighth, Util.getDataSource(importRequest.getSource()));

		String sixteenth = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__16th) WHERE NOT 'd6' IN labels(noteset) SET noteset :d6 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(sixteenth, Util.getDataSource(importRequest.getSource()));

		String thirtysecond = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__32nd) WHERE NOT 'd3' IN labels(noteset) SET noteset :d3 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(thirtysecond, Util.getDataSource(importRequest.getSource()));
					
		String sixtyfourth = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__64th) WHERE NOT 'd5' IN labels(noteset) SET noteset :d5 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(sixtyfourth, Util.getDataSource(importRequest.getSource()));

		String hundred29th = "MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__128th) WHERE NOT 'd7' IN labels(noteset) SET noteset :d7 RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(hundred29th, Util.getDataSource(importRequest.getSource()));

		String twohundred56th =	"MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration:mso__256th) WHERE NOT 'da' IN labels(noteset) SET noteset :da RETURN COUNT(duration);\n";			
		Neo4jConnector.getInstance().executeQuery(twohundred56th, Util.getDataSource(importRequest.getSource()));

		String durationStatement =					
				"MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)\n" + 
				"WITH  noteset,CASE SUBSTRING(labels(duration)[1],5)\n" + 
				"                WHEN 'Longa' THEN 0\n" + 
				"                WHEN 'Breve' THEN 9\n" + 
				"                WHEN 'Whole' THEN  1\n" + 
				"                WHEN 'Half' THEN  2\n" + 
				"                WHEN 'Quarter' THEN  4\n" + 
				"                WHEN 'Eighth' THEN  8\n" + 
				"                WHEN '16th' THEN  6\n" + 
				"                WHEN '32nd' THEN  3\n" + 
				"                WHEN '64th' THEN  5\n" + 
				"                WHEN '128th' THEN  7\n" + 
				"              END AS duration_numeric\n" + 
				"WHERE noteset.duration IS NULL \n" + 
				"SET noteset.duration = duration_numeric\n" + 
				"RETURN COUNT(noteset) AS Duration;";
		Neo4jConnector.getInstance().executeQuery(durationStatement, Util.getDataSource(importRequest.getSource()));
		
		String timeStatement = 
				"MATCH (measure)-[r:mso__hasTime]->(time)\n" + 
				"WHERE measure.beats IS NULL\n" + 
				"SET measure.beats = time.mso__hasBeats, measure.beatType = time.mso__hasBeatType\n" + 
				"DELETE r,time\n" + 
				"RETURN COUNT(measure) AS MeasureTimeSignature;";
		Neo4jConnector.getInstance().executeQuery(timeStatement, Util.getDataSource(importRequest.getSource()));
					
		String clefStatement = 
				"MATCH (noteset:mso__NoteSet)-[:mso__hasClef]->(clef)\n" + 
				"WHERE noteset.clef IS NULL\n" + 
				"SET noteset.clef = REPLACE(LOWER(labels(clef)[1]),'mso__','')\n" + 
				"RETURN COUNT(clef) AS NotesetClef;";			
		Neo4jConnector.getInstance().executeQuery(clefStatement, Util.getDataSource(importRequest.getSource()));
		
		String tonalityStatement = 
				"MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)\n" + 
				"MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)\n" + 
				"WITH measure, tonicNode,modeNode, \n" + 
				"     LOWER(SUBSTRING(modeNode.uri,39)) AS mode, \n" + 
				"     LOWER(SUBSTRING(tonicNode.uri,36)) AS tonic\n" + 
				"WITH measure,tonic,mode,\n" + 
				"   CASE tonic+'-'+mode\n" + 
				"       WHEN 'c-major' THEN '$' \n" + 
				"       WHEN 'a-minor' THEN '$'        \n" + 
				"       WHEN 'g-major' THEN '$xF'\n" + 
				"       WHEN 'e-minor' THEN '$xF'       \n" + 
				"       WHEN 'd-major' THEN '$xFC'\n" + 
				"       WHEN 'b-minor' THEN '$xFC'       \n" + 
				"       WHEN 'a-major' THEN '$xFCG' \n" + 
				"       WHEN 'fsharp-minor' THEN '$xFCG'        \n" + 
				"       WHEN 'e-major' THEN '$xFCGD' \n" + 
				"       WHEN 'csharp-minor' THEN '$xFCGD'        \n" + 
				"       WHEN 'b-major' THEN '$xFCGDA' \n" + 
				"       WHEN 'gsharp-minor' THEN '$xFCGDA'        \n" + 
				"       WHEN 'fsharp-major' THEN '$xFCGDAE'\n" + 
				"       WHEN 'dsharp-minor' THEN '$xFCGDAE'       \n" + 
				"       WHEN 'csharp-major' THEN '$xFCGDAEB'\n" + 
				"       WHEN 'asharp-minor' THEN '$xFCGDAEB'              \n" + 
				"       WHEN 'f-major' THEN '$bB'\n" + 
				"       WHEN 'd-minor' THEN '$bB'       \n" + 
				"       WHEN 'bflat-major' THEN '$bBE'\n" + 
				"       WHEN 'g-minor' THEN '$bBE'       \n" + 
				"       WHEN 'eflat-major' THEN '$bBEA'\n" + 
				"       WHEN 'c-minor' THEN '$bBEA'       \n" + 
				"       WHEN 'aflat-major' THEN '$bBEAD'\n" + 
				"       WHEN 'f-minor' THEN '$bBEAD'              \n" + 
				"       WHEN 'dflat-major' THEN '$bBEADG'\n" + 
				"       WHEN 'bflat-minor' THEN '$bBEADG'       \n" + 
				"       WHEN 'gflat-major' THEN '$bBEADGC'\n" + 
				"       WHEN 'eflat-minor' THEN '$bBEADGC'       \n" + 
				"       WHEN 'cflat-major' THEN '$bBEADGCF'\n" + 
				"       WHEN 'aflat-minor' THEN '$bBEADGCF'       \n" + 
				"     END AS key\n" +
				"WHERE measure.key IS NULL \n" +
				"SET measure.key = key, \n" + 
				"    measure.tonic = tonic, \n" + 
				"    measure.mode = mode\n" + 
				"RETURN COUNT(measure) AS MeasureKey;";			
		Neo4jConnector.getInstance().executeQuery(tonalityStatement, Util.getDataSource(importRequest.getSource()));
		
		
		String collections =
				"MATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)\n" + 
				"SET scr.collectionUri = collection.uri, scr.collectionLabel = collection.rdfs__label\n" + 
				"RETURN COUNT(scr.uri) AS Collections;";		
		Neo4jConnector.getInstance().executeQuery(collections, Util.getDataSource(importRequest.getSource()));
		
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Movement(mso__hasBeatsPerMinute);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Movement(beatUnit);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Measure(tonic,mode);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(issued);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(encoderUri);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(collectionUri);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__NoteSet(size);", Util.getDataSource(importRequest.getSource()));		
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :chord__Note(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mo__Score(format);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__ScorePart(rdfs__label);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__ScorePart(typeLabel);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__Measure(key);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :mso__Measure(beats,beatType);", Util.getDataSource(importRequest.getSource()));
		
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d1(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d2(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d3(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d4(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d5(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d6(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d7(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d8(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :d9(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :da(size);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :A(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :B(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :C(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :D(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :E(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :F(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :G(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Ax(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Bx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Cx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Dx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Ex(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Fx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Gx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Axx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Bxx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Cxx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Dxx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Exx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Fxx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Gxx(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));		
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Ab(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Bb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Cb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Db(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Eb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Fb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Gb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Abb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Bbb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Cbb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Dbb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Ebb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Fbb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		Neo4jConnector.getInstance().executeQuery("CREATE INDEX ON :Gbb(mso__hasOctave);", Util.getDataSource(importRequest.getSource()));
		

		
	}
	
	public static int insertScore(File file, WMSSImportRequest importRequest) {
					
		int result = 0;
		
		try {
						
			String createPredicates = 
					"CREATE (:NamespacePrefixDefinition {\n" + 
					"  `http://linkeddata.uni-muenster.de/ontology/musicscore#`: 'mso',\n" + 
					"  `http://purl.org/ontology/chord/`: 'chord',\n" + 
					"  `http://purl.org/dc/elements/1.1/`: 'dc',\n" + 
					"  `http://purl.org/dc/terms/`: 'dcterms',\n" + 
					"  `http://www.w3.org/1999/02/22-rdf-syntax-ns#`: 'rdfs',\n" + 
					"  `http://purl.org/ontology/chord/note/`: 'note',\n" + 
					"  `http://purl.org/ontology/tonality/`: 'ton',\n" + 
					"  `http://purl.org/ontology/tonality/mode/`: 'mode',\n" + 
					"  `http://purl.org/ontology/mo/`: 'mo',\n" + 
					"  `http://xmlns.com/foaf/0.1/`: 'foaf',\n" + 
					"  `http://www.w3.org/ns/prov#`: 'prov',\n" + 
					"  `http://d-nb.info/standards/elementset/gnd#`: 'gndo',\n" + 
					"  `http://www.w3.org/2000/01/rdf-schema#`: 'rdfs',\n" + 
					"  `http://www.w3.org/2004/02/skos/core#`: 'skos',\n" + 
					"  `http://purl.org/ontology/mo/mit#`: 'mit'\n" + 
					"});";
			
			
			StatementResult rs = Neo4jConnector.getInstance().executeQuery(createPredicates, Util.getDataSource(importRequest.getSource()));
						
			String importRDF = "CALL semantics.importRDF(\""+file.toURI().toURL()+"\",\""+importRequest.getFormat()+"\",{shortenUrls: true, commitSize: "+importRequest.getCommitSize()+"});";	
			logger.debug(importRDF);
			
			rs = Neo4jConnector.getInstance().executeQuery(importRDF, Util.getDataSource(importRequest.getSource()));

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
		String cypher = "\n\nMATCH (score:mo__Score {uri:\""+request.getIdentifier()+"\"})\n";

		if(request.getFormat().equals("musicxml")||request.getFormat().equals("")) {
			cypher = cypher +"RETURN score.mso__asMusicXML AS xml\n"; 
		} else if (request.getFormat().equals("mei")) {
			cypher = cypher +"RETURN score.mso__asMEI AS xml\n";
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
		String cypher = "MATCH (measure:mso__Measure) RETURN DISTINCT measure.tonic AS tonic, measure.mode AS mode";
		
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
		
		String cypher = "\n\nMATCH (score:mo__Score)\n" + 
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
			
			String previousDurationType = "";
			
			while(i<=noteSequence.size()-1) {

				
				String measureNode = "(measure:mso__Measure)";				
				//String currentTimeSignature = "";
				String currentDurationType = "mso__NoteSet";
								
				if(!wmssRequest.isIgnoreDuration()) {
					currentDurationType = "d"+ noteSequence.get(i).getDuration();					
				}	
				
				String currentPitchType = "chord__Note";
				if(!wmssRequest.isIgnorePitch()) {
					currentPitchType = noteSequence.get(i).getPitch()+noteSequence.get(i).getAccidental();
				}
				
				if(!noteSequence.get(i).getTime().equals("")) {					
					String[] time = noteSequence.get(i).getTime().split("/");
					where = where+ "AND measure"+noteSequence.get(i).getMeasure()+".beatType="+time[1]+"\n" 
							     + "AND measure"+noteSequence.get(i).getMeasure()+".beats="+time[0]+"\n";					
				}
				
				if(!noteSequence.get(i).getKey().equals("")) {
					where = where + "AND measure"+noteSequence.get(i).getMeasure()+".key=\""+noteSequence.get(i).getKey()+"\"\n"; 
				}
				
				if(i==0) {

					match = match + "MATCH "+scoreNode+"-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->"+instrumentNode+"-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(ns0:"+currentDurationType+")\n" + 
									"MATCH "+scoreNode+"-[:mo__movement]->(movements:mo__Movement) \n" + 
									"MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->"+measureNode+"-[:mso__hasNoteSet]->(ns0:"+currentDurationType+") \n";
							
					if(!wmssRequest.isIgnorePitch()) {						
						match = match +	"MATCH (ns0:"+currentDurationType+")-[:mso__hasNote]->(n0:"+currentPitchType+") \n";
					}
															
					if(!wmssRequest.isIgnoreOctaves()) {
						match = match +	"MATCH (n0:"+currentPitchType+" {mso__hasOctave:"+noteSequence.get(i).getOctave()+"}) \n";
					}
					
					previousDurationType = currentDurationType;
					
				} else {
					
					if(!noteSequence.get(i).isChord()) {
						notesetCounter++;
						if(notesetCounter>0) {
							match = match + "MATCH (ns"+(notesetCounter-1)+":"+previousDurationType+")-[:mso__nextNoteSet]->(ns"+notesetCounter+":"+currentDurationType+") \n";
						}
						
					}
											
					if(!wmssRequest.isIgnorePitch()) {											
						match = match + "MATCH (ns"+notesetCounter+":"+currentDurationType+")-[:mso__hasNote]->(n"+i+":"+currentPitchType+") \n";
					}										
					
					if(!wmssRequest.isIgnoreOctaves()) {	
						match = match +	"MATCH (n"+i+":" + currentPitchType + "{mso__hasOctave:"+noteSequence.get(i).getOctave()+"}) \n";
					}										 
				}

				if(wmssRequest.isIgnoreChords()) {
					where = where  + "AND ns"+notesetCounter+".size = 1 \n";
				}
				
				
				previousDurationType = currentDurationType;
				
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

		if(!wmssRequest.getKey().equals("")) {
			match = match + "MATCH (measure {key:\""+wmssRequest.getKey()+"\"})";
		}
		
		return match + "\nWHERE TRUE \n" + where;		
	}
			
	public static ArrayList<DeletedRecord> deleteScore(WMSSRequest request, DataSource dataSource) {

		ArrayList<DeletedRecord> result = new ArrayList<DeletedRecord>();
		String cypherQuery = "MATCH (score:mo__Score)-[:mo__movement]->(movement)-[:mso__hasScorePart]->(part)-[:mso__hasStaff]->(staff)-[:mso__hasVoice]->(voice)-[:mso__hasNoteSet]->(noteset)-[:mso__hasNote]->(note)\n" + 
				"MATCH (score:mo__Score)-[:mo__movement]->(movement)-[:mso__hasScorePart]->(part)-[:mso__hasMeasure]->(measure)\n" + 
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
