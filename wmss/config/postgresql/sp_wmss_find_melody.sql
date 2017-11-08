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

	array_input_melody := REGEXP_SPLIT_TO_ARRAY(melody,'/');

	FOR i IN 1 .. ARRAY_UPPER(array_input_melody, 1) LOOP
	
	    note := NULL;
	    note.pitch := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[1];
	    note.duration := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[2];
	    note.octave := (REGEXP_SPLIT_TO_ARRAY(array_input_melody[i],'-'))[3];

	    array_notes := array_notes || note;
	END LOOP;

	start_note = array_notes[1];

	melody_query := 'SELECT * FROM wmss.wmss_notes WHERE next_noteset_id IS NOT NULL AND ';
	
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

	        --next_note_result := (SELECT ROW(pitch, duration, octave, next_noteset_id) FROM wmss_notes 
		--		     WHERE noteset_id = current_note.next_noteset AND 
		--		           score_id = j.score_id AND 
		--			   movement_id = j.movement_id LIMIT 1);

					   
		FOR l IN SELECT pitch, duration, octave, next_noteset_id FROM wmss.wmss_notes 
				  WHERE noteset_id = current_note.next_noteset AND 
				        score_id = j.score_id AND 
					movement_id = j.movement_id LIMIT 1 LOOP

		    IF l.pitch = array_notes[k].pitch OR array_notes[k].pitch = '0' THEN matches_pitch := TRUE; END IF;
		    IF l.duration = array_notes[k].duration OR array_notes[k].duration = '0' THEN matches_duration := TRUE; END IF;
		    IF l.octave = array_notes[k].octave OR array_notes[k].octave = '0' THEN matches_octave := TRUE; END IF;

		    current_note.next_noteset = l.next_noteset_id;
		    
		END LOOP;

--		IF next_note_result.pitch = array_notes[k].pitch OR array_notes[k].pitch = '0' THEN matches_pitch := TRUE; END IF;
--		IF next_note_result.duration = array_notes[k].duration OR array_notes[k].duration = '0' THEN matches_duration := TRUE; END IF;
--		IF next_note_result.octave = array_notes[k].octave OR array_notes[k].octave = '0' THEN matches_octave := TRUE; END IF;

		--current_note.next_noteset = next_note_result.next_noteset;


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
	JOIN wmss.wmss_movement_performance_medium med ON med.file_performance_medium_id = tmp_instrument AND 
						     mov.movement_id = med.movement_id AND 
						     med.score_id = mov.score_id;
	--ORDER BY res_score, res_movement, res_instrument, res_measure ASC ;

END;$$
LANGUAGE plpgsql;
ALTER FUNCTION wmss.wmss_find_melody(VARCHAR,VARCHAR) OWNER TO postgres;


--SELECT public.wmss_find_melody('c-4-0/c-4-0/d-4-0/d-4-0/e-4-0');

--SELECT public.wmss_find_melody('c-8-5/c-8-4/b-8-3');


--SELECT public.wmss_find_melody('f-32-0/g-32-0/b-8-0');
--SET max_parallel_workers_per_gather TO 6;
--SELECT public.wmss_find_melody('d-8-0/rest-8-0/b-8-0/rest-8-0/g-8-0/rest-8-0/d-8-0/rest-8-0');


--where res_score in ('4280885')

--SELECT * FROM public.wmss_find_melody('a-w-0/a-w-0/a-w-0/a-w-0') where res_score in ('4280885') ORDER BY res_measure::INTEGER;


