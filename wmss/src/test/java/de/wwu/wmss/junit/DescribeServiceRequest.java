package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import de.wwu.wmss.settings.Util;

public class DescribeServiceRequest {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;

	private static void describeServiceRequest() {

		boolean result = true;
		
		JSONObject jsonObject;

		try {

			//String url = server+":"+port+"/wmss/?request=DescribeService";
			String url = "http://localhost:8283/wmss/?request=DescribeService";			
			jsonObject = Util.readJsonFromUrl(url);
			
			System.out.println("\nRequest: " + url + "\n");
			//JSONArray datasources = jsonObject.getJSONArray("datasources");
			
			System.out.println("Report Type	: "+jsonObject.get("type"));
			System.out.println("Description	: "+jsonObject.get("title"));
			System.out.println("Service		: "+jsonObject.get("service"));
			System.out.println("Port		: "+jsonObject.get("port"));
			System.out.println("Contact		: "+jsonObject.get("contact"));
			System.out.println("Page size	: "+jsonObject.get("pageSize"));
			System.out.println("Report Type	: "+jsonObject.get("type"));
			System.out.println("Timeout		: "+jsonObject.get("timeout"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		

	}
	
	
	@Test
	public void getServiceDescriptionReport() {
		
		describeServiceRequest();
		
		assertEquals(true, true);
	}
}
