-- Author: Jones
-- Comments: Function to extract notes from MusicXML documents.

CREATE OR REPLACE FUNCTION wmss.wmss_extract_musicxml_notes(document_id VARCHAR) RETURNS CHARACTER VARYING AS $BODY$

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
DECLARE ext_key_mode VARCHAR;
DECLARE ext_key_fifth VARCHAR;
DECLARE ext_is_chord BOOLEAN DEFAULT FALSE;
DECLARE noteset BIGINT;
DECLARE current_tonality VARCHAR;
BEGIN
	RAISE NOTICE 'Processing MusicXML from "%" ... ',document_id;   	
		
	ext_parts := (SELECT XPATH('//score-part/@id', score_document)::TEXT[] FROM wmss.wmss_document WHERE score_id = document_id AND document_type_id = 'musicxml');
	RAISE NOTICE '	Extracting notes of % instruments ... ', ARRAY_LENGTH(ext_parts,1);   	

	FOR i IN 1 .. ARRAY_LENGTH(ext_parts, 1) LOOP

	    ext_measures := (SELECT XPATH('//part[@id="'||ext_parts[i]||'"]/measure', score_document) FROM wmss.wmss_document WHERE score_id = document_id);

	    FOR j IN 1 .. ARRAY_LENGTH(ext_measures, 1) LOOP
	
	        ext_measure_id := (SELECT XPATH('//measure/@number', ext_measures[j]))[1];		
		ext_key_mode := LOWER((SELECT XPATH('//measure/attributes/key/mode/text()', ext_measures[j]))[1]::TEXT);		
		ext_key_fifth := (SELECT XPATH('//measure/attributes/key/fifths/text()', ext_measures[j]))[1];		

		
		IF ext_key_mode IS NOT NULL AND ext_key_fifth IS NOT NULL then 
		
		    IF ext_key_mode = 'major' THEN 

		        IF ext_key_fifth = '0' THEN current_tonality := 'cmajor'; END IF;
			IF ext_key_fifth = '1' THEN current_tonality := 'gmajor'; END IF;
			IF ext_key_fifth = '2' THEN current_tonality := 'dmajor'; END IF;
			IF ext_key_fifth = '3' THEN current_tonality := 'amajor'; END IF;
			IF ext_key_fifth = '4' THEN current_tonality := 'emajor'; END IF;
			IF ext_key_fifth = '5' THEN current_tonality := 'bmajor'; END IF;
			IF ext_key_fifth = '6' THEN current_tonality := 'fsmajor'; END IF;
			IF ext_key_fifth = '7' THEN current_tonality := 'csmajor'; END IF;
			IF ext_key_fifth = '-1' THEN current_tonality := 'fmajor'; END IF;
			IF ext_key_fifth = '-2' THEN current_tonality := 'bbmajor'; END IF;
			IF ext_key_fifth = '-3' THEN current_tonality := 'ebmajor'; END IF;
			IF ext_key_fifth = '-4' THEN current_tonality := 'abmajor'; END IF;
			IF ext_key_fifth = '-5' THEN current_tonality := 'dbmajor'; END IF;
			IF ext_key_fifth = '-6' THEN current_tonality := 'gbmajor'; END IF;			
			IF ext_key_fifth = '-7' THEN current_tonality := 'cbmajor'; END IF;
		    END IF;


		    IF ext_key_mode = 'minor' THEN 

		        IF ext_key_fifth = '0' THEN current_tonality := 'aminor'; END IF;
			IF ext_key_fifth = '1' THEN current_tonality := 'eminor'; END IF;
			IF ext_key_fifth = '2' THEN current_tonality := 'bminor'; END IF;
			IF ext_key_fifth = '3' THEN current_tonality := 'fsminor'; END IF;
			IF ext_key_fifth = '4' THEN current_tonality := 'csminor'; END IF;
			IF ext_key_fifth = '5' THEN current_tonality := 'gsminor'; END IF;
			IF ext_key_fifth = '6' THEN current_tonality := 'dsminor'; END IF;
			IF ext_key_fifth = '7' THEN current_tonality := 'abminor'; END IF;
			IF ext_key_fifth = '-1' THEN current_tonality := 'dminor'; END IF;
			IF ext_key_fifth = '-2' THEN current_tonality := 'gminor'; END IF;
			IF ext_key_fifth = '-3' THEN current_tonality := 'cminor'; END IF;
			IF ext_key_fifth = '-4' THEN current_tonality := 'fminor'; END IF;
			IF ext_key_fifth = '-5' THEN current_tonality := 'bbminor'; END IF;
			IF ext_key_fifth = '-6' THEN current_tonality := 'ebminor'; END IF;			
			IF ext_key_fifth = '-7' THEN current_tonality := 'gsminor'; END IF;

		    END IF;

		    RAISE NOTICE 'Measure: % > Tonality change (%)',ext_measure_id, current_tonality;
		    
		END IF;


		IF ext_measure_id = '1' THEN ext_movement := ext_movement + 1; END IF;

		ext_notes := (SELECT XPATH('//measure/note', ext_measures[j]));		

		FOR k IN 1 .. ARRAY_LENGTH(ext_notes, 1) LOOP

		    ext_pitch := LOWER((SELECT XPATH('//pitch/step/text()', ext_notes[k]))[1]::TEXT);
		    ext_accidental := LOWER((SELECT XPATH('//accidental/text()', ext_notes[k]))[1]::TEXT);

		    IF ext_accidental <> 'natural' AND ext_accidental <> '' THEN 
		    
			    IF current_tonality = 'aminor' OR current_tonality = 'cmajor' THEN

				
			    END IF;
			    
			    IF current_tonality = 'gmajor' OR current_tonality = 'eminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs';  END IF;

			    END IF;
			    
			    IF current_tonality = 'dmajor' OR current_tonality = 'bminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cs'; END IF;

			    END IF;

			    IF current_tonality = 'amajor' OR current_tonality = 'fminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cs'; END IF;
				IF ext_pitch = 'g' THEN ext_pitch = 'gs'; END IF;

			    END IF;

			    IF current_tonality = 'emajor' OR current_tonality = 'cminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cs'; END IF;
				IF ext_pitch = 'g' THEN ext_pitch = 'gs'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'ds'; END IF;

			    END IF;

			    IF current_tonality = 'bmajor' OR current_tonality = 'gminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cs'; END IF;
				IF ext_pitch = 'g' THEN ext_pitch = 'gs'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'ds'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'as'; END IF;

			    END IF;
			    
			    IF current_tonality = 'fsmajor' OR current_tonality = 'dsminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cs'; END IF;
				IF ext_pitch = 'g' THEN ext_pitch = 'gs'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'ds'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'as'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'es'; END IF;

			    END IF;
			    
			    IF current_tonality = 'csmajor' OR current_tonality = 'asminor' THEN

				IF ext_pitch = 'f' THEN ext_pitch = 'fs'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cs'; END IF;
				IF ext_pitch = 'g' THEN ext_pitch = 'gs'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'ds'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'as'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'es'; END IF;
				IF ext_pitch = 'b' THEN ext_pitch = 'bs'; END IF;
				
			    END IF;
			    
			    IF current_tonality = 'fmajor' OR current_tonality = 'dminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				
			    END IF;
			    
			    IF current_tonality = 'bbmajor' OR current_tonality = 'gminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'eb'; END IF;
				
			    END IF;

			    IF current_tonality = 'ebmajor' OR current_tonality = 'cminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'eb'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'ab'; END IF;
				
			    END IF;

			    IF current_tonality = 'abmajor' OR current_tonality = 'fminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'eb'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'ab'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'db'; END IF;		        
				
			    END IF;	

			    IF current_tonality = 'dbmajor' OR current_tonality = 'bbminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'eb'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'ab'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'db'; END IF;		        
				IF ext_pitch = 'g' THEN ext_pitch = 'gb'; END IF;
				
			    END IF;	

			    IF current_tonality = 'gbmajor' OR current_tonality = 'ebminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'eb'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'ab'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'db'; END IF;		        
				IF ext_pitch = 'g' THEN ext_pitch = 'gb'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cb'; END IF;
				
			    END IF;	

			    IF current_tonality = 'cbmajor' OR current_tonality = 'abminor' THEN

				IF ext_pitch = 'b' THEN ext_pitch = 'bb'; END IF;
				IF ext_pitch = 'e' THEN ext_pitch = 'eb'; END IF;
				IF ext_pitch = 'a' THEN ext_pitch = 'ab'; END IF;
				IF ext_pitch = 'd' THEN ext_pitch = 'db'; END IF;		        
				IF ext_pitch = 'g' THEN ext_pitch = 'gb'; END IF;
				IF ext_pitch = 'c' THEN ext_pitch = 'cb'; END IF;
				IF ext_pitch = 'f' THEN ext_pitch = 'fb'; END IF;
				
			    END IF;		
		    END IF;
		      	    	    		    
		    ext_octave := (SELECT XPATH('//pitch/octave/text()', ext_notes[k]))[1];
		    ext_duration := (SELECT XPATH('//type/text()', ext_notes[k]))[1];
		    ext_staff := (SELECT XPATH('//staff/text()', ext_notes[k]))[1];
		    ext_voice := (SELECT XPATH('//voice/text()', ext_notes[k]))[1];
		    ext_instrument := (SELECT XPATH('//instrument/@id', ext_notes[k]))[1];
		    ext_accidental := (SELECT XPATH('//accidental/text()', ext_notes[k]))[1];

		    IF (SELECT XPATH('//chord', ext_notes[k]))[1] IS NOT NULL THEN 
			
		        ext_is_chord := TRUE; 

		    ELSE

		        noteset := (SELECT nextval('seq_noteset'));
		        
		    END IF;

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
			ext_pitch := LOWER((SELECT XPATH('//pitch/step/text()', ext_notes[k]))[1]::TEXT);
			IF LOWER(ext_accidental) = 'flat' THEN ext_accidental := 'b'; END IF;
			IF LOWER(ext_accidental) = 'sharp' THEN ext_accidental := 's'; END IF;
			IF LOWER(ext_accidental) = 'double-sharp' THEN ext_accidental := 'ss'; END IF;
			IF LOWER(ext_accidental) = 'flat-flat' THEN ext_accidental := 'bb'; END IF;
			IF LOWER(ext_accidental) = 'natural' THEN ext_accidental := ''; END IF;
		    ELSE 
		        ext_accidental := '';
		    END IF;
		    INSERT INTO wmss.wmss_notes (score_id, movement_id, measure, octave, pitch, duration, voice, instrument,staff,chord,noteset_id,document_type_id) 
		    VALUES (document_id, ext_movement, ext_measure_id, ext_octave, ext_pitch||ext_accidental, ext_duration, ext_voice::INTEGER, ext_instrument, ext_staff::INTEGER,ext_is_chord,noteset,'musicxml');

		    ext_is_chord := FALSE;
		    --raise notice '[Part: % Movement: % Measure: %] pitch: % | octave: % | duration: % | staff: % | voice: % | instrument: %',parts[i], movement, measure_id, pitch, octave,duration,staff,voice,instrument;
		END LOOP;

	    END LOOP;

	    ext_movement := 0;
		
	END LOOP;
	
	RAISE NOTICE '	Creating note sequences. This might take a while ...';

	--UPDATE wmss_notes note 
	--SET next = (SELECT note_id 
	--	    FROM wmss_notes 
	--	    WHERE note_id > note.note_id AND
	--		  staff = note.staff AND
	--		  voice = note.voice AND
	--		  score_id = note.score_id AND
	--		  movement_id = note.movement_id AND
	--		  instrument = note.instrument AND 
	--		  chord IS FALSE
	--	    ORDER BY note_id
	--	    LIMIT 1)
	--WHERE score_id = document_id;


	UPDATE wmss.wmss_notes note 
	SET next_noteset_id = (SELECT noteset_id 
			       FROM wmss.wmss_notes 
			       WHERE noteset_id > note.noteset_id AND
				  staff = note.staff AND
				  voice = note.voice AND
				  score_id = note.score_id AND
				  movement_id = note.movement_id AND
				  instrument = note.instrument
			       ORDER BY noteset_id
			       LIMIT 1)
	WHERE score_id = document_id;

	RETURN 'Notes extraction for [' || document_id || '] finished.' ;

