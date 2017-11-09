-- Author: Jones
-- Comments: WMSS data model.






-- Data Model


DROP SCHEMA IF EXISTS wmss CASCADE;
CREATE SCHEMA wmss;


DROP TABLE IF EXISTS wmss.wmss_movement_performance_medium;
DROP TABLE IF EXISTS wmss.wmss_score_persons;
DROP TABLE IF EXISTS wmss.wmss_persons;
DROP TABLE IF EXISTS wmss.wmss_roles;
DROP TABLE IF EXISTS wmss.wmss_performance_medium;
DROP TABLE IF EXISTS wmss.wmss_performance_medium_type;
DROP TABLE IF EXISTS wmss.wmss_document;
DROP TABLE IF EXISTS wmss.wmss_score_movements;
DROP TABLE IF EXISTS wmss.wmss_scores;
DROP TABLE IF EXISTS wmss.wmss_document_type ;
DROP TABLE IF EXISTS wmss.wmss_collections;
DROP SEQUENCE IF EXISTS wmss.seq_scores;
CREATE SEQUENCE wmss.seq_scores START WITH 1;

-- wwms_performance_medium_type 

CREATE TABLE wmss.wmss_performance_medium_type (
performance_medium_type_id VARCHAR,
performance_medium_type_description VARCHAR,
CONSTRAINT medium_type_pkey PRIMARY KEY (performance_medium_type_id) 
);


INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('brass','Brass');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('drum','Drums');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('effect','Effects');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('keyboard','Keyboards');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('metal','Metals');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('pitched-percussion','Pitched Percussion');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('pluck','Pluck');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('rattle','Rattle');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('strings','Strings');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('synth','Synth');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('voice','Voice');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('wind','Wind');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('wood','Wood');
INSERT INTO wmss.wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('unspecified','Unspecified');

-- wmss.wmss_performance_medium

CREATE TABLE wmss.wmss_performance_medium (
performance_medium_id VARCHAR,
performance_medium_type_id VARCHAR REFERENCES wmss.wmss_performance_medium_type (performance_medium_type_id),
performance_medium_description VARCHAR,
--language_id VARCHAR REFERENCES wmss.wmss_languages (language_id),
CONSTRAINT medium_pkey PRIMARY KEY (performance_medium_id) 
);

INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.alphorn','brass','Alphorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.alto-horn','brass','Alto Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.baritone-horn','brass','Baritone Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle','brass','Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.alto','brass','Alto Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.baritone','brass','Baritone Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.contrabass','brass','Contrabass Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.euphonium-bugle','brass','Euphonium-Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.mellophone-bugle','brass','Mellophone-Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.soprano','brass','Soprano Bugle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cimbasso','brass','Cimbasso');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.conch-shell','brass','Conch Shell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornet','brass','Cornet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornet.soprano','brass','Soprano Cornet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornett','brass','Cornett');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornett.tenor','brass','Tenor Cornett');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornettino','brass','Cornettino');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.didgeridoo','brass','Didgeridoo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.euphonium','brass','Euphonium');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.fiscorn','brass','Fiscom');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.horn.b','brass','Horn in B');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.flugelhorn','brass','Flugelhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.french-horn','brass','French Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.group','brass','Group (Brass)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.group.synth','brass','Group Synth. (Brass)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.helicon','brass','Helicon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.horagai','brass','Horagai');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.kuhlohorn','brass','Kuhlohorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.mellophone','brass','Mellophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.natural-horn','brass','Natural Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.ophicleide','brass','Ophicleide');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.posthorn','brass','Posthorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.rag-dung','brass','Rag Dung');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt','brass','Sackbutt');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt.alto','brass','Alto Sackbutt');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt.bass','brass','Bass Sackbutt');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt.tenor','brass','Tenor Sackbutt');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.saxhorn','brass','Saxohorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.serpent','brass','Serpent');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.shofar','brass','Shofar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sousaphone','brass','Sousaphone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone','brass','Trombone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.alto','brass','Alto Trombone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.bass','brass','Bass Trombone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.contrabass','brass','Contrabass Trombone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.tenor','brass','Tenor Trombone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet','brass','Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.baroque','brass','Baroque Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.bass','brass','Bass Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.bflat','brass','Trumpet in B flat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.c','brass','Trumpet in C');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.d','brass','Trumpet in D');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.b','brass','Trumpet in B');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.piccolo','brass','Piccolo Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.pocket','brass','Pocket Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.slide','brass','Slide Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.tenor','brass','Tenor Trumpet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.tuba','brass','Tuba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.tuba.bass','brass','Bass Tuba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.tuba.subcontrabass','brass','Subcontrabass Tuba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.vienna-horn','brass','Vienna Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.vuvuzela','brass','Vuvuzela');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.wagner-tuba','brass','Wagner Tuba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.drum','drum','Drums');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.side-drum','drum','Side Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.apentemma','drum','Apentemma');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ashiko','drum','Ashiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.atabaque','drum','Atabaque');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.atoke','drum','Atoke');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.atsimevu','drum','Atsimevu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.axatse','drum','Axatse');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bass-drum','drum','Bass Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata','drum','Bata');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata.itotele','drum','Itolele Bata');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata.iya','drum','Iya Bata');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata.okonkolo','drum','Okonkolo Bata');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bendir','drum','Bendir');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bodhran','drum','Bodhran');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bombo','drum','Bombo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bongo','drum','Bongo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bougarabou','drum','Bougarabou');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.buffalo-drum','drum','Buffalo Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.cajon','drum','Cajon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.chenda','drum','Chenda');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.chu-daiko','drum','Chu Daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.conga','drum','Conga');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.cuica','drum','Cuica');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dabakan','drum','Dabakan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.daff','drum','Daff');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dafli','drum','Dafli');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.daibyosi','drum','Daibyosi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.damroo','drum','Damroo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.darabuka','drum','Darabuka');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.def','drum','Def');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dhol','drum','Dhol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dholak','drum','Dholak');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.djembe','drum','Djembe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.doira','drum','Doira');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dondo','drum','Dondo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.doun-doun-ba','drum','Doun Doun Ba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.duff','drum','Duff');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dumbek','drum','Dumbek');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.fontomfrom','drum','Fontomfrom');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.frame-drum','drum','Frame Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.frame-drum.arabian','drum','Arabian Frame Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.geduk','drum','Geduk');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ghatam','drum','Ghatam');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.gome','drum','Gome');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group','drum','Group (Drums)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.chinese','drum','Chinese Group (Drums)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.ewe','drum','Ewe Group (Drums)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.indian','drum','Indian Group (Drums)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.set','drum','Set Group (Drums)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.hand-drum','drum','Hand Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.hira-daiko','drum','Hira Daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ibo','drum','Ibo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.igihumurizo','drum','Igihumurizo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.inyahura','drum','Inyahura');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ishakwe','drum','Ishakwe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.jang-gu','drum','Jang-gu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kagan','drum','Kagan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kakko','drum','Kakko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kanjira','drum','Kanjira');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kendhang','drum','Kendhang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kendhang.ageng','drum','Ageng Kendhang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kendhang.ciblon','drum','Ciblon Kendhang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kenkeni','drum','Kenkeni');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.khol','drum','Khol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kick-drum','drum','Kick-drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kidi','drum','Kidi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ko-daiko','drum','Ko-daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kpanlogo','drum','Kpanlogo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kudum','drum','Kudum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.lambeg','drum','Lambeg');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.lion-drum','drum','Lion Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum','drum','Log Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum.african','drum','African Log Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum.native','drum','Native Log Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum.nigerian','drum','Ngerian Log Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.madal','drum','Madal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.maddale','drum','Maddale');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.mridangam','drum','Mridangam');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.naal','drum','Naal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.nagado-daiko','drum','Nagado Daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.nagara','drum','Nagara');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.naqara','drum','Naqara');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.o-daiko','drum','O-daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.okawa','drum','Okawa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.okedo-daiko','drum','Okedo-daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pahu-hula','drum','Pahu-hula');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pakhawaj','drum','Pakhawaj');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pandeiro','drum','Pandeiro');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pandero','drum','Pandero');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.powwow','drum','Powwow');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pueblo','drum','Pueblo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.repinique','drum','Repinique');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.riq','drum','Riq');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.rototom','drum','Rototom');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sabar','drum','Sabar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sakara','drum','Sakara');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sampho','drum','Sampho');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sangban','drum','Sangban');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.shime-daiko','drum','Shime-daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.slit-drum','drum','Slit-drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.slit-drum.krin','drum','Krin Slit-drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.snare-drum','drum','Sanre Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.snare-drum.electric','drum','Electric Sanre Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sogo','drum','Sogo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.surdo','drum','Surdo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tabla','drum','Tabla');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tabla.bayan','drum','Bayan Tabla');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tabla.dayan','drum','Dayan Tabla');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.taiko','drum','Taiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.talking','drum','Talking');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tama','drum','Tama');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tamborita','drum','Tamborita');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tambourine','drum','Tambourine');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tamte','drum','Tamte');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tangku','drum','Tangku');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tan-tan','drum','Tan-tan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.taphon','drum','Taphon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tar','drum','Tar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tasha','drum','Tasha');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tenor-drum','drum','Tenor Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.teponaxtli','drum','Teponaxtli');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.thavil','drum','Thavil');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.the-box','drum','The Box');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.timbale','drum','Timbale');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.timpani','drum','Timpani');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tinaja','drum','Tinaja');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.toere','drum','Toere');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tombak','drum','Tombak');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tom-tom','drum','Tom-tom');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tom-tom.synth','drum','Synth. Tom-tom');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tsuzumi','drum','Tsuzumi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tumbak','drum','Tumbak');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.uchiwa-daiko','drum','Uchiwa-daiko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.udaku','drum','Udaku');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.udu','drum','Udu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.zarb','drum','Zarb');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.aeolian-harp','effect','Aeolian Harp');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.air-horn','effect','Air Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.applause','effect','Applause');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bass-string-slap','effect','Bass String Slap');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bird','effect','Bird');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bird.nightingale','effect','Nightingale Bird');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bird.tweet','effect','Bird Tweet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.breath','effect','Breath');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bubble','effect','Bubble');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bullroarer','effect','Bullroarer');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.burst','effect','Burst');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car','effect','Car');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.crash','effect','Car Crash');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.engine','effect','Car Engine');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.pass','effect','Car Pass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.stop','effect','Car Stop');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.crickets','effect','Crickets');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.dog','effect','Dog');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.door.creak','effect','Door Creak');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.door.slam','effect','Door Slam');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.explosion','effect','Explosion');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.flute-key-click','effect','Flute Key Click');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.footsteps','effect','Footsteps');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.frogs','effect','Frogs');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.guitar-cutting','effect','Guitar Cutting');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.guitar-fret','effect','Guitar Fret');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.gunshot','effect','Gunshot');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.hand-clap','effect','Hand Clap');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.heartbeat','effect','Heartbeat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.helicopter','effect','Helicopter');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.high-q','effect','High Q');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.horse-gallop','effect','Horse Gallop');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.jet-plane','effect','Jep Plane');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.laser-gun','effect','Laser Gun');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.laugh','effect','Laugh');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.lions-roar','effect','Lions Roar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.machine-gun','effect','Machine Gun');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.marching-machine','effect','Marching Machine');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.metronome-bell','effect','Menotrnome Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.metronome-click','effect','Menotrnome Click');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.pat','effect','Pat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.punch','effect','Punch');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.rain','effect','Rain');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.scratch','effect','Stratch');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.scream','effect','Scream');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.seashore','effect','Seashore');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.siren','effect','Siren');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.slap','effect','Slap');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.snap','effect','Snap');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.stamp','effect','Stamp');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.starship','effect','Starship');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.stream','effect','Stream');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.telephone-ring','effect','Telephone Ring');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.thunder','effect','Thunder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.train','effect','Train');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.trash-can','effect','Train Crash');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whip','effect','Whip');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle','effect','Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.mouth-siren','effect','Mouth Siren');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.police','effect','Police (Whistle)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.slide','effect','Slide (Whistle)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.train','effect','Train (Whistle)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.wind','effect','Wind');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.accordion','keyboard','Accordion');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.bandoneon','keyboard','Bandoneon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.celesta','keyboard','Celesta');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.clavichord','keyboard','Clavichord');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.clavichord.synth','keyboard','Synth. Clavichord');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.concertina','keyboard','Concertina');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.fortepiano','keyboard','Fortepiano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.harmonium','keyboard','Harmonium');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.harpsichord','keyboard','Harpsichord');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.ondes-martenot','keyboard','Ondes Martenot');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ','keyboard','Organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.drawbar','keyboard','Drawbar Organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.percussive','keyboard','Percussive Organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.pipe','keyboard','Pipe Organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.reed','keyboard','Reed Organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.rotary','keyboard','Rotary Organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano','keyboard','Piano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.electric','keyboard','Electric Piano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.grand','keyboard','Grand Piano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.honky-tonk','keyboard','Honky-tonk Piano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.prepared','keyboard','Prepared Piano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.toy','keyboard','Piano Toy');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.upright','keyboard','Upright Piano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.virginal','keyboard','Virginal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.adodo','metal','Adodo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.anvil','metal','Anvil');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.babendil','metal','Babendil');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.agogo','metal','Agogo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.almglocken','metal','Almglocken');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.bell-plate','metal','Bell Plate');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.bell-tree','metal','Bell Tree');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.carillon','metal','Carillon Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.chimes','metal','Chimes Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.chimta','metal','Chimta Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.chippli','metal','Chippli Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.church','metal','Church Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.cowbell','metal','Cowbell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.dawuro','metal','Dawuro Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.gankokwe','metal','Gankokwe Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.ghungroo','metal','Ghungroo Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.hatheli','metal','Hatheli Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.jingle-bell','metal','Jingle Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.khartal','metal','Khartal Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.mark-tree','metal','Mark Tree Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.sistrum','metal','Sistrum Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.sleigh-bells','metal','Sleigh Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.temple','metal','Temple Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.tibetan','metal','Tibetan Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.tinklebell','metal','Tinklebell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.trychel','metal','Trychel Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.wind-chimes','metal','Wind Chimes Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.zills','metal','Zills Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.berimbau','metal','Berimbau');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.brake-drums','metal','Brame Drums');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.crotales','metal','Crotales');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal','metal','Cymbals');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.bo','metal','Bo Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.ceng-ceng','metal','Ceng-Ceng Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.chabara','metal','Chabara Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.chinese','metal','Chinese Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.ching','metal','Ching Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.clash','metal','Clash Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.crash','metal','Crash Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.finger','metal','Finger Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.hand','metal','Hand Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.kesi','metal','Kesi Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.manjeera','metal','Manjeera Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.reverse','metal','Reverse Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.ride','metal','Ride Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.sizzle','metal','Sizzle Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.splash','metal','Splash Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.suspended','metal','Suspended Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.tebyoshi','metal','Tebyoshi Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.tibetan','metal','Tibetan Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.tingsha','metal','Tingsha Cymbal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.flexatone','metal','Flexatone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong','metal','Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.ageng','metal','Ageng Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.agung','metal','Agung Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.chanchiki','metal','Chanchiki Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.chinese','metal','Chinese Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.gandingan','metal','Gandingan Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.kempul','metal','Kempul Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.kempyang','metal','Kempyang Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.ketuk','metal','Ketuk Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.kkwenggwari','metal','Kkwenggwari Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.luo','metal','Luo Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.singing','metal','Singing Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.thai','metal','Thai Gong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.guira','metal','Guira');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.hang','metal','Hang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.hi-hat','metal','Hi Hat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.jaw-harp','metal','Jaw Harp');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.kengong','metal','Kengong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.murchang','metal','Murchang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.musical-saw','metal','Musical Saw');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.singing-bowl','metal','Singing Bowl');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.spoons','metal','Spoons');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.steel-drums','metal','Steel Drums');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.tamtam','metal','Tamtam');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.thundersheet','metal','Thundersheet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.triangle','metal','Triangle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.washboard','metal','Washboard');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.angklung','pitched-percussion','Angklung');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.balafon','pitched-percussion','Balafon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bell-lyre','pitched-percussion','Lyre Bell');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bells','pitched-percussion','Bells');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bianqing','pitched-percussion','Bianqing');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bianzhong','pitched-percussion','Bianzhong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bonang','pitched-percussion','Bonang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.cimbalom','pitched-percussion','Cimbalom');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.crystal-glasses','pitched-percussion','Crystal Glasses');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.dan-tam-thap-luc','pitched-percussion','Dan tam thap luc');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.fangxiang','pitched-percussion','Fangxiang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gandingan-a-kayo','pitched-percussion','Gandingan-a-kayo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gangsa','pitched-percussion','Gangsa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gender','pitched-percussion','Gender');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.giying','pitched-percussion','Giying');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glass-harmonica','pitched-percussion','Glass Harmonica');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glockenspiel','pitched-percussion','Glockenspiel');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glockenspiel.alto','pitched-percussion','Alto Glockenspiel');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glockenspiel.soprano','pitched-percussion','Soprano Glockenspiel');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gyil','pitched-percussion','Gyil');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.hammer-dulcimer','pitched-percussion','Hammer Dulcimer');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.handbells','pitched-percussion','Handbells');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kalimba','pitched-percussion','Kalimba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kantil','pitched-percussion','Kantil');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.khim','pitched-percussion','Khim');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kulintang','pitched-percussion','Kulintang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kulintang-a-kayo','pitched-percussion','Kulintang-a-kayo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kulintang-a-tiniok','pitched-percussion','Kulintang-a-tiniok');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.likembe','pitched-percussion','Likembe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.luntang','pitched-percussion','Luntang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.marimba','pitched-percussion','Marimba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.marimba.bass','pitched-percussion','Bass Marimba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.mbira','pitched-percussion','Mbira');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.mbira.array','pitched-percussion','Arraz Mbira');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone','pitched-percussion','Metallophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone.alto','pitched-percussion','Alto Metallophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone.bass','pitched-percussion','Bass Metallophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone.soprano','pitched-percussion','Soprano Metallophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.music-box','pitched-percussion','Music Box');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.pelog-panerus','pitched-percussion','Pelog-panerus');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.pemade','pitched-percussion','Pemade');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.penyacah','pitched-percussion','Penyacah');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.ek','pitched-percussion','Ranat ek');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.ek-lek','pitched-percussion','Ranat ek-lek');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.thum','pitched-percussion','Ranat Thum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.thum-lek','pitched-percussion','Ranat Thum-lek');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.reyong','pitched-percussion','Reyong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.sanza','pitched-percussion','Sanza');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.saron-barung','pitched-percussion','Saron Barung');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.saron-demong','pitched-percussion','Saron Demong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.saron-panerus','pitched-percussion','Saron Panerus');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.slendro-panerus','pitched-percussion','Slendro Panerus');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.slentem','pitched-percussion','Slentem');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.tsymbaly','pitched-percussion','Tsymbaly');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.tubes','pitched-percussion','Tubes');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.tubular-bells','pitched-percussion','Tubular Bells');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.vibraphone','pitched-percussion','Vibraphone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone','pitched-percussion','Xylophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone.alto','pitched-percussion','Alto Xylophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone.bass','pitched-percussion','Bass Xylophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone.soprano','pitched-percussion','Soprano Xylophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylorimba','pitched-percussion','Xylorimba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.yangqin','pitched-percussion','Yangqin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.archlute','pluck','Archlute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.autoharp','pluck','Autoharp');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.baglama','pluck','Baglama');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bajo','pluck','Bajo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika','pluck','Balalaika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.alto','pluck','Alto Balalaika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.bass','pluck','Bass Balalaika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.contrabass','pluck','pluck.balalaika.contrabass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.piccolo','pluck','Piccolo Balalaika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.prima','pluck','Prima Balalaika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.secunda','pluck','Secunda Balalaika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bandola','pluck','Bandola');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bandura','pluck','Bandura');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bandurria','pluck','Bandurria');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.banjo','pluck','Banjo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.banjo.tenor','pluck','Tenor Banjo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.banjolele','pluck','Banjolele');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.barbat','pluck','Barbat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass','pluck','Bass (Pluck)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.acoustic','pluck','Acoustic Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.bolon','pluck','Bolon Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.electric','pluck','Electric Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.fretless','pluck','Fretless Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.guitarron','pluck','Guitarron Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.synth','pluck','Synth Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.synth.lead','pluck','Synth Lead Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.washtub','pluck','Washtube Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.whamola','pluck','Whamola Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.begena','pluck','Begena');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.biwa','pluck','Biwa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bordonua','pluck','Bordonua');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bouzouki','pluck','Bouzouki');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bouzouki.irish','pluck','Irish Bouzouki');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.celtic-harp','pluck','Celtic Harp');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.charango','pluck','Charango');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.chitarra-battente','pluck','Chitarra Battente');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.cithara','pluck','Cithara');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.cittern','pluck','Cittern');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.cuatro','pluck','Cuatro');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-bau','pluck','Dan-bau');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-nguyet','pluck','Dan-nguyet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-tranh','pluck','Dan-tranh');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-ty-ba','pluck','Dan-ty-ba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.diddley-bow','pluck','Diddley-bow');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.domra','pluck','Domra');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.domu','pluck','Domu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dulcimer','pluck','Dulcimer');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dutar','pluck','Dutar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.duxianqin','pluck','Duxianqin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ektara','pluck','Ektara');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.geomungo','pluck','Geomungo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.gottuvadhyam','pluck','Gottuvadhyam');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar','pluck','Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.acoustic','pluck','Acoustic Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.electric','pluck','Electric Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.nylon-string','pluck','Nylon String Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.pedal-steel','pluck','Guitar Pedal-Steel');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.portuguese','pluck','Portuguese Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.requinto','pluck','Requinto');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.resonator','pluck','Resonator');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.steel-string','pluck','Steel String Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitjo','pluck','Guitjo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitjo.double-neck','pluck','Double Neck Guitjo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guqin','pluck','Gugin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guzheng','pluck','Guzheng');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guzheng.choazhou','pluck','Choazhou Guzheng');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.harp','pluck','Harp');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.harp-guitar','pluck','Harp-Guitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.huapanguera','pluck','Huapanguera');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-huasteca','pluck','Jarana Huasteca');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha','pluck','Jarana Jarocha');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.mosquito','pluck','Jarana Jarocha Mosquito');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.primera','pluck','Jarana Jarocha Primera');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.segunda','pluck','Jarana Jarocha Segunda');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.tercera','pluck','Jarana Jarocha Tercera');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kabosy','pluck','Kabosy');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kantele','pluck','Kantele');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kanun','pluck','Kanun');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kayagum','pluck','Kayagum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kobza','pluck','Kobza');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.komuz','pluck','Komuy');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kora','pluck','Kora');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.koto','pluck','Koto');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kutiyapi','pluck','Kutiyapi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.langeleik','pluck','Langeleik');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.laud','pluck','Laud');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.lute','pluck','Lute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.lyre','pluck','Lyre');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandobass','pluck','Mandobass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandocello','pluck','Mandocello');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandola','pluck','Mandola');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandolin','pluck','Mandolin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandolin.octave','pluck','Mandolin Octave');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandora','pluck','Mandora');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandore','pluck','Mandore');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.marovany','pluck','Marovany');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.musical-bow','pluck','Musical Bow');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ngoni','pluck','Ngoni');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.oud','pluck','Oud');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.pipa','pluck','Pipa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.psaltery','pluck','Psaltery');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ruan','pluck','Ruan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sallaneh','pluck','Sallaneh');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sanshin','pluck','Sanshin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.santoor','pluck','Santoor');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sanxian','pluck','Sanxian');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sarod','pluck','Sarod');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.saung','pluck','Saung');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.saz','pluck','Saz');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.se','pluck','Se');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.setar','pluck','Setar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.shamisen','pluck','Shamisen');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sitar','pluck','Sitar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth','pluck','Synth (Pluck)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth.charang','pluck','pluck.synth.charang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth.chiff','pluck','pluck.synth.chiff');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth.stick','pluck','pluck.synth.stick');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura','pluck','pluck.tambura');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura.bulgarian','pluck','pluck.tambura.bulgarian');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura.female','pluck','pluck.tambura.female');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura.male','pluck','pluck.tambura.male');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tar','pluck','pluck.tar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.theorbo','pluck','pluck.theorbo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.timple','pluck','pluck.timple');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tiple','pluck','pluck.tiple');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tres','pluck','pluck.tres');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ukulele','pluck','pluck.ukulele');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ukulele.tenor','pluck','pluck.ukulele.tenor');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.valiha','pluck','pluck.valiha');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena','pluck','pluck.veena');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena.mohan','pluck','pluck.veena.mohan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena.rudra','pluck','pluck.veena.rudra');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena.vichitra','pluck','pluck.veena.vichitra');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.vihuela','pluck','pluck.vihuela');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.vihuela.mexican','pluck','pluck.vihuela.mexican');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.xalam','pluck','pluck.xalam');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.yueqin','pluck','pluck.yueqin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.zither','pluck','pluck.zither');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.zither.overtone','pluck','pluck.zither.overtone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.afoxe','rattle','rattle.afoxe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.birds','rattle','rattle.birds');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.cabasa','rattle','rattle.cabasa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.caxixi','rattle','rattle.caxixi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.cog','rattle','rattle.cog');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.ganza','rattle','rattle.ganza');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.hosho','rattle','rattle.hosho');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.jawbone','rattle','rattle.jawbone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.kayamba','rattle','rattle.kayamba');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.kpoko-kpoko','rattle','rattle.kpoko-kpoko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.lava-stones','rattle','rattle.lava-stones');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.maraca','rattle','rattle.maraca');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.rain-stick','rattle','rattle.rain-stick');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.ratchet','rattle','rattle.ratchet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.rattle','rattle','rattle.rattle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.shaker','rattle','rattle.shaker');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.shaker.egg','rattle','rattle.shaker.egg');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.shekere','rattle','rattle.shekere');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.sistre','rattle','rattle.sistre');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.televi','rattle','rattle.televi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.vibraslap','rattle','rattle.vibraslap');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.wasembe','rattle','rattle.wasembe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.ajaeng','strings','Ajaeng');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.arpeggione','strings','Arpeggione');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.baryton','strings','Baryton');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.cello','strings','Violoncello');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.cello.piccolo','strings','Piccolo Violoncello');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.contrabass','strings','Contrabass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.crwth','strings','Crwth');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.dan-gao','strings','Dan Gao');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.dihu','strings','Djhu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.erhu','strings','Erhu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.erxian','strings','Erxian');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.esraj','strings','Esraj');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.fiddle','strings','Fiddle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.fiddle.hardanger','strings','Hardanger Fiddle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.gadulka','strings','Gadulka');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.gaohu','strings','Gaohu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.gehu','strings','Gehu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.group','strings','Group (String)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.group.synth','strings','Synth Group (String)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.haegeum','strings','Haegeum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.hurdy-gurdy','strings','Hurdy Gurdy');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.igil','strings','Igil');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.kamancha','strings','Kamancha');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.kokyu','strings','Kokyu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.laruan','strings','Laruan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.leiqin','strings','Leigin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.lirone','strings','Lirone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.lyra.byzantine','strings','Byzantine Lyra');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.lyra.cretan','strings','Cretan Lyra');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.morin-khuur','strings','Morin Khuur');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.nyckelharpa','strings','Nyckelharpa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.octobass','strings','Octobass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.rebab','strings','Rebab');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.rebec','strings','Rebec');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.sarangi','strings','Sarangi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.stroh-violin','strings','Stroh Violin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.tromba-marina','strings','Tromba Marina');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.vielle','strings','Vielle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol','strings','Viol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.alto','strings','Alto Viol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.bass','strings','Bass Viol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.tenor','strings','Tenor Viol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.treble','strings','Trebble Viol');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.violone','strings','Violone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viola','strings','Viola');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viola-damore','strings','Viola d`Amore');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.violin','strings','Violin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.violono.piccolo','strings','Piccolo Violino');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.violotta','strings','Violotta');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.yayli-tanbur','strings','Yayli Tanbur');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.yazheng','strings','Yazheng');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.zhonghu','strings','Zhonghu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects','synth','Synth Effects');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.atmosphere','synth','Synth Effects Atmosphere');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.brightness','synth','Synth Effects Brightness');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.crystal','synth','Synth Effects Crystal');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.echoes','synth','Synth Effects Echoes');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.goblins','synth','Synth Effects Goblins');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.rain','synth','Synth Effects Rain');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.sci-fi','synth','Synth Effects Sci-fi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.soundtrack','synth','Synth Effects Soundtrack');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.group','synth','Group (Synth)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.group.fifths','synth','Group Fiftha (Synth)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.group.orchestra','synth','Group Orchestra (Synth)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad','synth','Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.bowed','synth','Bowed Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.choir','synth','Choir Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.halo','synth','Halo Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.metallic','synth','Metallic Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.polysynth','synth','Polysynth Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.sweep','synth','Sweep Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.warm','synth','Warm Pad');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.theremin','synth','Theremin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.tone.sawtooth','synth','Sawtooth Tone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.tone.sine','synth','Sine Tone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.tone.square','synth','Square Tone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.aa','voice','Aa');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.alto','voice','Alto');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.aw','voice','Aw');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.baritone','voice','Baritone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.bass','voice','Bass');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.child','voice','Child');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.countertenor','voice','Countertenor');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.doo','voice','Doo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.ee','voice','Ee');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.female','voice','Female Voice');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.kazoo','voice','Kazoo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.male','voice','Male Voice');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.mezzo-soprano','voice','Mezzo-Soprano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.mm','voice','Mm');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.oo','voice','Oo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.percussion','voice','Percussion (Voice)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.percussion.beatbox','voice','Beatbox Percussion (Voice)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.soprano','voice','Soprano');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.synth','voice','Synth Voice');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.talk-box','voice','Talk-Box');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.tenor','voice','Tenor');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.vocals','voice','Voice');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.bansuri','wind','Bansuri');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.blown-bottle','wind','Blow-Bottle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.calliope','wind','Calliope');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.danso','wind','Danso');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.di-zi','wind','Di-zi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.dvojnice','wind','Dvojnice');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.fife','wind','Fife');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flageolet','wind','Flageolet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute','wind','Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.alto','wind','Alto Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.bass','wind','Bass Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.contra-alto','wind','Contra-alto Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.contrabass','wind','Contrabass Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.double-contrabass','wind','Double Contrabass Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.irish','wind','Irish Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.piccolo','wind','Piccolo Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.subcontrabass','wind','Subcontrabass Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.fujara','wind','Fujara Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.gemshorn','wind','Gemshorn Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.hocchiku','wind','Hocchiku Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.hun','wind','Hun Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.kaval','wind','Kaval Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.kawala','wind','Kawala Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.khlui','wind','Khlui Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.knotweed','wind','Knotweed Flute');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.koncovka.alto','wind','Alto Koncovka');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.koudi','wind','Koudi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.ney','wind','Ney');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.nohkan','wind','Nohkan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.nose','wind','Nose (Flutes)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.ocarina','wind','Ocarina');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.overtone.tenor','wind','Tenor Overtone (Flutes)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.palendag','wind','Palendag');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.panpipes','wind','Panpipes');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.quena','wind','Quena');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder','wind','Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.alto','wind','Alto Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.bass','wind','Bass Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.contrabass','wind','Contrabass Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.descant','wind','Descant Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.garklein','wind','Garklein Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.great-bass','wind','Great Bass Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.sopranino','wind','Sopranino Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.soprano','wind','Soprano Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.tenor','wind','Tenor Recorder');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.ryuteki','wind','Ryuteki');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shakuhachi','wind','Shakuhachi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shepherds-pipe','wind','Shepherds Pipe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shinobue','wind','Shinobue');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shvi','wind','Shvi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.suling','wind','Suling');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.tarka','wind','Tarka');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.tumpong','wind','Tumpong');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.venu','wind','Venu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle','wind','Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.alto','wind','Alto Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.low-irish','wind','Low Irish Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.shiva','wind','Shiva Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.slide','wind','Slide Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.tin','wind','Tin Whistle');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.tin.bflat','wind','Tin Whistle in B flat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.tin.d','wind','Tin Whistle in D');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.xiao','wind','Xiao');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.xun','wind','Xun');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.group','wind','Group (Wind)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.jug','wind','Jug');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.bagpipes','wind','Bagpipe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.gaida','wind','Gaida');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.highland','wind','Highland Pipes');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.uilleann','wind','Uilleann');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pungi','wind','Pungi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.albogue','wind','Alboque');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.alboka','wind','Alboka');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.algaita','wind','Algaita');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.arghul','wind','Arghul');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.basset-horn','wind','Basset Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bassoon','wind','Basson');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bawu','wind','Bawu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bifora','wind','Bifora');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bombarde','wind','Bombarde');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.chalumeau','wind','Chalumeau');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet','wind','Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.a','wind','Clarinet in A');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.alto','wind','Alto Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.bass','wind','Bass Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.basset','wind','Basset Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.bflat','wind','Clarinet in B flat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.contra-alto','wind','Contra-alto Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.contrabass','wind','Contrabass Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.eflat','wind','Clarinet in E flat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.piccolo.aflat','wind','Clarinet Piccolo in A flat');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinette-damour','wind','Clarinet D`Amour');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.contrabass','wind','Contrabass (Wind)');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.contrabassoon','wind','Contrabassoon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.cornamuse','wind','Cornamuse');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.cromorne','wind','Cromorne');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn','wind','Crumhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.alto','wind','Alto Crumhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.bass','wind','Bass Crumhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.great-bass','wind','Great Bass Crumhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.soprano','wind','Soprano Crumhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.tenor','wind','Tenor Crumhorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.diple','wind','Diple');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.diplica','wind','Diplica');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.duduk','wind','Duduk');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.dulcian','wind','Dulcian');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.dulzaina','wind','Dulzaina');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.english-horn','wind','English Horn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.guanzi','wind','Guanzi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.harmonica','wind','Harmonica');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.harmonica.bass','wind','Bass Harmonica');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckel-clarina','wind','Heckel-clarina');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckelphone','wind','Heckelphone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckelphone.piccolo','wind','Piccolo Heckelphone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckelphone-clarinet','wind','Heckelphone Clarinet');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hichiriki','wind','Hichiriki');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hirtenschalmei','wind','Hirtenschalmei');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hne','wind','Hne');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hornpipe','wind','Hornpipe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.houguan','wind','Houguan');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hulusi','wind','Hulusi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.jogi-baja','wind','Jogi-baja');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.ken-bau','wind','Ken-bau');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.khaen-mouth-organ','wind','Khaen mouth organ');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.launeddas','wind','Launeddas');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.maqrunah','wind','Maqrunah');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.melodica','wind','Melodica');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.mijwiz','wind','Mijwiz');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.mizmar','wind','Mizmar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.nadaswaram','wind','Nadaswaram');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe','wind','Oboe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe.bass','wind','Bass Oboe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe.piccolo','wind','Piccolo Oboe');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe-da-caccia','wind','Oboe da Caccia');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe-damore','wind','Oboe D`Amore');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.octavin','wind','Octavin');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.pi','wind','Pi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.pibgorn','wind','Pibgorn');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.piri','wind','Piri');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rackett','wind','Racket');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rauschpfeife','wind','Rauschpfeife');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rhaita','wind','Rhaita');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rothphone','wind','Rothphone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sarrusaphone','wind','Sarrusaphone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxonette','wind','Saxonette');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone','wind','Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.alto','wind','Alto Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.aulochrome','wind','Aulochrome Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.baritone','wind','Baritone Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.bass','wind','Bass Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.contrabass','wind','Contrabass Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.melody','wind','Melody Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.mezzo-soprano','wind','Mezzo-Soprano Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.sopranino','wind','Sopranino Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.sopranissimo','wind','Sopranissimo Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.soprano','wind','Soprano Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.subcontrabass','wind','Subcontrabass Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.tenor','wind','Tenor Saxophone');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.shawm','wind','Shawm');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.shenai','wind','Shenai');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sheng','wind','Sheng');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sipsi','wind','Sipsi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sopila','wind','Sopila');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sorna','wind','Sorna');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sralai','wind','Sralai');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.suona','wind','Suona');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.surnai','wind','Surnai');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.taepyeongso','wind','Taepyeongso');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.tarogato','wind','Tarogato');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.tarogato.ancient','wind','Ancient Tarogato');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.trompeta-china','wind','Trompeta-china');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.tubax','wind','Tubax');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.xaphoon','wind','Xaphoon');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.zhaleika','wind','Zhaleika');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.zurla','wind','Zurla');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.zurna','wind','Zurna');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.agogo-block','wood','Agogo Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.agung-a-tamlang','wood','Agung-a-tamlang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.ahoko','wood','Ahoko');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.bones','wood','Bones');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.castanets','wood','Castanets');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.claves','wood','Claves');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.drum-sticks','wood','Drum Sticks');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.gourd','wood','Gourd');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.granite-block','wood','Granite Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.guban','wood','Guban');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.guiro','wood','Guiro');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.hyoushigi','wood','Hyoushigi');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.ipu','wood','Ipu');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.jam-block','wood','Jam Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kaekeeke','wood','Kaekeeke');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kagul','wood','Kagul');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kalaau','wood','Kalaau');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kashiklar','wood','Kashiklar');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kubing','wood','Kubing');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.pan-clappers','wood','Pan Clappers');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.sand-block','wood','Sand Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.slapstick','wood','Slapstick');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.stir-drum','wood','Stir Drum');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.temple-block','wood','Temple Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.tic-toc-block','wood','Tic-toc Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.tonetang','wood','Tonetang');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.wood-block','wood','Wood Block');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('unspecified.basso.continuo','unspecified','Basso Continuo');
INSERT INTO wmss.wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('unspecified.unspecified','unspecified','Unspecified');


