package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import de.wwu.wmss.core.MelodyLocation;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.RequestParameter;
import de.wwu.wmss.settings.Util;

public class ListScoresRequestsElgar {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;
	private static String source = StartWMSS.source;
		

	private boolean listScoresRequest(MusicScore score, MelodyLocation location, ArrayList<RequestParameter> parameters) {

		boolean result = false;

		JSONObject jsonObject;
		
		try {
			
			String url = server+":"+port+"/wmss/?request=ListScores";
			
			for (int i = 0; i < parameters.size(); i++) {
				url = url + "&" + parameters.get(i).getRequest()+"="+ URLEncoder.encode(parameters.get(i).getValue(),"UTF-8");
			}
			
			System.out.println("\nRequest: " + url + "\n");
			jsonObject = Util.readJsonFromUrl(url);
			
			JSONArray datasources = jsonObject.getJSONArray("datasources");

			for (int i = 0; i < datasources.length(); i++) {

				JSONArray scores =  datasources.getJSONObject(i).getJSONArray("scores");

				for (int j = 0; j < scores.length(); j++) {
					
					System.out.println("Score identifier: "+scores.getJSONObject(j).getString("scoreIdentifier"));
					System.out.println("Score title     : "+scores.getJSONObject(j).getString("title"));
					System.out.println("Date issued     : "+scores.getJSONObject(j).getString("dateIssued")+"\n");
				
					JSONArray melodies =  scores.getJSONObject(j).getJSONArray("melodyLocations");

					for (int k = 0; k < melodies.length(); k++) {

						JSONArray melody =  melodies.getJSONObject(k).getJSONArray("melodyLocation");

						for (int l = 0; l < melody.length(); l++) {
							
							if(melody.getJSONObject(l).get("startingMeasure").toString().equals(location.getStartingMeasure()) &&
							  melody.getJSONObject(l).get("voice").toString().equals(location.getVoice()) &&
							  melody.getJSONObject(l).get("staff").toString().equals(location.getStaff()) &&
							  melody.getJSONObject(l).get("instrumentName").toString().equals(location.getInstrumentName()) &&
							  melody.getJSONObject(l).get("melody").toString().equals(location.getMelody()) &&
							  scores.getJSONObject(j).getString("title").equals(score.getTitle()) &&
							  scores.getJSONObject(j).getString("scoreIdentifier").equals(score.getScoreId()) &&
							  scores.getJSONObject(j).getString("dateIssued").equals(score.getDateIssued()) &&
							  melodies.getJSONObject(k).get("movementName").toString().equals(location.getMovementName())
							  ) {
								result = true;
							}
							
							System.out.println("- Movement        : "+melodies.getJSONObject(k).get("movementName"));
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
	public void elgarCelloConcerto_6Notes_Full_Collection_Composer_PerformanceMedium_nonSolo_nonEmsemble_tempo_dateInverval_time_clef() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("collection", "https://wwu.greatcomposers.de"));
		parameters.add(new RequestParameter("person", "http://dbpedia.org/resource/Edward_Elgar"));
		parameters.add(new RequestParameter("personRole", "composer"));
		parameters.add(new RequestParameter("performanceMedium", "strings.cello"));
		parameters.add(new RequestParameter("solo", "false"));
		parameters.add(new RequestParameter("ensemble", "false"));
		parameters.add(new RequestParameter("tempoBeatUnit", "quarter"));
		parameters.add(new RequestParameter("tempoBeatsPerMinute", "120-125"));
		parameters.add(new RequestParameter("dateIssued", "1910-1920"));		
		parameters.add(new RequestParameter("melody", "%C-4 ,8AB'CDxDE"));
		parameters.add(new RequestParameter("format", "musicxml"));
		parameters.add(new RequestParameter("time", "4/4"));
		
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
		location.setMovementName("Adagio");
		location.setMelody("%C-4 ,8AB'CDxDE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	
	@Test
	public void elgarCelloConcerto_6Notes_Full() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
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
		location.setMovementName("Adagio");
		location.setMelody(",8AB'CDxDE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}

	@Test
	public void elgarCelloConcerto_6Notes_Full_MutipleRests() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("melody", ",,2E--4.-6,,B,xC"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("5");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMovementName("Adagio");
		location.setMelody(",,2E--4.-6,,B,xC");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	
	@Test
	public void elgarCelloConcerto_6Notes_Full_Title() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("melody", ",8AB'CDxDExE"));
		parameters.add(new RequestParameter("title", "op. 85"));
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
		location.setMovementName("Adagio");
		location.setMelody(",8AB'CDxDExE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}

	@Test
	public void elgarCelloConcerto_36Notes_Full() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
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
		location.setMovementName("Adagio");
		location.setMelody("'4xF8G4xF8A4B8A4G8E4D8E4C,8B4A8B4A'8C4D8C,4B8G4xF8G4E8D4xC8D4xC8E4xF8E4D,,8B4xA8B4G8xF2B");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}

	
	@Test
	public void elgarCelloConcerto_20Notes_Full_4Measures() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
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
		location.setMovementName("Adagio");
		location.setMelody("'4xF8G4xF8A4B8A4/G8E4D8E4C,8B/,4A8B4A'8C4D8C/,4B8G4xF8G4E8D");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	
	@Test
	public void elgarCelloConcerto_Chord() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
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
		location.setMovementName("Adagio");
		location.setMelody(",,2E^B^,G^'E");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	
	@Test
	public void elgarCelloConcerto_EmbeddedMelody() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "true"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "false"));
		parameters.add(new RequestParameter("melody", ",,2GB8G"));
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
		location.setMovementName("Adagio");
		location.setMelody(",,2GB8G");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	@Test
	public void elgarCelloConcerto_6Notes_TimeSignatureC() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "true"));
		parameters.add(new RequestParameter("melody", "@c ,8AB'CDxDExE"));
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
		location.setMovementName("Adagio");
		location.setMelody("@c ,8AB'CDxDExE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	@Test
	public void elgarCelloConcerto_6Notes_TimeSignatureC_IgnoreOctave() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "true"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "true"));
		parameters.add(new RequestParameter("melody", "@c ,,8ABC'DxDExE"));
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
		location.setMovementName("Adagio");
		location.setMelody("@c ,,8ABC'DxDExE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	@Test
	public void elgarCelloConcerto_6Notes_Key() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "false"));
		parameters.add(new RequestParameter("melody", "$xF ,8AB'CDxDE"));
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
		location.setMovementName("Adagio");
		location.setMelody("$xF ,8AB'CDxDE");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	@Test
	public void elgarCelloConcerto_5Notes_Dotted() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "false"));
		parameters.add(new RequestParameter("melody", "4.E4.xD.ExD,8A"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("48");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMovementName("Adagio");
		location.setMelody("4.E4.xD.ExD,8A");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	

	@Test
	public void elgarCelloConcerto_3Notes_BypassingGraceNote() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "false"));
		parameters.add(new RequestParameter("melody", ",8GxFgAG"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("3");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMovementName("Adagio");
		location.setMelody(",8GxFgAG");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
	@Test
	public void elgarCelloConcerto_3Notes_ExplicitGraceNote() {
		
		ArrayList<RequestParameter> parameters = new ArrayList<RequestParameter>();
		parameters.add(new RequestParameter("ignoreOctave", "false"));
		parameters.add(new RequestParameter("ignorePitch", "false"));
		parameters.add(new RequestParameter("ignoreDuration", "false"));
		parameters.add(new RequestParameter("ignoreChords", "false"));
		parameters.add(new RequestParameter("melody", ",8xFG2C"));
		parameters.add(new RequestParameter("source", source));
		
		MusicScore score = new MusicScore();
		score.setTitle("Cellokonzert e-Moll op. 85");
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setDateIssued("1919");		
		MelodyLocation location = new MelodyLocation();
		location.setStartingMeasure("3");
		location.setVoice("1");
		location.setStaff("1");
		location.setInstrumentName("Violoncello");
		location.setMovementName("Adagio");
		location.setMelody(",8xFG2C");
				
		assertEquals(true, this.listScoresRequest(score, location, parameters));
	}
	
}
