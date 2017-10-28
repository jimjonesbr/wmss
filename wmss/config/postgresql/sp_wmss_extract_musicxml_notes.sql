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
DECLARE ext_accidental VARCHAR;
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
		--TODO add here code for different keys!
		ext_notes := (SELECT XPATH('//measure/note', ext_measures[j]));		

		FOR k IN 1 .. ARRAY_LENGTH(ext_notes, 1) LOOP

		    ext_pitch := LOWER((SELECT XPATH('//pitch/step/text()', ext_notes[k]))[1]::TEXT);
		    ext_octave := (SELECT XPATH('//pitch/octave/text()', ext_notes[k]))[1];
		    ext_duration := (SELECT XPATH('//type/text()', ext_notes[k]))[1];
		    ext_staff := (SELECT XPATH('//staff/text()', ext_notes[k]))[1];
		    ext_voice := (SELECT XPATH('//voice/text()', ext_notes[k]))[1];
		    ext_instrument := (SELECT XPATH('//instrument/@id', ext_notes[k]))[1];
		    ext_accidental := (SELECT XPATH('//accidental/text()', ext_notes[k]))[1];

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
		    IF ext_accidental IS NOT NULL THEN 
			IF LOWER(ext_accidental) = 'flat' THEN ext_accidental := 'b'; END IF;
			IF LOWER(ext_accidental) = 'sharp' THEN ext_accidental := 's'; END IF;
			IF LOWER(ext_accidental) = 'natural' THEN ext_accidental := ''; END IF;
		    --raise notice 'accidental %',ext_accidental;
		    ELSE 
		        ext_accidental := '';
		    END IF;
		    INSERT INTO wmss_notes (score_id, movement_id, measure, octave, pitch, duration, voice, instrument,staff) VALUES (document_id, ext_movement, ext_measure_id, ext_octave, ext_pitch||ext_accidental, ext_duration, ext_voice::INTEGER, ext_instrument, ext_staff::INTEGER);
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
















DROP TABLE IF EXISTS wmss_notes_p1;
DROP TABLE IF EXISTS wmss_notes_p2;
DROP TABLE IF EXISTS wmss_notes_p3;
DROP TABLE IF EXISTS wmss_notes_p4;
DROP TABLE IF EXISTS wmss_notes_p5;
DROP TABLE IF EXISTS wmss_notes;

CREATE TABLE wmss_notes (
note_id SERIAL PRIMARY KEY,
score_id VARCHAR,
movement_id INTEGER,
instrument VARCHAR,
measure VARCHAR,
pitch VARCHAR,
octave VARCHAR,
duration VARCHAR,
voice INTEGER,
staff INTEGER,
next NUMERIC
);


-- wmss_notes partitions (every million records)
								
CREATE TABLE wmss_notes_p1 (CHECK ( note_id >= 1 AND note_id <= 1000000 )) INHERITS (wmss_notes);
CREATE TABLE wmss_notes_p2 (CHECK ( note_id > 1000000 AND note_id <= 2000000 )) INHERITS (wmss_notes);
CREATE TABLE wmss_notes_p3 (CHECK ( note_id > 2000000 AND note_id <= 3000000 )) INHERITS (wmss_notes);
CREATE TABLE wmss_notes_p4 (CHECK ( note_id > 3000000 AND note_id <= 4000000 )) INHERITS (wmss_notes);
CREATE TABLE wmss_notes_p5 (CHECK ( note_id > 4000000 )) INHERITS (wmss_notes);


CREATE INDEX idx_wmss_notes_p1 ON wmss_notes_p1 (note_id);
CREATE INDEX idx_wmss_notes_p1_pitch ON wmss_notes_p1 (pitch);
CREATE INDEX idx_wmss_notes_p1_octave ON wmss_notes_p1 (octave);
CREATE INDEX idx_wmss_notes_p1_duration ON wmss_notes_p1 (duration);
CREATE INDEX idx_wmss_notes_p1_next ON wmss_notes_p1 (next);

