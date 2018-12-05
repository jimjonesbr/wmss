//MATCH (n) DETACH DELETE n RETURN COUNT(n);

CREATE INDEX ON :Resource(uri);
CREATE INDEX ON :URI(uri);
CREATE INDEX ON :BNode(uri);
CREATE INDEX ON :Class(uri);

CREATE (:NamespacePrefixDefinition {
  `http://linkeddata.uni-muenster.de/ontology/musicscore#`: 'mso',
  `http://purl.org/ontology/chord/`: 'chord',
  `http://purl.org/dc/elements/1.1/`: 'dc',
  `http://purl.org/dc/terms/`: 'dcterms',
  `http://www.w3.org/1999/02/22-rdf-syntax-ns#`: 'rdfs',
  `http://purl.org/ontology/chord/note/`: 'note',
  `http://purl.org/ontology/tonality/`: 'ton',
  `http://purl.org/ontology/tonality/mode/`: 'mode',
  `http://purl.org/ontology/mo/`: 'mo',
  `http://xmlns.com/foaf/0.1/`: 'foaf',
  `http://www.w3.org/ns/prov#`: 'prov',
  `http://d-nb.info/standards/elementset/gnd#`: 'gndo',
  `http://www.w3.org/2000/01/rdf-schema#`: 'rdfs',
  `http://www.w3.org/2004/02/skos/core#`: 'skos',
  `http://purl.org/ontology/mo/mit#`: 'mit'
});

//CALL semantics.importRDF("http://linkeddata.uni-muenster.de/datasets/opendata/musicscores/statements.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
//CALL semantics.importRDF("http://linkeddata.uni-muenster.de/datasets/opendata/musicscores/statements_musescore.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
//CALL semantics.importRDF("file:///home/jones/git/musicowl-repository/musicowl/ntriples/elgar_cello_concerto_op.85.nt","N-Triples",{shortenUrls: true, commitSize: 10000});

MATCH (mov:mo__Movement)
OPTIONAL MATCH (mov:mo__Movement)-[:mso__hasBeatUnit]-(unit)
WITH mov,CASE unit 
           WHEN NULL THEN "unknown"
           ELSE LOWER(SUBSTRING(unit.uri,54)) 
        END AS b
SET mov.beatUnit = b
RETURN COUNT(mov) AS Movements;

MATCH (mov:mo__Movement)
WITH mov, CASE mov.mso__hasBeatsPerMinute 
            WHEN NULL THEN 0
            ELSE toInt(mov.mso__hasBeatsPerMinute) 
          END AS bpm
SET mov.mso__hasBeatsPerMinute = bpm
RETURN COUNT(mov) AS BPM;

MATCH (scr:mo__Score)
SET scr.issued = datetime(scr.dcterms__issued)
RETURN COUNT(scr.dcterms__issued) AS IssuedDates;

MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person)
WHERE scr.encoderUri IS NULL 
SET scr.encoderUri = encoder.uri,
    scr.encoderName = encoder.foaf__name,
    scr.provGeneratedAtTime = activity.prov__startedAtTime,
    scr.provComments = activity.rdfs__comment
RETURN COUNT(scr) AS Encoder;

// Thumbnails
MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail) 
WHERE scr.thumbnail IS NULL
SET scr.thumbnail = thumbnail.uri
RETURN COUNT(scr) AS Thumbnails;
MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail) 
DETACH DELETE thumbnail
RETURN COUNT(thumbnail) AS DeletedThumbnails;

// Collections
MATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)
SET scr.collectionUri = collection.uri, scr.collectionLabel = collection.rdfs__label
RETURN COUNT(scr.uri) AS Collections;

MATCH (noteset:mso__NoteSet)-[r:mso__hasNote]-()
WITH noteset,COUNT(r) AS size,
  CASE 
  WHEN COUNT(r)>1 THEN TRUE 
  WHEN COUNT(r)=1 THEN FALSE END AS chord
WHERE noteset.size IS NULL  
SET noteset.size = size
RETURN COUNT(noteset) AS NoteSets;

MATCH (score:mo__Score)
WITH  score, 
  CASE 
    WHEN NOT score.mso__asMusicXML = '' THEN 'musicxml'
    WHEN NOT score.mso__asMEI = '' THEN 'mei'
  END AS docFormat
WHERE score.format IS NULL  
SET score.format = docFormat
RETURN COUNT(docFormat) AS Format;



