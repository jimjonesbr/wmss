# Web Music Score Service (beta)

The Web Music Score Service (WMSS) provides an interface allowing requests for music scores on the web using platform-independent clients, working on top of relational databases and triple stores. It serves as an intermediate layer between data sets and application clients, providing standard access to MEI and MusicXML files. 

![GitHub Logo](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/wmss.jpg)

## Index

- [WMSS Data Model](#wmss-data-model)
	- [Relational Databases](#relational-databases)
  - [RDF Model](#rdf-model)
- [Configuring WMSS](#configuring-wmss)
- [Requests](#requests)
  - [DescribeService](#describeservice)
    - [Service Description Document](#service-description-report)
  - [ListScores](#listscores)
    - [Score List Document](#score-list-document)  
  - [GetScore](#getscore)
  - [Logging](#logging)
  - [Exceptions](#exceptions)
  	- [Service Exception Report](#service-exception-report)
  
## [WMSS Data Model](https://github.com/jimjonesbr/wmss/blob/master/README.md#wmss-data-model)

The WMSS generic data model is currently supported in relational databases and triple stores, and is designed as follows.

### [Relational Databases](https://github.com/jimjonesbr/wmss/blob/master/README.md#relational-databases)

<img src="https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/erm.svg" width="850"></img>

### [RDF Model](https://github.com/jimjonesbr/wmss/blob/master/README.md#rdf-model)
tbw.
## [Configuring WMSS](https://github.com/jimjonesbr/wmss/blob/master/README.md#configuring-wmss)

tbw.


## [Requests](https://github.com/jimjonesbr/wmss/blob/master/README.md#requests)

The WMSS communication protocol is based on the following HTTP requests:

### [DescribeService](https://github.com/jimjonesbr/wmss/blob/master/README.md#describeservice)

 
Lists all service related information as well as all repositories available: 
 
 ```http
 http://localhost:8295/wmss?request=DescribeService
 ```

The Service Description Report collects all available properties from each available data source. It gives the client all possible filters for each filter option, such as tonalities, tempo markings or instruments available for search.

#### [Service Description Report](https://github.com/jimjonesbr/wmss/blob/master/README.md#service-description-report) 
The Service Description Document is provided as JSON and is structured as follows:
 

`appVersion`&nbsp;   WMSS version.

`service`&nbsp;   Service name.

`type`&nbsp;   `ServiceDescriptionReport` (Standard value for Service Description Report)

`startup`&nbsp;   Service startup time.

`contact`&nbsp;   Administrator e-mail address.

`title`&nbsp;   Service title of description.

`supportedProtocols`&nbsp;   Protocols supported by the service.

`environment`&nbsp;  Environment settings.

&nbsp;&nbsp;&nbsp;&nbsp;`java`&nbsp;  Java version.

&nbsp;&nbsp;&nbsp;&nbsp;`os`&nbsp;  Operating system description.

`datasources`&nbsp;  Data sources available in the system.

&nbsp;&nbsp;&nbsp;&nbsp;`id`&nbsp;  Data source identifier.

&nbsp;&nbsp;&nbsp;&nbsp;`host`&nbsp;  Data source hosting server.

&nbsp;&nbsp;&nbsp;&nbsp;`enabled`&nbsp;  Boolean value to enable or disable access to a data source.

&nbsp;&nbsp;&nbsp;&nbsp;`type`&nbsp;  Data source type. Currently supported values are `database` and `triplestore`.

&nbsp;&nbsp;&nbsp;&nbsp;`storage`&nbsp;  Storage technology used in a data source. Currently supported values are: `postgresql` and `graphdb`.

&nbsp;&nbsp;&nbsp;&nbsp;`repository`&nbsp;  Specific repository of a data storage, e.g database for RDMS or a repository/named graph for triple stores.

&nbsp;&nbsp;&nbsp;&nbsp;`user`&nbsp;  Username used for accessing the data source.

&nbsp;&nbsp;&nbsp;&nbsp;`info`&nbsp;  Data source description.

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

### [ListScores](https://github.com/jimjonesbr/wmss/blob/master/README.md#listscores)
 
 Lists all scores from available repositories. 
 
  ```http
 http://localhost:8295/wmss?request=ListScores
 ```
 
 In order to facilitate the music score discovery, the ListScores request offers several filter capabilities:
 
 #### Data Source
 
 Constraints the __Score List Document__ to a specific data source:
 
  ```http
 http://localhost:8295/wmss?request=ListScores&source=postgres_wwu
 ```

 #### Collections

To facilitate the management of large repositories, WMSS offers the possibility to add music scores to specific collections, e.g. "ULB Histotic Collection", "Baroque Works", etc. The collection id, required for this parameter, is delivered together with the music score at the Score List Document.

 ```http
 http://localhost:8295/wmss?request=ListScores&source=postgres_wwu&collection=1
```

#### Persons

Selects all music scores containing specific persons and optionally with their respective roles. For instance, a request to list all scores from the person "Elgar" as a "Composer" is enconded like this:

```http
 http://localhost:8295/wmss?request=ListScores&person=Elgar&personRole=Composer
```

The __personRole__ parameter may contain the following values:

* Composer
* Arranger
* Encoder
* Dedicatee
* Librettist
* Editor
* Lyricist
* Translator
* Performer


#### Performance Medium (Instrument)

Selects all music scores containing specific performance mediums. The performance mediums are structured following the principle adopted by [MusicXML 3.0 Standard Sounds](http://www.musicxml.com/for-developers/standard-sounds/). For instance, requesting a list of all scores containing cello voices can be enconded like this:

 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=strings.cello
```

To constraint the search for the given performance medium to only solo mediums, use the *solo* parameter: 

 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=strings.cello&solo=true
```

This approach allows searching for groups and subgroups of performance mediums. For istance, all brass instruments:


 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=brass
```

All trumpets:

 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=brass.trumpet
```

Only baroque trumpets:

 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=brass.trumpet.baroque
```

A complete list of performance mediums containing approx. 900 items can be foun [here](https://github.com/jimjonesbr/wmss/tree/master/wmss/data/system/mediums.csv).

##### Performance Medium Type

It is also possible to select music scores based on performance medium types, e.g. Strings, Keyboard. The example bellow selects all records that contain movements that are played with bowed string instruments:
 
 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMediumType=strings
```

The performanceMediumType parameter expects the following codes:

| code|medium type| code|medium type|
|:-:|:-:|:-:|:-:|
|brass|Brass|pitched-percussion|Pitched Percussion|
|drum|Drums|pluck|Plucked|
|key|Keyboard|rattle|Rattle|
|metal|Metals|strings|Strings|
|synth|Synthesizer|voice|Voices|
|wind|Wind|wood|wood|


#### Tonalities

Selects records containing a specific tonality. For instance, a request to retrieve all records written in "E Major" is encoded as follows:

 ```http
http://localhost:8295/wmss?request=ListScores&tonalityTonic=e&tonalityMode=major
```

#### Tempo

Selects records containing movements played in a specific tempo, e.g. *adagio*, *largo*, *andante* etc.

 ```http
http://localhost:8295/wmss?request=ListScores&tempo=adagio
```
 
#### Creation Date

Selects records composed at a given time interval, e.g. 1700 - 1750:

 ```http
http://localhost:8295/wmss?request=ListScores&creationDateFrom=1700&creationDateTo=1750
```


#### Format
 
Selects records available in a specific format. The supported formats are:

  * mei (Music Encoding Initiative files)
  * musicxml (MusicXML files)
  
 ```http
http://localhost:8295/wmss?request=ListScores&format=mei
```

#### Melody

Selects records containing a specific melody (a sequence of notes). Each element of the melody has to be enconded in the following pattern: 

```
note-duration-octave/note-duration-octave
```

Example:

E♭ whole note, 3rd octave followed by E, whole note, 3rd octave:

```
http://localhost:8295/wmss?request=ListScores&melody=eb-w-3/e-w-3
```

Notes duration, pitch and octave can be ignored by placing a 0 (zero) in its position:

```
eb-w-0/e-w-0
```

##### Note list

|Note   | Code  || Note   | Code  || Note   | Code  || Note   | Code  | 
|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:|
| C  | c  ||D  | d  ||E  | e  ||G  | g  |
| C#  | cs  ||D#  | ds  ||E#  | es  ||G#  | gs  |
| C♭  | cb  || D♭  | db  || E♭  | eb  ||G♭  | gb  |
| B  | b  || E  | e  || F  | f  ||Unknown  | 0  |
| B#  | bs  || E#  | es  || F#  | fs  |
| B♭  | bb  || E♭  | eb  || F♭  | fb  |

##### Note Duration List
|Duration name (US)   |Duration name (UK) | Code  | Notation  |
|:-:|:-:|:-:|:-:|
| octuple whole note | large | ow  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/Music-octwholenote.svg/100px-Music-octwholenote.svg.png" width="75">
| quadruple whole note| longa | qw  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fc/Music-quadwholenote.svg/100px-Music-quadwholenote.svg.png" width="75">
| double whole note| breve | dw  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Music-alt-doublewholenote.svg/100px-Music-alt-doublewholenote.svg.png" width="75">
| whole note| semibreve | w  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/Music-wholenote.svg/100px-Music-wholenote.svg.png" width="75">
| half note|  minim | h  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c1/Music-halfnote.svg/100px-Music-halfnote.svg.png" width="75">
| quarter| crotchet | 4  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Music-quarternote.svg/100px-Music-quarternote.svg.png" width="75">
| eighth| quaver | 8  | <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Music-eighthnote.svg/100px-Music-eighthnote.svg.png" width="75">
| sixteenth| semiquaver | 16  |<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5b/Music-eighthnote.svg/100px-Music-eighthnote.svg.png" width="75">
| thirty-second| demisemiquaver	| 32  |<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Music-thirtysecondnote.svg/100px-Music-thirtysecondnote.svg.png" width="75">
| sixty-fourth| hemidemisemiquaver  | 64  |<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Sixtyfourth-note.svg/100px-Sixtyfourth-note.svg.png" width="75">
| hundred twenty-eighth| semihemidemisemiquaver | 128  |<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Music-hundredtwentyeighthnote.svg/100px-Music-hundredtwentyeighthnote.svg.png" width="75">
| two hundred fifty-sixth note| demisemihemidemisemiquaver | 256  |<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7f/Semigarrapatea.svg/100px-Semigarrapatea.svg.png" width="75">
| unknown  |   |0



#### [Score List Document](https://github.com/jimjonesbr/wmss/blob/master/README.md#score-list-document)
The Score List Document is provided as JSON and is structured as follows:

```json
{
  "type": "ScoreListReport",
  "size": 1,
  "datasources": [
    {
      "identifier": "postgres_wwu",
      "size": 1,
      "host": "localhost",
      "storage": "postgresql",
      "type": "database",
      "version": "9.5",
      "scores": [
        {
          "scoreIdentifier": "postgres_wwu:1",
          "title": "Walzer G-Dur",
          "groupDescription": "MEI Examples",
          "groupId": "1",
          "tonalityTonic": "g",
          "tonalityMode": "major",
          "creationDateFrom": 1784,
          "creationDateTo": 1849,
          "movements": [
            {
              "movementIdentifier": "1",
              "title": "Walzer G-Dur",
              "performanceMediumList": [
                {
                  "typeDescription": "Strings, plucked",
                  "mediumDescription": "Guitar",
                  "mediumClassification": "tb",
                  "mediumScoreDescription": "Guitar I",
                  "solo": false
                }
              ]
            }
          ],
          "formats": [
            {
              "formatId": "mei",
              "formatDescription": "MEI - Music Encoding Initiative"
            }
          ],
          "persons": [
            {
              "name": "Dionisio Aguado y García",
              "role": "Composer"
            }
          ]
        }
      ]
    }
  ]
}
```
 
  
  ### [GetScore](https://github.com/jimjonesbr/wmss/blob/master/README.md#getscore)
  
Retrieves a specific record based on its identifier:

  ```http
  http://localhost:8295/wmss/?request=getscore&identifier=postgres_wwu:1
  ```
 The output format can be selected using the parameter __format__:

  ```http
  http://localhost:8295/wmss/?request=getscore&identifier=postgres_wwu:1&format=mei
  ```

  * mei (Music Encoding Initiative files)
  * musicxml (MusicXML files)


  ### [Logging](https://github.com/jimjonesbr/wmss/blob/master/README.md#logging)
  tbw.
  
  
### [Exceptions](https://github.com/jimjonesbr/wmss/blob/master/README.md#exceptions)
  
|Code   | Message  | Cause |
|:-:|:-:|:-:|
|E0001|No request type provided| |
|E0002|Invalid request parameter| |
|E0003|Invalid document format| |
|E0004|Invalid mode| |
|E0005|Invalid tonic| |
|E0006|Invalid boolean value for 'solo' parameter| |
|E0007|Invalid protocol version| |
|E0008|No identifier provided for GetScore request| |
|E0009|Invalid data source| |
|E0010|Invalid filter requestd| |
|E0011|Unsupported filter| |
|E0012|Invalid melody| |
|E0013|Invalid collection string| |

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

