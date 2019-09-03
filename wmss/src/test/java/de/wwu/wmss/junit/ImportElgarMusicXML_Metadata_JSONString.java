package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.Test;
import de.wwu.wmss.settings.Util;

public class ImportElgarMusicXML_Metadata_JSONString {
		
	@Test
	public void importMusicXML() {
			
		boolean result = true;				
		URL url = this.getClass().getResource("/musicxml");		
		String metadata = 
				"{\n" + 
				"   \"scoreIdentifier\": \"http://dbpedia.org/resource/Cello_Concerto_(Elgar)\",\n" + 
				"   \"title\": \"Cellokonzert e-Moll op. 85\",\n" + 
				"   \"thumbnail\": \"https://www.rcm.ac.uk/media/Elgar%20Cello%20Concerto%20maunscript%206x4.jpg\",\n" + 
				"   \"issued\": \"1919\",\n" + 
				"   \"collections\": [       \n" + 
				"      {\n" + 
				"         \"collectionName\": \"Great Composers\",\n" + 
				"         \"collectionURL\": \"https://wwu.greatcomposers.de\"\n" + 
				"      },"+
				"	   {\n" + 
				"         \"collectionName\": \"Digitale Sammlung der Universität und Landesbibliothek Münster\",\n" + 
				"         \"collectionURL\": \"https://sammlungen.ulb.uni-muenster.de\"\n" + 
				"      }\n" + 
				"   ],\n" + 
				"   \"persons\": [\n" + 
				"      {\n" + 
				"         \"personIdentifier\": \"http://dbpedia.org/resource/Edward_Elgar\",\n" + 
				"         \"personName\": \"Sir Edward William Elgar\",\n" + 
				"         \"personRole\": \"Composer\"\n" + 
				"      },\n" + 
				"      {\n" + 
				"         \"personIdentifier\": \"http://jimjones.de\",\n" + 
				"         \"personName\": \"Jim Jones\",\n" + 
				"         \"personRole\": \"Encoder\"\n" + 
				"      }\n" + 
				"   ],\n" + 
				"   \"resources\": [\n" + 
				"      {\n" + 
				"         \"resourceURL\": \"https://musescore.com/score/152011/download/pdf\",\n" + 
				"         \"resourceDescription\": \"Print\",\n" + 
				"         \"resourceType\": \"application/pdf\"\n" + 
				"      },\n" + 
				"      {\n" + 
				"         \"resourceURL\": \"https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)\",\n" + 
				"         \"resourceDescription\": \"Wikipedia Article\",\n" + 
				"         \"resourceType\": \"text/html\"\n" + 
				"      }\n" + 
				"   ]\n" + 
				"}";

		
		try {
			
			File musicxml = new File(Paths.get(url.toURI()).toString()+"/elgar_cello_concerto_op.85.xml");
			result = Util.postMusicXMLMetadataString(StartWMSS.server, StartWMSS.port, StartWMSS.source, musicxml, 10000, metadata, "musicxml");

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		assertEquals(true, result);
			
	}

}
