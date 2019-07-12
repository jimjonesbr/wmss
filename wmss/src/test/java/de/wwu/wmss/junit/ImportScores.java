package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

public class ImportScores {

	private static String server = "http://localhost";
	private static String port = "8888";
	private static String source = "neo4j_local";
	private boolean post(File file, String format, int commitSize) {

		boolean result = false;

		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpEntity requestEntity = MultipartEntityBuilder.create().addBinaryBody("file", file).build();
		HttpPost post = new HttpPost(server+":"+port+"/wmss/import?source="+source+"&format="+format+"&commitSize="+commitSize);
		post.setEntity(requestEntity);
		
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
			String fileSize = importedFile.get("size").toString();
			String records = importedFile.get("records").toString();
			
			System.out.println("\nFile   : "+fileName+
							   "\nSize   : "+fileSize+
							   "\nRecords: "+records+
							   "\nTime   : "+time  +"\n");
			
			if(!records.equals("0") && fileName.equals(file.getName())) {
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;

	}


	@Test
	public void importFiles() {
			
		boolean result = true;				
		URL url = this.getClass().getResource("/rdf");		
		File folder = null;
		
		try {
			folder = new File(Paths.get(url.toURI()).toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		File[] rdfFiles = folder.listFiles();
		
		for (int i = 0; i < rdfFiles.length; i++) {
			
			boolean loaded = false;
			
			if(FilenameUtils.getExtension(rdfFiles[i].getName()).equals("ttl")) {
				
				System.out.println(rdfFiles[i]);
				loaded = this.post(rdfFiles[i].getAbsoluteFile(), "turtle",10000);
				
			} else if(FilenameUtils.getExtension(rdfFiles[i].getName()).equals("nt")) {
				loaded = this.post(rdfFiles[i].getAbsoluteFile(), "n-triples",10000);
			} else if(FilenameUtils.getExtension(rdfFiles[i].getName()).equals("json")) {
				loaded = this.post(rdfFiles[i].getAbsoluteFile(), "json-ld",10000);
			} else if(FilenameUtils.getExtension(rdfFiles[i].getName()).equals("trig")) {
				loaded = this.post(rdfFiles[i].getAbsoluteFile(), "trig",10000);
			} else if(FilenameUtils.getExtension(rdfFiles[i].getName()).equals("xml")) {
				loaded = this.post(rdfFiles[i].getAbsoluteFile(), "rdf/xml",10000);
			}  
							
			if (!loaded) {
				result = false;
			}
		}

		assertEquals(true, result);
			
	}		

}
