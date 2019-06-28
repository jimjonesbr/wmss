MATCH (note:chord__Note)-[:chord__natural]->(natural:chord__Natural) 
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
    note.octave = note.mso__hasOctave
WITH note
CALL apoc.create.addLabels(id(note),[note.signature,note.note]) YIELD node
RETURN COUNT(node) AS note_octave_signature;


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

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
REMOVE duration:Resource
WITH noteset,LOWER(SUBSTRING(labels(duration)[0],5)) AS d
CALL apoc.create.addLabels(id(noteset),["D_"+d]) YIELD node
RETURN COUNT(node) AS durations;

MATCH (scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(noteset)
OPTIONAL MATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)
OPTIONAL MATCH (scr:mo__Score)-[:dc__creator]->(creator:foaf__Person)-[:gndo__professionOrOccupation]->(role:prov__Role)
OPTIONAL MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail)
SET scr.thumbnail = thumbnail.uri,
    noteset.voice = voice.rdfs__label, 
    noteset.staff = staff.rdfs__label, 
    scr.collectionUri =  collection.uri, 
    scr.collectionLabel = collection.rdfs__label,
    scr.issued = datetime(scr.dcterms__issued),
    creator.roleUri = role.uri, 
    creator.roleName = role.gndo__preferredNameForTheSubjectHeading
RETURN COUNT(noteset) AS voice_staff_collection_role_dateissued;


MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person)
WHERE scr.encoderUri IS NULL 
SET scr.encoderUri = encoder.uri,
    scr.encoderName = encoder.foaf__name,
    scr.provGeneratedAtTime = activity.prov__startedAtTime,
    scr.provComments = activity.rdfs__comment
RETURN COUNT(scr) AS encoder;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute)
REMOVE attribute:Resource
WITH noteset,attribute
CALL apoc.create.addLabels(id(noteset),[SUBSTRING(labels(attribute)[0],5)]) YIELD node
RETURN COUNT(node) AS dots;

MATCH (measure:mso__Measure)-[r:mso__hasTime]->(time)
WHERE measure.beats IS NULL
SET measure.beats = time.mso__hasBeats, measure.beatType = time.mso__hasBeatType
DELETE r,time
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
CALL apoc.create.addLabels(id(part),[REPLACE(part.rdfs__label,'.','_')]) YIELD node
WITH node AS part
MATCH (part)-[:skos__broader]->(type)
CALL apoc.create.addLabels(id(part),[type.skos__prefLabel]) YIELD node
RETURN COUNT(node) AS instrument;

MATCH (score:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:skos__broader]->(type) 
MERGE (movement)-[:hasMediumType]-(t:mediumType {mediumTypeId:type.uri, mediumTypeDescription: type.skos__prefLabel})
MERGE (t)-[:hasMedium]->(i:Medium {mediumId: part.uri,mediumDescription: part.skos__prefLabel, mediumScoreDescription: part.dc__description, mediumCode: part.rdfs__label, ensemble: part.mso__isEnsemble, solo:part.mso__isSolo})
RETURN COUNT(i) AS mediums;

MATCH (mov:mo__Movement)
OPTIONAL MATCH (mov:mo__Movement)-[:mso__hasBeatUnit]-(unit)
WITH mov,CASE unit WHEN NULL THEN "unknown" 
         ELSE LOWER(SUBSTRING(unit.uri,54))
		 END AS b
WHERE mov.beatUnit IS NULL
SET mov.beatUnit = b
RETURN COUNT(mov) AS movements_beatunit;


MATCH (noteset:mso__NoteSet)-[:mso__hasClef]->(clef)
REMOVE clef:Resource
WITH noteset,clef
SET noteset.clef = REPLACE(LOWER(labels(clef)[0]),'mso__','')
RETURN COUNT(clef) AS noteset_clef;

MATCH (noteset:mso__NoteSet)-[:mso__hasClef]->(clef)-[:mso__sign]->(sign)
CALL apoc.create.addLabels(id(noteset),['c_'+SUBSTRING(sign.uri,36)+clef.mso__line]) YIELD node
RETURN COUNT(clef) AS noteset_clef_sign;

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
SET mov.mso__hasBeatsPerMinute = bpm
RETURN COUNT(mov) AS BPM;


MATCH (score:mo__Score)
SET score.asMusicXML = score.mso__asMusicXML
REMOVE score.mso__asMusicXML
RETURN COUNT(score.asMusicXML) AS asMusicXML;

MATCH (score:mo__Score)
SET score.asMEI = score.mso__asMEI
REMOVE score.mso__asMEI
RETURN COUNT(score.asMEI) AS asMEI;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[r0:mso__hasKey]-(key)-[r1:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[r0:mso__hasKey]-(key)-[r2:ton__mode]->(modeNode)
DETACH DELETE key,tonicNode,modeNode
RETURN COUNT(key) AS DeletedKeys;

MATCH (mov:mo__Movement)
SET mov.title = mov.dc__title
REMOVE mov.dc__title
RETURN COUNT(mov) AS MovementTitlesChanged;

MATCH (part:mso__ScorePart)
SET part.solo = part.mso__isSolo,
    part.isEnsemble = part.mso__isEnsemble
REMOVE part.mso__isSolo, part.mso__isEnsemble
RETURN COUNT(part) AS MovementTitlesChanged;

MATCH (score:mo_Score)
SET score.title = score.dc__title,
    score.issued = score.dcterms__issued
REMOVE score.dc__title,score.dcterms__issued
RETURN COUNT(score) AS ScoreTitlesChanged;


MATCH (n1)-[r:mso__nextNoteSet]->(n2) CREATE (n1)-[r2:NEXT]->(n2) DELETE r;
MATCH (n1)-[r:mso__nextMeasure]->(n2) CREATE (n1)-[r2:NEXT]->(n2) DELETE r;
MATCH (n1)-[r:mso__hasMeasure]->(n2) CREATE (n1)-[r2:MEASURE]->(n2) DELETE r;
MATCH (n1)-[r:mso__hasNoteSet]->(n2) CREATE (n1)-[r2:NOTESET]->(n2) DELETE r;
MATCH (n1)-[r:dc__creator]->(n2) CREATE (n1)-[r2:CREATOR]->(n2) DELETE r;
MATCH (n1)-[r:mo__movement]->(n2) CREATE (n1)-[r2:MOVEMENT]->(n2) DELETE r;
MATCH (n1)-[r:mso__hasScorePart]->(n2) CREATE (n1)-[r2:PART]->(n2) DELETE r;


MATCH (n:mso__Instant) SET n :Instant REMOVE n:mso__Instant;
MATCH (n:mso__NoteSet) SET n :NoteSet REMOVE n:mso__NoteSet;
MATCH (n:foaf__Person) SET n :Person REMOVE n:foaf__Person;
MATCH (n:mso__ScorePart) SET n :Part REMOVE n:mso__ScorePart;
MATCH (n:mo__Movement) SET n :Movement REMOVE n:mo__Movement;
MATCH (n:mso__Measure) SET n :Measure REMOVE n:mso__Measure;
MATCH (n:mo__Score) SET n :Score REMOVE n:mo__Score;






CREATE INDEX ON :Movement(mso__hasBeatsPerMinute);
CREATE INDEX ON :Movement(beatUnit);
CREATE INDEX ON :Score(issued);
CREATE INDEX ON :Score(encoderUri);
CREATE INDEX ON :Score(collectionUri);
CREATE INDEX ON :NoteSet(size);		
CREATE INDEX ON :NoteSet(clefShape,clefLine);
CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);
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


