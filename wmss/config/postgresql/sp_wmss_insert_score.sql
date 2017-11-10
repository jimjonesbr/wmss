-- Author: Jones
-- Parameter: 1) Path to the XML file
--	      2) File type (MEI or MusicXML)
--	      3) Group where the scores should be added. Default: 0 
-- Comments: This function loads either MEI or MusicXML files and stores in the database according to the WMSS data model.

CREATE OR REPLACE FUNCTION wmss.wmss_import_score(document_id VARCHAR, score_path VARCHAR, score_type VARCHAR, group_id INTEGER) RETURNS CHARACTER VARYING AS $BODY$

DECLARE i RECORD;
DECLARE j RECORD;
DECLARE main_id VARCHAR DEFAULT '';
DECLARE role_id INTEGER DEFAULT 0;
DECLARE score_title VARCHAR DEFAULT '';
DECLARE tonality_note VARCHAR DEFAULT '';
DECLARE tonality_mode VARCHAR DEFAULT '';
DECLARE score_date_max TEXT;
DECLARE score_date_min TEXT;
DECLARE score_date_text VARCHAR DEFAULT 0;
DECLARE isNumeric BOOLEAN DEFAULT FALSE;
DECLARE exist VARCHAR;

DECLARE person_name_array VARCHAR[];
DECLARE person_uri_array VARCHAR[];
DECLARE person_codedval_array VARCHAR[];
DECLARE person_authority_array VARCHAR[];
DECLARE person_role_array VARCHAR[];

DECLARE movement_id_array VARCHAR[];
DECLARE movement_peformance_medium_array VARCHAR[];
DECLARE movement_description VARCHAR;
DECLARE remove VARCHAR[] = ARRAY[' III',' II',' I',' IV',' Solo','Solo ',' 1',' 2',' 3',' 4',' 5',' 6',' 7',' 8','(1)','(2)','(3)','(4)','(5)','(6)','(7)','(8)' ];
DECLARE instrument_name VARCHAR;
DECLARE corrected_instrument_name VARCHAR;
DECLARE tempo VARCHAR DEFAULT '';
DECLARE solo BOOLEAN DEFAULT FALSE;
DECLARE solo_performance_medien VARCHAR[];

DECLARE instrument VARCHAR DEFAULT '';
DECLARE score_file XML;

--
DECLARE score_part XML[];
DECLARE score_movements XML[];
DECLARE array_persons TEXT[];
DECLARE composer TEXT;
DECLARE lyricist TEXT;
DECLARE encoder TEXT;
DECLARE arranger TEXT;
DECLARE movement TEXT;
DECLARE instrument_display_name TEXT;
DECLARE instrument_file_id TEXT;