-- wmss.wmss_collection

CREATE TABLE wmss.wmss_collections (
collection_id INTEGER,
collection_description VARCHAR,
CONSTRAINT collection_pkey PRIMARY KEY (collection_id) 
);

INSERT INTO wmss.wmss_collections (collection_id,collection_description) VALUES (0,'Default Collection');
INSERT INTO wmss.wmss_collections (collection_id,collection_description) VALUES (1,'MEI 3.0 Sample Collection');
INSERT INTO wmss.wmss_collections (collection_id,collection_description) VALUES (2,'Universität Münster - Digitale Sammlung');
INSERT INTO wmss.wmss_collections (collection_id,collection_description) VALUES (3,'Universität Münster - MIAMI');
INSERT INTO wmss.wmss_collections (collection_id,collection_description) VALUES (4,'MusicXML Official Samples');
INSERT INTO wmss.wmss_collections (collection_id,collection_description) VALUES (5,'MuseScore Samples');

-- wmss.wmss_scores


CREATE TABLE wmss.wmss_scores (
score_id VARCHAR,
score_name VARCHAR,
score_tonality_note VARCHAR,
score_tonality_mode VARCHAR,
score_creation_date_min DATE,
score_creation_date_max DATE,
score_print_resource VARCHAR,
score_online_resource VARCHAR,
score_thumbnail VARCHAR,
collection_id INTEGER REFERENCES wmss.wmss_collections(collection_id),
CONSTRAINT score_pkey PRIMARY KEY (score_id) 
);

