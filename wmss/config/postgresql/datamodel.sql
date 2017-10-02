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
DROP TABLE IF EXISTS wmss_groups;
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
performance_medium_type_id INTEGER,
performance_medium_type_description VARCHAR,
CONSTRAINT medium_type_pkey PRIMARY KEY (performance_medium_type_id) 
);


INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Brass',1);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Electronic', 2);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Keyboard', 3);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Percussion', 4);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Strings, bowed', 5);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Strings, plucked', 6);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Voices', 7);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Woodwinds', 8);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Unspecified instruments', 9);
INSERT INTO wmss_performance_medium_type (performance_medium_type_description, performance_medium_type_id) VALUES ('Unknown', 10);


-- wmss_performance_medium

CREATE TABLE wmss_performance_medium (
performance_medium_id VARCHAR,
performance_medium_type_id INTEGER REFERENCES wmss_performance_medium_type (performance_medium_type_id),
performance_medium_description VARCHAR,
language_id VARCHAR REFERENCES wmss_languages (language_id),
CONSTRAINT medium_pkey PRIMARY KEY (performance_medium_id) 
);

 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ba',1,'Horn','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bb',1,'Trumpet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bc',1,'Cornet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bd',1,'Trombone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('be',1,'Tuba','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bf',1,'Baritone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bn',1,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bu',1,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('by',1,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('bz',1,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ea',2,'Synthesizer','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('eb',2,'Tape','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ec',2,'Computer','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ed',2,'Ondes Martinot','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('en',2,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('eu',2,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ez',2,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ka',3,'Piano','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kb',3,'Organ','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kc',3,'Harpsichord','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kd',3,'Clavichord','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ke',3,'Continuo','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kf',3,'Celeste','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kn',3,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ku',3,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ky',3,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('kz',3,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pa',4,'Timpani','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pb',4,'Xylophone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pc',4,'Marimba','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pd',4,'Drum','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pn',4,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pu',4,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('py',4,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('pz',4,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sa',5,'Violin','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sb',5,'Viola','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sc',5,'Violoncello','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sd',5,'Double bass','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('se',5,'Viol','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sf',5,'Viola d`amore','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sg',5,'Viola da gamba','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sn',5,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('su',5,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sy',5,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('sz',5,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ta',6,'Harp','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tb',6,'Guitar','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tc',6,'Lute','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('td',6,'Mandolin','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tn',6,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tu',6,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ty',6,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('tz',6,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('va',7,'Soprano','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vb',7,'Mezzo Soprano','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vc',7,'Alto','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vd',7,'Tenor','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('ve',7,'Baritone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vf',7,'Bass','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vg',7,'Counter tenor','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vh',7,'High voice','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vi',7,'Medium voice','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vj',7,'Low voice','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vn',7,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vu',7,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('vy',7,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wa',8,'Flute','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wb',8,'Oboe','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wc',8,'Clarinet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wd',8,'Bassoon','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('we',8,'Piccolo','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wf',8,'English horn','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wg',8,'Bass clarinet','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wh',8,'Recorder','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wi',8,'Saxophone','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wn',8,'Unspecified','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wu',8,'Unknown','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wy',8,'Ethnic','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('wz',8,'Other','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('zn',9,'Unspecified instruments','en');
 INSERT INTO wmss_performance_medium (performance_medium_id,performance_medium_type_id,performance_medium_description,language_id) VALUES ('zu',10,'Unknown','en');


-- wmss_group

CREATE TABLE wmss_groups (
group_id INTEGER,
group_description VARCHAR,
CONSTRAINT group_pkey PRIMARY KEY (group_id) 
);

INSERT INTO wmss_groups (group_id,group_description) VALUES (0,'Default');
INSERT INTO wmss_groups (group_id,group_description) VALUES (1,'MEI Examples');

-- wmss_scores


CREATE TABLE wmss_scores (
score_id VARCHAR,
score_name VARCHAR,
score_tonality_note VARCHAR,
score_tonality_mode VARCHAR,
score_creation_date_min INTEGER,
score_creation_date_max INTEGER,
group_id INTEGER REFERENCES wmss_groups(group_id),
CONSTRAINT score_pkey PRIMARY KEY (score_id) 
);


-- wmss_document_type

CREATE TABLE wmss_document_type (
document_type_id VARCHAR,
document_type_description VARCHAR,
CONSTRAINT document_type_pkey PRIMARY KEY (document_type_id) 
);

INSERT INTO wmss_document_type (document_type_id,document_type_description) VALUES ('mei','MEI - Music Encoding Initiative');
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
score_id VARCHAR REFERENCES wmss_scores(score_id), 
local_performance_medium_id VARCHAR,
performance_medium_id VARCHAR REFERENCES wmss_performance_medium (performance_medium_id),
movement_performance_medium_description VARCHAR,
movement_performance_medium_solo BOOLEAN
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
SELECT public.wmss_insert_score('/home/jones/Aguado_Walzer_G-major.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Ahle_Jesu_meines_Herzens_Freud.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Altenburg_concerto_C_major.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Altenburg_Ein_feste_Burg.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Altenburg_Macht_auf_die_Tor.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_BrandenburgConcert_No.4_II.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_BrandenburgConcert_No.4_I.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_Ein_festeBurg.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_Herzliebster_Jesu.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_Hilf_Herr_Jesu.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach-J-C_Fughette_Gmaj.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach-J-C_Fughette_No2.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_Musikalisches_Opfer_Trio.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Bach_Wie_bist_du_Seele.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Beethoven_Hymn_to_joy.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Beethoven_op.18.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Beethoven_Song_Op98.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Berlioz_Symphony_op25.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Borodin_StringTrio_g.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Brahms_StringQuartet_Op51_No1.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Brahms_WieMelodienZiehtEsMir.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Chopin_Etude_op.10_no.9.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Chopin_Mazurka.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Czerny_op603_6.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Czerny_StringQuartet_d.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Debussy_GolliwoggsCakewalk.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Debussy_Mandoline.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Echigo-Jishi.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Gluck_CheFaroSenzaEuridice.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Grieg_op.43_butterfly.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Grieg_op.43_little_bird.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Handel_Arie.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Handel_concerto_grosso.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Handel_Messias.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Haydn_StringQuartet_Op1_No1.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Hopkins_GatherRoundTheChristmasTree.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Hummel_Concerto_for_trumpet.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Hummel_op.67_No.11.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Ives_TheCage.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Joplin_Elite_Syncopations.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Joplin_Maple_leaf_Rag.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/JSB_BWV1047_1.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/JSB_BWV1047_2.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/JSB_BWV1047_3.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Kirnberger_FugeInEFlatMajor.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Krebs_Trio_Eb_2.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Krebs_Trio_in_c.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Liszt_Four_little_pieces.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Lully_LaDescenteDeMars.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Mahler_Song.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Marney_BreakThouTheBreadOfLife.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/McFerrin_Dont_worry.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Mozart_DasVeilchen.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Mozart_Fuge_G_minor.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Mozart_Quintett_2013.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Mozart_Quintett.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Pachelbel_Canon_in_D.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Parker-Gillespie_ShawNuff.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Ponchielli_LarrivoDelRe.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Praetorius_PuerNobisNascitur.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Ravel_Le_tombeau.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Rimsky-Korsakov_StringQuartet_B.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Saint-Saens_LeCarnevalDesAnimmaux.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Scarlatti_Sonata_in_C_major.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schubert_Erlkönig.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schubert_Lindenbaum.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schumann_Landmann.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schumann_Op.41.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schumann_Song_Op48-1.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schutz_DomineDeus.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Schutz_Jubilate_Deo.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Telemann_Concert.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Telemann_Suite.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Vivaldi_Op8_No.2.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Weber_Arie.mei','mei',1);
SELECT public.wmss_insert_score('/home/jones/Webern_VariationsforPiano.mei','mei',1);