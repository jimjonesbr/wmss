package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import de.wwu.wmss.core.Collection;
import de.wwu.wmss.core.Movement;
import de.wwu.wmss.core.MusicScore;
import de.wwu.wmss.core.PerformanceMedium;
import de.wwu.wmss.core.Person;
import de.wwu.wmss.core.Resource;
import de.wwu.wmss.settings.Util;

public class EditScores {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;
	private static String source = StartWMSS.source;
	
	private boolean postEditScoreRequest(MusicScore score, String json) {
			
		try {
		
			JSONObject scoreListDocument = new JSONObject(Util.postListScoreRequest(server, port, source, json.toString()));			
			String url = server+":"+port+"/wmss/";
			System.out.println("\nRequest: " + url + "\n");			
						
			JSONArray datasources = scoreListDocument.getJSONArray("datasources");

			for (int i = 0; i < datasources.length(); i++) {

				JSONArray scores =  datasources.getJSONObject(i).getJSONArray("scores");

				for (int j = 0; j < scores.length(); j++) {
					
					System.out.println();
					System.out.println("Score identifier: "+scores.getJSONObject(j).getString("scoreIdentifier"));
					System.out.println("Score title     : "+scores.getJSONObject(j).getString("scoreTitle"));
					System.out.println("Date issued     : "+scores.getJSONObject(j).getString("dateIssued"));
					System.out.println("Score thumbail  : "+scores.getJSONObject(j).getString("thumbnail"));
									
					if(!score.getIdentifier().equals(scores.getJSONObject(j).getString("scoreIdentifier")) ||
					   !score.getTitle().equals(scores.getJSONObject(j).getString("scoreTitle")) ||
					   !score.getDateIssued().equals(scores.getJSONObject(j).getString("dateIssued")) ||
					   !score.getThumbnail().equals(scores.getJSONObject(j).getString("thumbnail"))) {
						return false;
					}
					
					JSONArray movements =  scores.getJSONObject(i).getJSONArray("movements");
					System.out.println();
					System.out.println("Movement identifier: "+movements.getJSONObject(j).getString("movementIdentifier"));
					System.out.println("Movement label     : "+movements.getJSONObject(j).getString("movementLabel"));
					System.out.println("Movement order     : "+movements.getJSONObject(j).getInt("movementOrder"));
					System.out.println("Beats per Minute   : "+movements.getJSONObject(j).getInt("beatsPerMinute"));
					System.out.println("Beat unit          : "+movements.getJSONObject(j).getString("beatUnit"));

					if(!score.getMovements().get(0).getIdentifier().equals(movements.getJSONObject(0).getString("movementIdentifier")) ||
					   !score.getMovements().get(0).getLabel().equals(movements.getJSONObject(0).getString("movementLabel")) ||
					   score.getMovements().get(0).getOrder() != movements.getJSONObject(0).getInt("movementOrder") ||
					   score.getMovements().get(0).getBeatsPerMinute() != movements.getJSONObject(0).getInt("beatsPerMinute") ||
					   !score.getMovements().get(0).getBeatUnit().equals(movements.getJSONObject(0).getString("beatUnit"))) {
						return false;
						
					} 
					
					JSONArray mediumTypes =  movements.getJSONObject(i).getJSONArray("mediumTypes");
					System.out.println("Medium type identifier : "+mediumTypes.getJSONObject(0).getString("mediumTypeIdentifier"));
					System.out.println("Medium type label : "+mediumTypes.getJSONObject(0).getString("mediumTypeLabel"));
					
					if(!score.getMovements().get(0).getMediums().get(0).getTypeIdentifier().equals(mediumTypes.getJSONObject(0).getString("mediumTypeIdentifier")) ||
					   !score.getMovements().get(0).getMediums().get(0).getTypeLabel().equals(mediumTypes.getJSONObject(0).getString("mediumTypeLabel"))) {						
						return false;						
					}
					
					JSONArray mediums =  mediumTypes.getJSONObject(i).getJSONArray("mediums");
					
					System.out.println();
					System.out.println("Medium identifier : "+mediums.getJSONObject(0).getString("mediumIdentifier"));
					System.out.println("Medium label      : "+mediums.getJSONObject(0).getString("mediumLabel"));
					System.out.println("Medium code       : "+mediums.getJSONObject(0).getString("mediumCode"));
					System.out.println("Medium score label: "+mediums.getJSONObject(0).getString("mediumScoreLabel"));
					System.out.println("Medium solo       : "+mediums.getJSONObject(0).getBoolean("solo"));
					System.out.println("Medium ensemble   : "+mediums.getJSONObject(0).getBoolean("ensemble"));
					
					if(!score.getMovements().get(0).getMediums().get(0).getIdentifier().equals(mediums.getJSONObject(0).getString("mediumIdentifier")) ||
					   !score.getMovements().get(0).getMediums().get(0).getLabel().equals(mediums.getJSONObject(0).getString("mediumLabel")) ||
					   !score.getMovements().get(0).getMediums().get(0).getCode().equals(mediums.getJSONObject(0).getString("mediumCode")) ||
					   !score.getMovements().get(0).getMediums().get(0).getScoreLabel().equals(mediums.getJSONObject(0).getString("mediumScoreLabel")) ||
					   !score.getMovements().get(0).getMediums().get(0).isSolo().equals(String.valueOf(mediums.getJSONObject(0).getBoolean("solo"))) ||
					   !score.getMovements().get(0).getMediums().get(0).isEnsemble().equals(String.valueOf(mediums.getJSONObject(0).getBoolean("ensemble")))){
						return false;
					}
					
					
				}

			}
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} 

		return true;

	}