END;
$BODY$
LANGUAGE plpgsql VOLATILE COST 100;
ALTER FUNCTION wmss.wmss_extract_musicxml_notes(VARCHAR) OWNER TO postgres;
















DROP TABLE IF EXISTS wmss_notes_p1;
DROP TABLE IF EXISTS wmss_notes_p2;
DROP TABLE IF EXISTS wmss_notes_p3;
DROP TABLE IF EXISTS wmss_notes_p4;
DROP TABLE IF EXISTS wmss_notes_p5;
DROP TABLE IF EXISTS wmss_notes;

CREATE TABLE wmss_notes (
noteset_id BIGINT,
next_noteset_id BIGINT,
score_id VARCHAR,
document_type_id VARCHAR,
movement_id INTEGER,
instrument VARCHAR,
measure VARCHAR,
pitch VARCHAR,
octave VARCHAR,
duration VARCHAR,
voice INTEGER,
staff INTEGER,
chord BOOLEAN,
FOREIGN KEY (score_id,document_type_id) REFERENCES wmss.wmss_document (score_id, document_type_id)
);




SELECT wmss.wmss_extract_musicxml_notes(score_id) FROM wmss.wmss_document WHERE document_type_id = 'musicxml'-- AND score_id = '4307727' ;-- limit 10;


