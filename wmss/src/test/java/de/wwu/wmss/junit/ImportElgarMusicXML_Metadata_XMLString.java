package de.wwu.wmss.junit;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import org.junit.Test;
import de.wwu.wmss.settings.Util;

public class ImportElgarMusicXML_Metadata_XMLString {
		
	@Test
	public void importMusicXML() {
			
		boolean result = true;				
		URL url = this.getClass().getResource("/musicxml");		
		String metadata = 
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<score>\n" + 
				"	<scoreIdentifier>http://dbpedia.org/resource/Cello_Concerto_(Elgar)</scoreIdentifier>\n" + 
				"	<scoreTitle>Cellokonzert e-Moll op. 85</scoreTitle>\n" + 
				"	<thumbnail>https://www.rcm.ac.uk/media/Elgar%20Cello%20Concerto%20maunscript%206x4.jpg</thumbnail>\n" + 
				"	<issued>1919</issued>\n" + 
				"	<collections>\n" + 
				"		<collection>\n" + 
				"			<collectionLabel>Great Composers</collectionLabel>\n" + 
				"			<collectionIdentifier>https://wwu.greatcomposers.de</collectionIdentifier>\n" + 
				"		</collection>\n" + 
				"		<collection>\n" + 
				"			<collectionLabel>Digitale Sammlung der Universität und Landesbibliothek Münster</collectionLabel>\n" + 
				"			<collectionIdentifier>https://sammlungen.ulb.uni-muenster.de</collectionIdentifier>\n" + 
				"		</collection>		\n" + 
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
				"			<resourceLabel>Print</resourceLabel>\n" + 
				"			<resourceType>application/pdf</resourceType>\n" + 
				"		</resource>\n" + 
				"		<resource>\n" + 
				"			<resourceURL>https://en.wikipedia.org/wiki/Cello_Concerto_(Elgar)</resourceURL>\n" + 
				"			<resourceLabel>Wikipedia Article</resourceLabel>\n" + 
				"			<resourceType>text/html</resourceType>\n" + 
				"		</resource>\n" + 
				"	</resources>\n" + 
				"</score>";
		
		try {
			
			File musicxml = new File(Paths.get(url.toURI()).toString()+"/elgar_cello_concerto_op.85.xml");
			result = Util.postMusicXMLMetadataString(StartWMSS.server, StartWMSS.port, StartWMSS.source, musicxml, 10000, metadata, "musicxml");

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		assertEquals(true, result);
			
	}

}
