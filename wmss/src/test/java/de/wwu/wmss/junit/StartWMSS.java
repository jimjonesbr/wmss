package de.wwu.wmss.junit;

import org.junit.Test;

import de.wwu.wmss.web.Start;

public class StartWMSS  {

	public static final int port = 8283;
	
	@Test
	public void startService() {
		Start wmss = new Start();
		
		wmss.setPort(port);
		
		Thread t1 = new Thread(wmss);
		t1.start();
	
	}

	

}
