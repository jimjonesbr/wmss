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
	
	public static String INVALID_TONALITY_MODE_CODE = "E0004";
	public static String INVALID_TONALITY_MODE_DESCRIPTION  = "Invalid tonality";
	public static String INVALID_TONALITY_MODE_HINT = "Tonalities can be only either 'minor' or 'major'";

	public static String INVALID_TONALITY_TONIC_CODE = "E0005";
	public static String INVALID_TONALITY_TONIC_DESCRIPTION  = "Invalid tonic";
	public static String INVALID_TONALITY_TONIC_HINT = "Provide one of the following tonics: 'a','aflat','asharp','b','bflat','bsharp','c','cflat','csharp','d','dflat','dsharp','e','eflat','esharp','f','fflat','fsharp','g','gflat','gsharp'.";

	public static String INVALID_DATASOURCE_CODE = "E0006";
	public static String INVALID_DATASOURCE_DESCRIPTION  = "Invalid data source";
	public static String INVALID_DATASOURCE_HINT = "The provided data source does not exist. Check the 'sources.conf' file and try again.";
	
	public static String INVALID_MELODY_ENCODING_CODE = "E0007";
	public static String INVALID_MELODY_ENCODING_DESCRIPTION  = "Invalid melody encoding";
	public static String INVALID_MELODY_ENCODING_HINT = "The following melody encodings are currently supported: pea (Plaine & Easie)"; //TODO create supported melody encodings at the settings file 

	public static String INVALID_IDENTIFIER_CODE = "E0008";
	public static String INVALID_IDENTIFIER_DESCRIPTION  = "Invalid score identifier";
	public static String INVALID_IDENTIFIER_HINT = "No identifier provided for 'GetScore' request.";

	public static String INVALID_REQUEST_MODE_CODE = "E0009";
	public static String INVALID_REQUEST_MODE_DESCRIPTION  = "Invalid request mode";
	public static String INVALID_REQUEST_MODE_HINT = "Please provide one of the following values: '"+SystemSettings.REQUEST_MODE_FULL+"', '"+SystemSettings.REQUEST_MODE_SIMPLIFIED+"'.";

	public static String INVALID_MELODY_LENGTH_CODE = "E0010";
	public static String INVALID_MELODY_LENGTH_DESCRIPTION  = "Invalid melody length";
	public static String INVALID_MELODY_LENGTH_HINT = "A melody must contain at least three valid elements."; //TODO create supported melody encodings at the settings file 

}