CREATE INDEX idx_wmss_notes_p2 ON wmss_notes_p2 (note_id);
CREATE INDEX idx_wmss_notes_p2_pitch ON wmss_notes_p2 (pitch);
CREATE INDEX idx_wmss_notes_p2_octave ON wmss_notes_p2 (octave);
CREATE INDEX idx_wmss_notes_p2_duration ON wmss_notes_p2 (duration);

CREATE INDEX idx_wmss_notes_p3 ON wmss_notes_p3 (note_id);
CREATE INDEX idx_wmss_notes_p3_pitch ON wmss_notes_p3 (pitch);
CREATE INDEX idx_wmss_notes_p3_octave ON wmss_notes_p3 (octave);
CREATE INDEX idx_wmss_notes_p3_duration ON wmss_notes_p3 (duration);

CREATE INDEX idx_wmss_notes_p4 ON wmss_notes_p4 (note_id);
CREATE INDEX idx_wmss_notes_p4_pitch ON wmss_notes_p4 (pitch);
CREATE INDEX idx_wmss_notes_p4_octave ON wmss_notes_p4 (octave);
CREATE INDEX idx_wmss_notes_p4_duration ON wmss_notes_p4 (duration);

CREATE INDEX idx_wmss_notes_p5 ON wmss_notes_p5 (note_id);
CREATE INDEX idx_wmss_notes_p5_pitch ON wmss_notes_p5 (pitch);
CREATE INDEX idx_wmss_notes_p5_octave ON wmss_notes_p5 (octave);
CREATE INDEX idx_wmss_notes_p5_duration ON wmss_notes_p5 (duration);



CREATE OR REPLACE FUNCTION wmss_notes_insert_trigger()
RETURNS TRIGGER AS $$
BEGIN					     
    IF ( NEW.note_id >= 0 AND NEW.note_id <= 1000000 ) THEN
        INSERT INTO wmss_notes_p1 VALUES (NEW.*);
    ELSIF ( NEW.note_id > 1000000 AND NEW.note_id <= 2000000) THEN
        INSERT INTO wmss_notes_p2 VALUES (NEW.*);
    ELSIF ( NEW.note_id > 2000000  AND NEW.note_id <= 3000000) THEN
        INSERT INTO wmss_notes_p3 VALUES (NEW.*);
    ELSIF ( NEW.note_id > 3000000 AND NEW.note_id <= 4000000) THEN
        INSERT INTO wmss_notes_p4 VALUES (NEW.*);
    ELSIF ( NEW.note_id > 4000000 ) THEN
        INSERT INTO wmss_notes_p5 VALUES (NEW.*);
    ELSE
        RAISE EXCEPTION 'Date out of range.  Fix the measurement_insert_trigger() function!';
    END IF;
    RETURN NULL;
END;
$$
LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS wmss_trigger_insert_notes ON wmss_notes;

CREATE TRIGGER wmss_trigger_insert_notes
    BEFORE INSERT ON wmss_notes
    FOR EACH ROW EXECUTE PROCEDURE wmss_notes_insert_trigger();


--CREATE INDEX idx_wmss_note ON wmss_notes (note_id);
SELECT wmss_extract_musicxml_notes(score_id) FROM wmss_document WHERE document_type_id = 'musicxml';-- limit 10;
--SELECT wmss_extract_musicxml_notes(score_id) FROM wmss_document WHERE document_type_id = 'musicxml';

--INSERT INTO wmss_notes (score_id, movement_id, instrument, measure, pitch, octave, duration, voice, staff, next) SELECT score_id, movement_id, instrument, measure, pitch, octave, duration, voice, staff, note_id+1 FROM wmss_notes;
--INSERT INTO wmss_notes (score_id, movement_id, instrument, measure, pitch, octave, duration, voice, staff, next) SELECT score_id, movement_id, instrument, measure, pitch, octave, duration, voice, staff, note_id+1 FROM wmss_notes;
--VACUUM full;


--select count(*) from wmss_notes_p2
--EXPLAIN ANALYZE SELECT ROW(pitch, duration, octave, next) FROM wmss_notes WHERE note_id = 373851 AND score_id = '4342391' AND movement_id = 4  LIMIT 1

--explain analyze SELECT public.wmss_find_melody('d-8-0/rest-8-0/b-8-0/rest-8-0/g-8-0/rest-8-0/d-8-0/rest-8-0');