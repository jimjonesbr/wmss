package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import de.wwu.wmss.settings.Util;

public class DescribeServiceRequest {

	private static String server = StartWMSS.server;
	private static int port = StartWMSS.port;

	@Test
	public void describeServiceRequest() {

		boolean result = true;
		boolean tested = false;
		ArrayList<String> testing_datasources = new ArrayList<String>();
		testing_datasources.add("neo4j_local");

		JSONObject jsonObject;

		try {

			String url = server+":"+port+"/wmss/?request=DescribeService";

			jsonObject = Util.readJsonFromUrl(url);		
			System.out.println("\nRequest: " + url + "\n");
			System.out.println("Report Type	: "+jsonObject.get("type"));
			System.out.println("Description	: "+jsonObject.get("title"));
			System.out.println("Service		: "+jsonObject.get("service"));
			System.out.println("Port		: "+jsonObject.get("port"));
			System.out.println("Contact		: "+jsonObject.get("contact"));
			System.out.println("Page size	: "+jsonObject.get("pageSize"));
			System.out.println("Report Type	: "+jsonObject.get("type"));
			System.out.println("Timeout		: "+jsonObject.get("timeout"));

			JSONArray datasources = jsonObject.getJSONArray("datasources");

			for (int i = 0; i < datasources.length(); i++) {

				if(datasources.getJSONObject(i).get("id").toString().equals("neo4j_local")) {
									

					System.out.println("\nSource: "+datasources.getJSONObject(i).get("id").toString());
					System.out.println("  |__Info: "+datasources.getJSONObject(i).get("info").toString());
					System.out.println("  |__Active: "+datasources.getJSONObject(i).get("active").toString());
					System.out.println("  |__Scores: "+datasources.getJSONObject(i).get("totalScores").toString());
					System.out.println("  |__Type: "+datasources.getJSONObject(i).get("type").toString());
					System.out.println("  |__Version: "+datasources.getJSONObject(i).get("version").toString());
					System.out.println("  |__Storage: "+datasources.getJSONObject(i).get("storage").toString());
					System.out.println("  |__Repository: "+datasources.getJSONObject(i).get("repository").toString());
					System.out.println("  |__Host: "+datasources.getJSONObject(i).get("host").toString());
					System.out.println("  |__Port: "+datasources.getJSONObject(i).get("port").toString());
					System.out.println("  |__User: "+datasources.getJSONObject(i).get("user").toString());


					JSONArray persons =  datasources.getJSONObject(i).getJSONArray("persons");
					System.out.println("  |__Persons");

					if(persons.length()!=4) {
						result = false;
					}

					for (int j = 0; j < persons.length(); j++) {

						String personUrl = persons.getJSONObject(j).get("url").toString();
						String personScores = persons.getJSONObject(j).get("totalRelatedScores").toString();
						String personRole = persons.getJSONObject(j).get("role").toString();
						String personName = persons.getJSONObject(j).get("name").toString();

						System.out.println("    |__URL: "+persons.getJSONObject(j).get("url").toString());
						System.out.println("    |__Name: "+persons.getJSONObject(j).get("name").toString());
						System.out.println("    |__Role: "+persons.getJSONObject(j).get("role").toString());
						System.out.println("    |__Scores: "+persons.getJSONObject(j).get("totalRelatedScores").toString());

						if(personUrl.equals("http://jimjones.de") && personRole.equals("Encoder") ) {

							if(!personScores.equals("3")) {
								result = false;
								System.err.println("Wrong number of related scores ("+personScores+") for '" + personName + "' (" + personUrl+")");
							}

						}

						if(personUrl.equals("http://dbpedia.org/resource/Edward_Elgar") && personRole.equals("Composer") ) {

							if(!personScores.equals("1")) {
								result = false;
								System.err.println("Wrong number of related scores ("+personScores+") for '" + personName + "' (" + personUrl+")");
							}

						}

						if(personUrl.equals("http://d-nb.info/gnd/100001718") && personRole.equals("Composer") ) {

							if(!personScores.equals("1")) {
								result = false;
								System.err.println("Wrong number of related scores ("+personScores+") for '" + personName + "' (" + personUrl+")");
							}

						}

						if(personUrl.equals("http://dbpedia.org/resource/Eppinger") && personRole.equals("Composer") ) {

							if(!personScores.equals("1")) {
								result = false;
								System.err.println("Wrong number of related scores ("+personScores+") for '" + personName + "' (" + personUrl+")");
							}

						}


					}



					JSONArray collections =  datasources.getJSONObject(i).getJSONArray("collections");
					System.out.println("  |__Collections");
					for (int j = 0; j < collections.length(); j++) {

						System.out.println("    |__Identifier: "+collections.getJSONObject(j).get("id").toString());
						System.out.println("    |__Description: "+collections.getJSONObject(j).get("description").toString());

						if(!collections.getJSONObject(j).get("id").toString().equals("https://url.collection.de") && 
								!collections.getJSONObject(j).get("id").toString().equals("https://sammlungen.ulb.uni-muenster.de")) {
							result = false;
							System.err.println("Unknown collection: '" + collections.getJSONObject(j).get("id").toString() + "' (" + collections.getJSONObject(j).get("description").toString() +")");
						}
					}


					JSONArray formats =  datasources.getJSONObject(i).getJSONArray("formats");
					System.out.println("  |__Formats");
					for (int j = 0; j < formats.length(); j++) {

						System.out.println("    |__Type: "+formats.getJSONObject(j).get("formatId").toString());
						System.out.println("    |__Description: "+formats.getJSONObject(j).get("formatDescription").toString());

						if(!formats.getJSONObject(j).get("formatId").toString().equals("musicxml") && 
								!formats.getJSONObject(j).get("formatId").toString().equals("mei")) {
							result = false;
							System.err.println("Unknown score format: '" + formats.getJSONObject(j).get("formatId").toString() + "' (" + formats.getJSONObject(j).get("formatDescription").toString() +")");
						}
					}

					JSONArray tonalities =  datasources.getJSONObject(i).getJSONArray("tonalities");

					if(tonalities.length()!=4) {
						result = false;
					}

					System.out.println("  |__Tonalities");				
					for (int j = 0; j < tonalities.length(); j++) {

						System.out.println("    |__Mode: "+tonalities.getJSONObject(j).get("mode").toString());
						System.out.println("    |__Tonic: "+tonalities.getJSONObject(j).get("tonic").toString());

						if((!tonalities.getJSONObject(j).get("tonic").toString().equals("G") && !tonalities.getJSONObject(j).get("mode").toString().equals("major")) &&
								(!tonalities.getJSONObject(j).get("tonic").toString().equals("F") && !tonalities.getJSONObject(j).get("mode").toString().equals("major")) &&
								(!tonalities.getJSONObject(j).get("tonic").toString().equals("E") && !tonalities.getJSONObject(j).get("mode").toString().equals("major")) && 
								(!tonalities.getJSONObject(j).get("tonic").toString().equals("Eb") && !tonalities.getJSONObject(j).get("mode").toString().equals("major"))) {

							result = false;

							System.err.println("Unexpected tonality: '" + tonalities.getJSONObject(j).get("tonic").toString() + "' (" + tonalities.getJSONObject(j).get("mode").toString() +")");

						}
					}


					JSONArray instrumentType =  datasources.getJSONObject(i).getJSONArray("performanceMediums");

					if(instrumentType.length()!=3) {
						result = false;
					}

					for (int j = 0; j < instrumentType.length(); j++) {

						System.out.println("  |__Instrument Type: " + instrumentType.getJSONObject(j).get("mediumTypeDescription").toString());

					}
					
					tested = true;

				} 

			}
			
			if(tested == false) {
				result = false;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}



		assertEquals(result, true);

	}


}
