package de.wwu.wmss.tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.RequestParameter;

public class ListScoresRequest {

	private static String server = "http://localhost";
	private static String port = "8295";
	private static String source = "neo4j_local";
		
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	private boolean listScoresRequest(MusicScore score, MelodyLocation location, ArrayList<RequestParameter> parameters) {

		boolean result = false;

		JSONObject jsonObject;
		
		try {
			
			String url = server+":"+port+"/wmss/?request=ListScores";
			
			for (int i = 0; i < parameters.size(); i++) {
				url = url + "&" + parameters.get(i).getRequest()+"="+parameters.get(i).getValue();
			}
			
			System.out.println("\nRequest: " + url + "\n");
			jsonObject = readJsonFromUrl(url);
			
			JSONArray datasources = jsonObject.getJSONArray("datasources");//1

			for (int i = 0; i < datasources.length(); i++) {

				JSONArray scores =  datasources.getJSONObject(i).getJSONArray("scores");

				for (int j = 0; j < scores.length(); j++) {
					
					System.out.println("Score identifier: "+scores.getJSONObject(j).getString("scoreIdentifier"));
					System.out.println("Score title     : "+scores.getJSONObject(j).getString("title"));
					System.out.println("Date issued     : "+scores.getJSONObject(j).getString("dateIssued")+"\n");
				
					JSONArray melodies =  scores.getJSONObject(j).getJSONArray("melodyLocations");

					for (int k = 0; k < melodies.length(); k++) {

						JSONArray melody =  melodies.getJSONObject(j).getJSONArray("melodyLocation");

						for (int l = 0; l < melody.length(); l++) {
							
							if(melody.getJSONObject(l).get("startingMeasure").toString().equals(location.getStartingMeasure()) &&
							  melody.getJSONObject(l).get("voice").toString().equals(location.getVoice()) &&
							  melody.getJSONObject(l).get("staff").toString().equals(location.getStaff()) &&
							  melody.getJSONObject(l).get("instrumentName").toString().equals(location.getInstrumentName()) &&
							  melody.getJSONObject(l).get("melody").toString().equals(location.getMelody()) &&
							  scores.getJSONObject(j).getString("title").equals(score.getTitle()) &&
							  scores.getJSONObject(j).getString("scoreIdentifier").equals(score.getScoreId()) &&
							  scores.getJSONObject(j).getString("dateIssued").equals(score.getDateIssued())
							  ) {
								result = true;
							}
							
							System.out.println("- Starting Measure: "+melody.getJSONObject(l).get("startingMeasure"));
							System.out.println("- Voice           : "+melody.getJSONObject(l).get("voice"));
							System.out.println("- Staff           : "+melody.getJSONObject(l).get("staff"));
							System.out.println("- Instrument Name : "+melody.getJSONObject(l).get("instrumentName"));
							System.out.println("- Melody          : "+melody.getJSONObject(l).get("melody")+"\n");
							

						}

					}

				}

			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}
	
	@Test
	public void elgarCelloConcerto_6Notes_Full() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOtctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("melody", ",8AB'CDxDE"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("8");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMelody(",8AB'CDxDE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}


	@Test
	public void elgarCelloConcerto_36Notes_Full() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOtctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("melody", "'4xF8G4xF8A4B8A4G8E4D8E4C,8B4A8B4A'8C4D8C,4B8G4xF8G4E8D4xC8D4xC8E4xF8E4D,,8B4xA8B4G8xF2B"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("80");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMelody("'4xF8G4xF8A4B8A4G8E4D8E4C,8B4A8B4A'8C4D8C,4B8G4xF8G4E8D4xC8D4xC8E4xF8E4D,,8B4xA8B4G8xF2B");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}

	
	@Test
	public void elgarCelloConcerto_20Notes_Full_4Measures() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOtctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("melody", "'4xF8G4xF8A4B8A4/G8E4D8E4C,8B/,4A8B4A'8C4D8C/,4B8G4xF8G4E8D"));
	
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("98");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMelody("'4xF8G4xF8A4B8A4/G8E4D8E4C,8B/,4A8B4A'8C4D8C/,4B8G4xF8G4E8D");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	
	@Test
	public void elgarCelloConcerto_Chord() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOtctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("melody", ",,2E^B^,G^'E"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("1");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMelody(",,2E^B^,G^'E");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
}
