package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.settings.Util;

public class ExceptionsTest {

	private static String server = "http://localhost";
	private static String source = "neo4j_local";
	private static int port = StartWMSS.port;

	public String getException(ArrayList<RequestParameter> parameters) {

		String result = "";

		JSONObject jsonObject;

		String url = server+":"+port+"/wmss?";

		for (int i = 0; i < parameters.size(); i++) {

			try {

				url = url + "&" + parameters.get(i).getRequest()+"="+ URLEncoder.encode(parameters.get(i).getValue(),"UTF-8");

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("\nRequest: " + url);

		try {
			jsonObject = Util.readJsonFromUrl(url);

			if(jsonObject !=null) {
				System.out.println("Code   : "+jsonObject.get("code").toString());
				System.out.println("Message: "+jsonObject.get("message").toString());
				result = jsonObject.get("code").toString();
			}
 
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}




		return result;
	}

	@Test
	public void invalidDataSource() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));	
		parameters.add(new RequestParameter("source", "invalid_data_source"));				
		assertEquals("E0005", this.getException(parameters));

	}

	@Test
	public void invalidRequestType() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "invalid_request_paramater"));	
		assertEquals("E0002", this.getException(parameters));

	}
	
	@Test
	public void invalidNoRequestType() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("solo", "true"));	
		assertEquals("E0001", this.getException(parameters));

	}

	@Test
	public void invalidScoreFormat() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "getScore"));
		parameters.add(new RequestParameter("identifier", "http://dbpedia.org/resource/Cello_Concerto_(Elgar)"));
		parameters.add(new RequestParameter("format", "invalid_score_format"));	
		assertEquals("E0003", this.getException(parameters));

	}
	
	@Test
	public void invalidTimeSignature() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("time", "invalid_time_signature"));	
		assertEquals("E0004", this.getException(parameters));

	}
	
	@Test
	public void invalidTimeSignature_PEA() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melody", "@x 8ABCDxDE"));	
		assertEquals("E0004", this.getException(parameters));

	}
	
	@Test
	public void invalidTimeSignature_PEA2() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melody", "@f/4 8ABCDxDE"));	
		assertEquals("E0004", this.getException(parameters));

	}
	
	@Test
	public void invalidMelodyEncoding() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melodyencoding", "invalid_melody_encoding"));	
		assertEquals("E0006", this.getException(parameters));
	}
	
	@Test	
	public void invalidScoreIdentifier() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "GetScore"));
		parameters.add(new RequestParameter("source", source));		
		assertEquals("E0007", this.getException(parameters));

	}
	
	@Test	
	public void invalidTempoBPM() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("tempobeatsperminute", "invalid_bpm"));
		assertEquals("E0010", this.getException(parameters));

	}
	
	@Test	
	public void invalidTempoUnit() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("tempoBeatUnit", "invalid_tempo_unit"));
		assertEquals("E0011", this.getException(parameters));

	}
	
	@Test	
	public void invalidMelodyLength() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melody", "BA"));
		assertEquals("E0009", this.getException(parameters));

	}
	
	
	@Test	
	public void invalidMelodyNotes() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melody", "XPTO"));
		assertEquals("E0009", this.getException(parameters));
	}
	
	@Test	
	public void invalidDate() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("dateIssued", "invalid_date"));
		assertEquals("E0012", this.getException(parameters));
	}

	@Test	
	public void invalidKey() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("key", "invalid_key"));
		assertEquals("E0016", this.getException(parameters));
	}
	

	@Test	
	public void invalidKey_PEA() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melody", "$tF ,8AB'CDxDE"));
		assertEquals("E0016", this.getException(parameters));
	}
	
	@Test	
	public void invalidClef() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("clef", "X-9"));
		assertEquals("E0017", this.getException(parameters));
	}
	
	@Test	
	public void invalidClef_PEA() {

		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("request", "ListScores"));
		parameters.add(new RequestParameter("source", source));
		parameters.add(new RequestParameter("melody", "%wrong ,8AB'CDxD"));
		//assertEquals("E0017", this.getException(parameters));
		
	}
}
