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


INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('brass','Brass');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('drum','Drums');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('effect','Effects');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('keyboard','Keyboards');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('metal','Metals');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('pitched-percussion','Pitched Percussion');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('pluck','Pluck');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('rattle','Rattle');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('strings','Strings');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('synth','Synth');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('voice','Voice');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('wind','Wind');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('wood','Wood');
INSERT INTO wmss_performance_medium_type (performance_medium_type_id, performance_medium_type_description) VALUES ('unspecified','Unspecified');

-- wmss_performance_medium

CREATE TABLE wmss_performance_medium (
performance_medium_id VARCHAR,
performance_medium_type_id VARCHAR REFERENCES wmss_performance_medium_type (performance_medium_type_id),
performance_medium_description VARCHAR,
--language_id VARCHAR REFERENCES wmss_languages (language_id),
CONSTRAINT medium_pkey PRIMARY KEY (performance_medium_id) 
);

INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.alphorn','brass','Alphorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.alto-horn','brass','Alto Horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.baritone-horn','brass','Baritone Horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle','brass','Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.alto','brass','Alto Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.baritone','brass','Baritone Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.contrabass','brass','Contrabass Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.euphonium-bugle','brass','Euphonium-Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.mellophone-bugle','brass','Mellophone-Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.bugle.soprano','brass','Soprano Bugle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cimbasso','brass','Cimbasso');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.conch-shell','brass','Conch Shell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornet','brass','Cornet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornet.soprano','brass','Soprano Cornet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornett','brass','Cornett');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornett.tenor','brass','Tenor Cornett');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.cornettino','brass','Comettino');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.didgeridoo','brass','Didgeridoo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.euphonium','brass','Euphonium');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.fiscorn','brass','Fiscom');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.flugelhorn','brass','Flugelhorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.french-horn','brass','French Horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.group','brass','Group (Brass)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.group.synth','brass','Group Synth. (Brass)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.helicon','brass','Helicon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.horagai','brass','Horagai');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.kuhlohorn','brass','Kuhlohorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.mellophone','brass','Mellophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.natural-horn','brass','Natural Horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.ophicleide','brass','Ophicleide');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.posthorn','brass','Posthorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.rag-dung','brass','Rag Dung');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt','brass','Sackbutt');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt.alto','brass','Alto Sackbutt');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt.bass','brass','Bass Sackbutt');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sackbutt.tenor','brass','Tenor Sackbutt');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.saxhorn','brass','Saxohorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.serpent','brass','Serpent');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.shofar','brass','Shofar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.sousaphone','brass','Sousaphone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone','brass','Trombone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.alto','brass','Alto Trombone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.bass','brass','Bass Trombone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.contrabass','brass','Contrabass Trombone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trombone.tenor','brass','Tenor Trombone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet','brass','Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.baroque','brass','Baroque Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.bass','brass','Bass Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.bflat','brass','B flat Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.c','brass','C Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.d','brass','D Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.piccolo','brass','Piccolo Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.pocket','brass','Pocket Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.slide','brass','Slide Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.trumpet.tenor','brass','Tenor Trumpet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.tuba','brass','Tuba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.tuba.bass','brass','Bass Tuba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.tuba.subcontrabass','brass','Subcontrabass Tuba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.vienna-horn','brass','Vienna Horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.vuvuzela','brass','Vuvuzela');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('brass.wagner-tuba','brass','Wagner Tuba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.apentemma','drum','Apentemma');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ashiko','drum','Ashiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.atabaque','drum','Atabaque');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.atoke','drum','Atoke');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.atsimevu','drum','Atsimevu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.axatse','drum','Axatse');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bass-drum','drum','Bass Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata','drum','Bata');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata.itotele','drum','Itolele Bata');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata.iya','drum','Iya Bata');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bata.okonkolo','drum','Okonkolo Bata');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bendir','drum','Bendir');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bodhran','drum','Bodhran');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bombo','drum','Bombo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bongo','drum','Bongo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.bougarabou','drum','Bougarabou');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.buffalo-drum','drum','Buffalo Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.cajon','drum','Cajon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.chenda','drum','Chenda');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.chu-daiko','drum','Chu Daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.conga','drum','Conga');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.cuica','drum','Cuica');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dabakan','drum','Dabakan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.daff','drum','Daff');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dafli','drum','Dafli');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.daibyosi','drum','Daibyosi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.damroo','drum','Damroo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.darabuka','drum','Darabuka');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.def','drum','Def');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dhol','drum','Dhol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dholak','drum','Dholak');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.djembe','drum','Djembe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.doira','drum','Doira');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dondo','drum','Dondo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.doun-doun-ba','drum','Doun Doun Ba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.duff','drum','Duff');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.dumbek','drum','Dumbek');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.fontomfrom','drum','Fontomfrom');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.frame-drum','drum','Frame Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.frame-drum.arabian','drum','Arabian Frame Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.geduk','drum','Geduk');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ghatam','drum','Ghatam');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.gome','drum','Gome');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group','drum','Group (Drums)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.chinese','drum','Chinese Group (Drums)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.ewe','drum','Ewe Group (Drums)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.indian','drum','Indian Group (Drums)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.group.set','drum','Set Group (Drums)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.hand-drum','drum','Hand Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.hira-daiko','drum','Hira Daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ibo','drum','Ibo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.igihumurizo','drum','Igihumurizo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.inyahura','drum','Inyahura');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ishakwe','drum','Ishakwe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.jang-gu','drum','Jang-gu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kagan','drum','Kagan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kakko','drum','Kakko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kanjira','drum','Kanjira');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kendhang','drum','Kendhang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kendhang.ageng','drum','Ageng Kendhang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kendhang.ciblon','drum','Ciblon Kendhang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kenkeni','drum','Kenkeni');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.khol','drum','Khol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kick-drum','drum','Kick-drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kidi','drum','Kidi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.ko-daiko','drum','Ko-daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kpanlogo','drum','Kpanlogo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.kudum','drum','Kudum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.lambeg','drum','Lambeg');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.lion-drum','drum','Lion Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum','drum','Log Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum.african','drum','African Log Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum.native','drum','Native Log Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.log-drum.nigerian','drum','Ngerian Log Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.madal','drum','Madal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.maddale','drum','Maddale');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.mridangam','drum','Mridangam');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.naal','drum','Naal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.nagado-daiko','drum','Nagado Daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.nagara','drum','Nagara');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.naqara','drum','Naqara');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.o-daiko','drum','O-daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.okawa','drum','Okawa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.okedo-daiko','drum','Okedo-daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pahu-hula','drum','Pahu-hula');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pakhawaj','drum','Pakhawaj');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pandeiro','drum','Pandeiro');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pandero','drum','Pandero');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.powwow','drum','Powwow');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.pueblo','drum','Pueblo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.repinique','drum','Repinique');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.riq','drum','Riq');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.rototom','drum','Rototom');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sabar','drum','Sabar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sakara','drum','Sakara');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sampho','drum','Sampho');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sangban','drum','Sangban');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.shime-daiko','drum','Shime-daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.slit-drum','drum','Slit-drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.slit-drum.krin','drum','Krin Slit-drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.snare-drum','drum','Sanre Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.snare-drum.electric','drum','Electric Sanre Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.sogo','drum','Sogo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.surdo','drum','Surdo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tabla','drum','Tabla');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tabla.bayan','drum','Bayan Tabla');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tabla.dayan','drum','Dayan Tabla');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.taiko','drum','Taiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.talking','drum','Talking');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tama','drum','Tama');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tamborita','drum','Tamborita');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tambourine','drum','Tambourine');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tamte','drum','Tamte');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tangku','drum','Tangku');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tan-tan','drum','Tan-tan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.taphon','drum','Taphon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tar','drum','Tar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tasha','drum','Tasha');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tenor-drum','drum','Tenor Drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.teponaxtli','drum','Teponaxtli');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.thavil','drum','Thavil');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.the-box','drum','The Box');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.timbale','drum','Timbale');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.timpani','drum','Timpani');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tinaja','drum','Tinaja');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.toere','drum','Toere');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tombak','drum','Tombak');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tom-tom','drum','Tom-tom');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tom-tom.synth','drum','Synth. Tom-tom');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tsuzumi','drum','Tsuzumi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.tumbak','drum','Tumbak');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.uchiwa-daiko','drum','Uchiwa-daiko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.udaku','drum','Udaku');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.udu','drum','Udu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('drum.zarb','drum','Zarb');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.aeolian-harp','effect','Aeolian Harp');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.air-horn','effect','Air Horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.applause','effect','Applause');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bass-string-slap','effect','Bass String Slap');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bird','effect','Bird');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bird.nightingale','effect','Nightingale Bird');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bird.tweet','effect','Bird Tweet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.breath','effect','Breath');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bubble','effect','Bubble');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.bullroarer','effect','Bullroarer');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.burst','effect','Burst');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car','effect','Car');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.crash','effect','Car Crash');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.engine','effect','Car Engine');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.pass','effect','Car Pass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.car.stop','effect','Car Stop');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.crickets','effect','Crickets');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.dog','effect','Dog');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.door.creak','effect','Door Creak');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.door.slam','effect','Door Slam');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.explosion','effect','Explosion');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.flute-key-click','effect','Flute Key Click');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.footsteps','effect','Footsteps');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.frogs','effect','Frogs');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.guitar-cutting','effect','Guitar Cutting');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.guitar-fret','effect','Guitar Fret');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.gunshot','effect','Gunshot');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.hand-clap','effect','Hand Clap');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.heartbeat','effect','Heartbeat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.helicopter','effect','Helicopter');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.high-q','effect','High Q');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.horse-gallop','effect','Horse Gallop');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.jet-plane','effect','Jep Plane');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.laser-gun','effect','Laser Gun');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.laugh','effect','Laugh');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.lions-roar','effect','Lions Roar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.machine-gun','effect','Machine Gun');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.marching-machine','effect','Marching Machine');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.metronome-bell','effect','Menotrnome Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.metronome-click','effect','Menotrnome Click');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.pat','effect','Pat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.punch','effect','Punch');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.rain','effect','Rain');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.scratch','effect','Stratch');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.scream','effect','Scream');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.seashore','effect','Seashore');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.siren','effect','Siren');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.slap','effect','Slap');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.snap','effect','Snap');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.stamp','effect','Stamp');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.starship','effect','Starship');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.stream','effect','Stream');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.telephone-ring','effect','Telephone Ring');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.thunder','effect','Thunder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.train','effect','Train');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.trash-can','effect','Train Crash');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whip','effect','Whip');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle','effect','Whistle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.mouth-siren','effect','Mouth Siren');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.police','effect','Police (Whistle)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.slide','effect','Slide (Whistle)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.whistle.train','effect','Train (Whistle)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('effect.wind','effect','Wind');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.accordion','keyboard','Accordion');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.bandoneon','keyboard','Bandoneon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.celesta','keyboard','Celesta');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.clavichord','keyboard','Clavichord');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.clavichord.synth','keyboard','Synth. Clavichord');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.concertina','keyboard','Concertina');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.fortepiano','keyboard','Fortepiano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.harmonium','keyboard','Harmonium');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.harpsichord','keyboard','Harpsichord');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.ondes-martenot','keyboard','Ondes Martenot');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ','keyboard','Organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.drawbar','keyboard','Drawbar Organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.percussive','keyboard','Percussive Organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.pipe','keyboard','Pipe Organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.reed','keyboard','Reed Organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.organ.rotary','keyboard','Rotary Organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano','keyboard','Piano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.electric','keyboard','Electric Piano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.grand','keyboard','Grand Piano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.honky-tonk','keyboard','Honky-tonk Piano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.prepared','keyboard','Prepared Piano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.toy','keyboard','Piano Toy');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.piano.upright','keyboard','Upright Piano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('keyboard.virginal','keyboard','Virginal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.adodo','metal','Adodo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.anvil','metal','Anvil');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.babendil','metal','Babendil');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.agogo','metal','Agogo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.almglocken','metal','Almglocken');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.bell-plate','metal','Bell Plate');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.bell-tree','metal','Bell Tree');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.carillon','metal','Carillon Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.chimes','metal','Chimes Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.chimta','metal','Chimta Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.chippli','metal','Chippli Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.church','metal','Church Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.cowbell','metal','Cowbell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.dawuro','metal','Dawuro Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.gankokwe','metal','Gankokwe Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.ghungroo','metal','Ghungroo Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.hatheli','metal','Hatheli Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.jingle-bell','metal','Jingle Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.khartal','metal','Khartal Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.mark-tree','metal','Mark Tree Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.sistrum','metal','Sistrum Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.sleigh-bells','metal','Sleigh Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.temple','metal','Temple Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.tibetan','metal','Tibetan Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.tinklebell','metal','Tinklebell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.trychel','metal','Trychel Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.wind-chimes','metal','Wind Chimes Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.bells.zills','metal','Zills Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.berimbau','metal','Berimbau');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.brake-drums','metal','Brame Drums');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.crotales','metal','Crotales');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.bo','metal','Bo Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.ceng-ceng','metal','Ceng-Ceng Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.chabara','metal','Chabara Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.chinese','metal','Chinese Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.ching','metal','Ching Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.clash','metal','Clash Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.crash','metal','Crash Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.finger','metal','Finger Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.hand','metal','Hand Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.kesi','metal','Kesi Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.manjeera','metal','Manjeera Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.reverse','metal','Reverse Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.ride','metal','Ride Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.sizzle','metal','Sizzle Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.splash','metal','Splash Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.suspended','metal','Suspended Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.tebyoshi','metal','Tebyoshi Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.tibetan','metal','Tibetan Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.cymbal.tingsha','metal','Tingsha Cymbal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.flexatone','metal','Flexatone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong','metal','Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.ageng','metal','Ageng Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.agung','metal','Agung Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.chanchiki','metal','Chanchiki Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.chinese','metal','Chinese Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.gandingan','metal','Gandingan Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.kempul','metal','Kempul Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.kempyang','metal','Kempyang Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.ketuk','metal','Ketuk Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.kkwenggwari','metal','Kkwenggwari Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.luo','metal','Luo Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.singing','metal','Singing Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.gong.thai','metal','Thai Gong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.guira','metal','Guira');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.hang','metal','Hang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.hi-hat','metal','Hi Hat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.jaw-harp','metal','Jaw Harp');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.kengong','metal','Kengong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.murchang','metal','Murchang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.musical-saw','metal','Musical Saw');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.singing-bowl','metal','Singing Bowl');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.spoons','metal','Spoons');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.steel-drums','metal','Steel Drums');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.tamtam','metal','Tamtam');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.thundersheet','metal','Thundersheet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.triangle','metal','Triangle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('metal.washboard','metal','Washboard');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.angklung','pitched-percussion','Angklung');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.balafon','pitched-percussion','Balafon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bell-lyre','pitched-percussion','Lyre Bell');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bells','pitched-percussion','Bells');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bianqing','pitched-percussion','Bianqing');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bianzhong','pitched-percussion','Bianzhong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.bonang','pitched-percussion','Bonang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.cimbalom','pitched-percussion','Cimbalom');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.crystal-glasses','pitched-percussion','Crystal Glasses');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.dan-tam-thap-luc','pitched-percussion','Dan tam thap luc');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.fangxiang','pitched-percussion','Fangxiang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gandingan-a-kayo','pitched-percussion','Gandingan-a-kayo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gangsa','pitched-percussion','Gangsa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gender','pitched-percussion','Gender');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.giying','pitched-percussion','Giying');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glass-harmonica','pitched-percussion','Glass Harmonica');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glockenspiel','pitched-percussion','Glockenspiel');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glockenspiel.alto','pitched-percussion','Alto Glockenspiel');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.glockenspiel.soprano','pitched-percussion','Soprano Glockenspiel');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.gyil','pitched-percussion','Gyil');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.hammer-dulcimer','pitched-percussion','Hammer Dulcimer');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.handbells','pitched-percussion','Handbells');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kalimba','pitched-percussion','Kalimba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kantil','pitched-percussion','Kantil');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.khim','pitched-percussion','Khim');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kulintang','pitched-percussion','Kulintang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kulintang-a-kayo','pitched-percussion','Kulintang-a-kayo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.kulintang-a-tiniok','pitched-percussion','Kulintang-a-tiniok');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.likembe','pitched-percussion','Likembe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.luntang','pitched-percussion','Luntang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.marimba','pitched-percussion','Marimba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.marimba.bass','pitched-percussion','Bass Marimba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.mbira','pitched-percussion','Mbira');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.mbira.array','pitched-percussion','Arraz Mbira');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone','pitched-percussion','Metallophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone.alto','pitched-percussion','Alto Metallophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone.bass','pitched-percussion','Bass Metallophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.metallophone.soprano','pitched-percussion','Soprano Metallophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.music-box','pitched-percussion','Music Box');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.pelog-panerus','pitched-percussion','Pelog-panerus');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.pemade','pitched-percussion','Pemade');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.penyacah','pitched-percussion','Penyacah');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.ek','pitched-percussion','Ranat ek');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.ek-lek','pitched-percussion','Ranat ek-lek');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.thum','pitched-percussion','Ranat Thum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.ranat.thum-lek','pitched-percussion','Ranat Thum-lek');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.reyong','pitched-percussion','Reyong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.sanza','pitched-percussion','Sanza');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.saron-barung','pitched-percussion','Saron Barung');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.saron-demong','pitched-percussion','Saron Demong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.saron-panerus','pitched-percussion','Saron Panerus');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.slendro-panerus','pitched-percussion','Slendro Panerus');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.slentem','pitched-percussion','Slentem');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.tsymbaly','pitched-percussion','Tsymbaly');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.tubes','pitched-percussion','Tubes');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.tubular-bells','pitched-percussion','Tubular Bells');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.vibraphone','pitched-percussion','Vibraphone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone','pitched-percussion','Xylophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone.alto','pitched-percussion','Alto Xylophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone.bass','pitched-percussion','Bass Xylophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylophone.soprano','pitched-percussion','Soprano Xylophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.xylorimba','pitched-percussion','Xylorimba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pitched-percussion.yangqin','pitched-percussion','Yangqin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.archlute','pluck','Archlute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.autoharp','pluck','Autoharp');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.baglama','pluck','Baglama');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bajo','pluck','Bajo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika','pluck','Balalaika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.alto','pluck','Alto Balalaika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.bass','pluck','Bass Balalaika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.contrabass','pluck','pluck.balalaika.contrabass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.piccolo','pluck','Piccolo Balalaika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.prima','pluck','Prima Balalaika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.balalaika.secunda','pluck','Secunda Balalaika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bandola','pluck','Bandola');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bandura','pluck','Bandura');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bandurria','pluck','Bandurria');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.banjo','pluck','Banjo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.banjo.tenor','pluck','Tenor Banjo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.banjolele','pluck','Banjolele');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.barbat','pluck','Barbat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass','pluck','Bass (Pluck)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.acoustic','pluck','Acoustic Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.bolon','pluck','Bolon Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.electric','pluck','Electric Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.fretless','pluck','Fretless Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.guitarron','pluck','Guitarron Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.synth','pluck','Synth Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.synth.lead','pluck','Synth Lead Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.washtub','pluck','Washtube Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bass.whamola','pluck','Whamola Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.begena','pluck','Begena');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.biwa','pluck','Biwa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bordonua','pluck','Bordonua');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bouzouki','pluck','Bouzouki');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.bouzouki.irish','pluck','Irish Bouzouki');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.celtic-harp','pluck','Celtic Harp');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.charango','pluck','Charango');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.chitarra-battente','pluck','Chitarra Battente');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.cithara','pluck','Cithara');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.cittern','pluck','Cittern');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.cuatro','pluck','Cuatro');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-bau','pluck','Dan-bau');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-nguyet','pluck','Dan-nguyet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-tranh','pluck','Dan-tranh');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dan-ty-ba','pluck','Dan-ty-ba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.diddley-bow','pluck','Diddley-bow');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.domra','pluck','Domra');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.domu','pluck','Domu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dulcimer','pluck','Dulcimer');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.dutar','pluck','Dutar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.duxianqin','pluck','Duxianqin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ektara','pluck','Ektara');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.geomungo','pluck','Geomungo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.gottuvadhyam','pluck','Gottuvadhyam');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar','pluck','Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.acoustic','pluck','Acoustic Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.electric','pluck','Electric Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.nylon-string','pluck','Nylon String Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.pedal-steel','pluck','Guitar Pedal-Steel');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.portuguese','pluck','Portuguese Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.requinto','pluck','Requinto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.resonator','pluck','Resonator');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitar.steel-string','pluck','Steel String Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitjo','pluck','Guitjo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guitjo.double-neck','pluck','Double Neck Guitjo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guqin','pluck','Gugin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guzheng','pluck','Guzheng');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.guzheng.choazhou','pluck','Choazhou Guzheng');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.harp','pluck','Harp');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.harp-guitar','pluck','Harp-Guitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.huapanguera','pluck','Huapanguera');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-huasteca','pluck','Jarana Huasteca');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha','pluck','Jarana Jarocha');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.mosquito','pluck','Jarana Jarocha Mosquito');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.primera','pluck','Jarana Jarocha Primera');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.segunda','pluck','Jarana Jarocha Segunda');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.jarana-jarocha.tercera','pluck','Jarana Jarocha Tercera');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kabosy','pluck','Kabosy');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kantele','pluck','Kantele');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kanun','pluck','Kanun');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kayagum','pluck','Kayagum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kobza','pluck','Kobza');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.komuz','pluck','Komuy');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kora','pluck','Kora');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.koto','pluck','Koto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.kutiyapi','pluck','Kutiyapi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.langeleik','pluck','Langeleik');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.laud','pluck','Laud');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.lute','pluck','Lute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.lyre','pluck','Lyre');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandobass','pluck','Mandobass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandocello','pluck','Mandocello');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandola','pluck','Mandola');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandolin','pluck','Mandolin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandolin.octave','pluck','Mandolin Octave');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandora','pluck','Mandora');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.mandore','pluck','Mandore');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.marovany','pluck','Marovany');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.musical-bow','pluck','Musical Bow');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ngoni','pluck','Ngoni');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.oud','pluck','Oud');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.pipa','pluck','Pipa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.psaltery','pluck','Psaltery');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ruan','pluck','Ruan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sallaneh','pluck','Sallaneh');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sanshin','pluck','Sanshin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.santoor','pluck','Santoor');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sanxian','pluck','Sanxian');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sarod','pluck','Sarod');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.saung','pluck','Saung');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.saz','pluck','Saz');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.se','pluck','Se');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.setar','pluck','Setar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.shamisen','pluck','Shamisen');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.sitar','pluck','Sitar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth','pluck','Synth (Pluck)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth.charang','pluck','pluck.synth.charang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth.chiff','pluck','pluck.synth.chiff');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.synth.stick','pluck','pluck.synth.stick');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura','pluck','pluck.tambura');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura.bulgarian','pluck','pluck.tambura.bulgarian');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura.female','pluck','pluck.tambura.female');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tambura.male','pluck','pluck.tambura.male');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tar','pluck','pluck.tar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.theorbo','pluck','pluck.theorbo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.timple','pluck','pluck.timple');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tiple','pluck','pluck.tiple');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.tres','pluck','pluck.tres');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ukulele','pluck','pluck.ukulele');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.ukulele.tenor','pluck','pluck.ukulele.tenor');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.valiha','pluck','pluck.valiha');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena','pluck','pluck.veena');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena.mohan','pluck','pluck.veena.mohan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena.rudra','pluck','pluck.veena.rudra');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.veena.vichitra','pluck','pluck.veena.vichitra');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.vihuela','pluck','pluck.vihuela');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.vihuela.mexican','pluck','pluck.vihuela.mexican');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.xalam','pluck','pluck.xalam');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.yueqin','pluck','pluck.yueqin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.zither','pluck','pluck.zither');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('pluck.zither.overtone','pluck','pluck.zither.overtone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.afoxe','rattle','rattle.afoxe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.birds','rattle','rattle.birds');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.cabasa','rattle','rattle.cabasa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.caxixi','rattle','rattle.caxixi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.cog','rattle','rattle.cog');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.ganza','rattle','rattle.ganza');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.hosho','rattle','rattle.hosho');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.jawbone','rattle','rattle.jawbone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.kayamba','rattle','rattle.kayamba');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.kpoko-kpoko','rattle','rattle.kpoko-kpoko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.lava-stones','rattle','rattle.lava-stones');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.maraca','rattle','rattle.maraca');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.rain-stick','rattle','rattle.rain-stick');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.ratchet','rattle','rattle.ratchet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.rattle','rattle','rattle.rattle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.shaker','rattle','rattle.shaker');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.shaker.egg','rattle','rattle.shaker.egg');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.shekere','rattle','rattle.shekere');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.sistre','rattle','rattle.sistre');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.televi','rattle','rattle.televi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.vibraslap','rattle','rattle.vibraslap');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('rattle.wasembe','rattle','rattle.wasembe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.ajaeng','strings','Ajaeng');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.arpeggione','strings','Arpeggione');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.baryton','strings','Baryton');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.cello','strings','Violoncello');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.cello.piccolo','strings','Piccolo Violoncello');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.contrabass','strings','Contrabass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.crwth','strings','Crwth');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.dan-gao','strings','Dan Gao');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.dihu','strings','Djhu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.erhu','strings','Erhu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.erxian','strings','Erxian');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.esraj','strings','Esraj');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.fiddle','strings','Fiddle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.fiddle.hardanger','strings','Hardanger Fiddle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.gadulka','strings','Gadulka');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.gaohu','strings','Gaohu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.gehu','strings','Gehu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.group','strings','Group (String)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.group.synth','strings','Synth Group (String)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.haegeum','strings','Haegeum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.hurdy-gurdy','strings','Hurdy Gurdy');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.igil','strings','Igil');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.kamancha','strings','Kamancha');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.kokyu','strings','Kokyu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.laruan','strings','Laruan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.leiqin','strings','Leigin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.lirone','strings','Lirone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.lyra.byzantine','strings','Byzantine Lyra');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.lyra.cretan','strings','Cretan Lyra');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.morin-khuur','strings','Morin Khuur');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.nyckelharpa','strings','Nyckelharpa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.octobass','strings','Octobass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.rebab','strings','Rebab');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.rebec','strings','Rebec');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.sarangi','strings','Sarangi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.stroh-violin','strings','Stroh Violin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.tromba-marina','strings','Tromba Marina');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.vielle','strings','Vielle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol','strings','Viol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.alto','strings','Alto Viol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.bass','strings','Bass Viol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.tenor','strings','Tenor Viol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.treble','strings','Trebble Viol');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viol.violone','strings','Violone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viola','strings','Viola');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.viola-damore','strings','Viola d`Amore');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.violin','strings','Violin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.violono.piccolo','strings','Piccolo Violino');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.violotta','strings','Violotta');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.yayli-tanbur','strings','Yayli Tanbur');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.yazheng','strings','Yazheng');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('strings.zhonghu','strings','Zhonghu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects','synth','synth.effects');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.atmosphere','synth','synth.effects.atmosphere');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.brightness','synth','synth.effects.brightness');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.crystal','synth','synth.effects.crystal');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.echoes','synth','synth.effects.echoes');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.goblins','synth','synth.effects.goblins');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.rain','synth','synth.effects.rain');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.sci-fi','synth','synth.effects.sci-fi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.effects.soundtrack','synth','synth.effects.soundtrack');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.group','synth','synth.group');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.group.fifths','synth','synth.group.fifths');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.group.orchestra','synth','synth.group.orchestra');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad','synth','synth.pad');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.bowed','synth','synth.pad.bowed');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.choir','synth','synth.pad.choir');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.halo','synth','synth.pad.halo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.metallic','synth','synth.pad.metallic');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.polysynth','synth','synth.pad.polysynth');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.sweep','synth','synth.pad.sweep');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.pad.warm','synth','synth.pad.warm');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.theremin','synth','synth.theremin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.tone.sawtooth','synth','synth.tone.sawtooth');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.tone.sine','synth','synth.tone.sine');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('synth.tone.square','synth','synth.tone.square');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.aa','voice','Aa');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.alto','voice','Alto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.aw','voice','Aw');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.baritone','voice','Baritone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.bass','voice','Bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.child','voice','Child');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.countertenor','voice','Countertenor');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.doo','voice','Doo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.ee','voice','Ee');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.female','voice','Female Voice');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.kazoo','voice','Kazoo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.male','voice','Male Voice');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.mezzo-soprano','voice','Mezzo-Soprano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.mm','voice','Mm');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.oo','voice','Oo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.percussion','voice','Percussion (Voice)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.percussion.beatbox','voice','Beatbox Percussion (Voice)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.soprano','voice','Soprano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.synth','voice','Synth Voice');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.talk-box','voice','Talk-Box');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.tenor','voice','Tenor');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('voice.vocals','voice','Vocals');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.bansuri','wind','wind.flutes.bansuri');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.blown-bottle','wind','wind.flutes.blown-bottle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.calliope','wind','wind.flutes.calliope');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.danso','wind','wind.flutes.danso');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.di-zi','wind','wind.flutes.di-zi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.dvojnice','wind','wind.flutes.dvojnice');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.fife','wind','wind.flutes.fife');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flageolet','wind','wind.flutes.flageolet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute','wind','Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.alto','wind','Alto Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.bass','wind','Bass Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.contra-alto','wind','Contra-alto Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.contrabass','wind','Contrabass Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.double-contrabass','wind','Double Contrabass Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.irish','wind','Irish Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.piccolo','wind','Piccolo Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.flute.subcontrabass','wind','Subcontrabass Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.fujara','wind','Fujara Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.gemshorn','wind','Gemshorn Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.hocchiku','wind','Hocchiku Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.hun','wind','Hun Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.kaval','wind','Kaval Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.kawala','wind','Kawala Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.khlui','wind','Khlui Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.knotweed','wind','Knotweed Flute');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.koncovka.alto','wind','Alto Koncovka');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.koudi','wind','Koudi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.ney','wind','Ney');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.nohkan','wind','Nohkan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.nose','wind','Nose (Flutes)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.ocarina','wind','Ocarina');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.overtone.tenor','wind','Tenor Overtone (Flutes)');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.palendag','wind','Palendag');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.panpipes','wind','Panpipes');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.quena','wind','Quena');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder','wind','Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.alto','wind','Alto Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.bass','wind','Bass Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.contrabass','wind','Contrabass Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.descant','wind','Descant Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.garklein','wind','Garklein Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.great-bass','wind','Great Bass Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.sopranino','wind','Sopranino Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.soprano','wind','Soprano Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.recorder.tenor','wind','Tenor Recorder');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.ryuteki','wind','wind.flutes.ryuteki');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shakuhachi','wind','wind.flutes.shakuhachi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shepherds-pipe','wind','wind.flutes.shepherds-pipe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shinobue','wind','wind.flutes.shinobue');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.shvi','wind','wind.flutes.shvi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.suling','wind','wind.flutes.suling');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.tarka','wind','wind.flutes.tarka');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.tumpong','wind','wind.flutes.tumpong');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.venu','wind','wind.flutes.venu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle','wind','wind.flutes.whistle');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.alto','wind','wind.flutes.whistle.alto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.low-irish','wind','wind.flutes.whistle.low-irish');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.shiva','wind','wind.flutes.whistle.shiva');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.slide','wind','wind.flutes.whistle.slide');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.tin','wind','wind.flutes.whistle.tin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.tin.bflat','wind','wind.flutes.whistle.tin.bflat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.whistle.tin.d','wind','wind.flutes.whistle.tin.d');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.xiao','wind','wind.flutes.xiao');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.flutes.xun','wind','wind.flutes.xun');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.group','wind','wind.group');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.jug','wind','wind.jug');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.bagpipes','wind','wind.pipes.bagpipes');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.gaida','wind','wind.pipes.gaida');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.highland','wind','wind.pipes.highland');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pipes.uilleann','wind','wind.pipes.uilleann');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.pungi','wind','wind.pungi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.albogue','wind','wind.reed.albogue');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.alboka','wind','wind.reed.alboka');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.algaita','wind','wind.reed.algaita');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.arghul','wind','wind.reed.arghul');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.basset-horn','wind','wind.reed.basset-horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bassoon','wind','wind.reed.bassoon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bawu','wind','wind.reed.bawu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bifora','wind','wind.reed.bifora');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.bombarde','wind','wind.reed.bombarde');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.chalumeau','wind','wind.reed.chalumeau');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet','wind','wind.reed.clarinet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.a','wind','wind.reed.clarinet.a');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.alto','wind','wind.reed.clarinet.alto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.bass','wind','wind.reed.clarinet.bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.basset','wind','wind.reed.clarinet.basset');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.bflat','wind','wind.reed.clarinet.bflat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.contra-alto','wind','wind.reed.clarinet.contra-alto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.contrabass','wind','wind.reed.clarinet.contrabass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.eflat','wind','wind.reed.clarinet.eflat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinet.piccolo.aflat','wind','wind.reed.clarinet.piccolo.aflat');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.clarinette-damour','wind','wind.reed.clarinette-damour');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.contrabass','wind','wind.reed.contrabass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.contrabassoon','wind','wind.reed.contrabassoon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.cornamuse','wind','wind.reed.cornamuse');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.cromorne','wind','wind.reed.cromorne');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn','wind','wind.reed.crumhorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.alto','wind','wind.reed.crumhorn.alto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.bass','wind','wind.reed.crumhorn.bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.great-bass','wind','wind.reed.crumhorn.great-bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.soprano','wind','wind.reed.crumhorn.soprano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.crumhorn.tenor','wind','wind.reed.crumhorn.tenor');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.diple','wind','wind.reed.diple');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.diplica','wind','wind.reed.diplica');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.duduk','wind','wind.reed.duduk');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.dulcian','wind','wind.reed.dulcian');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.dulzaina','wind','wind.reed.dulzaina');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.english-horn','wind','wind.reed.english-horn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.guanzi','wind','wind.reed.guanzi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.harmonica','wind','wind.reed.harmonica');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.harmonica.bass','wind','wind.reed.harmonica.bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckel-clarina','wind','wind.reed.heckel-clarina');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckelphone','wind','wind.reed.heckelphone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckelphone.piccolo','wind','wind.reed.heckelphone.piccolo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.heckelphone-clarinet','wind','wind.reed.heckelphone-clarinet');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hichiriki','wind','wind.reed.hichiriki');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hirtenschalmei','wind','wind.reed.hirtenschalmei');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hne','wind','wind.reed.hne');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hornpipe','wind','wind.reed.hornpipe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.houguan','wind','wind.reed.houguan');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.hulusi','wind','wind.reed.hulusi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.jogi-baja','wind','wind.reed.jogi-baja');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.ken-bau','wind','wind.reed.ken-bau');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.khaen-mouth-organ','wind','wind.reed.khaen-mouth-organ');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.launeddas','wind','wind.reed.launeddas');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.maqrunah','wind','wind.reed.maqrunah');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.melodica','wind','wind.reed.melodica');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.mijwiz','wind','wind.reed.mijwiz');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.mizmar','wind','wind.reed.mizmar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.nadaswaram','wind','wind.reed.nadaswaram');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe','wind','Oboe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe.bass','wind','Bass Oboe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe.piccolo','wind','Piccolo Oboe');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe-da-caccia','wind','wind.reed.oboe-da-caccia');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.oboe-damore','wind','wind.reed.oboe-damore');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.octavin','wind','wind.reed.octavin');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.pi','wind','wind.reed.pi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.pibgorn','wind','wind.reed.pibgorn');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.piri','wind','wind.reed.piri');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rackett','wind','wind.reed.rackett');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rauschpfeife','wind','wind.reed.rauschpfeife');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rhaita','wind','wind.reed.rhaita');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.rothphone','wind','wind.reed.rothphone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sarrusaphone','wind','wind.reed.sarrusaphone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxonette','wind','wind.reed.saxonette');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone','wind','wind.reed.saxophone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.alto','wind','wind.reed.saxophone.alto');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.aulochrome','wind','wind.reed.saxophone.aulochrome');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.baritone','wind','wind.reed.saxophone.baritone');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.bass','wind','wind.reed.saxophone.bass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.contrabass','wind','wind.reed.saxophone.contrabass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.melody','wind','wind.reed.saxophone.melody');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.mezzo-soprano','wind','wind.reed.saxophone.mezzo-soprano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.sopranino','wind','wind.reed.saxophone.sopranino');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.sopranissimo','wind','wind.reed.saxophone.sopranissimo');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.soprano','wind','wind.reed.saxophone.soprano');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.subcontrabass','wind','wind.reed.saxophone.subcontrabass');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.saxophone.tenor','wind','wind.reed.saxophone.tenor');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.shawm','wind','wind.reed.shawm');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.shenai','wind','wind.reed.shenai');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sheng','wind','wind.reed.sheng');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sipsi','wind','wind.reed.sipsi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sopila','wind','wind.reed.sopila');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sorna','wind','wind.reed.sorna');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.sralai','wind','wind.reed.sralai');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.suona','wind','wind.reed.suona');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.surnai','wind','wind.reed.surnai');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.taepyeongso','wind','wind.reed.taepyeongso');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.tarogato','wind','wind.reed.tarogato');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.tarogato.ancient','wind','wind.reed.tarogato.ancient');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.trompeta-china','wind','wind.reed.trompeta-china');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.tubax','wind','wind.reed.tubax');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.xaphoon','wind','wind.reed.xaphoon');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.zhaleika','wind','wind.reed.zhaleika');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.zurla','wind','wind.reed.zurla');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wind.reed.zurna','wind','wind.reed.zurna');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.agogo-block','wood','wood.agogo-block');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.agung-a-tamlang','wood','wood.agung-a-tamlang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.ahoko','wood','wood.ahoko');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.bones','wood','wood.bones');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.castanets','wood','wood.castanets');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.claves','wood','wood.claves');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.drum-sticks','wood','wood.drum-sticks');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.gourd','wood','wood.gourd');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.granite-block','wood','wood.granite-block');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.guban','wood','wood.guban');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.guiro','wood','wood.guiro');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.hyoushigi','wood','wood.hyoushigi');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.ipu','wood','wood.ipu');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.jam-block','wood','wood.jam-block');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kaekeeke','wood','wood.kaekeeke');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kagul','wood','wood.kagul');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kalaau','wood','wood.kalaau');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kashiklar','wood','wood.kashiklar');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.kubing','wood','wood.kubing');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.pan-clappers','wood','wood.pan-clappers');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.sand-block','wood','wood.sand-block');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.slapstick','wood','wood.slapstick');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.stir-drum','wood','wood.stir-drum');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.temple-block','wood','wood.temple-block');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.tic-toc-block','wood','wood.tic-toc-block');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.tonetang','wood','wood.tonetang');
INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('wood.wood-block','wood','wood.wood-block');

