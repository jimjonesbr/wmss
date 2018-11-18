# Web Music Score Service 

The Web Music Score Service (WMSS) provides an interface allowing requests for music scores on the web using platform-independent clients. It serves as an intermediate layer between data sets and application clients, providing standard access to MEI and MusicXML files. 

## Index

- [WMSS Data Model](#wmss-data-model)
- [Configuring WMSS](#configuring-wmss)
- [Requests](#requests)
  - [DescribeService](#describeservice)
    - [Service Description Report](#service-description-report)
  - [ListScores](#listscores)
    - [Score List Report](#score-list-report)  
  - [GetScore](#getscore)
  - [Logging](#logging)
  - [Exceptions](#exceptions)
  	- [Service Exception Report](#service-exception-report)
  
## [WMSS Data Model](https://github.com/jimjonesbr/wmss/blob/master/README.md#wmss-data-model)


## [Configuring WMSS](https://github.com/jimjonesbr/wmss/blob/master/README.md#configuring-wmss)

tbw.


## [Requests](https://github.com/jimjonesbr/wmss/blob/master/README.md#requests)

The WMSS communication protocol is based on the following HTTP requests:

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
 
 #### Data Source
 
 Constraints the __Score List Document__ to a specific data source:
 
  ```http
 http://localhost:8295/wmss?request=ListScores&source=neo4j_local
 ```

 #### Collections
Parameter: `collection`

To facilitate the management of large repositories, WMSS offers the possibility to add music scores to specific collections. The collection uri, required for this parameter, is delivered together with the music score in the Score List and Service Description Reports.

```http
 http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&collection=https://url.collection.de
```

#### Persons

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


#### Performance Medium (Instrument)

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

#### Performance Medium Type

Parameter: `performanceMediumType`

It is also possible to select music scores based on performance medium types, e.g. Strings, Keyboard. The example bellow selects all records that contain movements that are played with bowed string instruments:
 
 ```http
 http://localhost:8295/wmss?request=ListScores&performanceMediumType=strings
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


#### Tempo

Parameters: `tempoBeatUnit` / `tempoBeatsPerMinute`

Selects records containing movements played in a specific tempo, e.g. *adagio*, *largo*, *andante*, etc. Tempo markings may vary depending on the country of orign and century of composition, therefore tempo searches are encoded in two abstract parameters, namely `tempoBeatsPerMinute` and `tempoBeatUnit`. Beat units indicates the graphical note type to use in a metronome mark, which follows the principles adpoted by the [MusicXML Beat-Unit Element](https://usermanuals.musicxml.com/MusicXML/Content/EL-MusicXML-beat-unit.htm). The beats per unit parameter can be provided as a single integer value or an interval thereof. For instance, a *quarter* beat unit with an interval of 100-125 beats per minute, can be encoded as follows:

 ```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&tempoBeatUnit=quarter&tempoBeatsPerMinute=100-125
```
 
#### Date Issued

Parameter: `dateIssued`

Selects records composed at a given date or time interval, e.g. 1910-1920:

 ```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&dateIssued=1910-1920
```

Dates and intervals must be encoded as `yyyyMMdd`, `yyyyMM` or `yyyy`.


#### Format
Parameter: `format`

Selects records available in a specific format. The supported formats are:

  * `mei` (Music Encoding Initiative files)
  * `musicxml` (MusicXML files)
  
 ```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&format=musicxml
```

#### Melody
Parameter: `melody`


Selects records containing a specific a sequence of notes or phrases (not limited to *incipt*) throughout the database, encoded using the [Plaine & Easie musical notation](https://www.iaml.info/plaine-easie-code#toc-4) (PEA).  For instance, the value `,8AB'CDxDE`, which is going to be used throughout this section, corresponds to: 

![melody_sample](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/melody_sample.jpg)

Notes: `A` 3rd octave, `B` 3rd octave, `C` 4th octave, `D` 4th octave, `D#` 4th octave and `E` 4th octave. 

Duration:  `Eighth` 


#### Octaves
Parameter: `ignoreOctave`

To search for melodies encoded in specific octaves, set the parameter `ignoreOctave` to `false` (`true` by default). Note that in the PEA string the 4th octave is assumed, if no octave is explicitly defined. The following example searches for scores matching the sequence `A` 3rd octave, `B` 3rd octave, `C` 4th octave, `D` 4th octave, `D#` 4th octave and `E` 4th octave, all with the duration `eighth` (as described above):

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE&ignoreOctave=false
```

#### Durations 
Parameter: `ignoreDuration`

It is possible to search only for a sequence of pitches, ignoring their durations. It can be achieved by means of setting the parameter `ignoreDuration` to `true` (`false` by default). The following example searches for all scores containing the pitch sequence `A`, `B`, `C`, `D`, `D#` and `E`, ignoring their durations:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE&ignoreDuration=true
```

#### Pitches 
Parameter: `ignorePitch`

If you're only looking for a sequence of rhythmical elements (useful for percussionists), just set the parameter `ignorePitch` to `true` (`false` by default). The following example searches for all scores containing a sequence of 6 `eighth` notes, ignoring pitches:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,8AB'CDxDE&ignorePitch=true
```

#### Embedded Sequences (Note sequences inside chords) 
Parameter: `ignoreChords`

It is also possible to look for sequences whose notes are inside of chords. To achieve this, set the parameter `ignoreChords` to `false` (`true` by default). 

Consider the following chords ..

![three_chords](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/3_chords.jpg)

.. and the following search criteria `,,2GB8G`, which are notes embedded in the chords above:

![embedded_sequence](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/embedded_sequence.jpg)

To be able to find such an *embedded sequence*, set the parameter `ignoreChords` to `false`:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,,2GB8G&ignoreChords=false
```
**Note**: This feature assumes that the elements of such an embedded sequence are notes of the same voice. 

#### Chords search

Parameter: `melody`

Melodies containing chords can be searched by means of using the [PEA chords notation](https://www.iaml.info/plaine-easie-code#toc-19). The PEA notation states that every note of a chord is to be separated by `^`, starting by the upper note; then followed by the lower ones.

Searching for the following chord `,,2E^B^,G^'E` .. 

![embedded_sequence](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/1_chord.jpg)

.. can be done like this:

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=,,2E^B^,G^'E
```

#### Time signatures
Parameters: `melody` / `time`

Time signatures are to be encoded according to the [PEA key time signature notation](https://www.iaml.info/plaine-easie-code#toc-3). The time signature is preceded by `@` and followed by beats and beat unit, separated by `/`, e.g. `@3/4` (three-four or waltz time), `@2/4` (march time). Common time signatures can be also represented as `@c` and will be considered by the system as `@4/4`.

Examples

Common signature: `@c 8ABCDxDE`

![common_timesignature](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/common_time.jpg)
```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=@c 8ABCDxDE
```
Waltz time: `@3/4 8ABCDxDE`

![waltz_timesignature](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/waltz_time.jpg)
```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody=@3/4 8ABCDxDE
```

Alternatively, time signatures alone can be searched using the parameter `time`: 

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&time=4/4
```

See also: UNIMARC field 036 $o — MARC21 field 789 $g — MAB field 681 $h (RISM field 823)


#### Clefs
Parameters: `melody` / `clef`

Clefs are to be encoded according to the [PEA clef notation](https://www.iaml.info/plaine-easie-code#toc-1). In the `melody` parameter, clefs are preceded by `%`, and are three characters long. The first character specifies the clef shape (`G`,`C`,`F`,`g`). The second character is `-` to indicate modern notation, `+` to indicate mensural notation. The third character (numeric 1-5) indicates the position of the clef on the staff, starting from the bottom line. 

Clef examples: `G-2` (trebble clef), `F-4` (bass clef), `C-3` (alto clef), `C-4` (tenor clef).

<img src="https://upload.wikimedia.org/wikipedia/en/thumb/2/25/Common_clefs.svg/212px-Common_clefs.svg.png">

Request example `%C-4 ,8AB'CDxD`:
![tenor_clef](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/tenor_clef.jpg)
```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&melody=%C-4 ,8AB'CDxD
```
Alternatively, clefs alone can be searched using the parameter `clef`: 

```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&clef=F-4
```
See also: *UNIMARC field 036 $m — MARC21 field 789 $e — MAB field 681 $j (RISM field 820)*


#### Measures
Parameters: `melody`

If necessary, it is also possible to look for melodies contained in fixed measures. For instance, the melody `'4xF8G4xF8A4B8A4G8E4D8E4C,8B` will be searched no matter how the notes are distributed:

![no_measure](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/melody_no-measure.jpg)

```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&melody='4xF8G4xF8A4B8A4G8E4D8E4C,8B
```

By splitting the melodies with the character `/`, the system will look for exact matches with the given measure/notes distribution. This example will look for the given melody contained in exactly two measures `'4xF8G4xF8A4B8A4/G8E4D8E4C,8B`:

![measures](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/melody_measure.jpg)

```http
http://localhost:8295/wmss/?source=neo4j_local&request=listscores&melody='4xF8G4xF8A4B8A4/G8E4D8E4C,8B
```

#### Key signatures
Parameters: `melody` / `key`

Keys signatures are to be encoded according to the [PEA key signature notation](https://www.iaml.info/plaine-easie-code#toc-2). Accidentals are preceded by the character `$`; if there are no accidentals the `$` is omitted. The symbol `x` indicates sharpened keys, `b` flattened keys; the symbol is followed by the capital letters indicating the altered notes.

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

![elgar_theme](https://github.com/jimjonesbr/wmss/blob/master/wmss/config/img/elgar_theme.jpg)

```http
http://localhost:8295/wmss/?request=ListScores&source=neo4j_local&melody='4xF8G4xF8A4B8A4G8E4D8E4C,8B4A8B4A'8C4D8C,4B8G4xF8G4E8D4xC8D4xC8E4xF8E4D,,8B4xA8B4G8xF2B&ignoreOctave=false
```



#### [Score List Report](https://github.com/jimjonesbr/wmss/blob/master/README.md#score-list-report)
The Score List Document is provided as JSON and is structured as follows:


An example of a Score List Report can be found [here](https://github.com/jimjonesbr/wmss/tree/master/wmss/data/system/reports/ListScores.json).

  ### [GetScore](https://github.com/jimjonesbr/wmss/blob/master/README.md#getscore)
  
Retrieves a specific record based on its identifier:

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
|E0004|Invalid mode| The parameter `tonalityMode` accepts only the values `major` and `minor`|
|E0005|Invalid tonic|  |
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
