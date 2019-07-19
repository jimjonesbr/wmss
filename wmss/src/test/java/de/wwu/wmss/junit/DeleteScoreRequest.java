package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.settings.Util;


public class DeleteScoreRequest {

	private static String server = "http://localhost";
	private static int port = StartWMSS.port;
	private static String source = "neo4j_local";

	public boolean delete(MusicScore score) {

		boolean result = false;

		String url = server+":"+port+"/wmss?source="+source+"&request=DeleteScore&identifier="+score.getScoreId();
		System.out.println("\nRequest: " + url + "\n");
		try {

			JSONObject jsonObject = Util.readJsonFromUrl(url);
			
			JSONArray scoreObject = jsonObject.getJSONArray("score");//1

			for (int i = 0; i < scoreObject.length(); i++) {
				
				System.out.println("\nDeleted Score\n"+
						"- Identifier: "+scoreObject.getJSONObject(i).get("scoreIdentifier")+"\n"+ 
						"- Title     : "+scoreObject.getJSONObject(i).get("title")+"\n"+
						"- Collection: "+scoreObject.getJSONObject(i).get("collection")+"\n");
				
				if(scoreObject.getJSONObject(i).get("scoreIdentifier").toString().equals(score.getScoreId())) {
					result = true;
				}
				
			}
			
		} catch (JSONException | IOException e) {

			e.printStackTrace();
		}
		return result;
	
	}
	
	@Test
	public void deleteElgarScore() {
		
		MusicScore score = new MusicScore();
		score.setScoreId("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");		
		assertEquals(true, this.delete(score));
		
	}
	
	@Test
	public void deleteAchillesGrandOpera() {
		
		MusicScore score = new MusicScore();
		score.setScoreId("https://sammlungen.ulb.uni-muenster.de/id/5731633");		
		assertEquals(true, this.delete(score));
		
	}

	@Test
	public void deleteSiegesMaersche() {
		
		MusicScore score = new MusicScore();
		score.setScoreId("https://sammlungen.ulb.uni-muenster.de/id/5393365");		
		assertEquals(true, this.delete(score));
		
	}

}
