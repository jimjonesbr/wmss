
CREATE INDEX ON :Resource(uri);
CREATE INDEX ON :URI(uri);
CREATE INDEX ON :BNode(uri);
CREATE INDEX ON :Class(uri);

CREATE (:NamespacePrefixDefinition {
  `http://linkeddata.uni-muenster.de/ontology/musicscore#`: 'mso',
  `http://purl.org/ontology/chord/`: 'chord',
  `http://purl.org/dc/elements/1.1/`: 'dc',
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


CALL semantics.importRDF("file:///home/jones/Downloads/statements.nt","N-Triples",{shortenUrls: true});


MATCH (note:chord__Note)-[:chord__natural]->(natural:chord__Natural) 
OPTIONAL MATCH (note:chord__Note)-[:chord__modifier]->(modifier)
WITH note, CASE SUBSTRING(modifier.uri,31) 
        WHEN 'flat' THEN 'b'
            WHEN 'sharp' THEN 's'
            WHEN NULL THEN ''
           END AS modifier,
       LOWER(SUBSTRING(natural.uri,36)) AS natural           
SET note.note = natural + modifier
return COUNT(note);



MATCH (noteset:mso__NoteSet)-[:mso__hasDuration]->(duration)
WITH  noteset,CASE SUBSTRING(labels(duration)[1],5)
                WHEN 'OctupleWhole' THEN 80
                WHEN 'QuardupleWhole' THEN 40
                WHEN 'DoubleWhole' THEN 20
                WHEN 'Whole' THEN  0
                WHEN 'Half' THEN  2
                WHEN 'Quarter' THEN  4
                WHEN 'Eighth' THEN  8
                WHEN '16th' THEN  16
                WHEN '32nd' THEN  32
                WHEN '64th' THEN  64
                WHEN '128th' THEN  128
                WHEN '256th' THEN  256
              END AS duration_numeric
SET noteset.duration = duration_numeric
RETURN COUNT(noteset);


MATCH (noteset:mso__NoteSet)-[r:mso__hasNote]-()
WITH noteset,COUNT(r) AS size,
  CASE 
  WHEN COUNT(r)>1 THEN TRUE 
  WHEN COUNT(r)=1 THEN FALSE END AS chord
SET noteset.size = size
RETURN COUNT(noteset);


CREATE INDEX ON :chord__Note(mso__hasOctave);
CREATE INDEX ON :chord__Note(note);
CREATE INDEX ON :mso__NoteSet(duration);
CREATE INDEX ON :mso__NoteSet(size);
CREATE INDEX ON :prov__Role(gndo__preferredNameForTheSubjectHeading);





MATCH (:mo__Score)-[:mo__movement]->(movement:mo__Movement)-[:mso__hasScorePart]->(instrument:mo__Instrument) 
OPTIONAL MATCH (instrument:mo__Instrument)-[:skos__broader]->(type) 
SET instrument.typeUri = type.uri,
    instrument.typeLabel = type.skos__prefLabel
RETURN COUNT(instrument);