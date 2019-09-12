package de.wwu.wmss.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StartWMSS.class,
	ImportRDFScores.class,		
	ListScoresRequest.class,
	ExceptionsTest.class,
	DescribeServiceRequest.class,
	ImportRDFScoreFromURL.class,
	EditScores.class,
	GetScore.class,
	
	DeleteElgarScore.class,	
	ImportElgarMusicXML_Metadata_XMLString.class,	
	ListScoresRequestsElgar.class,
	GetScore.class,
	DeleteElgarScore.class,
	
	ImportElgarMusicXML_Metadata_JSONString.class,
	ListScoresRequestsElgar.class,
	GetScore.class,
	DeleteElgarScore.class,
	
	ImportElgarMusicXML_Metadata_XMLFile.class,
	ListScoresRequestsElgar.class,
	GetScore.class,
	DeleteElgarScore.class,

	ImportElgarMusicXML_Metadata_JSONFile.class,
	ListScoresRequestsElgar.class,		
	GetScore.class,	
	DeleteAllScores.class
	
})

public class TestSuiteWMSS {

}