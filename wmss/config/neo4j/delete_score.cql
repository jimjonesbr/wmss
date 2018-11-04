MATCH (score:mo__Score)-[:mo__movement]->(movement)-[:mso__hasScorePart]->(part)-[:mso__hasStaff]->(staff)-[:mso__hasVoice]->(voice)-[:mso__hasNoteSet]->(noteset)-[:mso__hasNote]->(note)-[:chord__natural]->(natural)
MATCH (score)-[:dc__creator]->(creator)-[:gndo__professionOrOccupation]->(role)

MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasTime]-(time)
MATCH (instant:mso__Instant)-[:mso__hasMeasure]-(measure)
MATCH (noteset)-[:mso__hasClef]-(clef)
MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonic)
MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(mode)

OPTIONAL MATCH (noteset)-[:mso__hasDuration]->(duration)-[:mso__hasDurationAttribute]->(attribute)
OPTIONAL MATCH (noteset)-[:mso__hasDynamic]->(dynamic)
WHERE score.uri = 'http://dbpedia.org/resource/Cello_Concerto_(Elgar)'
DETACH DELETE score, creator, role, movement, part, staff, voice, instant,measure, note, natural, noteset, key, clef, time, duration, attribute, dynamic, tonic, mode