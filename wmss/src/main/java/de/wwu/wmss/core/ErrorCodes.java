package de.wwu.wmss.core;

import de.wwu.wmss.settings.SystemSettings;

public class ErrorCodes {

	public static String MISSING_REQUEST_CODE = "E0001";
	public static String MISSING_REQUEST_DESCRIPTION = "No request type provided";
	public static String MISSING_REQUEST_HINT = "Provide one of the following request types: ListScores, GetScores, Checklog, DescribeService.";

	public static String INVALID_REQUEST_CODE = "E0002";
	public static String INVALID_REQUEST_DESCRIPTION = "Invalid request type";
	public static String INVALID_REQUEST_HINT = "Provide one of the following request types: ListScores, GetScores, Checklog, DescribeService.";

	public static String INVALID_DOCUMENT_FORMAT_CODE = "E0003";
	public static String INVALID_DOCUMENT_FORMAT_DESCRIPTION  = "Invalid document format";
	public static String INVALID_DOCUMENT_FORMAT_HINT = "Provide one of the following XML formats: musicxml, mei";

	public static String INVALID_TIMESIGNATURE_CODE = "E0004";
	public static String INVALID_TIMESIGNATURE_DESCRIPTION  = "Invalid time signature";
	public static String INVALID_TIMESIGNATURE_HINT = "Time signatures must be encoded in the following format: beat unit / beats. Examples: @3/4, @4/4, @6/8, @c (meaning common time and interpreted as @4/4) ";

	public static String INVALID_DATASOURCE_CODE = "E0005";
	public static String INVALID_DATASOURCE_DESCRIPTION  = "Invalid data source";
	public static String INVALID_DATASOURCE_HINT = "The provided data source does not exist. Check the 'sources.conf' file and try again.";
	
	public static String INVALID_MELODY_ENCODING_CODE = "E0006";
	public static String INVALID_MELODY_ENCODING_DESCRIPTION  = "Invalid melody encoding";
	public static String INVALID_MELODY_ENCODING_HINT = "The following melody encodings are currently supported: pea (Plaine & Easie)"; //TODO create supported melody encodings at the settings file 

	public static String INVALID_IDENTIFIER_CODE = "E0007";
	public static String INVALID_IDENTIFIER_DESCRIPTION  = "Invalid score identifier";
	public static String INVALID_IDENTIFIER_HINT = "Make sure you're providing a valid score identifier at the parapeter 'identifier'.";

	public static String INVALID_REQUEST_MODE_CODE = "E0008";
	public static String INVALID_REQUEST_MODE_DESCRIPTION  = "Invalid request mode";
	public static String INVALID_REQUEST_MODE_HINT = "Please provide one of the following values: '"+SystemSettings.REQUEST_MODE_FULL+"', '"+SystemSettings.REQUEST_MODE_SIMPLIFIED+"'.";

	public static String INVALID_MELODY_LENGTH_CODE = "E0009";
	public static String INVALID_MELODY_LENGTH_DESCRIPTION  = "Invalid melody length";
	public static String INVALID_MELODY_LENGTH_HINT = "A melody must contain at least three valid elements."; 

	public static String INVALID_TEMPO_BPM_CODE = "E0010";
	public static String INVALID_TEMPO_BPM_DESCRIPTION  = "Invalid tempo beats per minute";
	public static String INVALID_TEMPO_BPM_HINT = "Provide either a positive integer or an interval thereof, e.g. 148, 98-104."; 

	public static String INVALID_TEMPO_BEAT_UNIT_CODE = "E0011";
	public static String INVALID_TEMPO_BEAT_UNIT_DESCRIPTION  = "Invalid tempo beat unit";
	public static String INVALID_TEMPO_BEAT_UNIT_HINT = "Provide one of the following beat units: maxima, longa, breve, whole, half, quarter, eighth, 16th, 32nd, 64th, 128th, 256th, 512th, 1024th.";
	
	public static String INVALID_DATE_CODE = "E0012";
	public static String INVALID_DATE_DESCRIPTION  = "Invalid date or interval";
	public static String INVALID_DATE_HINT = "Provide a date or an inverval thereof in the following formats: 'yyyy', 'yyyymm', 'yyyymmdd'. For example: 1898, 189805, 19890501, 19890501-19900215, 1989-1990";

