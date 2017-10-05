# Web Music Score Service

The Web Music Score Service (WMSS) provides an interface allowing requests for music scores on the web using platform-independent clients, working on top of relational databases and triple stores. It serves as an intermediate layer between data sets and application clients, providing standard access to MEI and MusicXML files. 

![GitHub Logo](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/wmss.jpg)

## Index

- [WMSS Data Model](#wmss-data-model)
- [Configuring WMSS](#configuring-wmss)
- [Requests](#requests)
  - [DescribeService](#describeservice)
    - [Service Description Document](#service-description-document)
  - [ListScores](#listscores)
    - [Score List Document](#score-list-document)  
  - [GetScore](#getscore)
  - [Logging](#logging)
  
## [WMSS Data Model](https://github.com/jimjonesbr/wmss/blob/master/README.md#wmss-data-model)

The WMSS data model is inspired on the [MEI Header](http://music-encoding.org/support/tutorials/mei-1st/exploring-the-mei-header/) encoded by the [Music Encoding Initiative (MEI)](http://music-encoding.org/).

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
#### [Service Description Document](https://github.com/jimjonesbr/wmss/blob/master/README.md#service-description-document) 
The Service Description Document is provided as JSON and is structured as follows:
 
```json
{
  "type": "ServiceDescriptionReport",
  "port": 8295,
  "timeout": 10000,
  "service": "wmss",
  "startup": "2017/10/04 13:02:12",
  "contact": "jim.jones@uni-muenster.de",
  "title": "Web Music Score Service - University of Münster",
  "version": "Dev-0.0.1",
  "supportedProtocols": [
    "1.0",
    "1.1"
  ],
  "environment": {
    "java": "1.8.0_131",
    "os": "Linux 4.4.0-96-generic (amd64)"
  },
  "datasources": [
    {
      "port": 5432,
      "filterCapabilities": {
        "melody": false,
        "group": true,
        "personRole": true,
        "performanceMedium": true,
        "performanceMediumType": true,
        "solo": true,
        "tonalityTonic": true,
        "tonalityMode": true,
        "tempo": true,
        "creationDateFrom": false,
        "creationDateTo": false,
        "source": true,
        "identifier": true,
        "format": true
      },
      "host": "localhost",
      "active": true,
      "id": "postgres_wwu",
      "storage": "postgresql",
      "type": "database",
      "repository": "wmss",
      "version": "9.5",
      "user": "postgres",
      "info": "Test PostgreSQL repository."
    }
  ]
}
```



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

 #### Group (Collection)

In WMSS it is possible to add music scores to groups, so that specific collections can be also represented, e.g. "ULB Histotic Collection", "Baroque Works", etc. 

 ```http
 http://localhost:8295/wmss?request=ListScores&source=postgres_wwu&group=1
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


#### Performance Medium

Selects all music scores containing specific performance mediums. For instance, a request to list all scores containing cello voices can be enconded like this:

 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=sc
```

To constraint the search for the given performance medium to only mediums, use the *solo* parameter. 

 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMedium=sc&solo=true
```

##### Performance Medium List
| id  	|  medium  	| id |medium| id |medium|
|:-:	|:-:	|:-:	|:-:	|:-:	|:-:	|
|ba	|Horn| pd	|Drum| vg	|Counter tenor|
|bb	|Trumpet| pn	|Unspecified| vh	|High voice|
|bc	|Cornet| pu	|Unknown| vi	|Medium voice|
|bd	|Trombone| py	|Ethnic|vj	|Low voice|
|be	|Tuba| pz	|Other| vn	|Unspecified|
|bf	|Baritone| sa	|Violin| vu	|Unknown|
|bn	|Unspecified| sb	|Viola| vy	|Ethnic|
|bu	|Unknown| sc	|Violoncello|wa	|Flute|
|by	|Ethnic| sd	|Double bass|wb	|Oboe|
|bz	|Other| se	|Viol|wc	|Clarinet|
|ea	|Synthesizer|sf	|Viola d`amore| wd	|Bassoon|
|eb	|Tape| sg	|Viola da gamba| we	|Piccolo|
|ec	|Computer|sn	|Unspecified| wf	|English horn|
|ed	|Ondes Martinot| su	|Unknown| wg	|Bass clarinet|
|en	|Unspecified| sy	|Ethnic| wh	|Recorder|
|eu	|Unknown| sz	|Other| wi	|Saxophone|
|ez	|Other| ta	|Harp| wn	|Unspecified|
|ka	|Piano| tb	|Guitar| wu	|Unknown|
|kb	|Organ| tc	|Lute| wy	|Ethnic|
|kc	|Harpsichord| td	|Mandolin| wz	|Other|
|kd	|Clavichord| tn	|Unspecified| zn	|Unspecified instruments|
|ke	|Continuo| tu	|Unknown| zu	|Unknown|
|kf	|Celeste| ty	|Ethnic|
|kn	|Unspecified| tz	|Other|
|ku	|Unknown| va	|Soprano|
|ky	|Ethnic| vb	|Mezzo Soprano|
|kz	|Other| vc	|Alto|
|pa	|Timpani| vd	|Tenor|
|pb	|Xylophone| ve	|Baritone|
|pc	|Marimba| vf	|Bass|



Listing all records containing voices written for bowed string instruments:
 
 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMediumType=StringsBowed
```

The performanceMediumType parameters expects the following values:

* Brass
* Electronic
* Keyboard
* Percussion
* StringsBowed
* StringsPlucked
* Voices
* Woodwinds

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

  * mei (Music Encoding Initiative filles)
  * musicxml (MusicXML files)
  
 ```http
http://localhost:8295/wmss?request=ListScores&format=mei
```

#### Melody

Selects records containing a specific melody (a sequence of notes). Each element of the melody has to be enconded in the following pattern: 

```
note-duration-octave/note-duration-octave
```

Example.

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
|Note duration   | Code  | 
|:-:|:-:|
| octuple whole note  | ow  |
| quadruple whole note  | qw  |
| double whole note  | dw  |
| whole note  | w  |
| half note  | h  |
| quarter  | 4  |
| eighth  | 8  |
| sixteenth  | 16  |
| thirty-second  | 32  |
| sixty-fourth  | 64  |
| hundred twenty-eighth  | 128  |
| unknown  | 0  |



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

  * mei (Music Encoding Initiative filles)
  * musicxml (MusicXML files)


  ### [Logging](https://github.com/jimjonesbr/wmss/blob/master/README.md#logging)
  tbw.
