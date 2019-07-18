MATCH (note:chord__Note)-[:chord__natural]->(natural) 
OPTIONAL MATCH (note:chord__Note)-[:chord__modifier]->(modifier)
WITH note, CASE SUBSTRING(modifier.uri,31) 
        	WHEN 'flat' THEN 'b'
        	WHEN 'doubleflat' THEN 'bb'
        	WHEN 'sharp' THEN 'x'
        	WHEN 'doublesharp' THEN 'xx'
        	WHEN NULL THEN ''
           END AS modifier,
       SUBSTRING(natural.uri,36) AS natural           
SET note.signature = modifier+natural+note.mso__hasOctave,
    note.note = modifier+natural,
    note.octave = note.mso__hasOctave;

MATCH (ns:mso__NoteSet)-[:mso__hasNote]->(n:chord__Note)
CALL apoc.create.addLabels(id(ns),[n.signature]) YIELD node
WITH node AS ns
MATCH (ns)-[:mso__hasNote]->(n:chord__Note)
CALL apoc.create.addLabels(id(ns),[n.note]) YIELD node
RETURN COUNT(node) AS noteset_signature_label;

MATCH (noteset:mso__NoteSet)-[r:mso__hasNote]-()
WITH noteset,COUNT(r) AS size
SET noteset.size = size
RETURN COUNT(noteset) AS noteset_size;

MATCH (noteset:mso__NoteSet)-[r1:mso__hasDuration]->(duration)
REMOVE duration:Resource
WITH noteset,LOWER(SUBSTRING(labels(duration)[0],5)) AS d,r1
CALL apoc.create.addLabels(id(noteset),["Duration_"+d]) YIELD node
RETURN COUNT(node) AS durations;

MATCH (scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(noteset)
OPTIONAL MATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)
OPTIONAL MATCH (scr:mo__Score)-[:dc__creator]->(creator:foaf__Person)-[:dbo__occupation]->(role)
OPTIONAL MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail)
SET scr.thumbnail = thumbnail.uri,
    noteset.voice = voice.rdfs__label, 
    noteset.staff = staff.rdfs__label, 
    scr.collectionUri =  collection.uri, 
    scr.collectionLabel = collection.rdfs__label,
    creator.roleUri = role.uri, 
    creator.roleName = role.rdfs_label
RETURN COUNT(noteset) AS voice_staff_collection_role_dateissued;


MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person)
WHERE scr.encoderUri IS NULL 
SET scr.provGeneratedAtTime = activity.prov__startedAtTime,
    scr.provComments = activity.rdfs__comment
RETURN COUNT(scr) AS encoder;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute:mso__Dot)
SET noteset :Dot;
MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute:mso__DoubleDot)
SET noteset :DoubleDot;
MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute:mso__TripleDot)
SET noteset :TripleDot;

MATCH (measure:mso__Measure)-[r:mso__hasTime]->(time)
WHERE measure.beats IS NULL
SET measure.beats = time.mso__hasBeats, measure.beatType = time.mso__hasBeatType
RETURN COUNT(measure) AS MeasureTimeSignature;


MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WITH measure, tonicNode,modeNode, 
     LOWER(SUBSTRING(modeNode.uri,39)) AS mode, 
     LOWER(SUBSTRING(tonicNode.uri,36)) AS tonic
WITH measure,tonic,mode,
   CASE tonic+'-'+mode
       WHEN 'c-major' THEN 'standard_key' 
       WHEN 'a-minor' THEN 'standard_key'        
       WHEN 'g-major' THEN 'xF'
       WHEN 'e-minor' THEN 'xF'       
       WHEN 'd-major' THEN 'xFC'
       WHEN 'b-minor' THEN 'xFC'       
       WHEN 'a-major' THEN 'xFCG' 
       WHEN 'fsharp-minor' THEN 'xFCG'        
       WHEN 'e-major' THEN 'xFCGD' 
       WHEN 'csharp-minor' THEN 'xFCGD'        
       WHEN 'b-major' THEN 'xFCGDA' 
       WHEN 'gsharp-minor' THEN 'xFCGDA'        
       WHEN 'fsharp-major' THEN 'xFCGDAE'
       WHEN 'dsharp-minor' THEN 'xFCGDAE'       
       WHEN 'csharp-major' THEN 'xFCGDAEB'
       WHEN 'asharp-minor' THEN 'xFCGDAEB'              
       WHEN 'f-major' THEN 'bB'
       WHEN 'd-minor' THEN 'bB'       
       WHEN 'bflat-major' THEN 'bBE'
       WHEN 'g-minor' THEN 'bBE'       
       WHEN 'eflat-major' THEN 'bBEA'
       WHEN 'c-minor' THEN 'bBEA'       
       WHEN 'aflat-major' THEN 'bBEAD'
       WHEN 'f-minor' THEN 'bBEAD'              
       WHEN 'dflat-major' THEN 'bBEADG'
       WHEN 'bflat-minor' THEN 'bBEADG'       
       WHEN 'gflat-major' THEN 'bBEADGC'
       WHEN 'eflat-minor' THEN 'bBEADGC'       
       WHEN 'cflat-major' THEN 'bBEADGCF'
       WHEN 'aflat-minor' THEN 'bBEADGCF'       
     END AS key
