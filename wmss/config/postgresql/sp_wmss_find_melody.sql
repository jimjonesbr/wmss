-- Author: Jones
-- Parameter: 1) Note sequence
-- Comments: 

--DROP TYPE IF EXISTS melody_result;
--CREATE TYPE melody_result AS (document_id VARCHAR, melody_location VARCHAR );

DROP TYPE IF EXISTS note;
CREATE TYPE note AS (pitch VARCHAR, duration VARCHAR, octave VARCHAR, next_note NUMERIC);


CREATE OR REPLACE FUNCTION public.wmss_find_melody(melody VARCHAR)
RETURNS TABLE (
res_score VARCHAR, 
res_movement VARCHAR,
res_movement_name VARCHAR, 
res_measure VARCHAR, 
res_staff VARCHAR, 
res_voice VARCHAR, 
res_instrument VARCHAR,
res_instrument_name VARCHAR) AS $$
--AS $BODY$

DECLARE i INTEGER;
DECLARE j RECORD;
DECLARE k INTEGER;

--DECLARE result melody_result[];
DECLARE array_input_melody VARCHAR[];
DECLARE array_notes NOTE[];
DECLARE note NOTE;
DECLARE start_note NOTE;

DECLARE current_note NOTE;
DECLARE next_note_id NUMERIC; 


DECLARE array_result NOTE[];
DECLARE matches_pitch BOOLEAN DEFAULT FALSE;
DECLARE matches_octave BOOLEAN DEFAULT FALSE;
DECLARE matches_duration BOOLEAN DEFAULT FALSE;
DECLARE next_note_result NOTE;

DECLARE melody_query TEXT;

BEGIN
		
	RAISE NOTICE 'Parsing given melody ... ';   	
	DROP TABLE IF EXISTS tmp_result;
	CREATE TEMPORARY TABLE tmp_result (tmp_score VARCHAR, tmp_movement VARCHAR, tmp_movement_name VARCHAR, tmp_measure VARCHAR, tmp_staff VARCHAR, tmp_voice VARCHAR, tmp_instrument VARCHAR, tmp_instrument_name VARCHAR);

	array_input_melody := REGEXP_SPLIT_TO_ARRAY(melody,'/');

	FOR i IN 1 .. ARRAY_UPPER(array_input_melody, 1) LOOP
	
	    note := NULL;
	    note.pitch := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[1];
	    note.duration := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[2];
	    note.octave := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[3];

	    array_notes := array_notes || note;
	END LOOP;

	start_note = array_notes[1];

	melody_query := 'SELECT * FROM wmss_notes WHERE next IS NOT NULL AND ';
	
	IF start_note.octave <> '0' THEN 
	    melody_query := melody_query || 'octave=' || QUOTE_LITERAL(start_note.octave); 
	END IF;
	
	IF start_note.pitch <> '0' THEN 
	
	    IF start_note.octave <> '0' THEN 
	        melody_query := melody_query || ' AND '; 
	    END IF;
	    
	    melody_query := melody_query || 'pitch=' || QUOTE_LITERAL(start_note.pitch); 
	END IF;
	
	IF start_note.duration <> '0' THEN 
	
	    IF start_note.octave <> '0' OR start_note.pitch <> '0'  THEN 
	        melody_query := melody_query || ' AND '; 
	    END IF;
	    
	    melody_query := melody_query || 'duration=' || QUOTE_LITERAL(start_note.duration); 	    
	END IF;

	raise notice 'Query -> %', melody_query;
		
	FOR j IN EXECUTE melody_query LOOP
	
	    current_note.pitch := j.pitch;
	    current_note.duration := j.duration;
	    current_note.octave := j.octave;
	    current_note.next_note := j.next;
	    array_result := array_result || current_note;
	    	
	    FOR k IN 2 .. ARRAY_LENGTH(array_notes, 1) LOOP
		
		matches_duration := FALSE;
		matches_pitch := FALSE;
		matches_octave := FALSE;

	        next_note_result := (SELECT ROW(pitch, duration, octave, next) FROM wmss_notes 
				     WHERE note_id = current_note.next_note AND 
				           score_id = j.score_id AND 
					   movement_id = j.movement_id LIMIT 1);

		IF next_note_result.pitch = array_notes[k].pitch OR array_notes[k].pitch = '0' THEN matches_pitch := TRUE; END IF;
		IF next_note_result.duration = array_notes[k].duration OR array_notes[k].duration = '0' THEN matches_duration := TRUE; END IF;
		IF next_note_result.octave = array_notes[k].octave OR array_notes[k].octave = '0' THEN matches_octave := TRUE; END IF;

		current_note.next_note = next_note_result.next_note;


		EXIT WHEN NOT matches_duration OR NOT matches_octave OR NOT matches_pitch ;

		IF matches_pitch AND matches_duration AND matches_octave THEN 
		    array_result := array_result || next_note_result; 
		END IF;
		
		
	    END LOOP;
	    
	    IF ARRAY_LENGTH(array_notes, 1) = ARRAY_LENGTH(array_result, 1) THEN 

		raise notice 'Score: % | Movement: % | Meausure: % | Staff: % Voice: % Instrument: % Sequence: % ', j.score_id, j.movement_id, j.measure, j.staff, j.voice, j.instrument, array_result;
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
	JOIN wmss_score_movements mov ON mov.movement_id = tmp_movement AND mov.score_id = tmp_score
	JOIN wmss_movement_performance_medium med ON med.file_performance_medium_id = tmp_instrument AND 
						     mov.movement_id = med.movement_id AND 
						     med.score_id = mov.score_id;
	--ORDER BY res_score, res_movement, res_instrument, res_measure ASC ;

END;$$
LANGUAGE plpgsql;
ALTER FUNCTION public.wmss_find_melody(VARCHAR) OWNER TO postgres;


--SELECT public.wmss_find_melody('c-4-0/c-4-0/d-4-0/d-4-0/e-4-0');

--SELECT public.wmss_find_melody('c-8-5/c-8-4/b-8-3');


--SELECT public.wmss_find_melody('f-32-0/g-32-0/b-8-0');
--SET max_parallel_workers_per_gather TO 6;
--SELECT public.wmss_find_melody('d-8-0/rest-8-0/b-8-0/rest-8-0/g-8-0/rest-8-0/d-8-0/rest-8-0');

--SELECT * FROM public.wmss_find_melody('c-w-0/c-w-0/c-w-0/c-w-0/c-w-0') 
--where res_score in ('4280885')

SELECT * FROM public.wmss_find_melody('a-w-0/a-w-0/a-w-0/a-w-0') where res_score in ('4280885') ORDER BY res_measure::INTEGER;

SELECT * FROM public.wmss_find_melody('c-4-0/d-4-0/e-4-0/f-4-0/g-4-0/a-4-0') 



select * from wmss_notes where score_id = '3530337' and (measure = '14' or measure = '15') and instrument = 'P2-I1' and movement_id = 1
