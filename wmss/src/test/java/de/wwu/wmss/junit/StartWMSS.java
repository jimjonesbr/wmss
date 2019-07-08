package de.wwu.wmss.junit;

import org.junit.Test;

import de.wwu.wmss.web.Start;

public class StartWMSS  {

	@Test
	public void startService() {
		Start wmss = new Start();
		
		wmss.setPort(8888);
		
		Thread t1 = new Thread(wmss);
		t1.start();
	
	}

	

}