INSERT INTO wmss_performance_medium (performance_medium_id, performance_medium_type_id, performance_medium_description) VALUES ('unspecified','unspecified','Unspecified Performance Medium');


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
INSERT INTO wmss_document_type (document_type_id,document_type_description) VALUES ('musicxml','MusicXML 3.0');

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
--local_performance_medium_id VARCHAR,
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


-- ULB Sammlung


SELECT wmss_insert_score('3e724fa4-f67e-4dff-94d6-a01b78d73049','/home/jones/git/wmss/wmss/data/musicxml/3e724fa4-f67e-4dff-94d6-a01b78d73049@SinfoniaA-DurfurStreicherundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('4a12fece-a4ac-4654-8407-2e32be8d3e56','/home/jones/git/wmss/wmss/data/musicxml/4a12fece-a4ac-4654-8407-2e32be8d3e56@Sonatee-MollfürTraversflöteundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('5c3047f9-f8bc-46eb-8073-0dc3ffb28d30','/home/jones/git/wmss/wmss/data/musicxml/5c3047f9-f8bc-46eb-8073-0dc3ffb28d30@Sedunamortiranno.xml','musicxml',2);
SELECT wmss_insert_score('5e61ff05-7ceb-4e5c-b81e-19f8105f4a53','/home/jones/git/wmss/wmss/data/musicxml/5e61ff05-7ceb-4e5c-b81e-19f8105f4a53@BetrübtesHerz,zerbrich.xml','musicxml',2);
SELECT wmss_insert_score('6be19ce2-fbf2-442f-af37-08b0eb487d45','/home/jones/git/wmss/wmss/data/musicxml/6be19ce2-fbf2-442f-af37-08b0eb487d45@Aquellleggiadrovolto.xml','musicxml',2);
SELECT wmss_insert_score('20b0dcec-ff6a-43d2-8bfe-9e077937a1cf','/home/jones/git/wmss/wmss/data/musicxml/20b0dcec-ff6a-43d2-8bfe-9e077937a1cf@SonateD-Durfür2TraversflötenundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('899c8c9e-dd50-4041-8706-8c0479eb5de5','/home/jones/git/wmss/wmss/data/musicxml/899c8c9e-dd50-4041-8706-8c0479eb5de5@Confusasmarritaspiegartivorrei.xml','musicxml',2);
SELECT wmss_insert_score('01924ae1-3991-464f-97f0-b6498f973560','/home/jones/git/wmss/wmss/data/musicxml/01924ae1-3991-464f-97f0-b6498f973560@SinfoniaB-DurfürStreicherundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('825151','/home/jones/git/wmss/wmss/data/musicxml/825151@TroisDuosConcertans.xml','musicxml',2);
SELECT wmss_insert_score('948617','/home/jones/git/wmss/wmss/data/musicxml/948617@nouvellepolonaisepourlepianoforte.xml','musicxml',2);
SELECT wmss_insert_score('1009591','/home/jones/git/wmss/wmss/data/musicxml/1009591@kleinerfeierabend.xml','musicxml',2);
SELECT wmss_insert_score('1118465','/home/jones/git/wmss/wmss/data/musicxml/1118465@AchtharmloseLiederfürUngelehrte.xml','musicxml',2);
SELECT wmss_insert_score('1639763','/home/jones/git/wmss/wmss/data/musicxml/1639763@AlteundneueVolksliederfürMännerchor.xml','musicxml',2);
SELECT wmss_insert_score('1642539','/home/jones/git/wmss/wmss/data/musicxml/1642539@Amgeburtstagedeskönigs.xml','musicxml',2);
SELECT wmss_insert_score('1883542','/home/jones/git/wmss/wmss/data/musicxml/1883542@18MärschefürTrommelundFlöte.xml','musicxml',2);
SELECT wmss_insert_score('2398460','/home/jones/git/wmss/wmss/data/musicxml/2398460@AchtzehnneueLiederfürGemischtenChor.xml','musicxml',2);
SELECT wmss_insert_score('3079496','/home/jones/git/wmss/wmss/data/musicxml/3079496@AmGrabederMutter.xml','musicxml',2);
SELECT wmss_insert_score('3079600','/home/jones/git/wmss/wmss/data/musicxml/3079600@AchGottwemsollichsklagen.xml','musicxml',2);
SELECT wmss_insert_score('3079686','/home/jones/git/wmss/wmss/data/musicxml/3079686@DasalteLied.xml','musicxml',2);
SELECT wmss_insert_score('3079699','/home/jones/git/wmss/wmss/data/musicxml/3079699@AmRhein.xml','musicxml',2);
SELECT wmss_insert_score('3098742','/home/jones/git/wmss/wmss/data/musicxml/3098742@25stuckefürorgel.xml','musicxml',2);
SELECT wmss_insert_score('3368467','/home/jones/git/wmss/wmss/data/musicxml/3368467@AllesnurumsGeld.xml','musicxml',2);
SELECT wmss_insert_score('3368606','/home/jones/git/wmss/wmss/data/musicxml/3368606@AufnachChina.xml','musicxml',2);
SELECT wmss_insert_score('3530337','/home/jones/git/wmss/wmss/data/musicxml/3530337@AufderAlm.xml','musicxml',2);
SELECT wmss_insert_score('3886826','/home/jones/git/wmss/wmss/data/musicxml/3886826@AlterMünsterischerCantusTriumphalis.xml','musicxml',2);
SELECT wmss_insert_score('4272171','/home/jones/git/wmss/wmss/data/musicxml/4272171@PremièreouverturedeIoperaDonMendoze.xml','musicxml',2);
SELECT wmss_insert_score('4272244','/home/jones/git/wmss/wmss/data/musicxml/4272244@LaCachucha.xml','musicxml',2);
SELECT wmss_insert_score('4276689','/home/jones/git/wmss/wmss/data/musicxml/4276689@OverturadellOperaScipio.xml','musicxml',2);
SELECT wmss_insert_score('4276790','/home/jones/git/wmss/wmss/data/musicxml/4276790@LeBalmasque.xml','musicxml',2);
SELECT wmss_insert_score('4276911','/home/jones/git/wmss/wmss/data/musicxml/4276911@Kinder-Sinfonie.xml','musicxml',2);
SELECT wmss_insert_score('4279917','/home/jones/git/wmss/wmss/data/musicxml/4279917@AndanteetPolacca.xml','musicxml',2);
SELECT wmss_insert_score('4280245','/home/jones/git/wmss/wmss/data/musicxml/4280245@DerGrafvonHabsburg.xml','musicxml',2);
SELECT wmss_insert_score('4280660','/home/jones/git/wmss/wmss/data/musicxml/4280660@sehnsucht.xml','musicxml',2);
SELECT wmss_insert_score('4280885','/home/jones/git/wmss/wmss/data/musicxml/4280885@TeDeumLaudamus.xml','musicxml',2);
SELECT wmss_insert_score('4280969','/home/jones/git/wmss/wmss/data/musicxml/4280969@Andanteetpolonaises.xml','musicxml',2);
SELECT wmss_insert_score('4281006','/home/jones/git/wmss/wmss/data/musicxml/4281006@CantabileetthemevariésuivisdunAllegretto.xml','musicxml',2);
SELECT wmss_insert_score('4281042','/home/jones/git/wmss/wmss/data/musicxml/4281042@Concertinopourlevioloncelle.xml','musicxml',2);
SELECT wmss_insert_score('4283787','/home/jones/git/wmss/wmss/data/musicxml/4283787@FrohwallichzumHeiligthume.xml','musicxml',2);
SELECT wmss_insert_score('4287452','/home/jones/git/wmss/wmss/data/musicxml/4287452@DeuxAirsRussesVariés.xml','musicxml',2);
SELECT wmss_insert_score('4287515','/home/jones/git/wmss/wmss/data/musicxml/4287515@DieHarmoniederSphaeren.xml','musicxml',2);
SELECT wmss_insert_score('4307727','/home/jones/git/wmss/wmss/data/musicxml/4307727@SinfoniaAllaTurca.xml','musicxml',2);
SELECT wmss_insert_score('4308235','/home/jones/git/wmss/wmss/data/musicxml/4308235@Rondeaualamodedeparis.xml','musicxml',2);
SELECT wmss_insert_score('4339428','/home/jones/git/wmss/wmss/data/musicxml/4339428@Concertinosuisse.xml','musicxml',2);
SELECT wmss_insert_score('4339906','/home/jones/git/wmss/wmss/data/musicxml/4339906@TroisSonatesfacilesetProgressives.xml','musicxml',2);
SELECT wmss_insert_score('4339998','/home/jones/git/wmss/wmss/data/musicxml/4339998@1erconcertinopourlevioloncelle.xml','musicxml',2);
SELECT wmss_insert_score('4340117','/home/jones/git/wmss/wmss/data/musicxml/4340117@LaBelleBergère.xml','musicxml',2);
SELECT wmss_insert_score('4341718','/home/jones/git/wmss/wmss/data/musicxml/4341718@troisduosconcertanspourdeuxflutes.xml','musicxml',2);
SELECT wmss_insert_score('4341767','/home/jones/git/wmss/wmss/data/musicxml/4341767@3duospourdeuxVioloncelles.xml','musicxml',2);
SELECT wmss_insert_score('4342391','/home/jones/git/wmss/wmss/data/musicxml/4342391@LeBalmasque.xml','musicxml',2);
SELECT wmss_insert_score('75266515-9803-41de-ac1b-bc2796adc12d','/home/jones/git/wmss/wmss/data/musicxml/75266515-9803-41de-ac1b-bc2796adc12d@SonateA-Durfur2ViolinenundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('a13a60c1-8170-40cb-aaf2-4805931f9465','/home/jones/git/wmss/wmss/data/musicxml/a13a60c1-8170-40cb-aaf2-4805931f9465@konzertBdurfurviolinestreicherundbassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('ad3ed640-90e3-49ce-8ce4-b3dc69c597a5','/home/jones/git/wmss/wmss/data/musicxml/ad3ed640-90e3-49ce-8ce4-b3dc69c597a5@SonateG-Durfür2ViolinenundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('bd2527e4-155e-401d-a6ba-7d1056d09b37','/home/jones/git/wmss/wmss/data/musicxml/bd2527e4-155e-401d-a6ba-7d1056d09b37@SonateA-Durfür2TraversflötenundBassocontinuo.xml','musicxml',2);
SELECT wmss_insert_score('cfc09cec-206c-4a97-b3ac-b4c304080350','/home/jones/git/wmss/wmss/data/musicxml/cfc09cec-206c-4a97-b3ac-b4c304080350@sonate.xml','musicxml',2);
SELECT wmss_insert_score('d7c0073f-8406-4dc0-be5c-3f267e7f5789','/home/jones/git/wmss/wmss/data/musicxml/d7c0073f-8406-4dc0-be5c-3f267e7f5789@andermond.xml','musicxml',2);
SELECT wmss_insert_score('e73f74ec-6711-499f-b3eb-503ca99c6b14','/home/jones/git/wmss/wmss/data/musicxml/e73f74ec-6711-499f-b3eb-503ca99c6b14@sonate.xml','musicxml',2);
SELECT wmss_insert_score('ebb4d5a6-3096-492f-934a-bc7c0e6644bf','/home/jones/git/wmss/wmss/data/musicxml/ebb4d5a6-3096-492f-934a-bc7c0e6644bf@AndenMond.xml','musicxml',2);
