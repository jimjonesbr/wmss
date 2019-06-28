CREATE INDEX ON :Resource(uri);
CREATE INDEX ON :URI(uri);
CREATE INDEX ON :BNode(uri);
CREATE INDEX ON :Class(uri);

CREATE (:NamespacePrefixDefinition {
  `http://linkeddata.uni-muenster.de/ontology/musicscore#`: 'mso',
  `http://purl.org/ontology/chord/`: 'chord',
  `http://purl.org/dc/elements/1.1/`: 'dc',
  `http://purl.org/dc/terms/`: 'dcterms',
  `http://www.w3.org/2000/01/rdf-schema#`: 'rdfs',
  `http://purl.org/ontology/chord/note/`: 'note',
  `http://purl.org/ontology/tonality/`: 'ton',
  `http://purl.org/ontology/tonality/mode/`: 'mode',
  `http://purl.org/ontology/mo/`: 'mo',
  `http://xmlns.com/foaf/0.1/`: 'foaf',
  `http://www.w3.org/ns/prov#`: 'prov',
  `http://d-nb.info/standards/elementset/gnd#`: 'gndo',
  `http://www.w3.org/2004/02/skos/core#`: 'skos',
  `http://purl.org/ontology/mo/mit#`: 'mit'
});