	@Test
	public void editElgarCelloConcertoFull() {
		
		MusicScore score = new MusicScore();
		score.setIdentifier("http://dbpedia.org/resource/Cello_Concerto_(Elgar)");
		score.setTitle("score-title-updated");		
		score.setDateIssued("9999");		
		score.setThumbnail("https://www.designtagebuch.de/wp-content/uploads/mediathek//2017/01/wwu-logo-700x377.png");
		
		Movement movement = new Movement();
		movement.setIdentifier("http://linkeddata.uni-muenster.de/node/21dde7d3-e280-44d5-900a-4660e91ba4f4_MOV1");
		movement.setOrder(1);
		movement.setBeatsPerMinute(999);
		movement.setBeatUnit("beat-unit-updated");
		movement.setLabel("movement-label-updated");
		PerformanceMedium medium = new PerformanceMedium();
		medium.setIdentifier("http://linkeddata.uni-muenster.de/node/21dde7d3-e280-44d5-900a-4660e91ba4f4_MOV1_PART_P1");
		medium.setEnsemble("true");
		medium.setSolo("true");
		medium.setLabel("medium-label-updated");
		medium.setCode("medium.code.updated");
		medium.setScoreLabel("medium-score-label-updated");
		medium.setTypeIdentifier("medium.type.identifier.updated");
		medium.setTypeLabel("medium-type-label-updated");
		movement.getMediums().add(medium);
		score.getMovements().add(movement);
		
		Collection collection1 = new Collection();		
		collection1.setIdentifier("https://sammlungen.ulb.uni-muenster.de");
		collection1.setLabel("collection-sammlung-label-updated");
		score.getCollections().add(collection1);
		
		Collection collection2 = new Collection();
		collection2.setIdentifier("https://github.com/jimjonesbr/wmss#collections");
		collection2.setLabel("new-collection");
		score.getCollections().add(collection2);
		
		Person person1 = new Person();
		person1.setIdentifier("http://dbpedia.org/resource/Edward_Elgar");
		person1.setName("elgar-updated");
		person1.setRole("elgar-role-updated");
		score.getPersons().add(person1);
		
		Person person2 = new Person();
		person2.setIdentifier("http://new.person.de");
		person2.setName("new-person");
		person2.setRole("new-person-role");
		score.getPersons().add(person2);
		
		Resource resource1 = new Resource();
		resource1.setUrl("https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)");
		resource1.setLabel("wikipedia-resource-updated");
		resource1.setType("text/html");
		score.getResources().add(resource1);
		
		Resource resource2 = new Resource();
		resource2.setUrl("http://new.resource.de/");
		resource2.setLabel("new-resource");
		resource2.setType("application/pdf");
		score.getResources().add(resource2);
		
		
		String json = "";
		try {
			
			URL url = this.getClass().getResource("/query");		
			File file = new File(Paths.get(url.toURI()).toString()+"/editscore-request.json");
			json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		assertEquals(true, postEditScoreRequest(score, json));
		
	}
}
