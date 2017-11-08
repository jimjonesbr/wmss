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







-- MEI Examples
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Aguado_Walzer_G-major.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Ahle_Jesu_meines_Herzens_Freud.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Altenburg_concerto_C_major.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Altenburg_Ein_feste_Burg.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Altenburg_Macht_auf_die_Tor.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_BrandenburgConcert_No.4_II.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_BrandenburgConcert_No.4_I.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Ein_festeBurg.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Herzliebster_Jesu.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Hilf_Herr_Jesu.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach-J-C_Fughette_Gmaj.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach-J-C_Fughette_No2.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Musikalisches_Opfer_Trio.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Wie_bist_du_Seele.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Beethoven_Hymn_to_joy.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Beethoven_op.18.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Beethoven_Song_Op98.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Berlioz_Symphony_op25.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Borodin_StringTrio_g.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Brahms_StringQuartet_Op51_No1.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Brahms_WieMelodienZiehtEsMir.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Chopin_Etude_op.10_no.9.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Chopin_Mazurka.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Czerny_op603_6.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Czerny_StringQuartet_d.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Debussy_GolliwoggsCakewalk.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Debussy_Mandoline.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Echigo-Jishi.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Gluck_CheFaroSenzaEuridice.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Grieg_op.43_butterfly.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Grieg_op.43_little_bird.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Handel_Arie.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Handel_concerto_grosso.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Handel_Messias.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Haydn_StringQuartet_Op1_No1.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Hopkins_GatherRoundTheChristmasTree.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Hummel_Concerto_for_trumpet.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Hummel_op.67_No.11.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Ives_TheCage.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Joplin_Elite_Syncopations.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Joplin_Maple_leaf_Rag.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/JSB_BWV1047_1.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/JSB_BWV1047_2.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/JSB_BWV1047_3.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Kirnberger_FugeInEFlatMajor.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Krebs_Trio_Eb_2.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Krebs_Trio_in_c.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Liszt_Four_little_pieces.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Lully_LaDescenteDeMars.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Mahler_Song.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Marney_BreakThouTheBreadOfLife.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/McFerrin_Dont_worry.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_DasVeilchen.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_Fuge_G_minor.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_Quintett_2013.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_Quintett.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Pachelbel_Canon_in_D.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Parker-Gillespie_ShawNuff.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Ponchielli_LarrivoDelRe.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Praetorius_PuerNobisNascitur.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Ravel_Le_tombeau.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Rimsky-Korsakov_StringQuartet_B.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Saint-Saens_LeCarnevalDesAnimmaux.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Scarlatti_Sonata_in_C_major.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schubert_Erlkönig.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schubert_Lindenbaum.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schumann_Landmann.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schumann_Op.41.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schumann_Song_Op48-1.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schutz_DomineDeus.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Schutz_Jubilate_Deo.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Telemann_Concert.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Telemann_Suite.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Vivaldi_Op8_No.2.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Weber_Arie.mei','mei',1);
SELECT wmss.wmss_import_score('','/home/jones/git/wmss/wmss/data/mei/Webern_VariationsforPiano.mei','mei',1);


-- ULB Sammlung