--SELECT * FROM public.wmss_find_melody('c-4-0/d-4-0/e-4-0/f-4-0/g-4-0/a-4-0','''1'',''2'',''3'',''4'',''5'',''6'',''7'',''8'',''9'',''10'',''11'',''12'',''13'',''14'',''15'',''16'',''17'',''18'',''19'',''20'',''21'',''22'',''23'',''24'',''25'',''26'',''27'',''28'',''29'',''30'',''31'',''32'',''33'',''34'',''35'',''36'',''37'',''38'',''39'',''40'',''41'',''42'',''43'',''44'',''45'',''46'',''47'',''48'',''49'',''50'',''51'',''52'',''53'',''54'',''55'',''56'',''57'',''58'',''59'',''60'',''61'',''62'',''63'',''64'',''65'',''66'',''67'',''68'',''69'',''70'',''71'',''72'',''73'',''74'',''75'',''76'',''3e724fa4-f67e-4dff-94d6-a01b78d73049'',''4a12fece-a4ac-4654-8407-2e32be8d3e56'',''5c3047f9-f8bc-46eb-8073-0dc3ffb28d30'',''5e61ff05-7ceb-4e5c-b81e-19f8105f4a53'',''6be19ce2-fbf2-442f-af37-08b0eb487d45'',''20b0dcec-ff6a-43d2-8bfe-9e077937a1cf'',''899c8c9e-dd50-4041-8706-8c0479eb5de5'',''01924ae1-3991-464f-97f0-b6498f973560'',''825151'',''948617'',''1009591'',''1118465'',''1639763'',''1642539'',''1883542'',''2398460'',''3079496'',''3079600'',''3079686'',''3079699'',''3098742'',''3368467'',''3368606'',''3530337'',''3886826'',''4272171'',''4272244'',''4276689'',''4276790'',''4276911'',''4279917'',''4280245'',''4280660'',''4280885'',''4280969'',''4281006'',''4281042'',''4283787'',''4287452'',''4287515'',''4307727'',''4308235'',''4339428'',''4339906'',''4339998'',''4340117'',''4341718'',''4341767'',''4342391'',''75266515-9803-41de-ac1b-bc2796adc12d'',''a13a60c1-8170-40cb-aaf2-4805931f9465'',''ad3ed640-90e3-49ce-8ce4-b3dc69c597a5'',''bd2527e4-155e-401d-a6ba-7d1056d09b37'',''cfc09cec-206c-4a97-b3ac-b4c304080350'',''d7c0073f-8406-4dc0-be5c-3f267e7f5789'',''e73f74ec-6711-499f-b3eb-503ca99c6b14'',''ebb4d5a6-3096-492f-934a-bc7c0e6644bf'',''4339837'',''974543''');
--SELECT * FROM public.wmss_find_melody('c-4-0/d-4-0/e-4-0/f-4-0/g-4-0/a-4-0','*');

--select * from wmss_notes where score_id = '3530337' and (measure = '14' or measure = '15') and instrument = 'P2-I1' and movement_id = 1


--SELECT * FROM public.wmss_find_melody('c-w-0/c-w-0/c-w-0/c-w-0/c-w-0','*') where res_score = '4307727' order by res_measure::int 
--SELECT * FROM wmss_find_melody('c-w-0/c-w-0/c-w-0/c-w-0/c-w-0','6,7,16,20,25,29,33,35,37,55,56,62,68,4272244,4276689,4276790,4276911,4279917,4280245,4281006,4287452,4307727,4339428,4339906,4340117,4341767,4342391')



--select * from wmss_notes where score_id = '4307727' and movement_id = 1 and (measure = '227' or measure = '228' OR measure = '229'OR measure = '230'OR measure = '231')  and instrument = 'P4-I1'

--select * from wmss_notes where score_id = '4307727' and chord limit 1000

--select * from wmss_notes where next_noteset_id is null

--SELECT * FROM public.wmss_find_melody('c-w-0/c-w-0/c-w-0/c-w-0/c-w-0','*')
--SELECT * FROM public.wmss_find_melody('a-w-0/a-w-0/a-w-0/a-w-0/a-w-0','*')

SELECT DISTINCT * FROM wmss.wmss_find_melody('c-4-0/d-4-0/e-4-0/f-4-0/g-4-0/a-4-0','*');