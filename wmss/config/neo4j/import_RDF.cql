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


//CALL semantics.importRDF("http://linkeddata.uni-muenster.de/datasets/opendata/musicscores/statements_musescore.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
CALL semantics.importRDF("http://linkeddata.uni-muenster.de/datasets/opendata/musicscores/statements.nt","N-Triples",{shortenUrls: true, commitSize: 10000});
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
CREATE INDEX ON :mo__Movement(beatUnit,mso__hasBeatsPerMinute);

CREATE INDEX ON :mo__Score(issued);
CREATE INDEX ON :mo__Score(encoderUri);
CREATE INDEX ON :mo__Score(collectionUri);

CREATE INDEX ON :chord__Note(note);
CREATE INDEX ON :chord__Note(accidental);
CREATE INDEX ON :chord__Note(mso__hasOctave);
CREATE INDEX ON :chord__Note(note,accidental);
CREATE INDEX ON :chord__Note(note,mso__hasOctave);
CREATE INDEX ON :chord__Note(note,accidental,mso__hasOctave);

CREATE INDEX ON :mso__NoteSet(duration);
CREATE INDEX ON :mso__NoteSet(size);
CREATE INDEX ON :mso__NoteSet(duration,size);

CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);
CREATE INDEX ON :mo__Score(format);
CREATE INDEX ON :mso__ScorePart(rdfs__label);
CREATE INDEX ON :mso__ScorePart(typeLabel);



