package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Paths;
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

public class ImportMusicXML {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;
	private static String source = StartWMSS.source;
	
	private boolean post(File file, int commitSize, String metadata) {

		boolean result = false;

		HttpClient httpClient = HttpClientBuilder.create().build();

		try { 

			HttpEntity requestEntity = MultipartEntityBuilder.create().addBinaryBody("file", file).build();
			HttpPost post = new HttpPost(server+":"+port+"/wmss/import?source="+source+"&format=musicxml&commitSize="+commitSize+"&metadata="+URLEncoder.encode(metadata, "UTF-8"));
			post.setEntity(requestEntity);
			
			HttpResponse response=null;

			response = httpClient.execute(post);

			HttpEntity entity = response.getEntity(); 
			
			JSONParser parser = new JSONParser();
			@SuppressWarnings("deprecation")
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
	public void importMusicXML() {
			
		boolean result = true;				
		URL url = this.getClass().getResource("/musicxml");		
		String metadata = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<score>\n" + 
				"	<scoreIdentifier>http://dbpedia.org/resource/Cello_Concerto_(Elgar)</scoreIdentifier>\n" + 
				"	<title>Cellokonzert e-Moll op. 85</title>\n" + 
				"	<thumbnail>https://www.rcm.ac.uk/media/Elgar%20Cello%20Concerto%20maunscript%206x4.jpg</thumbnail>\n" + 
				"	<issued>1919</issued>\n" + 
				"	<collections>\n" + 
				"		<collection>\n" + 
				"			<collectionName>Great Composers</collectionName>\n" + 
				"			<collectionURL>https://wwu.greatcomposers.de</collectionURL>\n" + 
				"		</collection>\n" + 
				"	</collections>\n" + 
				"	<persons>\n" + 
				"		<person>\n" + 
				"			<personIdentifier>http://dbpedia.org/resource/Edward_Elgar</personIdentifier>\n" + 
				"			<personName>Sir Edward William Elgar</personName>\n" + 
				"			<personRole>Composer</personRole>\n" + 
				"		</person>\n" + 
				"		<person>\n" + 
				"			<personIdentifier>http://jimjones.de</personIdentifier>\n" + 
				"			<personName>Jim Jones</personName>\n" + 
				"			<personRole>Encoder</personRole>\n" + 
				"		</person>\n" + 
				"	</persons>\n" + 
				"	<resources>\n" + 
				"		<resource>\n" + 
				"			<resourceURL>https://musescore.com/score/152011/download/pdf</resourceURL>\n" + 
				"			<resourceDescription>Print</resourceDescription>\n" + 
				"			<resourceType>application/pdf</resourceType>\n" + 
				"		</resource>\n" + 
				"		<resource>\n" + 
				"			<resourceURL>https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)</resourceURL>\n" + 
				"			<resourceDescription>Wikipedia Article</resourceDescription>\n" + 
				"			<resourceType>text/html</resourceType>\n" + 
				"		</resource>\n" + 
				"	</resources>\n" + 
				"</score>\n" + 
				"";
		File folder = null;
		
		try {
			folder = new File(Paths.get(url.toURI()).toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		File[] rdfFiles = folder.listFiles();
		
		for (int i = 0; i < rdfFiles.length; i++) {
						
			System.out.println("Posting '"+ rdfFiles[i]+"' .. ");
			try {
				result = this.post(rdfFiles[i].getAbsoluteFile(),10000,metadata);
			} catch (Exception e) {
				e.printStackTrace();
			}
							

		}

		assertEquals(true, result);
			
	}

}