CREATE INDEX idx_score_creation_date_min ON wmss.wmss_scores (score_creation_date_min);
CREATE INDEX idx_score_creation_date_max ON wmss.wmss_scores (score_creation_date_max);
-- wmss.wmss_document_type

CREATE TABLE wmss.wmss_document_type (
document_type_id VARCHAR,
document_type_description VARCHAR,
CONSTRAINT document_type_pkey PRIMARY KEY (document_type_id) 
);

INSERT INTO wmss.wmss_document_type (document_type_id,document_type_description) VALUES ('mei','MEI 3.0 - Music Encoding Initiative');
INSERT INTO wmss.wmss_document_type (document_type_id,document_type_description) VALUES ('musicxml','MusicXML 3.0');
INSERT INTO wmss.wmss_document_type (document_type_id,document_type_description) VALUES ('sib','Sibelius');
-- wmss.wmss_document

CREATE TABLE wmss.wmss_document (
score_id VARCHAR REFERENCES wmss.wmss_scores(score_id),
score_document XML,
document_type_id VARCHAR REFERENCES wmss.wmss_document_type (document_type_id),
PRIMARY KEY (score_id,document_type_id)
);

-- wmss.wmss_score_movements

CREATE TABLE wmss.wmss_score_movements (
score_id VARCHAR REFERENCES wmss.wmss_scores(score_id),
movement_id VARCHAR,
score_movement_description VARCHAR,
movement_tempo VARCHAR,
CONSTRAINT movements_pkey PRIMARY KEY (movement_id,score_id) 
);

