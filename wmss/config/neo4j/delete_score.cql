MATCH (score:mo__Score)-[:mo__movement]->(movement)-[:mso__hasScorePart]->(part)-[:mso__hasStaff]->(staff)-[:mso__hasVoice]->(voice)-[:mso__hasNoteSet]->(noteset)-[:mso__hasNote]->(note)
MATCH (score)-[:dc__creator]->(creator)-[:gndo__professionOrOccupation]->(role)

OPTIONAL MATCH (activity)-[:prov__generated]->(score)
OPTIONAL MATCH (score)-[:foaf__thumbnail]->(thumbnail)
OPTIONAL MATCH (noteset)-[:mso__hasClef]-(clef)
OPTIONAL MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)
OPTIONAL MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__tonic]->(tonic)
OPTIONAL MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasKey]-(key)-[:ton__mode]->(mode)
OPTIONAL MATCH (part)-[:mso__hasMeasure]->(measure)-[:mso__hasTime]-(time)

WHERE score.uri = 'http://dbpedia.org/resource/Cello_Concerto_(Elgar)'
DETACH DELETE score, creator, role, movement, part, staff, voice, measure, note, noteset, key, clef, time, tonic, mode, thumbnail, activity