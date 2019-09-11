MATCH (score:Score {uri:"SCORE_URI_PLACEHOLDER"})-[:MOVEMENT]->(movement:Movement)
MATCH (movement)-[:MEDIUM]->(part:Medium)
MATCH (part)-[:MEASURE]->(measure:Measure)
MATCH (measure)-[:NOTESET]->(noteset:NoteSet)
WITH score,movement,part,measure,noteset,score.uri AS uri, score.title AS title, score.collectionUri as collection
DETACH DELETE score,movement,part,measure,noteset
RETURN DISTINCT uri,title,collection;

MATCH (person:Person)
WHERE NOT ()-[:CREATOR]->(person)
DETACH DELETE person;

MATCH (mediumType:mediumType)
WHERE NOT ()-[:MEDIUMTYPE]->(mediumType:mediumType)
DETACH DELETE mediumType;

MATCH (activity:Activity)
WHERE NOT (activity)-[:GENERATED]->()
DETACH DELETE activity;

MATCH (n) WHERE NOT (n)--() 
DELETE n;