-- wmss.wmss_score_movement_performance_medium

CREATE TABLE wmss.wmss_movement_performance_medium (
movement_id VARCHAR,
score_id VARCHAR, 
file_performance_medium_id VARCHAR,
performance_medium_id VARCHAR REFERENCES wmss.wmss_performance_medium (performance_medium_id),
movement_performance_medium_description VARCHAR,
movement_performance_medium_solo BOOLEAN,
FOREIGN KEY(movement_id, score_id) REFERENCES wmss.wmss_score_movements (movement_id, score_id)
);

-- wmss.wmss_roles

CREATE TABLE wmss.wmss_roles (
role_id INTEGER,
role_description VARCHAR,
CONSTRAINT roles_pkey PRIMARY KEY (role_id) 
);

INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (1,'Composer');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (2,'Arranger');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (3,'Encoder');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (4,'Dedicatee');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (5,'Librettist');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (6,'Editor');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (7,'Lyricist');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (8,'Translator');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (9,'Performer');
INSERT INTO wmss.wmss_roles (role_id,role_description) VALUES (99,'Unknown');


-- wmss.wmss_person_roles

CREATE TABLE wmss.wmss_persons (
person_id SERIAL,
person_name VARCHAR,
person_uri VARCHAR,
person_codedval VARCHAR,
person_authority VARCHAR,
CONSTRAINT persons_pkey PRIMARY KEY (person_id) 
);