BEGIN
		
	RAISE NOTICE 'Inserting % file "%" ... ',UPPER(score_type),score_path;   	
	
	score_file := wmss.wmss_loadxml(score_path);

	
	IF UPPER(score_type) = 'MUSICXML' THEN

	    score_title := (SELECT XPATH('//work-title/text()', score_file))[1]::TEXT;
	    score_part := (SELECT XPATH('//score-part', score_file));
	    composer := TRIM((SELECT XPATH('//creator[@type="composer"]/text()', score_file))[1]::TEXT);
	    lyricist := TRIM((SELECT XPATH('//creator[@type="lyricist"]/text()', score_file))[1]::TEXT);
	    arranger := TRIM((SELECT XPATH('//creator[@type="arranger"]/text()', score_file))[1]::TEXT);
	    --encoder := TRIM((SELECT XPATH('//encoder/text()', score_file))[1]::TEXT);
	    encoder := 'Jim Jones';

	    tonality_note := 'unspecified';
	    tonality_mode := 'unspecified';
	    score_date_min := '0001-01-01';
	    score_date_max := '0001-01-01';
	    score_date_text := '';


	    IF document_id = '' THEN 
	        main_id := (SELECT nextval('wmss.seq_scores')); 
	    ELSE
	        main_id := document_id;
	    END IF;


	--delete from wmss_score_persons where score_id = main_id;
	--delete from wmss_persons;
	--delete from wmss_document where score_id = main_id;
	--delete from wmss_scores where score_id = main_id;
	
	    INSERT INTO wmss.wmss_scores (score_id, score_name, collection_id, score_tonality_note, score_tonality_mode, score_creation_date_min, score_creation_date_max) 
            VALUES (main_id, score_title, group_id, tonality_note, tonality_mode, TO_DATE(score_date_min,'yyyy-mm-dd'), TO_DATE(score_date_max,'yyyy-mm-dd'));

	    INSERT INTO wmss.wmss_document (score_id,score_document,document_type_id) VALUES (main_id, score_file,'musicxml');
	    

	    array_persons := REGEXP_SPLIT_TO_ARRAY(composer,',');

	    IF ARRAY_LENGTH(array_persons,1) IS NOT NULL THEN

	        FOR i IN 1 .. ARRAY_UPPER(array_persons, 1) LOOP

		    composer := TRIM(regexp_replace(array_persons[i]::TEXT,E'[\\n\\r\\t]+', '', 'g'));		
		    exist := (SELECT person_id FROM wmss.wmss_persons WHERE person_name = composer);

		    IF exist IS NULL THEN 
			
			INSERT INTO wmss.wmss_persons (person_name, person_authority, person_uri, person_codedval) 
			VALUES (composer,'', '', '');
			
		    END IF;

		    INSERT INTO wmss.wmss_score_persons (score_id,person_id,role_id) VALUES (main_id,(SELECT DISTINCT person_id FROM wmss.wmss_persons WHERE person_name = composer),1);

	        END LOOP;

	    END IF;


	    -- Lyricists

	    array_persons := REGEXP_SPLIT_TO_ARRAY(lyricist,',');

	    IF ARRAY_LENGTH(array_persons,1) IS NOT NULL THEN

	        FOR i IN 1 .. ARRAY_UPPER(array_persons, 1) LOOP
	            
		    lyricist := TRIM(regexp_replace(array_persons[i]::TEXT,E'[\\n\\r\\t]+', '', 'g'));		
		    exist := (SELECT person_id FROM wmss.wmss_persons WHERE person_name = lyricist);

		    IF exist IS NULL THEN 
			
			INSERT INTO wmss.wmss_persons (person_name, person_authority, person_uri, person_codedval) 
			VALUES (lyricist,'', '', '');
			
		    END IF;
		    RAISE NOTICE '    Adding lyricist: %',lyricist;
		    INSERT INTO wmss.wmss_score_persons (score_id,person_id,role_id) VALUES (main_id,(SELECT DISTINCT person_id FROM wmss.wmss_persons WHERE person_name = lyricist),7);

	        END LOOP;

	    END IF;



	    -- Arrangers

	    array_persons := REGEXP_SPLIT_TO_ARRAY(arranger,',');

	    IF ARRAY_LENGTH(array_persons,1) IS NOT NULL THEN

	        FOR i IN 1 .. ARRAY_UPPER(array_persons, 1) LOOP
	            
		    arranger := TRIM(regexp_replace(array_persons[i]::TEXT,E'[\\n\\r\\t]+', '', 'g'));		
		    exist := (SELECT person_id FROM wmss.wmss_persons WHERE person_name = arranger);

		    IF exist IS NULL THEN 
			
			INSERT INTO wmss.wmss_persons (person_name, person_authority, person_uri, person_codedval) 
			VALUES (arranger,'', '', '');
			
		    END IF;
		    RAISE NOTICE '    Adding arranger: %',arranger;
		    INSERT INTO wmss.wmss_score_persons (score_id,person_id,role_id) VALUES (main_id,(SELECT DISTINCT person_id FROM wmss.wmss_persons WHERE person_name = arranger),2);

	        END LOOP;

	    END IF;


	    -- Encoders

	    array_persons := REGEXP_SPLIT_TO_ARRAY(encoder,',');

	    IF ARRAY_LENGTH(array_persons,1) IS NOT NULL THEN

	        FOR i IN 1 .. ARRAY_UPPER(array_persons, 1) LOOP
	            
		    encoder := TRIM(regexp_replace(array_persons[i]::TEXT,E'[\\n\\r\\t]+', '', 'g'));		
		    exist := (SELECT person_id FROM wmss.wmss_persons WHERE person_name = encoder);

		    IF exist IS NULL THEN 
			
			INSERT INTO wmss.wmss_persons (person_name, person_authority, person_uri, person_codedval) 
			VALUES (encoder,'', '', '');
			
		    END IF;
		    RAISE NOTICE '    Adding encoder: %',encoder;
		    INSERT INTO wmss.wmss_score_persons (score_id,person_id,role_id) VALUES (main_id,(SELECT DISTINCT person_id FROM wmss.wmss_persons WHERE person_name = encoder),3);

	        END LOOP;

	    END IF;






	    score_movements := (SELECT XPATH('//part[@id="P1"]/measure[@number="1"]', score_file));

	    FOR i IN 1 .. ARRAY_UPPER(score_movements, 1) LOOP


	        movement := TRIM((SELECT XPATH('//direction/direction-type/words/text()', score_movements[i]))[1]::TEXT);
	        IF movement IS NULL THEN movement = 'Unspecified'; END IF;
		RAISE NOTICE 'Movement: % (%)',i,movement;
		
	        INSERT INTO wmss.wmss_score_movements (movement_id, score_id, movement_tempo, score_movement_description) 
	        VALUES (i, main_id, '', movement);




	        FOR j IN 1 .. ARRAY_UPPER(score_part, 1) LOOP

   	            IF ARRAY_LENGTH(score_part,1) > 0 THEN 

	                RAISE NOTICE '    Inserting instrument for movement %: %',i, (SELECT XPATH('//instrument-sound/text()', score_part[j]));
			
			instrument_name := (SELECT (XPATH('//instrument-name/text()', score_part[j]))[1]::TEXT);
			instrument_display_name := (SELECT (XPATH('//part-name-display/display-text/text()', score_part[j]))[1]::TEXT);
			instrument_file_id := (SELECT (XPATH('//score-instrument/@id', score_part[j]))[1]::TEXT);

			IF instrument_display_name IS NULL THEN instrument_display_name := instrument_name; END IF;
			
		        IF (SELECT (XPATH('//instrument-sound', score_part[j]))[1]) IS NULL THEN
			    
			    corrected_instrument_name := instrument_name;
    
			    RAISE NOTICE '        Instrument name: %',instrument_display_name;

			    FOR k IN 1 .. ARRAY_UPPER(remove, 1) LOOP
				
			        corrected_instrument_name := REPLACE(corrected_instrument_name,remove[k],'');
				
			    END LOOP;

			    instrument := (SELECT performance_medium_id 
				           FROM wmss.wmss_performance_medium 
				           WHERE LOWER(TRIM(performance_medium_description)) = LOWER(TRIM(corrected_instrument_name)) 
				           ORDER BY performance_medium_description LIMIT 1);

			ELSE

			    instrument := (SELECT (XPATH('//instrument-sound/text()', score_part[j]))[1]::TEXT);
			    
		        END IF;
		   
			IF instrument IS NULL THEN instrument = 'unspecified.unspecified'; END IF;
			IF (SELECT (XPATH('//solo', score_part[j]))[1]) IS NOT NULL THEN solo := TRUE; END IF;
			
			INSERT INTO wmss.wmss_movement_performance_medium (
				movement_id, 					 					
				file_performance_medium_id,
				movement_performance_medium_description,
				performance_medium_id,
				score_id,
				movement_performance_medium_solo) 
			VALUES (
				i,
				instrument_file_id,
				instrument_display_name,	
				instrument,
				main_id,
				solo);

			solo := FALSE;
	            END IF;

	        END LOOP;


	    END LOOP;
	    

	END IF;
	








	
	IF UPPER(score_type) = 'MEI' THEN

		score_title := (SELECT XPATH('//mei:titleStmt/mei:title/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT;
		movement_id_array := (SELECT XPATH('//mei:work/mei:titleStmt/mei:title/@n', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']])::TEXT[]);
		tonality_note := (SELECT XPATH('//mei:work/mei:key/@pname', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT;
		tonality_mode := (SELECT XPATH('//mei:work/mei:key/@mode', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT;

		score_date_min := (SELECT (XPATH('//mei:creation/mei:date/@notbefore', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT)::INTEGER;
		score_date_max := (SELECT (XPATH('//mei:creation/mei:date/@notafter', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT)::INTEGER;
		score_date_text := (SELECT (XPATH('//mei:creation/mei:date/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);

		
		IF score_date_min IS NULL OR score_date_max IS NULL THEN

			isNumeric := (SELECT  score_date_text ~ '^[0-9]+$');
			
			IF isNumeric AND score_date_min IS NULL AND score_date_max IS NULL THEN

				score_date_min := score_date_text::INTEGER;
				score_date_max := score_date_text::INTEGER;
			ELSE

				RAISE NOTICE 'Invalid year for creation date -> %. Value ignored!', score_date_text;
				
			END IF;

			IF score_date_min IS NULL AND score_date_max IS NOT NULL THEN

				score_date_min := score_date_max;				
				
			END IF;

			IF score_date_min IS NOT NULL AND score_date_max IS NULL THEN

				score_date_max := score_date_min;				
				
			END IF;

		END IF;

		IF ARRAY_LENGTH(movement_id_array, 1) IS NULL THEN movement_id_array = ARRAY['1']; END IF;
		
		IF document_id = '' THEN 
			main_id := (SELECT nextval('wmss.seq_scores')); 
		ELSE
			main_id := document_id;
		END IF;
				
		INSERT INTO wmss.wmss_scores (score_id, score_name, collection_id, score_tonality_note, score_tonality_mode, score_creation_date_min, score_creation_date_max) 
                VALUES (main_id, score_title, group_id, tonality_note, tonality_mode, TO_DATE(score_date_min,'yyyy-mm-dd'), TO_DATE(score_date_max,'yyyy-mm-dd'));


		INSERT INTO wmss.wmss_document (score_id,score_document,document_type_id) VALUES (main_id, score_file,'mei');
		

		-- Parsing persons info (composers, arrangers, lyricists, etc..)
		person_uri_array := (SELECT (XPATH('//mei:source/mei:titleStmt/mei:respStmt/mei:persName/@authURI', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);		
		person_name_array := (SELECT (XPATH('//mei:source/mei:titleStmt/mei:respStmt/mei:persName/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);
		person_codedval_array := (SELECT (XPATH('//mei:source/mei:titleStmt/mei:respStmt/mei:persName/@codedval', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);
		person_authority_array := (SELECT (XPATH('//mei:source/mei:titleStmt/mei:respStmt/mei:persName/@authority', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);
		person_role_array := (SELECT (XPATH('//mei:source/mei:titleStmt/mei:respStmt/mei:persName/@role', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);

		IF ARRAY_LENGTH(person_name_array,1) > 0 THEN 

			FOR i IN 1 .. ARRAY_UPPER(person_name_array, 1) LOOP
								
				exist := (SELECT person_id FROM wmss.wmss_persons WHERE person_name = person_name_array[i]);

				IF exist IS NULL THEN 
					
					INSERT INTO wmss.wmss_persons (person_name, person_authority, person_uri, person_codedval) 
					VALUES (TRIM(person_name_array[i]),TRIM(person_authority_array[i]), TRIM(person_uri_array[i]), TRIM(person_codedval_array[i]));
					
				END IF;
				raise notice ' %: % ',TRIM(person_name_array[i]), TRIM(person_role_array[i]);
				
				IF LOWER(person_role_array[i]) = 'composer' THEN role_id = 1; END IF;
				IF LOWER(person_role_array[i]) = 'creator' THEN role_id = 1; END IF;
				IF LOWER(person_role_array[i]) = 'arranger' THEN role_id = 2; END IF;
				IF LOWER(person_role_array[i]) = 'encoder' THEN role_id = 3; END IF;				
				IF LOWER(person_role_array[i]) = 'dedicatee' THEN role_id = 4; END IF;
				IF LOWER(person_role_array[i]) = 'librettist' THEN role_id = 5; END IF;
				IF LOWER(person_role_array[i]) = 'editor' THEN role_id = 6; END IF;
				IF LOWER(person_role_array[i]) = 'lyricist' THEN role_id = 7; END IF;
				IF LOWER(person_role_array[i]) = 'translator' THEN role_id = 8; END IF;
				IF LOWER(person_role_array[i]) = 'performer' THEN role_id = 9; END IF;
				
				IF role_id = 0 THEN role_id = 99; END IF;
				INSERT INTO wmss.wmss_score_persons (score_id,person_id,role_id) 
				VALUES (main_id,(SELECT DISTINCT person_id FROM wmss.wmss_persons WHERE person_name = person_name_array[i]),role_id);
				role_id = 0;
			END LOOP;

		END IF;


		-- Parsing movements info
		solo_performance_medien := (SELECT (XPATH('//mei:perfRes[@solo="true"]/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);
		
		FOR i IN 1 .. ARRAY_UPPER(movement_id_array, 1) LOOP

			raise notice '[%] Inserting movement %', score_title, movement_id_array[i];
				
			
			IF ARRAY_LENGTH(movement_id_array, 1) = 1 THEN 
				
				movement_peformance_medium_array := (SELECT (XPATH('//mei:perfRes/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);
				
				tempo := (SELECT (XPATH('//mei:workDesc/mei:work/mei:tempo/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);
				movement_description := (SELECT (XPATH('//mei:titleStmt/mei:title/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);
			ELSE

				movement_peformance_medium_array := (SELECT (XPATH('//mei:workDesc/mei:work[@n='||movement_id_array[i]||']/mei:perfMedium/mei:perfResList/mei:perfRes/@n', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))::TEXT[]);	
				tempo := (SELECT (XPATH('//mei:workDesc/mei:work[@n='||movement_id_array[i]||']/mei:tempo/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);
				movement_description := (SELECT (XPATH('//mei:titleStmt/mei:title[@n='||movement_id_array[i]||']/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);

			END IF;
			
			INSERT INTO wmss.wmss_score_movements 
				(movement_id, 
				 score_id, 
				 movement_tempo,
				 score_movement_description				 
				 ) 
			VALUES (movement_id_array[i],
				main_id, 
				tempo, 
				movement_description);
					
			
			FOR j IN 1 .. ARRAY_UPPER(movement_peformance_medium_array, 1) LOOP

				IF ARRAY_LENGTH(movement_id_array, 1) > 1 THEN 

					instrument_name := (SELECT (XPATH('//mei:workDesc/mei:work[@n='||movement_id_array[i]||']/mei:perfMedium/mei:perfResList/mei:perfRes[@n='||movement_peformance_medium_array[j]||']/text()', 
									   score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);
					instrument := (SELECT (XPATH('//mei:workDesc/mei:work[@n='||movement_id_array[i]||']/mei:perfMedium/mei:perfResList/mei:perfRes[@n='||movement_peformance_medium_array[j]||']/@codedval', 
						score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[1]::TEXT);
									
				ELSE

					instrument_name := (SELECT (XPATH('//mei:perfRes/text()', score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[j]::TEXT);					
					instrument := (SELECT (XPATH('//mei:perfRes/@codedval',   score_file, ARRAY[ARRAY['mei', 'http://www.music-encoding.org/ns/mei']]))[j]::TEXT);

					
				END IF;


				IF ARRAY_LENGTH(solo_performance_medien, 1) > 0 THEN 

					FOR j IN 1 .. ARRAY_UPPER(solo_performance_medien, 1) LOOP
						
						IF solo_performance_medien[j] = instrument_name THEN
							solo := TRUE;
						END IF;

					END LOOP;

				END IF;


				RAISE NOTICE '	Inserting performance medium "%" for movement "%" -> % (solo %)',movement_peformance_medium_array[j],movement_id_array[i],instrument_name,solo;
				
				corrected_instrument_name := instrument_name;
				FOR k IN 1 .. ARRAY_UPPER(remove, 1) LOOP
				
				    corrected_instrument_name := REPLACE(corrected_instrument_name,remove[k],'');
				
				END LOOP;
												
				instrument := (SELECT performance_medium_id FROM wmss.wmss_performance_medium WHERE LOWER(TRIM(performance_medium_description)) = LOWER(TRIM(corrected_instrument_name)) ORDER BY performance_medium_description LIMIT 1);
				
				IF instrument IS NULL THEN instrument = 'unspecified.unspecified'; END IF;
				INSERT INTO wmss.wmss_movement_performance_medium (
					movement_id, 					 					
					--local_performance_medium_id,
					movement_performance_medium_description,
					performance_medium_id,
					score_id,
					movement_performance_medium_solo) 
				VALUES (movement_id_array[i],
					--movement_peformance_medium_array[j],
					instrument_name,	
					instrument,
					main_id,
					solo);
 
				solo := FALSE;

			END LOOP;

		END LOOP;
		

	END IF;

	RETURN 'Import finished.';

END;
$BODY$
LANGUAGE plpgsql VOLATILE COST 100;
ALTER FUNCTION wmss.wmss_import_score(VARCHAR,VARCHAR,VARCHAR,INTEGER) OWNER TO postgres;


--SELECT public.wmss_insert_score('/home/jones/Brahms_StringQuartet_Op51_No1.mei','mei',1);
--SELECT public.wmss_insert_score('/home/jones/Liszt_Four_little_pieces.mei','mei',1);
--SELECT public.wmss_insert_score('/home/jones/Hummel_Concerto_for_trumpet.mei','mei',1);

--SELECT public.wmss_insert_score('/home/jones/Hummel_Concerto_for_trumpet.mei','mei',1);

--SELECT * from wmss_scores;
--SELECT  --DISTINCT
	--wmss_scores.score_id	AS id,
	--wmss_scores.score_name	AS score_name,
--	*
--FROM wmss_scores
--JOIN wmss_score_movements ON wmss_scores.score_id = wmss_score_movements.score_id 
--JOIN wmss_movement_performance_medium ON wmss_score_movements.movement_id = wmss_movement_performance_medium.movement_id AND wmss_movement_performance_medium.score_id = wmss_scores.score_id 
--JOIN wmss_performance_medium ON wmss_movement_performance_medium.performance_medium_id = wmss_performance_medium.performance_medium_id
--JOIN wmss_score_persons ON wmss_score_persons.score_id = wmss_scores.score_id 
--JOIN wmss_persons ON wmss_persons.person_id = wmss_score_persons.person_id 
--JOIN wmss_roles ON wmss_roles.role_id = wmss_score_persons.role_id
--JOIN wmss_groups ON wmss_groups.group_id = wmss_scores.group_id
--JOIN wmss_document ON wmss_document.score_id = wmss_scores.score_id
--JOIN wmss_document_type ON wmss_document_type.document_type_id = wmss_document.document_type_id
--limit 100

--SELECT * FROM wmss_score_persons order by person_id;
--SELECT * FROM wmss_persons where person_id = 6
--SELECT * FROM wmss_score_movements WHERE score_id = 37
--SELECT * FROM wmss_movement_performance_medium WHERE movement_performance_medium_solo = TRUE; --WHERE score_id = 37 and movement_id = '1'
--

--SELECT public.wmss_insert_score('/home/jones/Gluck_CheFaroSenzaEuridice.mei','mei',1);


--SELECT * FROM wmss_score_persons 
--join wmss_persons on wmss_persons.person_id = wmss_score_persons.person_id 
--join wmss_roles ON wmss_roles.role_id = wmss_score_persons.role_id
--WHERE score_id = 28


-- SELECT public.wmss_insert_score('4276911','/home/jones/git/wmss/wmss/data/musicxml/4287515@DieHarmoniederSphaeren.xml','musicxml',2);
--SELECT public.wmss_insert_score('825151','/home/jones/git/wmss/wmss/data/musicxml/825151@TroisDuosConcertans.xml','musicxml',2);








