-- Author: Jones
-- Comments: This script creates the WMSS data model.


DROP TABLE IF EXISTS wmss_movement_performance_medium;
DROP TABLE IF EXISTS wmss_score_persons;
DROP TABLE IF EXISTS wmss_persons;
DROP TABLE IF EXISTS wmss_roles;
DROP TABLE IF EXISTS wmss_performance_medium;
DROP TABLE IF EXISTS wmss_performance_medium_type;
DROP TABLE IF EXISTS wmss_languages;
DROP TABLE IF EXISTS wmss_document;
DROP TABLE IF EXISTS wmss_score_movements;
DROP TABLE IF EXISTS wmss_scores;
DROP TABLE IF EXISTS wmss_document_type ;
DROP TABLE IF EXISTS wmss_collections;
DROP SEQUENCE IF EXISTS seq_scores;
CREATE SEQUENCE seq_scores START WITH 1;

-- wmss_languages

CREATE TABLE wmss_languages (
language_id VARCHAR,
language_description VARCHAR,
CONSTRAINT language_id_pkey PRIMARY KEY (language_id) 
);

INSERT INTO wmss_languages (language_id,language_description) VALUES ('en','English');
INSERT INTO wmss_languages (language_id,language_description) VALUES ('de','Deutsch');
INSERT INTO wmss_languages (language_id,language_description) VALUES ('br','Potuguês');

-- wwms_performance_medium_type

CREATE TABLE wmss_performance_medium_type (
performance_medium_type_id VARCHAR,
performance_medium_type_description VARCHAR,
CONSTRAINT medium_type_pkey PRIMARY KEY (performance_medium_type_id) 
);


INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Brass','bra');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Electronic', 'ele');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Keyboard', 'key');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Percussion', 'per');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Strings, bowed', 'stb');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Strings, plucked', 'stl');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Voices', 'voi');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Woodwinds', 'wwi');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Unspecified instruments', 'uns');
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Unknown', 'unk');


-- wmss_performance_medium

