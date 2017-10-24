-- Author: Jones
-- Parameter: 1) Note sequence
-- Comments: 

DROP TYPE IF EXISTS melody_result;
CREATE TYPE melody_result AS (document_id VARCHAR, melody_location VARCHAR );

DROP TYPE IF EXISTS note;
CREATE TYPE note AS (pitch VARCHAR, duration VARCHAR, octave VARCHAR, next_note NUMERIC);

CREATE OR REPLACE FUNCTION public.wmss_find_melody(melody VARCHAR) RETURNS CHARACTER VARYING AS $BODY$

DECLARE i INTEGER;
DECLARE j RECORD;
DECLARE k INTEGER;


DECLARE result melody_result[];
DECLARE array_input_melody VARCHAR[];
DECLARE array_notes NOTE[];
DECLARE note NOTE;
DECLARE start_note NOTE;

DECLARE current_note NOTE;
DECLARE next_note_id NUMERIC; 


DECLARE array_result NOTE[];
DECLARE matches BOOLEAN DEFAULT FALSE;
--DECLARE current_note NUMERIC;
DECLARE next_note NOTE;

DECLARE melody_query TEXT;
BEGIN
		
	RAISE NOTICE 'Parsing given melody ... ';   	

	array_input_melody := REGEXP_SPLIT_TO_ARRAY(melody,'/');

	FOR i IN 1 .. ARRAY_UPPER(array_input_melody, 1) LOOP
	
	    note := NULL;
	    note.pitch := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[1];
	    note.duration := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[2];
	    note.octave := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[3];

	    array_notes := array_notes || note;
	END LOOP;

	start_note = array_notes[1];
	
	melody_query := 'SELECT * FROM wmss_notes WHERE ';
	
	IF start_note.octave <> '0' THEN melody_query := melody_query || 'octave=' || QUOTE_LITERAL(start_note.octave); END IF;
	IF start_note.pitch <> '0' THEN 
	    IF start_note.octave <> '0' THEN melody_query := melody_query || ' AND '; END IF;
	    melody_query := melody_query || 'pitch=' || QUOTE_LITERAL(start_note.pitch); 
	END IF;
	IF start_note.duration <> '0' THEN 
	    IF start_note.octave <> '0' OR start_note.pitch <> '0'  THEN melody_query := melody_query || ' AND '; END IF;
	    melody_query := melody_query || 'duration=' || QUOTE_LITERAL(start_note.duration); 	    
	END IF;

	raise notice 'Query -> %', melody_query;




	FOR j IN EXECUTE melody_query LOOP

	    
	    current_note.pitch = j.pitch;
	    current_note.duration = j.duration;
	    current_note.octave = j.octave;
	    current_note.next_note = j.next;

	    next_note_id = j.next;	    
	    array_result := array_result || current_note;
	    
	    --FOR k IN 2 .. ARRAY_UPPER(array_input_melody, 1) LOOP
	    k= 2;
	    matches := TRUE;
	    
	    WHILE k <= ARRAY_UPPER(array_notes, 1) AND matches = TRUE LOOP

		
	        next_note := (SELECT ROW(pitch, duration, octave, next) FROM wmss_notes WHERE note_id = next_note_id);

		IF next_note.pitch <> array_notes[k].pitch AND array_notes[k].pitch <> '0' THEN matches := FALSE; END IF;
		IF next_note.duration <> array_notes[k].duration AND array_notes[k].duration <> '0' THEN matches := FALSE; END IF;
		IF next_note.octave <> array_notes[k].octave AND array_notes[k].octave <> '0' THEN matches := FALSE; END IF;

		array_result := array_result || next_note;

		next_note_id := next_note.next_note;
		k=k+1;
	    END LOOP;
	    

	    IF matches THEN 
	    raise notice 'Score: % | Movement: % | Meausure: % | Sequence: % ', j.score_id, j.movement_id, j.measure, array_result;
	    END IF;

	    array_result := NULL;






	    
	    
	END LOOP;
	
	RETURN result;

END;
$BODY$
LANGUAGE plpgsql VOLATILE COST 100;
ALTER FUNCTION public.wmss_find_melody(VARCHAR) OWNER TO postgres;


SELECT public.wmss_find_melody('f-32-0/g-32-0/b-8-0');