MATCH (:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(instrument:mo__Instrument) 
OPTIONAL MATCH (instrument:mo__Instrument)-[:skos__broader]->(type) 
SET instrument.typeUri = type.uri,
    instrument.typeLabel = type.skos__prefLabel
RETURN COUNT(instrument);


MATCH (note:chord__Note {note:'A', accidental: ''}) SET note :A RETURN COUNT(note) AS A;
MATCH (note:chord__Note {note:'A', accidental: 'x'}) SET note :Ax RETURN COUNT(note) AS Ax;
MATCH (note:chord__Note {note:'A', accidental: 'xx'}) SET note :Axx RETURN COUNT(note) AS Axx;
MATCH (note:chord__Note {note:'A', accidental: 'b'}) SET note :Ab RETURN COUNT(note) AS Ab;
MATCH (note:chord__Note {note:'A', accidental: 'bb'}) SET note :Abb RETURN COUNT(note) AS Abb;

MATCH (note:chord__Note {note:'B', accidental: ''}) SET note :B RETURN 'Note B: '+COUNT(note);
MATCH (note:chord__Note {note:'B', accidental: 'x'}) SET note :Bx RETURN 'Note Bx: '+COUNT(note);
MATCH (note:chord__Note {note:'B', accidental: 'xx'}) SET note :Bxx RETURN 'Note Bxx: '+COUNT(note);
MATCH (note:chord__Note {note:'B', accidental: 'b'}) SET note :Bb RETURN 'Note Bb: '+COUNT(note);
MATCH (note:chord__Note {note:'B', accidental: 'bb'}) SET note :Bbb RETURN 'Note Bbb: '+COUNT(note);

MATCH (note:chord__Note {note:'C', accidental: ''}) SET note :C RETURN 'Note C: '+COUNT(note);
MATCH (note:chord__Note {note:'C', accidental: 'x'}) SET note :Cx RETURN 'Note Cx: '+COUNT(note);
MATCH (note:chord__Note {note:'C', accidental: 'xx'}) SET note :Cxx RETURN 'Note Cxx: '+COUNT(note);
MATCH (note:chord__Note {note:'C', accidental: 'b'}) SET note :Cb RETURN 'Note Cb: '+COUNT(note);
MATCH (note:chord__Note {note:'C', accidental: 'bb'}) SET note :Cbb RETURN 'Note Cbb: '+COUNT(note);

MATCH (note:chord__Note {note:'D', accidental: ''}) SET note :D RETURN 'Note D: '+COUNT(note);
MATCH (note:chord__Note {note:'D', accidental: 'x'}) SET note :Dx RETURN 'Note Dx: '+COUNT(note);
MATCH (note:chord__Note {note:'D', accidental: 'xx'}) SET note :Dxx RETURN 'Note Dxx: '+COUNT(note);
MATCH (note:chord__Note {note:'D', accidental: 'b'}) SET note :Db RETURN 'Note Db: '+COUNT(note);
MATCH (note:chord__Note {note:'D', accidental: 'bb'}) SET note :Dbb RETURN 'Note Dbb: '+COUNT(note);

MATCH (note:chord__Note {note:'E', accidental: ''}) SET note :E RETURN 'Note E: '+COUNT(note);
MATCH (note:chord__Note {note:'E', accidental: 'x'}) SET note :Ex RETURN 'Note Ex: '+COUNT(note);
MATCH (note:chord__Note {note:'E', accidental: 'xx'}) SET note :Exx RETURN 'Note Exx: '+COUNT(note);
MATCH (note:chord__Note {note:'E', accidental: 'b'}) SET note :Eb RETURN 'Note Eb: '+COUNT(note);
MATCH (note:chord__Note {note:'E', accidental: 'bb'}) SET note :Ebb RETURN 'Note Ebb: '+COUNT(note);

MATCH (note:chord__Note {note:'F', accidental: ''}) SET note :F RETURN 'Note F: '+COUNT(note);
MATCH (note:chord__Note {note:'F', accidental: 'x'}) SET note :Fx RETURN 'Note Fx: '+COUNT(note);
MATCH (note:chord__Note {note:'F', accidental: 'xx'}) SET note :Fxx RETURN 'Note Fxx: '+COUNT(note);
MATCH (note:chord__Note {note:'F', accidental: 'b'}) SET note :Fb RETURN 'Note Fb: '+COUNT(note);
MATCH (note:chord__Note {note:'F', accidental: 'bb'}) SET note :Fbb RETURN 'Note Fbb: '+COUNT(note);

MATCH (note:chord__Note {note:'G', accidental: ''}) SET note :G RETURN 'Note G: '+COUNT(note);
MATCH (note:chord__Note {note:'G', accidental: 'x'}) SET note :Gx RETURN 'Note Gx: '+COUNT(note);
MATCH (note:chord__Note {note:'G', accidental: 'xx'}) SET note :Gxx RETURN 'Note Gxx: '+COUNT(note);
MATCH (note:chord__Note {note:'G', accidental: 'b'}) SET note :Gb RETURN 'Note Gb: '+COUNT(note);
MATCH (note:chord__Note {note:'G', accidental: 'bb'}) SET note :Gbb RETURN 'Note Gbb: '+COUNT(note);

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Longa' SET noteset :d0 RETURN 'Longa: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Breve' SET noteset :d9 RETURN 'Breve: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Whole' SET noteset :d1 RETURN 'Whole: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Half' SET noteset :d2 RETURN 'Half: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Quarter' SET noteset :d4 RETURN 'Quarter: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = 'Eighth' SET noteset :d8 RETURN 'Eighth: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '16th' SET noteset :d6 RETURN '16th: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '32nd' SET noteset :d3 RETURN '32nd: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '64th' SET noteset :d5 RETURN '64th: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '128th' SET noteset :d7 RETURN '128th: '+COUNT(duration) AS result;

MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WHERE SUBSTRING(labels(duration)[1],5) = '256th' SET noteset :da RETURN '256th: '+COUNT(duration) AS result;





MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WITH  noteset,CASE SUBSTRING(labels(duration)[1],5)
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




//MATCH (noteset:mso__NoteSet)-[predicate:mso__hasDuration]->(duration:mso__Duration)
//DELETE predicate, duration
//RETURN COUNT(*);

//MATCH (note:chord__Note)-[predicate:chord__natural]->(natural:chord__Natural)
//DETACH DELETE predicate, natural
//RETURN COUNT(*);

//MATCH (note:chord__Note)-[predicate:chord__modifier]->(modifier)
//DETACH DELETE predicate, modifier
//RETURN COUNT(*);