MATCH (:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(instrument:mo__Instrument) 
OPTIONAL MATCH (instrument:mo__Instrument)-[:skos__broader]->(type) 
WHERE instrument.typeUri IS NULL
SET instrument.typeUri = type.uri,
    instrument.typeLabel = type.skos__prefLabel
RETURN COUNT(instrument) AS Instruments;



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
SET note.note = natural, note.accidental = modifier
RETURN COUNT(note) AS Note_Accidentals;


//Adds rests 
MATCH (note:chord__Note)
WHERE note.note IS NULL
SET note.note = '-', note.accidental = ''
RETURN COUNT(note) AS Rests;




MATCH (note:chord__Note {note:'A', accidental: ''}) SET note :A RETURN COUNT(note) AS A;
MATCH (note:chord__Note {note:'A', accidental: 'x'}) SET note :Ax RETURN COUNT(note) AS Ax;
MATCH (note:chord__Note {note:'A', accidental: 'xx'}) SET note :Axx RETURN COUNT(note) AS Axx;
MATCH (note:chord__Note {note:'A', accidental: 'b'}) SET note :Ab RETURN COUNT(note) AS Ab;
MATCH (note:chord__Note {note:'A', accidental: 'bb'}) SET note :Abb RETURN COUNT(note) AS Abb;

MATCH (note:chord__Note {note:'B', accidental: ''}) SET note :B RETURN COUNT(note) AS B;
MATCH (note:chord__Note {note:'B', accidental: 'x'}) SET note :Bx RETURN COUNT(note) AS Bx;
MATCH (note:chord__Note {note:'B', accidental: 'xx'}) SET note :Bxx RETURN COUNT(note) AS Bxx;
MATCH (note:chord__Note {note:'B', accidental: 'b'}) SET note :Bb RETURN COUNT(note) AS Bb;
MATCH (note:chord__Note {note:'B', accidental: 'bb'}) SET note :Bbb RETURN COUNT(note) AS Bbb;

MATCH (note:chord__Note {note:'C', accidental: ''}) SET note :C RETURN COUNT(note) AS C;
MATCH (note:chord__Note {note:'C', accidental: 'x'}) SET note :Cx RETURN COUNT(note) AS Cx;
MATCH (note:chord__Note {note:'C', accidental: 'xx'}) SET note :Cxx RETURN COUNT(note) AS Cxx;
MATCH (note:chord__Note {note:'C', accidental: 'b'}) SET note :Cb RETURN COUNT(note) AS Cb;
MATCH (note:chord__Note {note:'C', accidental: 'bb'}) SET note :Cbb RETURN COUNT(note) AS Cbb;

MATCH (note:chord__Note {note:'D', accidental: ''}) SET note :D RETURN COUNT(note) AS D;
MATCH (note:chord__Note {note:'D', accidental: 'x'}) SET note :Dx RETURN COUNT(note) AS Dx;
MATCH (note:chord__Note {note:'D', accidental: 'xx'}) SET note :Dxx RETURN COUNT(note) AS dxx;
MATCH (note:chord__Note {note:'D', accidental: 'b'}) SET note :Db RETURN COUNT(note) AS Db;
MATCH (note:chord__Note {note:'D', accidental: 'bb'}) SET note :Dbb RETURN COUNT(note) AS Dbb;

MATCH (note:chord__Note {note:'E', accidental: ''}) SET note :E RETURN COUNT(note) AS E;
MATCH (note:chord__Note {note:'E', accidental: 'x'}) SET note :Ex RETURN COUNT(note) AS Ex;
MATCH (note:chord__Note {note:'E', accidental: 'xx'}) SET note :Exx RETURN COUNT(note) AS Exx;
MATCH (note:chord__Note {note:'E', accidental: 'b'}) SET note :Eb RETURN COUNT(note) AS Eb;
MATCH (note:chord__Note {note:'E', accidental: 'bb'}) SET note :Ebb RETURN COUNT(note) AS Ebb;

MATCH (note:chord__Note {note:'F', accidental: ''}) SET note :F RETURN COUNT(note) AS F;
MATCH (note:chord__Note {note:'F', accidental: 'x'}) SET note :Fx RETURN COUNT(note) AS Fx;
MATCH (note:chord__Note {note:'F', accidental: 'xx'}) SET note :Fxx RETURN COUNT(note) AS Fxx;
MATCH (note:chord__Note {note:'F', accidental: 'b'}) SET note :Fb RETURN COUNT(note) AS Fb;
MATCH (note:chord__Note {note:'F', accidental: 'bb'}) SET note :Fbb RETURN COUNT(note) AS Fbb;

MATCH (note:chord__Note {note:'G', accidental: ''}) SET note :G RETURN COUNT(note) AS G;
MATCH (note:chord__Note {note:'G', accidental: 'x'}) SET note :Gx RETURN COUNT(note) AS Gx;
MATCH (note:chord__Note {note:'G', accidental: 'xx'}) SET note :Gxx RETURN COUNT(note) AS Gxx;
MATCH (note:chord__Note {note:'G', accidental: 'b'}) SET note :Gb RETURN COUNT(note) AS Gb;
MATCH (note:chord__Note {note:'G', accidental: 'bb'}) SET note :Gbb RETURN COUNT(note) AS Gbb;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Longa' SET noteset :d0 RETURN COUNT(duration) AS longa;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Breve' SET noteset :d9 RETURN COUNT(duration) AS breve;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Whole' SET noteset :d1 RETURN COUNT(duration) AS whole;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Half' SET noteset :d2 RETURN COUNT(duration) AS half;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Quarter' SET noteset :d4 RETURN COUNT(duration) AS quarter;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Eighth' SET noteset :d8 RETURN COUNT(duration) AS eighth;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '16th' SET noteset :d6 RETURN COUNT(duration) AS sixteenth;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '32nd' SET noteset :d3 RETURN COUNT(duration) AS thirtysecond;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '64th' SET noteset :d5 RETURN COUNT(duration) AS sixtyfourth;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '128th' SET noteset :d7 RETURN COUNT(duration) AS hundredtwentyeighth;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '256th' SET noteset :da RETURN COUNT(duration) AS twohundredtwentyeighth;


MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute:mso__Dot)
SET noteset :dotted
RETURN COUNT(attribute);

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute:mso__DoubleDot)
SET noteset :doubleDotted
RETURN COUNT(attribute);

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute:mso__TripleDot)
SET noteset :tripleDotted
RETURN COUNT(attribute);

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WITH noteset,CASE SUBSTRING(labels(duration)[1],5)
                WHEN 'Longa' THEN 0
                WHEN 'Breve' THEN 9
                WHEN 'Whole' THEN  1
                WHEN 'Half' THEN  2
                WHEN 'Quarter' THEN  4
                WHEN 'Eighth' THEN  8
                WHEN '16th' THEN  6
                WHEN '32nd' THEN  3
                WHEN '64th' THEN  5
                WHEN '128th' THEN  7
              END AS duration_numeric