CALL apoc.create.addLabels(id(measure),[key]) YIELD node
RETURN COUNT(measure) AS MeasureKey;

MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WITH measure, tonicNode,modeNode, 
     LOWER(SUBSTRING(modeNode.uri,39)) AS mode, 
     SUBSTRING(tonicNode.uri,36) AS tonic
SET measure.tonic = tonic, measure.mode = mode
RETURN COUNT(measure) AS MeasureKey;

MATCH (part:mso__ScorePart)
CALL apoc.create.addLabels(id(part),[REPLACE(part.skos__altLabel,'.','_')]) YIELD node
WITH node AS part
MATCH (part)-[:skos__broader]->(type)
CALL apoc.create.addLabels(id(part),[type.skos__prefLabel]) YIELD node
RETURN COUNT(node) AS instrument;

MATCH (score:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:skos__broader]->(type) 
MERGE (movement)-[:MEDIUMTYPE]-(t:mediumType {mediumTypeId:type.uri, mediumTypeDescription: type.skos__prefLabel})
MERGE (t)-[:MEDIUM]->(i:Medium {mediumId: part.uri,mediumDescription: part.skos__prefLabel, mediumScoreDescription: part.dc__description, mediumCode: part.rdfs__label, ensemble: part.mso__isEnsemble, solo:part.mso__isSolo})
RETURN COUNT(i) AS mediums;

MATCH (mov:mo__Movement)
OPTIONAL MATCH (mov:mo__Movement)-[:mso__hasBeatUnit]-(unit)
WITH mov,CASE unit WHEN NULL THEN "unknown" 
         ELSE LOWER(SUBSTRING(unit.uri,54))
		 END AS b
WHERE mov.beatUnit IS NULL
SET mov.beatUnit = b, 
    mov.order = mov.dbo__order
REMOVE mov.dbo__order;


MATCH (score:mo__Score)
WITH  score, 
  CASE 
    WHEN NOT score.mso__asMusicXML = '' THEN 'musicxml'
    WHEN NOT score.mso__asMEI = '' THEN 'mei'
  END AS docFormat
WHERE score.format IS NULL  
SET score.format = docFormat
RETURN COUNT(docFormat) AS format;

MATCH (mov:mo__Movement)
WITH mov, CASE mov.mso__hasBeatsPerMinute 
            WHEN NULL THEN 0
            ELSE toInt(mov.mso__hasBeatsPerMinute) 
          END AS bpm
SET mov.beatsPerMinute = bpm
RETURN COUNT(mov) AS BPM;





MATCH (noteset:mso__NoteSet)-[:mso__hasNote]->(note:chord__Note) 
MATCH (note)-[:mso__hasNoteAttribute]->(grace:mso__GraceNote)
MATCH (previousNoteSet:mso__NoteSet)-[:mso__nextNoteSet]->(noteset)
MATCH (noteset)-[:mso__nextNoteSet]->(nextNoteSet:mso__NoteSet)
SET noteset :GraceNote
CREATE (previousNoteSet)-[:NEXT]->(nextNoteSet);


MATCH (noteset:mso__NoteSet)-[:mso__hasClef]->(clef)-[:mso__sign]->(sign)
MATCH (noteset)-[r:mso__nextNoteSet]->(nextNoteset:mso__NoteSet)
CALL apoc.create.addLabels(id(noteset),['Clef_'+SUBSTRING(sign.uri,36)+clef.mso__line]) YIELD node
CREATE (noteset)-[:NEXT]->(nextNoteset)
DELETE r
RETURN COUNT(clef) AS noteset_clef_sign;

MATCH (score:mo__Score)
SET score.asMusicXML = score.mso__asMusicXML
REMOVE score.mso__asMusicXML
RETURN COUNT(score.asMusicXML) AS asMusicXML;

MATCH (score:mo__Score)
SET score.asMEI = score.mso__asMEI
REMOVE score.mso__asMEI
RETURN COUNT(score.asMEI) AS asMEI;

