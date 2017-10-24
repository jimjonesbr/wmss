-- Author: Jones
-- Parameter: 1) Score ID
-- Comments: This extracts all note sequences from MusicXML documents.

CREATE OR REPLACE FUNCTION public.wmss_extract_musicxml_notes(document_id VARCHAR) RETURNS CHARACTER VARYING AS $BODY$

DECLARE i RECORD;
DECLARE j RECORD;
DECLARE ext_parts VARCHAR[];
DECLARE ext_measures XML[];
DECLARE ext_notes XML[];

DECLARE ext_staff VARCHAR;
DECLARE ext_voice VARCHAR;
DECLARE ext_measure VARCHAR;
DECLARE ext_measure_id VARCHAR;
DECLARE ext_pitch VARCHAR;
DECLARE ext_octave VARCHAR;
DECLARE ext_duration VARCHAR;
DECLARE ext_instrument VARCHAR;
DECLARE ext_movement INTEGER DEFAULT 0;

BEGIN
	RAISE NOTICE 'Processing MusicXML from "%" ... ',document_id;   	
	
	
	ext_parts := (SELECT XPATH('//score-part/@id', score_document)::TEXT[] FROM wmss_document WHERE score_id = document_id);
	RAISE NOTICE '	Extracting notes of % instruments ... ', ARRAY_LENGTH(ext_parts,1);   	

	FOR i IN 1 .. ARRAY_LENGTH(ext_parts, 1) LOOP

	    --RAISE NOTICE '	Instrument %',ext_parts[i];
	    ext_measures := (SELECT XPATH('//part[@id="'||ext_parts[i]||'"]/measure', score_document) FROM wmss_document WHERE score_id = document_id);
	    
	    FOR j IN 1 .. ARRAY_LENGTH(ext_measures, 1) LOOP
	
	        ext_measure_id := (SELECT XPATH('//measure/@number', ext_measures[j]))[1];		

		IF ext_measure_id = '1' THEN ext_movement := ext_movement + 1; END IF;

		ext_notes := (SELECT XPATH('//measure/note', ext_measures[j]));		

		FOR k IN 1 .. ARRAY_LENGTH(ext_notes, 1) LOOP

		    ext_pitch := LOWER((SELECT XPATH('//pitch/step/text()', ext_notes[k]))[1]::TEXT);
		    ext_octave := (SELECT XPATH('//pitch/octave/text()', ext_notes[k]))[1];
		    ext_duration := (SELECT XPATH('//type/text()', ext_notes[k]))[1];
		    ext_staff := (SELECT XPATH('//staff/text()', ext_notes[k]))[1];
		    ext_voice := (SELECT XPATH('//voice/text()', ext_notes[k]))[1];
		    ext_instrument := (SELECT XPATH('//instrument/@id', ext_notes[k]))[1];

		    IF ext_voice = '' OR ext_voice IS NULL THEN ext_voice := '1'; END IF;
		    IF ext_duration = 'whole' THEN ext_duration = 'w'; END IF;
		    IF ext_duration = 'half' THEN ext_duration = 'h'; END IF;
		    IF ext_duration = 'quarter' THEN ext_duration = '4'; END IF;
		    IF ext_duration = 'eighth' THEN ext_duration = '8'; END IF;
		    IF ext_duration = '16th' THEN ext_duration = '16'; END IF;
		    IF ext_duration = '32nd' THEN ext_duration = '32'; END IF;
		    IF ext_duration = '64th' THEN ext_duration = '64'; END IF;
		    IF ext_duration = '128th' THEN ext_duration = '128'; END IF;
		    IF ext_duration = '256th' THEN ext_duration = '256'; END IF;
		    IF ext_pitch = '' OR ext_pitch IS NULL THEN ext_pitch := 'rest'; END IF;
		    IF ext_octave = '' OR ext_octave IS NULL THEN ext_octave := 'rest'; END IF;
		
		    INSERT INTO wmss_notes (score_id, movement_id, measure, octave, pitch, duration, voice, instrument,staff) VALUES (document_id, ext_movement, ext_measure_id, ext_octave, ext_pitch, ext_duration, ext_voice, ext_instrument, ext_staff);
		    --raise notice '[Part: % Movement: % Measure: %] pitch: % | octave: % | duration: % | staff: % | voice: % | instrument: %',parts[i], movement, measure_id, pitch, octave,duration,staff,voice,instrument;
		END LOOP;

	    END LOOP;

	    ext_movement := 0;
		
	END LOOP;
	

	RAISE NOTICE '	Creating note sequences. This might take a while ...';

	UPDATE wmss_notes note 
	SET next = (SELECT note_id 
		    FROM wmss_notes 
		    WHERE note_id > note.note_id AND
			  staff = note.staff AND
			  voice = note.voice AND
			  score_id = note.score_id AND
			  movement_id = note.movement_id AND
			  instrument = note.instrument
		    ORDER BY note_id
		    LIMIT 1)
	WHERE score_id = document_id;
	
	RETURN 'Notes extraction for [' || document_id || '] finished.' ;

END;
$BODY$
LANGUAGE plpgsql VOLATILE COST 100;
ALTER FUNCTION public.wmss_extract_musicxml_notes(VARCHAR) OWNER TO postgres;

DROP TABLE IF EXISTS wmss_notes;
CREATE TABLE wmss_notes (
note_id SERIAL,
score_id VARCHAR,
movement_id INTEGER,
instrument VARCHAR,
measure VARCHAR,
pitch VARCHAR,
octave VARCHAR,
duration VARCHAR,
voice VARCHAR,
staff VARCHAR,
next NUMERIC
);
CREATE INDEX idx_wmss_note ON wmss_notes (note_id);
CREATE INDEX idx_wmss_staff ON wmss_notes (staff);
CREATE INDEX idx_wmss_voice ON wmss_notes (voice);
CREATE INDEX idx_wmss_movement ON wmss_notes (movement_id);
CREATE INDEX idx_wmss_instrument ON wmss_notes (instrument);

--SELECT wmss_extract_musicxml_notes('825151');
SELECT wmss_extract_musicxml_notes(score_id) FROM wmss_document WHERE document_type_id = 'musicxml';

--SELECT * FROM wmss_notes limit 1000









--UPDATE wmss_notes note 
--SET next = (SELECT note_id 
--	    FROM wmss_notes 
--	    WHERE note_id > note.note_id AND
--		  staff = note.staff AND
--		  voice = note.voice AND
--		  score_id = note.score_id AND
--		  movement_id = note.movement_id AND
--		  instrument = note.instrument
--	    ORDER BY note_id
--	    LIMIT 1)
--WHERE score_id = '4340117';