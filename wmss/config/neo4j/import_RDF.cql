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


CALL semantics.importRDF("http://linkeddata.uni-muenster.de/datasets/opendata/musicscores/statements_musescore.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
//CALL semantics.importRDF("http://linkeddata.uni-muenster.de/datasets/opendata/musicscores/statements.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
//CALL semantics.importRDF("file:///home/jones/git/musicowl/musicowl/ntriples/elgar_cello_concerto_op.85.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
//CALL semantics.importRDF("file:///home/jones/Downloads/statements.nt","N-Triples",{shortenUrls: true, commitSize: 10000});

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
RETURN 'Notes: '+COUNT(note);

MATCH (mov:mo__Movement)
OPTIONAL MATCH (mov:mo__Movement)-[:mso__hasBeatUnit]-(unit)
WITH mov,CASE unit 
           WHEN NULL THEN "unknown"
           ELSE LOWER(SUBSTRING(unit.uri,54)) 
        END AS b
SET mov.beatUnit = b
RETURN 'Movements: '+COUNT(mov);

MATCH (mov:mo__Movement)
WITH mov, CASE mov.mso__hasBeatsPerMinute 
            WHEN NULL THEN 0
            ELSE toInt(mov.mso__hasBeatsPerMinute) 
          END AS bpm
SET mov.mso__hasBeatsPerMinute = bpm
RETURN 'Beats per minute: '+COUNT(mov);

MATCH (scr:mo__Score)
SET scr.issued = datetime(scr.dcterms__issued)
RETURN 'Issued: '+COUNT(scr.dcterms__issued);

MATCH (scr:mo__Score)-[:prov__wasGeneratedBy]->(activity:prov__Activity)-[:prov__wasAssociatedWith]->(encoder:foaf__Person) 
SET scr.encoderUri = encoder.uri,
    scr.encoderName = encoder.foaf__name,
    scr.provGeneratedAtTime = activity.prov__startedAtTime,
    scr.provComments = activity.rdfs__comment
RETURN 'Encoding: '+COUNT(scr);

//Adds rests 
MATCH (note:chord__Note)
WHERE note.note IS NULL
SET note.note = '-', note.accidental = ''
RETURN 'Rests: '+COUNT(note);

MATCH (scr:mo__Score)-[:foaf__thumbnail]->(thumbnail) 
SET scr.thumbnail = thumbnail.uri
RETURN 'Thumbnails: '+COUNT(scr);

MATCH (collection:prov__Collection)-[:prov__hadMember]->(scr:mo__Score)
SET scr.collectionUri = collection.uri,
    scr.collectionLabel = collection.rdfs__label
RETURN 'Collections: '+COUNT(scr.uri);

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WITH  noteset,CASE SUBSTRING(labels(duration)[1],5)
                WHEN 'QuardupleWhole' THEN 0
                WHEN 'DoubleWhole' THEN 9
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
RETURN 'Durations: '+COUNT(noteset);

MATCH (noteset:mso__NoteSet)-[r:mso__hasNote]-()
WITH noteset,COUNT(r) AS size,
  CASE 
  WHEN COUNT(r)>1 THEN TRUE 
  WHEN COUNT(r)=1 THEN FALSE END AS chord
SET noteset.size = size
RETURN 'Notesets: '+COUNT(noteset);

MATCH (score:mo__Score)
WITH  score, 
  CASE 
    WHEN NOT score.mso__asMusicXML = '' THEN 'musicxml'
    WHEN NOT score.mso__asMEI = '' THEN 'mei'
  END AS docFormat
SET score.format = docFormat
RETURN score.uri, docFormat;

CREATE INDEX ON :mo__Movement(mso__hasBeatsPerMinute);
CREATE INDEX ON :mo__Movement(beatUnit);
CREATE INDEX ON :mo__Score(issued);
CREATE INDEX ON :mo__Score(encoderUri);
CREATE INDEX ON :mo__Score(collectionUri);
CREATE INDEX ON :chord__Note(mso__hasOctave);
CREATE INDEX ON :chord__Note(note);
CREATE INDEX ON :chord__Note(accidental);
CREATE INDEX ON :mso__NoteSet(duration);
CREATE INDEX ON :mso__NoteSet(size);
CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);
CREATE INDEX ON :mo__Score(format);
CREATE INDEX ON :mso__ScorePart(rdfs__label);
CREATE INDEX ON :mso__ScorePart(typeLabel);
CREATE INDEX ON :mso__NoteSet(duration,size);
CREATE INDEX ON :chord__Note(note,accidental);

MATCH (:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(instrument:mo__Instrument) 
OPTIONAL MATCH (instrument:mo__Instrument)-[:skos__broader]->(type) 
SET instrument.typeUri = type.uri,
    instrument.typeLabel = type.skos__prefLabel
RETURN COUNT(instrument);

//MATCH (noteset:mso__NoteSet)-[predicate:mso__hasDuration]->(duration:mso__Duration)
//DELETE predicate, duration
//RETURN COUNT(*);

//MATCH (note:chord__Note)-[predicate:chord__natural]->(natural:chord__Natural)
//DETACH DELETE predicate, natural
//RETURN COUNT(*);

//MATCH (note:chord__Note)-[predicate:chord__modifier]->(modifier)
//DETACH DELETE predicate, modifier
//RETURN COUNT(*);