-- wmss.wmss_score_persons

CREATE TABLE wmss.wmss_score_persons (
role_id INTEGER REFERENCES wmss.wmss_roles (role_id),
person_id INTEGER REFERENCES wmss.wmss_persons (person_id),
score_id VARCHAR REFERENCES wmss.wmss_scores (score_id)
);



-- Notes partitions


--DROP TABLE IF EXISTS wmss.wmss_notes_p1;
--DROP TABLE IF EXISTS wmss.wmss_notes_p2;
--DROP TABLE IF EXISTS wmss.wmss_notes_p3;
--DROP TABLE IF EXISTS wmss.wmss_notes_p4;
--DROP TABLE IF EXISTS wmss.wmss_notes_p5;
--DROP TABLE IF EXISTS wmss.wmss_notes;

CREATE TABLE wmss.wmss_notes (
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


--DROP SEQUENCE IF EXISTS wmss.seq_noteset;
CREATE SEQUENCE wmss.seq_noteset START WITH 1;

-- wmss.wmss_notes partitions (every million records)
								
CREATE TABLE wmss.wmss_notes_p1 (CHECK ( noteset_id >= 1 AND noteset_id <= 1000000 )) INHERITS (wmss.wmss_notes);
CREATE TABLE wmss.wmss_notes_p2 (CHECK ( noteset_id > 1000000 AND noteset_id <= 2000000 )) INHERITS (wmss.wmss_notes);
CREATE TABLE wmss.wmss_notes_p3 (CHECK ( noteset_id > 2000000 AND noteset_id <= 3000000 )) INHERITS (wmss.wmss_notes);
CREATE TABLE wmss.wmss_notes_p4 (CHECK ( noteset_id > 3000000 AND noteset_id <= 4000000 )) INHERITS (wmss.wmss_notes);
CREATE TABLE wmss.wmss_notes_p5 (CHECK ( noteset_id > 4000000 )) INHERITS (wmss.wmss_notes);

CREATE INDEX idx_wmss_notes_p1_noteset ON wmss.wmss_notes_p1 (noteset_id);
CREATE INDEX idx_wmss_notes_p1_pitch ON wmss.wmss_notes_p1 (pitch);
CREATE INDEX idx_wmss_notes_p1_octave ON wmss.wmss_notes_p1 (octave);
CREATE INDEX idx_wmss_notes_p1_duration ON wmss.wmss_notes_p1 (duration);
CREATE INDEX idx_wmss_notes_p1_next_noteset ON wmss.wmss_notes_p1 (next_noteset_id);

CREATE INDEX idx_wmss_notes_p2_noteset ON wmss.wmss_notes_p2 (noteset_id);
CREATE INDEX idx_wmss_notes_p2_pitch ON wmss.wmss_notes_p2 (pitch);
CREATE INDEX idx_wmss_notes_p2_octave ON wmss.wmss_notes_p2 (octave);
CREATE INDEX idx_wmss_notes_p2_duration ON wmss.wmss_notes_p2 (duration);
CREATE INDEX idx_wmss_notes_p2_next_noteset ON wmss.wmss_notes_p2 (next_noteset_id);

CREATE INDEX idx_wmss_notes_p3_noteset ON wmss.wmss_notes_p3 (noteset_id);
CREATE INDEX idx_wmss_notes_p3_pitch ON wmss.wmss_notes_p3 (pitch);
CREATE INDEX idx_wmss_notes_p3_octave ON wmss.wmss_notes_p3 (octave);
CREATE INDEX idx_wmss_notes_p3_duration ON wmss.wmss_notes_p3 (duration);
CREATE INDEX idx_wmss_notes_p3_next_noteset ON wmss.wmss_notes_p3 (next_noteset_id);

CREATE INDEX idx_wmss_notes_p4_noteset ON wmss.wmss_notes_p4 (noteset_id);
CREATE INDEX idx_wmss_notes_p4_pitch ON wmss.wmss_notes_p4 (pitch);
CREATE INDEX idx_wmss_notes_p4_octave ON wmss.wmss_notes_p4 (octave);
CREATE INDEX idx_wmss_notes_p4_duration ON wmss.wmss_notes_p4 (duration);
CREATE INDEX idx_wmss_notes_p4_next_noteset ON wmss.wmss_notes_p4 (next_noteset_id);

CREATE INDEX idx_wmss_notes_p5_noteset ON wmss.wmss_notes_p5 (noteset_id);
CREATE INDEX idx_wmss_notes_p5_pitch ON wmss.wmss_notes_p5 (pitch);
CREATE INDEX idx_wmss_notes_p5_octave ON wmss.wmss_notes_p5 (octave);
CREATE INDEX idx_wmss_notes_p5_duration ON wmss.wmss_notes_p5 (duration);
CREATE INDEX idx_wmss_notes_p5_next_noteset ON wmss.wmss_notes_p5 (next_noteset_id);



CREATE OR REPLACE FUNCTION wmss.wmss_notes_insert_trigger()
RETURNS TRIGGER AS $$
BEGIN					     
    IF ( NEW.noteset_id >= 0 AND NEW.noteset_id <= 1000000 ) THEN
        INSERT INTO wmss.wmss_notes_p1 VALUES (NEW.*);
    ELSIF ( NEW.noteset_id > 1000000 AND NEW.noteset_id <= 2000000) THEN
        INSERT INTO wmss.wmss_notes_p2 VALUES (NEW.*);
    ELSIF ( NEW.noteset_id > 2000000  AND NEW.noteset_id <= 3000000) THEN
        INSERT INTO wmss.wmss_notes_p3 VALUES (NEW.*);
    ELSIF ( NEW.noteset_id > 3000000 AND NEW.noteset_id <= 4000000) THEN
        INSERT INTO wmss.wmss_notes_p4 VALUES (NEW.*);
    ELSIF ( NEW.noteset_id > 4000000 ) THEN
        INSERT INTO wmss.wmss_notes_p5 VALUES (NEW.*);
    ELSE
        RAISE EXCEPTION 'Noteset out of range. Fix the wmss.wmss_notes_insert_trigger() function!';
    END IF;
    RETURN NULL;
END;
$$
LANGUAGE plpgsql;


--DROP TRIGGER IF EXISTS wmss_trigger_insert_notes ON wmss.wmss_notes;

CREATE TRIGGER wmss_trigger_insert_notes
    BEFORE INSERT ON wmss.wmss_notes
    FOR EACH ROW EXECUTE PROCEDURE wmss.wmss_notes_insert_trigger();

