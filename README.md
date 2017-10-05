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
|bb	|Trumpet| pn	|Unspecified (Percussion)| vh	|High voice|
|bc	|Cornet| pu	|Unknown (Percussion)| vi	|Medium voice|
|bd	|Trombone| py	|Ethnic (Percussion)|vj	|Low voice|
|be	|Tuba| pz	|Other (Percussion)| vn	|Unspecified (Voice)|
|bf	|Baritone| sa	|Violin| vu	|Unknown (Voice)|
|bn	|Unspecified (Brass)| sb	|Viola| vy	|Ethnic (Voice)|
|bu	|Unknown (Brass)| sc	|Violoncello|wa	|Flute|
|by	|Ethnic (Brass)| sd	|Double bass|wb	|Oboe|
|bz	|Other (Brass)| se	|Viol|wc	|Clarinet|
|ea	|Synthesizer|sf	|Viola d`amore| wd	|Bassoon|
|eb	|Tape| sg	|Viola da gamba| we	|Piccolo|
|ec	|Computer|sn	|Unspecified (Strings, bowed)| wf	|English horn|
|ed	|Ondes Martinot| su	|Unknown (Strings, bowed)| wg	|Bass clarinet|
|en	|Unspecified (Electronic)| sy	|Ethnic (Strings, bowed)| wh	|Recorder|
|eu	|Unknown (Electronic)| sz	|Other (Strings, bowed)| wi	|Saxophone|
|ez	|Other (Electronic)| ta	|Harp| wn	|Unspecified (Woodwinds)|
|ka	|Piano| tb	|Guitar| wu	|Unknown (Woodwinds)|
|kb	|Organ| tc	|Lute| wy	|Ethnic (Woodwinds)|
|kc	|Harpsichord| td	|Mandolin| wz	|Other (Woodwinds)|
|kd	|Clavichord| tn	|Unspecified (Strings, plucked)| zn	|Unspecified instrument|
|ke	|Continuo| tu	|Unknown (Strings, plucked)| zu	|Unknown|
|kf	|Celeste| ty	|Ethnic (Strings, plucked)|
|kn	|Unspecified (Keyboard)| tz	|Other (Strings, plucked)|
|ku	|Unknown (Keyboard)| va	|Soprano|
|ky	|Ethnic (Keyboard)| vb	|Mezzo Soprano|
|kz	|Other (Keyboard)| vc	|Alto|
|pa	|Timpani| vd	|Tenor|
|pb	|Xylophone| ve	|Baritone|
|pc	|Marimba| vf	|Bass|

##### Performance Medium Type

It is also possible to select music scores based on performance medium types, e.g. Woodwinds, Keyboard. The example bellow selects all records that contain movements that are played with bowed string instruments:
 
 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMediumType=stb
```

The performanceMediumType parameter expects the following codes:

| code|medium type| code|medium type|
|:-:|:-:|:-:|:-:|
|bra|Brass|stb|Strings, bowed|
|ele|Electronic|stl|Strings, plucked|
|key|Keyboard|voi|Voices|
|per|Percussion|wwi|Woodwinds|


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

  * mei (Music Encoding Initiative filles)
  * musicxml (MusicXML files)


  ### [Logging](https://github.com/jimjonesbr/wmss/blob/master/README.md#logging)
  tbw.
