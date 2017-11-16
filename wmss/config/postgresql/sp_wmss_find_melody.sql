-- Author: Jones
-- Parameter: 1) Note sequence (melody)
--	      2) Identifiers of scores to be harversted. For all available music scores use '*'.
-- Comments: 

DROP TYPE IF EXISTS wmss.note;
CREATE TYPE wmss.note AS (pitch VARCHAR, duration VARCHAR, octave VARCHAR, next_noteset NUMERIC);

CREATE OR REPLACE FUNCTION wmss.wmss_find_melody(melody VARCHAR, identifiers VARCHAR)
RETURNS TABLE (
res_score VARCHAR, 
res_movement VARCHAR,
res_movement_name VARCHAR, 
res_measure VARCHAR, 
res_staff VARCHAR, 
res_voice VARCHAR, 
res_instrument VARCHAR,
res_instrument_name VARCHAR) AS $$

DECLARE i INTEGER;
DECLARE j RECORD;
DECLARE k INTEGER;
DECLARE l RECORD;

DECLARE array_input_melody VARCHAR[];
DECLARE array_notes wmss.NOTE[];
DECLARE note wmss.NOTE;
DECLARE start_note wmss.NOTE;

DECLARE current_note wmss.NOTE;
DECLARE next_note_id NUMERIC; 


DECLARE array_result wmss.NOTE[];
DECLARE matches_pitch BOOLEAN DEFAULT FALSE;
DECLARE matches_octave BOOLEAN DEFAULT FALSE;
DECLARE matches_duration BOOLEAN DEFAULT FALSE;
DECLARE next_note_result wmss.NOTE;

DECLARE melody_query TEXT;
DECLARE array_identifiers TEXT[];

BEGIN
		
	RAISE NOTICE 'Parsing given melody ... ';   	
	DROP TABLE IF EXISTS tmp_result;
	CREATE TEMPORARY TABLE tmp_result (tmp_score VARCHAR, tmp_movement VARCHAR, tmp_movement_name VARCHAR, tmp_measure VARCHAR, tmp_staff VARCHAR, tmp_voice VARCHAR, tmp_instrument VARCHAR, tmp_instrument_name VARCHAR);

	array_input_melody := REGEXP_SPLIT_TO_ARRAY(melody,'>');

	FOR i IN 1 .. ARRAY_UPPER(array_input_melody, 1) LOOP
	
	    note := NULL;
	    note.pitch := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[1];
	    note.duration := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[2];
	    note.octave := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[3];

	    array_notes := array_notes || note;
	END LOOP;

	start_note = array_notes[1];

	melody_query := 'SELECT * FROM wmss.wmss_notes WHERE next_noteset_id IS NOT NULL AND ';
	
	IF start_note.octave <> '*' THEN 
	    melody_query := melody_query || 'octave=' || QUOTE_LITERAL(start_note.octave); 
	END IF;
	
	IF start_note.pitch <> '*' THEN 
	
	    IF start_note.octave <> '*' THEN 
	        melody_query := melody_query || ' AND '; 
	    END IF;
	    
	    melody_query := melody_query || 'pitch=' || QUOTE_LITERAL(start_note.pitch); 
	END IF;
	
	IF start_note.duration <> '*' THEN 
	
	    IF start_note.octave <> '*' OR start_note.pitch <> '*'  THEN 
	        melody_query := melody_query || ' AND '; 
	    END IF;
	    
	    melody_query := melody_query || 'duration=' || QUOTE_LITERAL(start_note.duration); 	    
	END IF;


	IF identifiers <> '*' THEN

	    array_identifiers := REGEXP_SPLIT_TO_ARRAY(identifiers,',');
	    identifiers := '';
	    FOR j IN 1 .. ARRAY_LENGTH(array_identifiers, 1) LOOP

	        identifiers := identifiers || QUOTE_LITERAL(array_identifiers[j]);

	        IF j <> ARRAY_LENGTH(array_identifiers, 1) THEN identifiers := identifiers || ','; END IF;

	    END LOOP;
	    melody_query := melody_query || ' AND score_id IN (' || identifiers || ') ';
	    
	END IF;
	
	raise notice 'Query -> %', melody_query;
		
	FOR j IN EXECUTE melody_query LOOP
	
	    current_note.pitch := j.pitch;
	    current_note.duration := j.duration;
	    current_note.octave := j.octave;
	    current_note.next_noteset := j.next_noteset_id;
	    array_result := array_result || current_note;
	    	
	    FOR k IN 2 .. ARRAY_LENGTH(array_notes, 1) LOOP
		
		matches_duration := FALSE;
		matches_pitch := FALSE;
		matches_octave := FALSE;


					   
		FOR l IN SELECT pitch, duration, octave, next_noteset_id FROM wmss.wmss_notes 
				  WHERE noteset_id = current_note.next_noteset AND 
				        score_id = j.score_id AND 
					movement_id = j.movement_id LIMIT 1 LOOP

		    IF l.pitch = array_notes[k].pitch OR array_notes[k].pitch = '0' THEN matches_pitch := TRUE; END IF;
		    IF l.duration = array_notes[k].duration OR array_notes[k].duration = '0' THEN matches_duration := TRUE; END IF;
		    IF l.octave = array_notes[k].octave OR array_notes[k].octave = '0' THEN matches_octave := TRUE; END IF;

		    current_note.next_noteset = l.next_noteset_id;
		    
		END LOOP;




		EXIT WHEN NOT matches_duration OR NOT matches_octave OR NOT matches_pitch ;

		IF matches_pitch AND matches_duration AND matches_octave THEN 
		    array_result := array_result || next_note_result; 
		END IF;
		
		
	    END LOOP;
	    
	    IF ARRAY_LENGTH(array_notes, 1) = ARRAY_LENGTH(array_result, 1) THEN 

		--raise notice 'Score: % | Movement: % | Meausure: % | Staff: % Voice: % Instrument: % Sequence: % ', j.score_id, j.movement_id, j.measure, j.staff, j.voice, j.instrument, array_result;
		INSERT INTO tmp_result (tmp_score, tmp_movement, tmp_movement_name, tmp_measure, tmp_staff, tmp_voice, tmp_instrument, tmp_instrument_name) 
		VALUES (j.score_id, j.movement_id,'', j.measure, j.staff, j.voice, j.instrument,'');

	    END IF;

	    array_result := NULL;

	    
	    
	END LOOP;
	
	RETURN QUERY 
	       SELECT tmp_score, 
		      tmp_movement, 
		      mov.score_movement_description, 
		      tmp_measure, 
		      tmp_staff, 
		      tmp_voice, 
		      tmp_instrument, 
		      med.movement_performance_medium_description
	FROM tmp_result 
	JOIN wmss.wmss_score_movements mov ON mov.movement_id = tmp_movement AND mov.score_id = tmp_score
	JOIN wmss.wmss_movement_performance_medium med ON TRIM(med.file_performance_medium_id) = TRIM(tmp_instrument) AND 
						     mov.movement_id = med.movement_id AND 
						     med.score_id = mov.score_id;
	--ORDER BY res_score, res_movement, res_instrument, res_measure ASC ;

END;$$
LANGUAGE plpgsql;
ALTER FUNCTION wmss.wmss_find_melody(VARCHAR,VARCHAR) OWNER TO postgres;

SELECT DISTINCT * FROM wmss.wmss_find_melody('c-8-0>b-8-0','121')


