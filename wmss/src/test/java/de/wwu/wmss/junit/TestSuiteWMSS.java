package de.wwu.wmss.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StartWMSS.class,
	ImportMusicXML.class,
	ImportRDFScores.class,		
	ListScoresRequest.class,
	ExceptionsTest.class,
	DescribeServiceRequest.class,
	GetScore.class,
	ImportRDFScoreFromURL.class,
	DeleteScoreRequest.class
})

public class TestSuiteWMSS {

}