	public static String NONSUPPORTED_REQUEST_CODE = "E0013";
	public static String NONSUPPORTED_REQUEST_DESCRIPTION  = "Request type not supported";
	public static String NONSUPPORTED_REQUEST_DATE_HINT = "Check the request section at the settings file";

	public static String INVALID_RDFFILE_CODE = "E0014";
	public static String INVALID_RDFFILE_DESCRIPTION  = "Invalid RDF file";
	public static String INVALID_RDFFILE_HINT = "Make sure that the imported file is properly encoded in one of the following formats: JSON-LD, Turtle, RDF/XML and N-Triples";

	public static String INVALID_RDFFORMAT_CODE = "E0015";
	public static String INVALID_RDFFORMAT_DESCRIPTION  = "Invalid RDF format";
	public static String INVALID_RDFFORMAT_HINT = "Please provide one for the following formats in the 'format' parameter: JSON-LD, Turtle, RDF/XML and N-Triples";
	
	public static String INVALID_KEY_CODE = "E0016";
	public static String INVALID_KEY_DESCRIPTION  = "Invalid key";
	public static String INVALID_KEY_HINT = 
			"Provide one of the following keys: '$' (C major/A minor), 'xF' (G major/E minor), 'xFC' (D major/B minor), 'xFCG' (A major/F# minor), 'xFCGD' (E major/C# minor), 'xFCGDA' (B major/G# minor), 'xFCGDAE' (F# major/D# minor), 'xFCGDAEB' (C# major/A# minor),"
			+ "'bB' (F major/D minor), 'bBE' (Bb major/G minor), 'bBEA' (Eb major/C minor), 'bBEAD' (Ab major/F minor), 'bBEADG' (Db major/Bb minor), 'bBEADGC' (Gb major/Eb minor), 'bBEADGCF' (Cb major/Ab minor) ";



	public static String INVALID_CLEF_CODE = "E0017";
	public static String INVALID_CLEF_DESCRIPTION  = "Invalid clef";
	public static String INVALID_CLEF_HINT = "The clef code is preceded by '%', and is three characters long. The first character specifies the clef shape (G,C,F,g). The second character is '-' to indicate modern notation, '+' to indicate mensural notation. The third character (numeric 1-5) indicates the position of the clef on the staff, starting from the bottom line. Examples: G-2, C-3, F-4.";

	public static String SCORE_EXISTS_CODE = "E0018";
	public static String SCORE_EXISTS_DESCRIPTION  = "Score already exists";
	public static String SCORE_EXISTS_HINT = "Either the score already exists or there is another score with the same identifier. ";

	public static String DATA_IMPORT_CODE = "E0019";
	public static String DATA_IMPORT_DESCRIPTION  = "An error occurred while importing an RDF file ";
	public static String DATA_IMPORT_HINT = "";
	
	public static String INVALID_CONTENT_TYPE_CODE = "E0020";
	public static String INVALID_CONTENT_TYPE_DESCRIPTION  = "An error occurred while importing an RDF file ";
	public static String INVALID_CONTENT_TYPE_HINT = "Check the uploaded file and try again.";

	public static String INSUFFICIENT_DATA_CODE = "E0021";
	public static String INSUFFICIENT_DATA_DESCRIPTION  = "Insufficient data: ";
	public static String INSUFFICIENT_DATA_HINT = "Check the request document and try again.";
	
	public static String RELATIONSHIP_CONFLICT_CODE = "E0022";
	public static String RELATIONSHIP_CONFLICT_DESCRIPTION  = "Relationship already exists. ";
	public static String RELATIONSHIP_CONFLICT_HINT = "Check the request document and try again.";
	
	public static String WARNING_CONFLICTING_CHORDS_PARAMETER_CODE = "W0001";
	public static String WARNING_CONFLICTING_CHORDS_PARAMETER_DESCRIPTION  = "Conflicting parameter 'ignoreChords'.";
	public static String WARNING_CONFLICTING_CHORDS_PARAMETER_HINT = "The searched melody contains chords but the parameter 'ignoreChords' ist set to 'true'. The 'ignoreChords' parameter will be ignored.";

}