SELECT wmss.wmss_import_score('3e724fa4-f67e-4dff-94d6-a01b78d73049','/home/jones/git/wmss/wmss/data/musicxml/3e724fa4-f67e-4dff-94d6-a01b78d73049@SinfoniaA-DurfurStreicherundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('4a12fece-a4ac-4654-8407-2e32be8d3e56','/home/jones/git/wmss/wmss/data/musicxml/4a12fece-a4ac-4654-8407-2e32be8d3e56@Sonatee-MollfürTraversflöteundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('5c3047f9-f8bc-46eb-8073-0dc3ffb28d30','/home/jones/git/wmss/wmss/data/musicxml/5c3047f9-f8bc-46eb-8073-0dc3ffb28d30@Sedunamortiranno.xml','musicxml',3);
SELECT wmss.wmss_import_score('5e61ff05-7ceb-4e5c-b81e-19f8105f4a53','/home/jones/git/wmss/wmss/data/musicxml/5e61ff05-7ceb-4e5c-b81e-19f8105f4a53@BetrübtesHerz,zerbrich.xml','musicxml',3);
SELECT wmss.wmss_import_score('6be19ce2-fbf2-442f-af37-08b0eb487d45','/home/jones/git/wmss/wmss/data/musicxml/6be19ce2-fbf2-442f-af37-08b0eb487d45@Aquellleggiadrovolto.xml','musicxml',3);
SELECT wmss.wmss_import_score('20b0dcec-ff6a-43d2-8bfe-9e077937a1cf','/home/jones/git/wmss/wmss/data/musicxml/20b0dcec-ff6a-43d2-8bfe-9e077937a1cf@SonateD-Durfür2TraversflötenundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('899c8c9e-dd50-4041-8706-8c0479eb5de5','/home/jones/git/wmss/wmss/data/musicxml/899c8c9e-dd50-4041-8706-8c0479eb5de5@Confusasmarritaspiegartivorrei.xml','musicxml',3);
SELECT wmss.wmss_import_score('01924ae1-3991-464f-97f0-b6498f973560','/home/jones/git/wmss/wmss/data/musicxml/01924ae1-3991-464f-97f0-b6498f973560@SinfoniaB-DurfürStreicherundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('825151','/home/jones/git/wmss/wmss/data/musicxml/825151@TroisDuosConcertans.xml','musicxml',2);
SELECT wmss.wmss_import_score('948617','/home/jones/git/wmss/wmss/data/musicxml/948617@nouvellepolonaisepourlepianoforte.xml','musicxml',2);
SELECT wmss.wmss_import_score('1009591','/home/jones/git/wmss/wmss/data/musicxml/1009591@kleinerfeierabend.xml','musicxml',2);
SELECT wmss.wmss_import_score('1118465','/home/jones/git/wmss/wmss/data/musicxml/1118465@AchtharmloseLiederfürUngelehrte.xml','musicxml',2);
SELECT wmss.wmss_import_score('1639763','/home/jones/git/wmss/wmss/data/musicxml/1639763@AlteundneueVolksliederfürMännerchor.xml','musicxml',2);
SELECT wmss.wmss_import_score('1642539','/home/jones/git/wmss/wmss/data/musicxml/1642539@Amgeburtstagedeskönigs.xml','musicxml',2);
SELECT wmss.wmss_import_score('1883542','/home/jones/git/wmss/wmss/data/musicxml/1883542@18MärschefürTrommelundFlöte.xml','musicxml',2);
SELECT wmss.wmss_import_score('2398460','/home/jones/git/wmss/wmss/data/musicxml/2398460@AchtzehnneueLiederfürGemischtenChor.xml','musicxml',2);
SELECT wmss.wmss_import_score('3079496','/home/jones/git/wmss/wmss/data/musicxml/3079496@AmGrabederMutter.xml','musicxml',2);
SELECT wmss.wmss_import_score('3079600','/home/jones/git/wmss/wmss/data/musicxml/3079600@AchGottwemsollichsklagen.xml','musicxml',2);
SELECT wmss.wmss_import_score('3079686','/home/jones/git/wmss/wmss/data/musicxml/3079686@DasalteLied.xml','musicxml',2);
SELECT wmss.wmss_import_score('3079699','/home/jones/git/wmss/wmss/data/musicxml/3079699@AmRhein.xml','musicxml',2);
SELECT wmss.wmss_import_score('3098742','/home/jones/git/wmss/wmss/data/musicxml/3098742@25stuckefürorgel.xml','musicxml',2);
SELECT wmss.wmss_import_score('3368467','/home/jones/git/wmss/wmss/data/musicxml/3368467@AllesnurumsGeld.xml','musicxml',2);
SELECT wmss.wmss_import_score('3368606','/home/jones/git/wmss/wmss/data/musicxml/3368606@AufnachChina.xml','musicxml',2);
SELECT wmss.wmss_import_score('3530337','/home/jones/git/wmss/wmss/data/musicxml/3530337@AufderAlm.xml','musicxml',2);
SELECT wmss.wmss_import_score('3886826','/home/jones/git/wmss/wmss/data/musicxml/3886826@AlterMünsterischerCantusTriumphalis.xml','musicxml',2);
SELECT wmss.wmss_import_score('4272171','/home/jones/git/wmss/wmss/data/musicxml/4272171@PremièreouverturedeIoperaDonMendoze.xml','musicxml',2);
SELECT wmss.wmss_import_score('4272244','/home/jones/git/wmss/wmss/data/musicxml/4272244@LaCachucha.xml','musicxml',2);
SELECT wmss.wmss_import_score('4276689','/home/jones/git/wmss/wmss/data/musicxml/4276689@OverturadellOperaScipio.xml','musicxml',2);
SELECT wmss.wmss_import_score('4276790','/home/jones/git/wmss/wmss/data/musicxml/4276790@LeBalmasque.xml','musicxml',2);
SELECT wmss.wmss_import_score('4276911','/home/jones/git/wmss/wmss/data/musicxml/4276911@Kinder-Sinfonie.xml','musicxml',2);
SELECT wmss.wmss_import_score('4279917','/home/jones/git/wmss/wmss/data/musicxml/4279917@AndanteetPolacca.xml','musicxml',2);
SELECT wmss.wmss_import_score('4280245','/home/jones/git/wmss/wmss/data/musicxml/4280245@DerGrafvonHabsburg.xml','musicxml',2);
SELECT wmss.wmss_import_score('4280660','/home/jones/git/wmss/wmss/data/musicxml/4280660@sehnsucht.xml','musicxml',2);
SELECT wmss.wmss_import_score('4280885','/home/jones/git/wmss/wmss/data/musicxml/4280885@TeDeumLaudamus.xml','musicxml',2);
SELECT wmss.wmss_import_score('4280969','/home/jones/git/wmss/wmss/data/musicxml/4280969@Andanteetpolonaises.xml','musicxml',2);
SELECT wmss.wmss_import_score('4281006','/home/jones/git/wmss/wmss/data/musicxml/4281006@CantabileetthemevariésuivisdunAllegretto.xml','musicxml',2);
SELECT wmss.wmss_import_score('4281042','/home/jones/git/wmss/wmss/data/musicxml/4281042@Concertinopourlevioloncelle.xml','musicxml',2);
SELECT wmss.wmss_import_score('4283787','/home/jones/git/wmss/wmss/data/musicxml/4283787@FrohwallichzumHeiligthume.xml','musicxml',2);
SELECT wmss.wmss_import_score('4287452','/home/jones/git/wmss/wmss/data/musicxml/4287452@DeuxAirsRussesVariés.xml','musicxml',2);
SELECT wmss.wmss_import_score('4287515','/home/jones/git/wmss/wmss/data/musicxml/4287515@DieHarmoniederSphaeren.xml','musicxml',2);
SELECT wmss.wmss_import_score('4307727','/home/jones/git/wmss/wmss/data/musicxml/4307727@SinfoniaAllaTurca.xml','musicxml',2);
SELECT wmss.wmss_import_score('4308235','/home/jones/git/wmss/wmss/data/musicxml/4308235@Rondeaualamodedeparis.xml','musicxml',2);
SELECT wmss.wmss_import_score('4339428','/home/jones/git/wmss/wmss/data/musicxml/4339428@Concertinosuisse.xml','musicxml',2);
SELECT wmss.wmss_import_score('4339906','/home/jones/git/wmss/wmss/data/musicxml/4339906@TroisSonatesfacilesetProgressives.xml','musicxml',2);
SELECT wmss.wmss_import_score('4339998','/home/jones/git/wmss/wmss/data/musicxml/4339998@1erconcertinopourlevioloncelle.xml','musicxml',2);
SELECT wmss.wmss_import_score('4340117','/home/jones/git/wmss/wmss/data/musicxml/4340117@LaBelleBergère.xml','musicxml',2);
SELECT wmss.wmss_import_score('4341718','/home/jones/git/wmss/wmss/data/musicxml/4341718@troisduosconcertanspourdeuxflutes.xml','musicxml',2);
SELECT wmss.wmss_import_score('4341767','/home/jones/git/wmss/wmss/data/musicxml/4341767@3duospourdeuxVioloncelles.xml','musicxml',2);
SELECT wmss.wmss_import_score('4342391','/home/jones/git/wmss/wmss/data/musicxml/4342391@LeBalmasque.xml','musicxml',2);
SELECT wmss.wmss_import_score('75266515-9803-41de-ac1b-bc2796adc12d','/home/jones/git/wmss/wmss/data/musicxml/75266515-9803-41de-ac1b-bc2796adc12d@SonateA-Durfur2ViolinenundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('a13a60c1-8170-40cb-aaf2-4805931f9465','/home/jones/git/wmss/wmss/data/musicxml/a13a60c1-8170-40cb-aaf2-4805931f9465@konzertBdurfurviolinestreicherundbassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('ad3ed640-90e3-49ce-8ce4-b3dc69c597a5','/home/jones/git/wmss/wmss/data/musicxml/ad3ed640-90e3-49ce-8ce4-b3dc69c597a5@SonateG-Durfür2ViolinenundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('bd2527e4-155e-401d-a6ba-7d1056d09b37','/home/jones/git/wmss/wmss/data/musicxml/bd2527e4-155e-401d-a6ba-7d1056d09b37@SonateA-Durfür2TraversflötenundBassocontinuo.xml','musicxml',3);
SELECT wmss.wmss_import_score('cfc09cec-206c-4a97-b3ac-b4c304080350','/home/jones/git/wmss/wmss/data/musicxml/cfc09cec-206c-4a97-b3ac-b4c304080350@sonate.xml','musicxml',3);
SELECT wmss.wmss_import_score('d7c0073f-8406-4dc0-be5c-3f267e7f5789','/home/jones/git/wmss/wmss/data/musicxml/d7c0073f-8406-4dc0-be5c-3f267e7f5789@andermond.xml','musicxml',3);
SELECT wmss.wmss_import_score('e73f74ec-6711-499f-b3eb-503ca99c6b14','/home/jones/git/wmss/wmss/data/musicxml/e73f74ec-6711-499f-b3eb-503ca99c6b14@sonate.xml','musicxml',3);
SELECT wmss.wmss_import_score('ebb4d5a6-3096-492f-934a-bc7c0e6644bf','/home/jones/git/wmss/wmss/data/musicxml/ebb4d5a6-3096-492f-934a-bc7c0e6644bf@AndenMond.xml','musicxml',3);
SELECT wmss.wmss_import_score('4339837','/home/jones/git/wmss/wmss/data/musicxml/4339837@PolonoisepourlaHarpe.xml','musicxml',2);
SELECT wmss.wmss_import_score('974543','/home/jones/git/wmss/wmss/data/musicxml/974543@Pot-pourrisurdesmélodiesdeloperaDonJuan.xml','musicxml',2);



-- WWU specific updates

UPDATE wmss.wmss_scores 
SET score_online_resource = 'https://sammlungen.ulb.uni-muenster.de/id/' || score_id 
WHERE collection_id = 2;

UPDATE wmss.wmss_scores 
SET score_online_resource = 'https://miami.uni-muenster.de/Record/' || score_id 
WHERE collection_id = 3;