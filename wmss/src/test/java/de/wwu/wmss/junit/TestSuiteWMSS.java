package de.wwu.wmss.junit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	StartWMSS.class,
	ImportScores.class, 
	ListScoresRequest.class
})

public class TestSuiteWMSS {

}