MATCH (part:mso__ScorePart)-[r3:mso__hasMeasure]->(measure:mso__Measure)-[r0:mso__hasKey]-(key)-[r1:ton__tonic]->(tonicNode)
MATCH (measure:mso__Measure)-[r0:mso__hasKey]-(key)-[r2:ton__mode]->(modeNode)
SET measure.order = measure.dbo__order
REMOVE measure.dbo__order
CREATE (part)-[:MEASURE]->(measure)
DETACH DELETE r3;

MATCH (measure:mso__Measure)-[r1:mso__hasNoteSet]-(noteset:mso__NoteSet)
MATCH (noteset)-[r2:mso__hasNote]->(note)
CREATE (measure)-[:NOTESET]->(noteset)
DELETE r1;

MATCH (score:mo__Score)-[r1:dc__creator]->(person:foaf__Person)
MATCH (person)-[r2:dbo__occupation]->(occupation)
MATCH (person)-[r4:prov__hadRole]->(role:prov__Role)
WITH score,person,occupation,role,r1,r2,r4,
  CASE occupation.uri
      WHEN 'http://dbpedia.org/resource/Composer' THEN 'Composer'
      WHEN 'http://dbpedia.org/resource/Lyricist' THEN 'Lyricist'
      WHEN 'http://dbpedia.org/resource/Arranger' THEN 'Arranger'
      WHEN 'http://dbpedia.org/resource/Librettist' THEN 'Librettist'
      WHEN 'http://dbpedia.org/resource/Editor' THEN 'Editor'
      WHEN 'http://dbpedia.org/resource/Performer' THEN 'Performer'
      WHEN 'http://dbpedia.org/resource/Translator' THEN 'Translator'
      WHEN 'http://dbpedia.org/resource/Dedicatee' THEN 'Dedicatee'
      WHEN 'http://dbpedia.org/resource/Composer' THEN 'Composer'
      WHEN 'http://dbpedia.org/resource/Unknown' THEN 'Unknown'
  END AS occupationType
CALL apoc.create.addLabels(id(person),[occupationType]) YIELD node
CREATE (score)-[:CREATOR]->(person)
SET person.name = person.foaf__name
DELETE r1,r2,r4
RETURN COUNT(person);


MATCH (person:foaf__Person)<-[r1:prov__wasAssociatedWith]-(activity:prov__Activity)
MATCH (activity:prov__Activity)-[r2:prov__generated]->(score:mo__Score)
MATCH (person)-[r4:prov__hadRole]->(role:prov__Role)
MATCH (person)-[r5:dbo__occupation]->(occupation)
SET person:Encoder,	
	person.roleUri = 'http://dbpedia.org/resource/Encoder',
	person.name = person.foaf__name
CREATE (score)-[:CREATOR]->(person)
CREATE (activity)-[:ASSOCIATEDWITH]->(person)
DELETE r1,r4,r5;


MATCH (score:mo__Score)-[r:mo__movement]->(movement:mo__Movement) 
CREATE (score)-[:MOVEMENT]->(movement)
DELETE r;

MATCH (movement:mo__Movement)-[r:mso__hasScorePart]->(part:mso__ScorePart) 
CREATE (movement)-[:PART]->(part)
DELETE r;

MATCH (movement:mo__Movement)-[:PART]->(part:mso__ScorePart)
SET 
	movement.title = movement.dc__title,
    part.description = part.dc__description,
    part.solo = part.mso__isSolo,
    part.ensemble = part.mso__isEnsemble,
    part.label = part.rdfs__label,
    part.altLabel = part.skos__altLabel,
    part.prefLabel = part.skos__prefLabel;
    
MATCH (movement:mo__Movement)-[:PART]->(part:mso__ScorePart)    
REMOVE 
	movement.mso__hasBeatsPerMinute,
    movement.dc__title,
    part.dc__description,
    part.mso__isEnsemble,
    part.mso__isSolo,
    part.rdfs__label,
    part.skos__altLabel,
    part.skos__prefLabel;





MATCH (activity:prov__Activity)-[r4:prov__wasAssociatedWith]->(software:prov__SoftwareAgent)
MATCH (score:mo__Score)-[r5:prov__wasGeneratedBy]->(activity:prov__Activity)
MATCH (activity:prov__Activity)-[r6:prov__generated]->(score:mo__Score)
MATCH (score:mo__Score)-[r7:prov__wasGeneratedBy]->(software:prov__SoftwareAgent)
MATCH (score:mo__Score)-[r8:foaf__thumbnail]->(thumbnail)
MATCH (collection:prov__Collection)-[r9:prov__hadMember]->(score:mo__Score)
SET	software.label = software.rdfs__label,
    activity :Activity, 
	activity.startedAtTime = activity.prov__startedAtTime,
	activity.endedAtTime = activity.prov__endedAtTime,
	activity.comment = activity.rdfs__comment,
	score.title = score.dc__title,
    score.issued = score.dcterms__issued,
	score.generatedAtTime = score.provGeneratedAtTime,
	score.comments = score.provComments,
	score.thumbnail = thumbnail.uri,
	collection :Collection,
	collection.label = collection.rdfs__label
