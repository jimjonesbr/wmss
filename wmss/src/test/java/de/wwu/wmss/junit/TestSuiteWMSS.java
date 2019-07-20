package de.wwu.wmss.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StartWMSS.class,
	ImportScores.class,
	DescribeServiceRequest.class,
	ListScoresRequest.class,
	ExceptionsTest.class,
	DeleteScoreRequest.class
})

public class TestSuiteWMSS {

}