CREATE TABLE wmss_performance_medium (
performance_medium_id VARCHAR,
performance_medium_type_id VARCHAR REFERENCES wmss_performance_medium_type (performance_medium_type_id),
performance_medium_description VARCHAR,
language_id VARCHAR REFERENCES wmss_languages (language_id),
CONSTRAINT medium_pkey PRIMARY KEY (performance_medium_id) 
);

 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ba','bra','Horn','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bb','bra','Trumpet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bc','bra','Cornet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bd','bra','Trombone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('be','bra','Tuba','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bf','bra','Baritone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bn','bra','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bu','bra','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('by','bra','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bz','bra','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ea','ele','Synthesizer','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('eb','ele','Tape','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ec','ele','Computer','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ed','ele','Ondes Martinot','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('en','ele','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('eu','ele','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ez','ele','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ka','key','Piano','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kb','key','Organ','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kc','key','Harpsichord','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kd','key','Clavichord','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ke','key','Continuo','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kf','key','Celeste','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kn','key','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ku','key','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ky','key','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kz','key','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pa','per','Timpani','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pb','per','Xylophone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pc','per','Marimba','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pd','per','Drum','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pn','per','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pu','per','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('py','per','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pz','per','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sa','stb','Violin','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sb','stb','Viola','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sc','stb','Violoncello','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sd','stb','Double bass','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('se','stb','Viol','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sf','stb','Viola d`amore','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sg','stb','Viola da gamba','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sn','stb','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('su','stb','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sy','stb','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sz','stb','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ta','stl','Harp','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tb','stl','Guitar','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tc','stl','Lute','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('td','stl','Mandolin','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tn','stl','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tu','stl','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ty','stl','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tz','stl','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('va','voi','Soprano','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vb','voi','Mezzo Soprano','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vc','voi','Alto','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vd','voi','Tenor','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ve','voi','Baritone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vf','voi','Bass','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vg','voi','Counter tenor','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vh','voi','High voice','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vi','voi','Medium voice','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vj','voi','Low voice','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vn','voi','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vu','voi','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vy','voi','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wa','wwi','Flute','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wb','wwi','Oboe','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wc','wwi','Clarinet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wd','wwi','Bassoon','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('we','wwi','Piccolo','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wf','wwi','English horn','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wg','wwi','Bass clarinet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wh','wwi','Recorder','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wi','wwi','Saxophone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wn','wwi','Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wu','wwi','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wy','wwi','Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wz','wwi','Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('zn','uns','Unspecified instruments','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('zu','unk','Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('oc','uns','String Orchestra','en');


-- wmss_collection

CREATE TABLE wmss_collections (
collection_id INTEGER,
collection_description VARCHAR,
CONSTRAINT collection_pkey PRIMARY KEY (collection_id) 
);

INSERT INTO wmss_collections (collection_id,collection_description) VALUES (0,'Default Collection');
INSERT INTO wmss_collections (collection_id,collection_description) VALUES (1,'MEI 3.0 Sample Collection');
INSERT INTO wmss_collections (collection_id,collection_description) VALUES (2,'ULB Digitale Sammlung');

-- wmss_scores


CREATE TABLE wmss_scores (
score_id VARCHAR,
score_name VARCHAR,
score_tonality_note VARCHAR,
score_tonality_mode VARCHAR,
score_creation_date_min INTEGER,
score_creation_date_max INTEGER,
collection_id INTEGER REFERENCES wmss_collections(collection_id),
CONSTRAINT score_pkey PRIMARY KEY (score_id) 
);


-- wmss_document_type

CREATE TABLE wmss_document_type (
document_type_id VARCHAR,
document_type_description VARCHAR,
CONSTRAINT document_type_pkey PRIMARY KEY (document_type_id) 
);

INSERT INTO wmss_document_type (document_type_id,document_type_description) VALUES ('mei','MEI - Music Encoding Initiative 3.0');
INSERT INTO wmss_document_type (document_type_id,document_type_description) VALUES ('musicxml','MusicXML');

-- wmss_document

CREATE TABLE wmss_document (
score_id VARCHAR REFERENCES wmss_scores(score_id),
score_document XML,
document_type_id VARCHAR REFERENCES wmss_document_type (document_type_id)
);

-- wmss_score_movements

CREATE TABLE wmss_score_movements (
score_id VARCHAR REFERENCES wmss_scores(score_id),
movement_id VARCHAR,
score_movement_description VARCHAR,
movement_tempo VARCHAR,
CONSTRAINT movements_pkey PRIMARY KEY (movement_id,score_id) 
);

-- wmss_score_movement_performance_medium

CREATE TABLE wmss_movement_performance_medium (
movement_id VARCHAR,
score_id VARCHAR, 
local_performance_medium_id VARCHAR,
performance_medium_id VARCHAR REFERENCES wmss_performance_medium (performance_medium_id),
movement_performance_medium_description VARCHAR,
movement_performance_medium_solo BOOLEAN,
FOREIGN KEY(movement_id, score_id) REFERENCES wmss_score_movements (movement_id, score_id)
);

-- wmss_roles

CREATE TABLE wmss_roles (
role_id INTEGER,
role_description VARCHAR,
CONSTRAINT roles_pkey PRIMARY KEY (role_id) 
);

INSERT INTO wmss_roles (role_id,role_description) VALUES (1,'Composer');
INSERT INTO wmss_roles (role_id,role_description) VALUES (2,'Arranger');
INSERT INTO wmss_roles (role_id,role_description) VALUES (3,'Encoder');
INSERT INTO wmss_roles (role_id,role_description) VALUES (4,'Dedicatee');
INSERT INTO wmss_roles (role_id,role_description) VALUES (5,'Librettist');
INSERT INTO wmss_roles (role_id,role_description) VALUES (6,'Editor');
INSERT INTO wmss_roles (role_id,role_description) VALUES (7,'Lyricist');
INSERT INTO wmss_roles (role_id,role_description) VALUES (8,'Translator');
INSERT INTO wmss_roles (role_id,role_description) VALUES (9,'Performer');
INSERT INTO wmss_roles (role_id,role_description) VALUES (99,'Unknown');


-- wmss_person_roles

CREATE TABLE wmss_persons (
person_id SERIAL,
person_name VARCHAR,
person_uri VARCHAR,
person_codedval VARCHAR,
person_authority VARCHAR,
CONSTRAINT persons_pkey PRIMARY KEY (person_id) 
);

-- wmss_score_persons

CREATE TABLE wmss_score_persons (
role_id INTEGER REFERENCES wmss_roles (role_id),
person_id INTEGER REFERENCES wmss_persons (person_id),
score_id VARCHAR REFERENCES wmss_scores (score_id)
);






-- MEI Examples
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Aguado_Walzer_G-major.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Ahle_Jesu_meines_Herzens_Freud.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Altenburg_concerto_C_major.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Altenburg_Ein_feste_Burg.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Altenburg_Macht_auf_die_Tor.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_BrandenburgConcert_No.4_II.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_BrandenburgConcert_No.4_I.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Ein_festeBurg.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Herzliebster_Jesu.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Hilf_Herr_Jesu.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach-J-C_Fughette_Gmaj.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach-J-C_Fughette_No2.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Musikalisches_Opfer_Trio.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Bach_Wie_bist_du_Seele.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Beethoven_Hymn_to_joy.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Beethoven_op.18.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Beethoven_Song_Op98.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Berlioz_Symphony_op25.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Borodin_StringTrio_g.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Brahms_StringQuartet_Op51_No1.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Brahms_WieMelodienZiehtEsMir.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Chopin_Etude_op.10_no.9.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Chopin_Mazurka.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Czerny_op603_6.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Czerny_StringQuartet_d.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Debussy_GolliwoggsCakewalk.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Debussy_Mandoline.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Echigo-Jishi.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Gluck_CheFaroSenzaEuridice.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Grieg_op.43_butterfly.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Grieg_op.43_little_bird.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Handel_Arie.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Handel_concerto_grosso.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Handel_Messias.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Haydn_StringQuartet_Op1_No1.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Hopkins_GatherRoundTheChristmasTree.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Hummel_Concerto_for_trumpet.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Hummel_op.67_No.11.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Ives_TheCage.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Joplin_Elite_Syncopations.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Joplin_Maple_leaf_Rag.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/JSB_BWV1047_1.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/JSB_BWV1047_2.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/JSB_BWV1047_3.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Kirnberger_FugeInEFlatMajor.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Krebs_Trio_Eb_2.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Krebs_Trio_in_c.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Liszt_Four_little_pieces.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Lully_LaDescenteDeMars.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Mahler_Song.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Marney_BreakThouTheBreadOfLife.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/McFerrin_Dont_worry.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_DasVeilchen.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_Fuge_G_minor.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_Quintett_2013.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Mozart_Quintett.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Pachelbel_Canon_in_D.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Parker-Gillespie_ShawNuff.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Ponchielli_LarrivoDelRe.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Praetorius_PuerNobisNascitur.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Ravel_Le_tombeau.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Rimsky-Korsakov_StringQuartet_B.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Saint-Saens_LeCarnevalDesAnimmaux.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Scarlatti_Sonata_in_C_major.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schubert_Erlkönig.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schubert_Lindenbaum.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schumann_Landmann.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schumann_Op.41.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schumann_Song_Op48-1.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schutz_DomineDeus.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Schutz_Jubilate_Deo.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Telemann_Concert.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Telemann_Suite.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Vivaldi_Op8_No.2.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Weber_Arie.mei','mei',1);
SELECT public.wmss_insert_score('','/home/jones/git/wmss/wmss/data/mei/Webern_VariationsforPiano.mei','mei',1);