REMOVE 
	score.dc__title, 
	score.dcterms__issued,
	score.provComments,
	score.provGeneratedAtTime,
	software:prov__SoftwareAgent,
	software.rdfs__label,
	software.foaf__name,
	collection.rdfs__label,
	collection:prov__Collection	
CREATE (activity)-[:ASSOCIATEDWITH]->(software)
CREATE (activity)-[:GENERATED]->(score)
CREATE (score)-[:COLLECTION]->(collection)
DELETE r4,r5,r6,r7,r8,r9;

		
MATCH (n)-[r1:mso__hasVoice]->(voice:mso__Voice)
MATCH (voice)-[r2:mso__hasNoteSet]->(noteset:mso__NoteSet)
DELETE r1,r2;


MATCH (n1)-[r:mso__nextMeasure]->(n2) 
CREATE (n1)-[r2:NEXT]->(n2) 
DELETE r;

MATCH (n1)-[r:mso__hasStaff]->(staff:mso__Staff)
DELETE r;







MATCH (person)-[r2:dbo__occupation]->(occupation)
MATCH (person)-[r3:gndo__professionOrOccupation]->(role:prov__Role) 
DETACH DELETE role,occupation; 

MATCH (person)-[r4:prov__hadRole]->(role:prov__Role)
MATCH (person)-[r5:dbo__occupation]->(occupation)
DETACH DELETE role,occupation;

MATCH (measure:mso__Measure)-[r:mso__hasTime]->(time)
DETACH DELETE time;

MATCH (movement:mo__Movement)-[:mso__hasBeatUnit]->(unit)
DETACH DELETE unit;

MATCH (part:mso__ScorePart)-[:skos__broader]-(mit)
DETACH DELETE mit;

MATCH (note:chord__Note) 
DETACH DELETE note;

MATCH (noteset:NoteSet)-[:mso__hasDuration]->(duration) 
DETACH DELETE duration;

MATCH (clef)-[:mso__sign]->(sign)
DETACH DELETE sign,clef;

MATCH (noteset)-[:mso__hasDuration]->(duration)
DETACH DELETE duration;

MATCH (voice:mso__Voice)
DETACH DELETE voice;

MATCH (instant:mso__Instant)
DETACH DELETE instant;

MATCH (key:ton__Key)
DETACH DELETE key;

MATCH (noteset:mso__NoteSet)-[:mso__hasDynamic]->(dynamic)
DETACH DELETE dynamic;

MATCH (n) WHERE NOT (n)--() 
DELETE n;

MATCH (n:mo__Score) SET n :Score REMOVE n:mo__Score;
MATCH (n:mo__Movement) SET n :Movement REMOVE n:mo__Movement;
MATCH (n:mso__ScorePart) SET n :Part REMOVE n:mso__ScorePart;
MATCH (n:mo__Instrument) SET n :Instrument REMOVE n:mo__Instrument, n:skos__Concept;
MATCH (n:mso__Measure) SET n :Measure REMOVE n:mso__Measure;
MATCH (n:mso__NoteSet) SET n :NoteSet REMOVE n:mso__NoteSet;
MATCH (n:foaf__Person) SET n :Person REMOVE n:foaf__Person;
MATCH (n:prov__Activity) SET n :Activity REMOVE n:prov__Activity;
MATCH (n:prov__SoftwareAgent) SET n :Software REMOVE n:prov__SoftwareAgent;
MATCH (n:prov__Collection) SET n :Collection REMOVE n:prov__Collection;

CREATE INDEX ON :Movement(beatUnit);
CREATE INDEX ON :Score(encoderUri);
CREATE INDEX ON :Score(collectionUri);
CREATE INDEX ON :NoteSet(size);		
CREATE INDEX ON :NoteSet(clefShape,clefLine);
CREATE INDEX ON :Score(format);
CREATE INDEX ON :Measure(key);
CREATE INDEX ON :Measure(beats);
CREATE INDEX ON :Measure(beatType);
CREATE INDEX ON :NoteSet(clefSign);
CREATE INDEX ON :NoteSet(clefLine);		
CREATE INDEX ON :D_longa(size);
CREATE INDEX ON :D_whole(size);
CREATE INDEX ON :D_half(size);
CREATE INDEX ON :D_quarter(size);
CREATE INDEX ON :D_eighth(size);
CREATE INDEX ON :D_16th(size);
CREATE INDEX ON :D_32nd(size);
CREATE INDEX ON :D_64th(size);
CREATE INDEX ON :D_128th(size);
CREATE INDEX ON :D_256th(size);