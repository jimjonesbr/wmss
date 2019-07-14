# Web Music Score Service 

[![Build Status](https://travis-ci.com/jimjonesbr/wmss.svg?branch=master)](https://travis-ci.com/jimjonesbr/wmss)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)


The Web Music Score Service (WMSS) provides an interface allowing requests for music scores on the web using platform-independent clients. It serves as an intermediate layer between data sets and application clients, providing standard access to MEI and MusicXML files.

![wmss](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/wmss.png)

The application relies on datasets encoded using the [MusicOWL ontology](http://linkeddata.uni-muenster.de/ontology/musicscore/mso.owl). For more information on creating MusicOWL datasets see [MusicXML to RDF converter](https://github.com/jimjonesbr/musicowl).  

## Index

- [Configuring WMSS](#configuring-wmss)
  - [Server Settings](#server-settings)
  - [Data Source Settings](#data-source-settings)
  - [Configuring Neo4j](#configuring-neo4j)
  - [Starting the Server](#starting-the-server)
- [Requests](#requests)
  - [DescribeService](#describeservice)
    - [Service Description Report](#service-description-report)
  - [ListScores](#listscores)
    - [Data Source](#data-source)  
    - [Title](#title)      
    - [Collections](#collections)  
    - [Persons](#persons)  
    - [Performance Medium (Instrument)](#performance-medium-instrument)  
    - [Performance Medium Type](#performance-medium-type)
    - [Tempo](#tempo)    
    - [Date Issued](#date-issued)  
    - [Format](#format)  
    - [Melodies](#melody)  
    - [Octaves](#octaves)
    - [Durations](#durations)   
    - [Dotted Durations](#dotted-durations) 
    - [Pitches](#pitches)  
    - [Grace Notes](#grace-notes)  
    - [Embedded Note Sequences](#embedded-sequences-note-sequences-inside-chords)  
    - [Chords Search](#chords-search)  
    - [Time Signatures](#time-signatures)
    - [Clefs](#clefs)
    - [Measures](#measures)
    - [Rests](#rests)      
    - [Key Signatures](#key-signatures)  
    - [Score List Report](#score-list-report)  
  - [GetScore](#getscore)
  - [DeleteScore](#deletescore)    
  - [GetLogging](#logging)
- [Exceptions](#exceptions)
  - [Service Exception Report](#service-exception-report)
- [Importing Scores](#importing-scores)  

## [Configuring WMSS](https://github.com/jimjonesbr/wmss/blob/master/README.md#configuring-wmss)

#### [Server Settings](https://github.com/jimjonesbr/wmss/blob/master/README.md#server-settings)

File: `conf/settings.json`

`port`&nbsp;   Listening port for the WMSS server.

`service`&nbsp;   Service name for the server.

`pageSize`&nbsp;   Number of records per page in the Score List Document.

`defaultRequestMode`&nbsp;   Default request mode for the `ListScore` requests (default `full`). Supported request modes are: `simplified`, omitting the movements and performance medium data, and `full` for a complete  Score List Document.

`contact`&nbsp;   E-mail address of the server administrator.

`title`&nbsp;   Server title.

`logPreview`&nbsp;   Number of lines shown in the `GetLogging` request.

`defaultMelodyEncoding`&nbsp;  Default encoding type for melody request. Supported encoding formats: `pea`.

`maxFileSize`&nbsp;   Maximum file size for inserting new scores.

`defaultRDFFormat`&nbsp;  RDF format for inserting new scores. Supported RDF formats: `JSON-LD`, `Turtle`, `RDF/XML` and `N-Triples`.

`defaultCommitSize`&nbsp;   Number of triples partially committed in the insert scores transaction.

#### [Data Source Settings](https://github.com/jimjonesbr/wmss/blob/master/README.md#data-source-settings)
(multiple data sources supported)

File: `conf/sources.json` 

`id`&nbsp;   Data source identifier.

`info`&nbsp;   Data source complementary information.

`active`&nbsp;   Indicates if the data source is available for requests. Supported values: `true`, `false`.

`storage`&nbsp;   Data source storage technology. Supported storages: `neo4j`

`type`&nbsp;   Data source type. Supported types: `lpg` (Label Property Graphs)

`port`&nbsp;   Listening port for the data source.

`repository`&nbsp;   Data source repository (if applicable).

`version`&nbsp;   Version of the data source application.

`user`/`password`&nbsp;   Credentials for accessing the data source.



#### [Configuring Neo4j](https://github.com/jimjonesbr/wmss/blob/master/README.md#configuring-neo4j)

In order to be able to import RDF data into Neo4j, WMSS requires the plugins [neosemantics](https://github.com/jbarrasa/neosemantics/) and [APOC](https://github.com/neo4j-contrib/neo4j-apoc-procedures/). Neo4j and its plugins are constantly being updated, which makes the compatibility among them quite hard sometimes. To make things a little easier, you may use this compatibility matrix for Neo4j 3.5.5 - in case you're installing Neo4j from scratch.

| Neo4j | neosemantics          | APOC             |
|-------|-----------------------|------------------|
| [3.5.5](https://neo4j.com/download-center/) | [neosemantics-3.5.0.1](https://github.com/jbarrasa/neosemantics/releases/download/3.5.0.1/neosemantics-3.5.0.1.jar) | [apoc-3.5.0.3-all](https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/3.5.0.1/apoc-3.5.0.1-all.jar) |

Depending on the amount of RDF data you want to import, consider increasing the memory settings in the `<NEO_HOME>/conf/neo4j.conf` file. 
Example:
```
dbms.memory.heap.initial_size=12G
dbms.memory.heap.max_size=12G
```

Click [here](https://neo4j.com/developer/guide-performance-tuning/) for more information on the Neo4j memory guidelines.

Also add the following lines to enable the usage of APOC and neosemantics plugins:

```
dbms.unmanaged_extension_classes=semantics.extension=/rdf
dbms.security.procedures.unrestricted=apoc.*
```

Check their respectively repositories for more information.


##### Running Neo4j using Docker

The Neo4j official docker image does not include the plugins WMSS needs. So before we deploy our [docker container](https://www.docker.com/resources/what-container), let's first create a volume where Neo4j can find these plugins:

```shell
$ mkdir neo4j_plugins
$ wget https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/download/3.5.0.1/apoc-3.5.0.1-all.jar -P neo4j_plugins/
$ wget https://github.com/jbarrasa/neosemantics/releases/download/3.5.0.1/neosemantics-3.5.0.1.jar -P neo4j_plugins/
```

Optionally, we can also create a volume for managing Neo4j internal data, so that we have directly access to it via an external folder.

```shell
$ mkdir neo4j_data
```

Once our plugins and data volumes are properly configured, we can finally deploy our Neo4j instance. Using `docker-compose` we can add all our settings in single file, so that our container starts just the way we want it to, the `docker-compose.yml`:

```yml
version: '2'
services:
  neo4j:
    container_name: neo4j-wmss
    network_mode: host  
    image: neo4j:3.5.5
    volumes:
      - ./neo4j_plugins:/plugins
      - ./neo4j_data:/data      
    environment:
      - NEO4J_dbms_security_procedures_unrestricted=apoc.*
      - NEO4J_dbms_connectors_default_listen_address=0.0.0.0     
      - NEO4J_dbms_unmanaged_extension_classes=semantics.extension=/rdf
      - NEO4J_AUTH=none   
      - NEO4J_dbms_memory_heap_maxSize=4G
      - NEO4J_dbms_memory_heap.initial_size=4G
      - NEO4J_dbms_memory_heap.max_size=1G
    ports:
      - 7474:7474
      - 7687:7687
volumes:
  neo4j_plugins:
  neo4j_data:    
```

 A few comments to this file:
 
 `network_mode: host` 
 
 This parameter allows connections from inside the container to the host. 
 
 `image: neo4j:3.5.5` 
 
Downloads the Neo4j 3.5.5 image. Changing it to `neo4j:latest` downloads the most recent Neo4j version, but before doing something that will give you a lot of headache, check if the previously downloaded plugins are compatible with the latest Neo4j version.
 
 ```yml
 volumes:
      - ./neo4j_plugins:/plugins
      - ./neo4j_data:/data
 ```

Links container and host directories, which we previously configured.


Here you can tune your Neo4j instace just the way you'd have done in the `neo4j.conf` file.

`- NEO4J_dbms_security_procedures_unrestricted=apoc.*` 

Allows APOC to use internal API procedure

`- NEO4J_dbms_connectors_default_listen_address=0.0.0.0`

Default network interface to listen for incoming connections. To listen for connections on all interfaces, use "0.0.0.0".

`- NEO4J_dbms_unmanaged_extension_classes=semantics.extension=/rdf`

Enables the usage of `neosemantics`

`- NEO4J_AUTH=none`

In this variabe you can set the initial credentials to access Neo4j, e.g. `NEO4J_AUTH=neo4j/secret` says that the user `neo4j` has the initial password `secret`. Setting it to `none` grants access without any credentials.

```yml
- NEO4J_dbms_memory_heap_maxSize=4G
- NEO4J_dbms_memory_heap.initial_size=4G
- NEO4J_dbms_memory_heap.max_size=1G
```

Tunes the [memory and cache](https://neo4j.com/developer/guide-performance-tuning/) usage for Neo4j. 


Once you have your plugin environment and your `docker-compose.yml` ready, just run the container with the following command:

`$ docker-compose up`

If you want it to run in the backgoud, just add the parameter `-d` to it:

`$ docker-compose up -d`

To shut it down:

`$ docker-compose down`





#### [Starting the Server](https://github.com/jimjonesbr/wmss/blob/master/README.md#starting-the-server)

**From the console**:
```shell
$ java -jar wmss-[VERSION].jar
```

**For the source code**: Execute the main method of the Java class `de.wwu.wmss.web.Start.java`

After successfully starting the server you will see a message like this:

```
Web Music Score Service - University of Münster
Service Name: wmss
Default Protocol: 1.0
WMSS Version: 1.0.0
Port: 8295
Application Startup: 2018/11/19 14:46:14
Default Melody Encoding: pea
Time-out: 5000ms
Page Size: 10 records 
System Administrator: jim.jones@math.uni-muenster.de
```

After seeing this message, you can access the server API via the HTTP requests described bellow.

## [Requests](https://github.com/jimjonesbr/wmss/blob/master/README.md#requests)

The WMSS communication protocol is based on the following HTTP requests: `DescribeService`, `ListScores`, `GetScore`, `GetLogging`.

### [DescribeService](https://github.com/jimjonesbr/wmss/blob/master/README.md#describeservice)

 
Lists all service related information as well as all repositories available: 
 
 ```http
 http://localhost:8295/wmss?request=DescribeService
 ```

The Service Description Report collects all available properties and filter possibilities from each available data source, giving the client all possible filters for each filter option, such as tonalities, tempo markings or instruments.

#### [Service Description Report](https://github.com/jimjonesbr/wmss/blob/master/README.md#service-description-report) 
The [Service Description Document](https://github.com/jimjonesbr/wmss/blob/master/wmss/data/system/reports/DescribeService.json) is provided as JSON and is structured as follows:
 
`appVersion`&nbsp;   WMSS version.

`type`&nbsp;   `ServiceDescriptionReport` (Standard value for Service Description Report)

`title`&nbsp;   Service title of description.

`contact`&nbsp;   Administrator e-mail address.

`service`&nbsp;   Service name.

`port`&nbsp;   Listening port for the service.

`timeout`&nbsp;   Time-out for server internal requests.

`pageSize`&nbsp;   Default page size for ListScores request.

`startup`&nbsp;   Service startup time.

`environment`&nbsp;  Environment settings.

&nbsp;&nbsp;&nbsp;&nbsp;`java`&nbsp;  Java version.

&nbsp;&nbsp;&nbsp;&nbsp;`os`&nbsp;  Operating system description.

`supportedProtocols`&nbsp;   Protocols supported by the service.

`datasources`&nbsp;  Data sources available in the system.

&nbsp;&nbsp;&nbsp;&nbsp;`id`&nbsp;  Data source identifier.

&nbsp;&nbsp;&nbsp;&nbsp;`host`&nbsp;  Data source hosting server.

&nbsp;&nbsp;&nbsp;&nbsp;`port`&nbsp;  Listening port for the data storage.

&nbsp;&nbsp;&nbsp;&nbsp;`active`&nbsp;  Boolean value to enable or disable access to a data source.

&nbsp;&nbsp;&nbsp;&nbsp;`type`&nbsp;  Data source type. Currently supported values are `database` and `triplestore`.

&nbsp;&nbsp;&nbsp;&nbsp;`storage`&nbsp;  Storage technology used in a data source. Currently supported values are: `postgresql` and `graphdb`.

&nbsp;&nbsp;&nbsp;&nbsp;`repository`&nbsp;  Specific repository of a data storage, e.g database for RDMS or a repository/named graph for triple stores.

&nbsp;&nbsp;&nbsp;&nbsp;`version`&nbsp;  Version for the data storage.

&nbsp;&nbsp;&nbsp;&nbsp;`user`&nbsp;  Username used for accessing the data source.

&nbsp;&nbsp;&nbsp;&nbsp;`info`&nbsp;  Data source description.

&nbsp;&nbsp;&nbsp;&nbsp;`totalScores`&nbsp;  Number of available music scores for the data storage.

&nbsp;&nbsp;&nbsp;&nbsp;`formats`&nbsp;  Formats available in a data source, e.g. MusicXML, MEI.

&nbsp;&nbsp;&nbsp;&nbsp;`tempoMarkings`&nbsp;  Tempo markings available in a data source, e.g. adagion, andante.

&nbsp;&nbsp;&nbsp;&nbsp;`tonalities`&nbsp;  Tonalities available in a data source, e.g. C major, E minor.

&nbsp;&nbsp;&nbsp;&nbsp;`collections`&nbsp;  Collections available in a data source.

&nbsp;&nbsp;&nbsp;&nbsp;`performanceMediums`&nbsp;  Performance mediums (instruments) available in a data source.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`mediumTypeId`&nbsp;  Identifier of the performance medium type

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`mediumTypeDescription`&nbsp;  Description of the performance medium type

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`mediums`&nbsp;  Performance mediums (instruments) available for a certain performance medium type

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`mediumId`&nbsp;  Performance medium identifier.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`mediumDescription`&nbsp;  Performance medium description.

An example of a Service Description Report can be found [here](https://github.com/jimjonesbr/wmss/tree/master/wmss/data/system/reports/DescribeService.json).
### [ListScores](https://github.com/jimjonesbr/wmss/blob/master/README.md#listscores)
 
 Lists all scores from available repositories. 
 
 ```http
 http://localhost:8295/wmss?request=ListScores
 ```
 
 In order to facilitate the music score discovery, the ListScores request offers several filter capabilities:
 
 #### [Data Source](https://github.com/jimjonesbr/wmss/blob/master/README.md#data-source)
 Parameter: `source`
 
 Constraints the __Score List Document__ to a specific data source:
 
 ```http
 http://localhost:8295/wmss?request=ListScores&source=neo4j_local
 ```

 #### [Title](https://github.com/jimjonesbr/wmss/blob/master/README.md#title)
Parameter: `title` 
 
 Searches for scores containing a certain string in the title (case insensitive).
 
 ```http
 http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&title=cellokonzert
```
 
 #### [Collections](https://github.com/jimjonesbr/wmss/blob/master/README.md#collections)
Parameter: `collection`

To facilitate the management of large repositories, WMSS offers the possibility to add music scores to specific collections. The collection uri, required for this parameter, is delivered together with the music score in the Score List and Service Description Reports.

```http
 http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&collection=https://url.collection.de
```

#### [Persons](https://github.com/jimjonesbr/wmss/blob/master/README.md#persons)

Parameters: `person` / `personRole`

Selects all music scores containing specific persons and optionally with their respective roles. For instance, a request to list all scores from the person "Elgar" as a "Composer" is enconded like this:

```http
 http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&person=http://dbpedia.org/resource/Edward_Elgar&personRole=composer
```

The __personRole__ parameter may contain the following values:

* `composer`
* `arranger`
* `encoder`
* `dedicatee`
* `librettist`
* `editor`
* `lyricist`
* `translator`
* `performer`


#### [Performance Medium (Instrument)](https://github.com/jimjonesbr/wmss/blob/master/README.md#performance-medium-instrument)

**Parameters**: `performanceMedium` / `solo`

Selects all music scores containing specific performance mediums. The performance mediums are structure follows the principles adopted by [MusicXML 3.0 Standard Sounds](https://github.com/w3c/musicxml/blob/v3.1/schema/sounds.xml). For instance, requesting a list of all scores containing cello voices can be enconded like this:

 ```http
 http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&performanceMedium=strings.cello
```

To constraint the search for the given performance medium to only solo mediums, use the *solo* parameter: 

 ```http
 http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&performanceMedium=strings.cello&solo=true
```

A complete list of performance mediums containing approx. 900 items can be found [here](https://github.com/jimjonesbr/wmss/tree/master/wmss/data/system/mediums.csv).

#### [Performance Medium Type](https://github.com/jimjonesbr/wmss/blob/master/README.md#performance-medium-type)

Parameter: `performanceMediumType`

It is also possible to select music scores based on performance medium types, e.g. Strings, Keyboard. The example bellow selects all records that contain movements that are played with bowed string instruments:
 
 ```http
 http://localhost:8295/wmss?request=ListScores&source=neo4j_local&performanceMediumType=strings
```

The performanceMediumType paramater is also based on the [MusicXML 3.0 Standard Sounds](https://github.com/w3c/musicxml/blob/v3.1/schema/sounds.xml) and the following codes:

| code|medium type| code|medium type|
|:-:|:-:|:-:|:-:|
|`brass`|Brass|`pitched-percussion`|Pitched Percussion|
|`drum`|Drums|`pluck`|Plucked|
|`key`|Keyboard|`rattle`|Rattle|
|`metal`|Metals|`strings`|Strings|
|`synth`|Synthesizer|`voice`|Voices|
|`wind`|Wind|`wood`|wood|


#### [Tempo](https://github.com/jimjonesbr/wmss/blob/master/README.md#tempo)

Parameters: `tempoBeatUnit` / `tempoBeatsPerMinute`

Selects records containing movements played in a specific tempo, e.g. *adagio*, *largo*, *andante*, etc. Tempo markings may vary depending on the country of orign and century of composition, therefore tempo searches are encoded in two abstract parameters, namely `tempoBeatsPerMinute` and `tempoBeatUnit`. Beat units indicates the graphical note type to use in a metronome mark, which follows the principles adpoted by the [MusicXML Beat-Unit Element](https://usermanuals.musicxml.com/MusicXML/Content/EL-MusicXML-beat-unit.htm). The beats per unit parameter can be provided as a single integer value or an interval thereof. For instance, a *quarter* beat unit with an interval of 100-125 beats per minute, can be encoded as follows:

 ```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&tempoBeatUnit=quarter&tempoBeatsPerMinute=100-125
```
 
#### [Date Issued](https://github.com/jimjonesbr/wmss/blob/master/README.md#date-issued)

Parameter: `dateIssued`

Selects records composed at a given date or time interval, e.g. 1910-1920:

 ```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&dateIssued=1910-1920
```

Dates and intervals must be encoded as `yyyyMMdd`, `yyyyMM` or `yyyy`.


#### [Format](https://github.com/jimjonesbr/wmss/blob/master/README.md#format)
Parameter: `format`

Selects records available in a specific format. The supported formats are:

  * `mei` (Music Encoding Initiative files)
  * `musicxml` (MusicXML files)
  
 ```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&format=musicxml
```

#### [Melody](https://github.com/jimjonesbr/wmss/blob/master/README.md#melody)
Parameter: `melody`


Selects records containing a specific a sequence of notes or phrases (not limited to *incipt*) throughout the database, encoded using the [Plaine & Easie musical notation](https://www.iaml.info/plaine-easie-code#toc-4) (PEA).  For instance, the value `,8AB'CDxDE` - which is going to be used throughout this section - corresponds to ..

![melody_sample](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/melody_sample.jpg)

Notes: `A` 3rd octave, `B` 3rd octave, `C` 4th octave, `D` 4th octave, `D#` 4th octave and `E` 4th octave. 

Duration:  `Eighth` 

..  and can be searched like this:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE
```

#### [Octaves](https://github.com/jimjonesbr/wmss/blob/master/README.md#octaves)
Parameter: `ignoreOctave`

To search for melodies encoded in specific octaves, set the parameter `ignoreOctave` to `false` (`true` by default). Note that in the PEA string the 4th octave is assumed, if no octave is explicitly defined. The following example searches for scores matching the sequence `A` 3rd octave, `B` 3rd octave, `C` 4th octave, `D` 4th octave, `D#` 4th octave and `E` 4th octave, all with the duration `eighth` (as described above):

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE&ignoreOctave=false
```

#### [Durations](https://github.com/jimjonesbr/wmss/blob/master/README.md#durations) 
Parameter: `ignoreDuration`

It is possible to search only for a sequence of pitches, ignoring their durations. It can be achieved by means of setting the parameter `ignoreDuration` to `true` (`false` by default). The following example searches for all scores containing the pitch sequence `A`, `B`, `C`, `D`, `D#` and `E`, ignoring their durations:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE&ignoreDuration=true
```

#### [Dotted Durations](https://github.com/jimjonesbr/wmss/blob/master/README.md#dotted-durations) 

To extend the note duration add either a `.` (dot), `..` (double dot) or `...` (triple dot) right after the note duration, as described at [Plaine & Easie rhythmic values](https://www.iaml.info/plaine-easie-code#toc-7). For instance `4.G4.xF4.G4.xF`

![embedded_sequence](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/dotted.jpg)

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=4.G4.xF4.G4.xF
```


#### [Pitches](https://github.com/jimjonesbr/wmss/blob/master/README.md#pitches) 
Parameter: `ignorePitch`

If you're only looking for a sequence of rhythmical elements (useful for percussionists), just set the parameter `ignorePitch` to `true` (`false` by default). The following example searches for all scores containing a sequence of 6 `eighth` notes, ignoring pitches:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE&ignorePitch=true
```

#### [Grace Notes](https://github.com/jimjonesbr/wmss/blob/master/README.md#grace-notes) 
Parameters: `melody`

Read this carefully. In case a sequence of notes in the database contains a grace note, a few things need to be taken into account:

* __Grace notes are bypassed__: It creates a link between the last __non__-grace note and the next one. This link enables searches that do not explicity 
* __Grace notes are also explicity encoded__: Grace notes are encoded as such and are linked to their predecessors and successors in the note sequence. 

In other words, a search for `,8AGxFgAG2C` or `,8AGxFG2C` will return the following match:


![embedded_sequence](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/grace.jpg)

Grace notes in melody searches are to be encoded using either `g` (acciaccatura) or `q` (appoggiatura), according to the [Plaine & Easie notation](https://www.iaml.info/plaine-easie-code#toc-11). 

#### [Embedded Sequences (Note sequences inside chords)](https://github.com/jimjonesbr/wmss/blob/master/README.md#embedded-sequences-note-sequences-inside-chords)
Parameter: `ignoreChords`

It is also possible to look for sequences whose notes are inside of chords. To achieve this, set the parameter `ignoreChords` to `false` (`true` by default). 

Consider the following chords ..

![three_chords](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/3_chords.jpg)

.. and the following search criteria `,,2GB8G`, which are notes embedded in the chords above:

![embedded_sequence](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/embedded_sequence.jpg)

To be able to find such an *embedded sequence*, set the parameter `ignoreChords` to `false`:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,,2GB8G&ignoreChords=false
```
**Note**: This feature assumes that the elements of such an embedded sequence are notes of the same voice. 

#### [Chords search](https://github.com/jimjonesbr/wmss/blob/master/README.md#chords-search)

Parameter: `melody`

Melodies containing chords can be searched by means of using the [PEA chords notation](https://www.iaml.info/plaine-easie-code#toc-19). The PEA notation states that every note of a chord is to be separated by `^`, starting by the upper note; then followed by the lower ones.

Searching for the following chord `,,2E^B^,G^'E` .. 

![embedded_sequence](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/1_chord.jpg)

.. can be done like this:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,,2E^B^,G^'E&ignoreOctave=false
```

#### [Time signatures](https://github.com/jimjonesbr/wmss/blob/master/README.md#time-signatures)
Parameters: `melody` / `time`

Time signatures are to be encoded according to the [PEA key time signature notation](https://www.iaml.info/plaine-easie-code#toc-3). Time signatures embedded in melodies are preceded by `@` and followed by beats and beat unit, separated by `/`, e.g. `@3/4` (three-four or waltz time), `@2/4` (march time). Common time signatures can be also represented as `@c` and will be considered by the system as `@4/4`.

Examples

Common signature: `@c 8ABCDxDE`

![common_timesignature](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/common_time.jpg)
```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=@c 8ABCDxDE
```
Waltz time: `@3/4 8ABCDxDE`

![waltz_timesignature](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/waltz_time.jpg)
```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=@3/4 8ABCDxDE
```

Alternatively, time signatures alone can be searched using the parameter `time`: 

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&time=4/4
```

See also: *UNIMARC field 036 $o — MARC21 field 789 $g — MAB field 681 $h (RISM field 823)*


#### [Clefs](https://github.com/jimjonesbr/wmss/blob/master/README.md#clefs)
Parameters: `melody` / `clef`

Clefs are to be encoded according to the [PEA clef notation](https://www.iaml.info/plaine-easie-code#toc-1). Clefs embedded in melodies are preceded by `%`, and are three characters long. The first character specifies the clef shape (`G`,`C`,`F`,`g`). The second character is `-` to indicate modern notation, `+` to indicate mensural notation. The third character (numeric 1-5) indicates the position of the clef on the staff, starting from the bottom line. 

Clef examples: `G-2` (trebble clef), `F-4` (bass clef), `C-3` (alto clef), `C-4` (tenor clef).

<img src="https://upload.wikimedia.org/wikipedia/en/thumb/2/25/Common_clefs.svg/212px-Common_clefs.svg.png">

Request example `%C-4 ,8AB'CDxD`:

![tenor_clef](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/tenor_clef.jpg)
```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&melody=%C-4 ,8AB'CDxD
```
Alternatively, clefs alone can be searched using the parameter `clef`: 

```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&clef=F-4
```
See also: *UNIMARC field 036 $m — MARC21 field 789 $e — MAB field 681 $j (RISM field 820)*


#### [Measures](https://github.com/jimjonesbr/wmss/blob/master/README.md#measures)
Parameters: `melody`

If necessary, it is also possible to look for melodies contained in fixed measures. For instance, the melody `'4xF8G4xF8A4B8A4G8E4D8E4C,8B` will be searched no matter how the notes are distributed:

![no_measure](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/melody_no-measure.jpg)

```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&melody='4xF8G4xF8A4B8A4G8E4D8E4C,8B
```

By splitting the melodies with the character `/`, the system will look for exact matches with the given measure/notes distribution. This example will look for the given melody contained in exactly two measures `'4xF8G4xF8A4B8A4/G8E4D8E4C,8B`:

![measures](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/melody_measure.png)

```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&melody='4xF8G4xF8A4B8A4/G8E4D8E4C,8B
```

#### [Rests](https://github.com/jimjonesbr/wmss/blob/master/README.md#rests) 
Parameters: `melody`

Melody containing [rests](https://www.iaml.info/plaine-easie-code#toc-12) can be encoded by replacing the pitch with a `-`, e.g. `,,2E-/-4.-6,,B,xC`

![rests](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/rest.jpg)


#### [Key signatures](https://github.com/jimjonesbr/wmss/blob/master/README.md#key-signatures)
Parameters: `melody` / `key`

Keys signatures are to be encoded according to the [PEA key signature notation](https://www.iaml.info/plaine-easie-code#toc-2). Key signatures embedded in melodies are preceded by the character `$`; The symbol `x` indicates sharpened keys, `b` flattened keys; the symbol is followed by the capital letters indicating the altered notes.

Sharpened keys have to be encoded in the following order: `F♯ C♯ G♯ D♯ A♯ E♯ B♯`

||Major key   | Minor Key  | PEA key 
|:-:|:-:|:-:|:-:
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/C-major_a-minor.svg/220px-C-major_a-minor.svg.png" width="80">|C major|A minor|`$`
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/G-major_e-minor.svg/220px-G-major_e-minor.svg.png" width="80">|G major|E minor|`xF`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/D-major_h-minor.svg/220px-D-major_h-minor.svg.png" width="80">|D major|B minor|`xFC`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/A-major_f-sharp-minor.svg/220px-A-major_f-sharp-minor.svg.png" width="80">|A major|F♯ minor|`xFCG`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f3/E-major_c-sharp-minor.svg/220px-E-major_c-sharp-minor.svg.png" width="80">|E major|C♯ minor|`xFCGD`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/B-major_g-sharp-minor.svg/220px-B-major_g-sharp-minor.svg.png" width="80">|B major|G♯ minor|`xFCGDA`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3d/F-sharp-major_d-sharp-minor.svg/220px-F-sharp-major_d-sharp-minor.svg.png" width="80">|F♯ major|D♯ minor|`xFCGDAE`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/2d/C-sharp-major_a-sharp-minor.svg/220px-C-sharp-major_a-sharp-minor.svg.png" width="80">|C♯ major|A♯ minor|`xFCGDAEB`|

Flattened keys have to be encoded in the following order: `B♭ E♭ A♭ D♭ G♭ C♭ F♭`

||Major key   | Minor Key  | PEA key 
|:-:|:-:|:-:|:-:
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/33/C-major_a-minor.svg/220px-C-major_a-minor.svg.png" width="80">|C major|A minor|`$`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b4/F-major_d-minor.svg/220px-F-major_d-minor.svg.png" width="80">|F major|D minor|`bB`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/B-flat-major_g-minor.svg/220px-B-flat-major_g-minor.svg.png" width="80">|B♭ major|G minor|`bBE`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/cf/E-flat-major_c-minor.svg/220px-E-flat-major_c-minor.svg.png" width="80">|E♭ major|C minor|`bBEA`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/A-flat-major_f-minor.svg/100px-A-flat-major_f-minor.svg.png" width="80">|A♭ major|F minor|`bBEAD`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/D-flat-major_b-flat-minor.svg/100px-D-flat-major_b-flat-minor.svg.png" width="80">|D♭ major|B♭ minor|`bBEADG`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/76/G-flat-major_e-flat-minor.svg/100px-G-flat-major_e-flat-minor.svg.png" width="80">|G♭ major|E♭ minor|`bBEADGC`|
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/C-flat-major_a-flat-minor.svg/100px-C-flat-major_a-flat-minor.svg.png" width="80">|C♭ major|A♭ minor|`bBEADGCF`|

See also: *UNIMARC field 036 $n — MARC21 field 789 $f — MAB field 681 $k (RISM field 826 — first part)*

To search for melodies encoded with an specific key signature, place the key before the melody (preceded by space). For instance, searching the previously mentioned melody with the signature G Major / E minor can be done as follows:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=$xF ,8AB'CDxDE
```

Alternatively, key signatures alone can be searched using the parameter `key`. The following request seraches for all music scores containing measures written in `C♯ minor`:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&key=xFCGDA
```


##### ListScore Request Examples 

The following example deals with a melody of 36 notes in containing several durations and octaves:

![elgar_theme](https://github.com/jimjonesbr/wmss/blob/master/wmss/web/img/elgar_theme.jpg)

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody='4xF8G4xF8A4B8A4G8E4D8E4C,8B4A8B4A'8C4D8C,4B8G4xF8G4E8D4xC8D4xC8E4xF8E4D,,8B4xA8B4G8xF2B&ignoreOctave=false
```



#### [Score List Report](https://github.com/jimjonesbr/wmss/blob/master/README.md#score-list-report)
The Score List Document is provided as JSON and is structured as follows:


An example of a Score List Report can be found [here](https://github.com/jimjonesbr/wmss/tree/master/wmss/data/system/reports/ListScores.json).

  ### [GetScore](https://github.com/jimjonesbr/wmss/blob/master/README.md#getscore)
  
Retrieves a specific score based on its identifier:

  ```http
  http://localhost:8295/wmss/?request=GetScore&source=neo4j_local&identifier=http://dbpedia.org/resource/Cello_Concerto_(Elgar)
  ```
 The output format can be selected using the parameter `format`:

  ```http
  http://localhost:8295/wmss/?request=GetScore&source=neo4j_local&identifier=http://dbpedia.org/resource/Cello_Concerto_(Elgar)&format=musicxml
  ```

  * mei (Music Encoding Initiative files)
  * musicxml (MusicXML files)


  ### [Logging](https://github.com/jimjonesbr/wmss/blob/master/README.md#logging)
  
  Displays the WMSS log file. The parameter `logPreview` limits the amount of lines displayed. If omitted, the default value in the settings file is assumed. The the last 1000 lines from the log file can be requested as follows:

```http
http://localhost:8295/wmss/?request=Checklog&logPreview=1000
```
  
  
### [Exceptions](https://github.com/jimjonesbr/wmss/blob/master/README.md#exceptions)
  
|Code   | Message  | Hint |
|:-:|:-:|:-:|
|E0001|No request type provided| The request type has to provided in the parameter `request`, containing one of the following request types: `ListScores`, `GetScores`, `DescribeService`, `Checklog`. |
|E0002|Invalid request parameter| Provide one of the following request types: `ListScores`, `GetScores`, `DescribeService`, `Checklog`. |
|E0003|Invalid document format| Provide one of the following XML formats: `musicxml`, `mei` |
|E0004|Invalid time signature| Time signatures must be encoded in the following format: beat unit / beats. Examples: `3/4,` `4/4`, `6/8`, `c` (meaning common time and interpreted as `4/4`). |
|E0005|Invalid data source| The provided data source does not exist. Check the `sources.conf` file and try again.|
|E0006|Invalid melody encoding| The following melody encodings are currently supported: `pea` (Plaine & Easie) |
|E0007|Invalid score identifier|Make sure you're providing a valid score identifier at the parapeter `identifier`. |
|E0008|Invalid request mode| Please provide one of the following values: `full`, `simplified`. |
|E0009|Invalid melody length|A melody must contain at least three valid elements. |
|E0010|Invalid tempo beats per minute|Provide either a positive integer or an interval thereof, e.g. `148`, `98-104`. |
|E0011|Invalid tempo beat unit|Provide one of the following beat units: `maxima`, `longa`, `breve`, `whole`, `half`, `quarter`, `eighth`, `16th`, `32nd`, `64th`, `128th`, `256th`, `512th`, `1024th`. |
|E0012|Invalid date or interval |Provide a date or an inverval thereof in the following formats: `'yyyy`, `yyyymm`, `yyyymmdd`. For example: `1898`, `189805`, `19890501`, `19890501-19900215`, `1989-1990` |
|E0013|Request type not supported |Check the request section at the settings file |
|E0014|Invalid RDF file |Make sure that the imported file is properly encoded in one of the following formats: `JSON-LD`, `Turtle`, `RDF/XML` and `N-Triples` |
|E0015|Invalid RDF format |Please provide one for the following formats in the `format` parameter: `JSON-LD`, `Turtle`, `RDF/XML` and `N-Triples` |
|E0016|Invalid key |Provide one of the following keys: `$` (C major/A minor), `xF` (G major/E minor), `xFC` (D major/B minor), `xFCG` (A major/F# minor), `xFCGD` (E major/C# minor), `xFCGDA` (B major/G# minor), `xFCGDAE` (F# major/D# minor), `xFCGDAEB` (C# major/A# minor),`bB` (F major/D minor), `bBE` (Bb major/G minor), `bBEA` (Eb major/C minor), `bBEAD` (Ab major/F minor), `bBEADG` (Db major/Bb minor), `bBEADGC` (Gb major/Eb minor), `bBEADGCF` (Cb major/Ab minor)  |
|E0017|Invalid clef |The clef code is preceded by `%`, and is three characters long. The first character specifies the clef shape (G,C,F,g). The second character is `-` to indicate modern notation, `+` to indicate mensural notation. The third character (numeric 1-5) indicates the position of the clef on the staff, starting from the bottom line. Examples: `G-2`, `C-3`, `F-4`. |

#### [Importing Scores](https://github.com/jimjonesbr/wmss/blob/master/README.md#importing-scores)

New music scores can be inserted ther via POST requests, which requires the following parameters:


`source`&nbsp;  Data source where the file will be inserted. See Service Description Report for more details.

`format`&nbsp; Indicates the file RDF format. Supported formats are: `JSON-LD`, `Turtle`, `RDF/XML` and `N-Triples`

`commitSize`&nbsp; Commits a partial transaction every *n* triples. Useful for large RDF files.

Example using CURL:

```shell
curl -F file=@elgar_cello_concerto_op.85.nt "http://localhost:8295/wmss/import?source=neo4j_local&format=n-triples&commitsize=10000"
```

If everything goes well, you will recieve a `ImportReport`:

```json
{
  "timeElapsed": "780 ms",
  "size": 1,
  "files": [
    {
      "file": "elgar_cello_concerto_op.85.nt",
      "size": "2 MB",
      "records": 10846
    }
  ],
  "type": "ImportReport"
}
```

For inserting multiple files just add extra `-F` parameters to your command:

```shell
curl -F file=@file2.nt -F file=@file2.nt "http://localhost:8295/wmss/import?source=neo4j_local&format=n-triples&commitsize=10000"
```
#### [DeleteScore](https://github.com/jimjonesbr/wmss/blob/master/README.md#deletescore)

Deletes a specific score based on its identifier:

```
http://localhost:8295/wmss/?source=neo4j_local&request=DeleteScore&identifier=http://dbpedia.org/resource/Cello_Concerto_(Elgar)
```

If the score was successfully deleted, the `DeleteScoreReport` is shown:

```json
{
 "type": "DeleteScoreReport",
  "score": [
    {
      "scoreIdentifier": "http://dbpedia.org/resource/Cello_Concerto_(Elgar)",
      "title": "Cellokonzert e-Moll op. 85",
      "collection": "https://url.collection.de"
    }
  ] 
}
```

#### [Service Exception Report](https://github.com/jimjonesbr/wmss/blob/master/README.md#service-exception-report)

The Service Exception Report is provided as JSON and is structured as follows:

```json
{
  "type": "ExceptionReport",
  "code": "E0009",
  "message": "Invalid data source [fake_repo].",
  "hint": "The provided data source cannot be found. Check the 'Service Description Report' for more information on the available data sources."  
}
```