SET noteset.duration = duration_numeric
RETURN COUNT(noteset) AS Duration;

MATCH (measure)-[r:mso__hasTime]->(time)
SET measure.beats = time.mso__hasBeats, measure.beatType = time.mso__hasBeatType
RETURN COUNT(measure) AS MeasureTimeSignature;


// Clefs //


MATCH (noteset:mso__NoteSet)-[:mso__hasClef]->(clef)-[:mso__sign]->(sign)
SET noteset.clefSign = SUBSTRING(sign.uri,36), noteset.clefLine = clef.mso__line
RETURN COUNT(clef) AS NotesetClef;










MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "c" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :cmajor
RETURN COUNT(measure) AS cmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "g" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :gmajor
RETURN COUNT(measure) AS gmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "d" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :dmajor
RETURN COUNT(measure) AS dmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "a" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :amajor
RETURN COUNT(measure) AS amajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "e" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :emajor
RETURN COUNT(measure) AS emajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "b" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :bmajor
RETURN COUNT(measure) AS bmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "fsharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :fsharpmajor
RETURN COUNT(measure) AS fsharpmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "csharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :csharpmajor
RETURN COUNT(measure) AS csharpmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "f" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :fmajor
RETURN COUNT(measure) AS fmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "bflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :bflatmajor
RETURN COUNT(measure) AS bflatmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "eflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :eflatmajor
RETURN COUNT(measure) AS eflatmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "aflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :aflatmajor
RETURN COUNT(measure) AS aflatmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "dflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :dflatmajor
RETURN COUNT(measure) AS dflatmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "gflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :gflatmajor
RETURN COUNT(measure) AS gflatmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "cflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "major") 
SET measure :cflatmajor
RETURN COUNT(measure) AS cflatmajor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "a" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :aminor
RETURN COUNT(measure) AS aminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "e" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :eminor
RETURN COUNT(measure) AS eminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "b" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :bminor
RETURN COUNT(measure) AS bminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "fsharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :fsharpminor
RETURN COUNT(measure) AS fsharpminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "csharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :csharpminor
RETURN COUNT(measure) AS chsarpminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "gsharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :gsharpminor
RETURN COUNT(measure) AS gsharpminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "dsharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :dsharpminor
RETURN COUNT(measure) AS dsharpminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "asharp" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :asharpminor
RETURN COUNT(measure) AS asharpminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "d" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :dminor
RETURN COUNT(measure) AS dminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "g" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :gminor
RETURN COUNT(measure) AS gminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "c" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :cminor
RETURN COUNT(measure) AS cminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "f" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :fminor
RETURN COUNT(measure) AS fminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "bflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :bflatminor
RETURN COUNT(measure) AS bflatminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "eflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :eflatminor
RETURN COUNT(measure) AS eflatminor;

MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part:mso__ScorePart)-[:mso__hasMeasure]->(measure:mso__Measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WHERE (LOWER(SUBSTRING(tonicNode.uri,36)) = "aflat" AND LOWER(SUBSTRING(modeNode.uri,39)) = "minor") 
SET measure :aflatminor
RETURN COUNT(measure) AS aflatminor;

MATCH (scr:mo__Score)-[:dc__creator]->(creator:foaf__Person)-[:gndo__professionOrOccupation]->(role:prov__Role)
SET creator.roleUri = role.uri, creator.roleName = role.gndo__preferredNameForTheSubjectHeading
RETURN COUNT(creator) AS CreatorRoles;

MATCH (scr:mo__Score)-[:mo__movement]->(mov:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:mso__hasStaff]->(staff:mso__Staff)-[:mso__hasVoice]->(voice:mso__Voice)-[:mso__hasNoteSet]->(noteset)
SET noteset.voice = voice.rdfs__ID, noteset.staff = staff.rdfs__ID
RETURN COUNT(noteset);

MATCH (score:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(part:mso__ScorePart)-[:skos__broader]->(type) 
MERGE (movement)-[:hasMediumType]-(t:mediumType {mediumTypeId:type.uri, mediumTypeDescription: type.skos__prefLabel})
MERGE (t)-[:hasMedium]->(i:Medium {mediumId: part.uri,mediumDescription: part.skos__prefLabel, mediumScoreDescription: part.dc__description, mediumCode: part.rdfs__label, ensemble: part.mso__isEnsemble, solo:part.mso__isSolo})
RETURN COUNT(i) AS mediums;



MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonicNode)
MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(modeNode)
WITH measure, tonicNode,modeNode, 
     LOWER(SUBSTRING(modeNode.uri,39)) AS mode, 
     LOWER(SUBSTRING(tonicNode.uri,36)) AS tonic
WITH measure,tonic,mode,
   CASE tonic+'-'+mode
       WHEN 'c-major' THEN '$' 
       WHEN 'a-minor' THEN '$'        
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
SET measure.key = key, 
    measure.tonic = tonic, 
    measure.mode = mode
RETURN COUNT(measure) AS MeasureKey;


//Indexes

CREATE INDEX ON :mo__Movement(mso__hasBeatsPerMinute);
CREATE INDEX ON :mo__Movement(beatUnit);
CREATE INDEX ON :mo__Movement(beatUnit,mso__hasBeatsPerMinute);
CREATE INDEX ON :mo__Score(issued);
CREATE INDEX ON :mo__Score(encoderUri);
CREATE INDEX ON :mo__Score(collectionUri);
CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);
CREATE INDEX ON :mo__Score(format);
CREATE INDEX ON :mso__ScorePart(rdfs__label);
CREATE INDEX ON :mso__ScorePart(typeLabel);
CREATE INDEX ON :mso__Measure(key);
CREATE INDEX ON :mso__Measure(beats);
CREATE INDEX ON :mso__Measure(beatType);
CREATE INDEX ON :mso__NoteSet(clefLine);
CREATE INDEX ON :mso__NoteSet(clefSign);

