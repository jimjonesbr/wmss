package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class ImportScoreFromURL {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;
	private static String source = StartWMSS.source;
	
	private boolean post(String url, String format, int commitSize) {

		boolean result = false;

		HttpClient httpClient = HttpClientBuilder.create().build();	
		HttpPost post = new HttpPost(server+":"+port+"/wmss/import?source="+source+"&format="+format+"&commitSize="+commitSize+"&url="+url);				
		HttpResponse response=null;
		
		try { 
			response = httpClient.execute(post);

			HttpEntity entity = response.getEntity(); 
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(IOUtils.toString(entity.getContent()));
			
			String time = json.get("timeElapsed").toString();			
			JSONArray fileArray = (JSONArray) json.get("files");			
			JSONObject importedFile = (JSONObject) fileArray.get(0);
			
			String fileName = importedFile.get("file").toString();			
			String records = importedFile.get("records").toString();
			
			System.out.println("\nFile   : "+fileName+ 
							   "\nRecords: "+records+
							   "\nTime   : "+time  +"\n");
			
			if(!records.equals("0")) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;

	}


	@Test
	public void importFiles() {
					
		assertEquals(true, this.post("http://linkeddata.uni-muenster.de/datasets/3079355_Nacht%20ohne%20Licht.ttl", "turtle",10000));
			
	}		
	
}
